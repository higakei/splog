package jp.co.hottolink.splogfilter.learning.ui;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.learning.boosting.classifier.ClassifierImpl;
import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;
import jp.co.hottolink.splogfilter.learning.master.MasterDataCache;
import jp.co.hottolink.splogfilter.learning.master.entity.ClassifiedTypeEntity;

/**
 * <p>
 * ブースティングを検証するクラス.
 * </p>
 * @author higa
 */
public class BoostingVerifier {

	public static void main(String[] args) {

		SQLExecutor executor = null;

		try {
			// マスターデータ
			ClassifiedTypeEntity classifiedType = MasterDataCache.getClassifiedType("spam");
			List<ClassifierEntity> classifiers = MasterDataCache.getClassifierList();
			String table = "trial_result";

			// 学習機械の取得
			executor = new SQLExecutor("splog_learning_db");
			Map<String, Double> reliabilities = getLearner(executor);

			System.out.println("学習機械");
			System.out.println("classifier"
					+ "\t" + "reliability");
			for (String key: reliabilities.keySet()) {
				System.out.println(MasterDataCache.getClassifier(key).getLabel()
						+ "\t" + reliabilities.get(key));
			}
			System.out.println("");
			
			// 集計
			Map<String, Map<String, Integer>> map = count(executor, table, classifiedType, reliabilities, classifiers);

			System.out.println("検証結果");
			System.out.println(""
					+ "\t" + "splog_recall"
					+ "\t" + "splog_precision"
					+ "\t" + "blog_recall"
					+ "\t" + "blog_precision"
					+ "\t" + "recall"
					+ "\t" + "precision"
					+ "\t" + "f_measure");

			// 計算
			for (String key: map.keySet()) {
				Map<String, Integer> counter = map.get(key);
				int true_true = counter.get("true_true");
				int true_not = counter.get("true_not");
				int not_true = counter.get("not_true");
				int not_not = counter.get("not_not");
				int unknown_true = counter.get("unknown_true");
				int unknown_not = counter.get("unknown_not");

				double splog_recall = divide(true_true, (true_true + not_true));
				double splog_precision = divide(true_true, (true_true + true_not));
				double blog_recall = divide(not_not, (not_not + true_not));
				double blog_precision = divide(not_not, (not_not + not_true));
				double recall = divide((true_true + not_not), (true_true + true_not + not_not + not_true + unknown_true + unknown_not));
				double precision = divide((true_true + not_not), (true_true + true_not + not_not + not_true));
				double f_measure = divide((2 * recall * precision), (recall + precision));

				ClassifierEntity classifier = MasterDataCache.getClassifier(key);
				String label = "学習機械";
				if (classifier != null) label = classifier.getLabel();
				
				System.out.println(label
						+ "\t" + splog_recall
						+ "\t" + splog_precision
						+ "\t" + blog_recall
						+ "\t" + blog_precision
						+ "\t" + recall
						+ "\t" + precision
						+ "\t" + f_measure);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (executor != null) executor.finalize();
		}
	}

	/**
	 * <p>
	 * select句を作成する.
	 * </p>
	 * @param classifiers 判別器リスト
	 * @return select句
	 */
	private static String createSelect(List<ClassifierEntity> classifiers) {

		StringBuffer sql = new StringBuffer("select correct");

		for (ClassifierEntity classifier: classifiers) {
			String name = classifier.getName();
			sql.append(", " + name + ".answer as " + name + "_answer");
		}

		return sql.toString();
	}

	/**
	 * <p>
	 * from句を作成する.
	 * </p>
	 * @param classifiers 判別器リスト
	 * @param table テーブル名
	 * @return from句
	 */
	private static String createFrom(List<ClassifierEntity> classifiers, String table) {

		StringBuffer sql = new StringBuffer(" from ");

		for (int i = 0; i < classifiers.size(); i++) {
			ClassifierEntity classifier = classifiers.get(i); 
			String name = classifier.getName();
			if (i > 0) sql.append(" join");
			sql.append(" (select data_id, answer, matching");
			if (i == 0) sql.append(", correct");
			sql.append(" from " + table);
			sql.append(" where classifier = '" + name + "'");
			sql.append(") " + name);
		}

		return sql.toString();
	}

	/**
	 * <p>
	 * where句を作成する.
	 * </p>
	 * @param classifiers 判別器リスト
	 * @return where句
	 */
	private static String createWhere(List<ClassifierEntity> classifiers) {

		StringBuffer sql = new StringBuffer(" where ");
		String left = null;

		for (int i = 0; i < classifiers.size(); i++) {
			ClassifierEntity classifier = classifiers.get(i); 
			String name = classifier.getName();
			String right = name + ".data_id";
			if (i > 1) sql.append(" and ");
			if (i > 0) sql.append(left + " = " + right);
			left = right;
		}

		return sql.toString();
	}

	/**
	 * <p>
	 * 集計テーブルを作成する.
	 * </p>
	 * @return 集計テーブル
	 */
	private static Map<String, Integer> createCounter() {
		Map<String, Integer> counter = new HashMap<String, Integer>();
		counter.put("true_true", 0);
		counter.put("true_not", 0);
		counter.put("not_true", 0);
		counter.put("not_not", 0);
		counter.put("unknown_true", 0);
		counter.put("unknown_not", 0);
		return counter;
	}

	/**
	 * <p>
	 * 学習機械を取得する.
	 * </p>
	 * @param executor SQL実行クラス.
	 * @return 学習機械
	 * @throws SQLException 
	 */
	private static Map<String, Double> getLearner(SQLExecutor executor) throws SQLException {

		try {
			// SQLの実行
			String sql = "select * from learner where training_id is null";
			executor.executeQuery(sql);

			// データの取得
			Map<String, Double> learner = new HashMap<String, Double>();
			while (executor.next()) {
				String classifier = executor.getString("classifier");
				Double reliability = executor.getDouble("reliability");
				learner.put(classifier, reliability);
			}

			return learner;

		} finally {
			if (executor != null) executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 集計を行う.
	 * </p>
	 * @param executor SQL実行クラス
	 * @param table トライアル結果テーブル
	 * @param classifiedType 判別情報
	 * @param reliabilities 学習機械
	 * @param classifiers 判別器リスト
	 * @return 集計結果
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 */
	private static Map<String, Map<String, Integer>> count(
			SQLExecutor executor, String table,
			ClassifiedTypeEntity classifiedType,
			Map<String, Double> reliabilities,
			List<ClassifierEntity> classifiers) throws SQLException,
			IllegalArgumentException, SecurityException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException {

		try {
			// 集計の初期化
			Map<String, Map<String, Integer>> map = new LinkedHashMap<String, Map<String,Integer>>();
			for (ClassifierEntity classifier: classifiers) {
				map.put(classifier.getName(), createCounter());
			}
			map.put("learner", createCounter());

			// SQLの実行
			StringBuffer sql = new StringBuffer();
			sql.append(createSelect(classifiers));
			sql.append(createFrom(classifiers, table));
			sql.append(createWhere(classifiers));
			executor.executeQuery(sql.toString());

			// 集計
			while (executor.next()) {
				double hypothesis = 0;
				String correct = executor.getString("correct");

				for (ClassifierEntity entity: classifiers) {
					// 解答の取得
					String name = entity.getName();
					String answer = executor.getString(name + "_answer");

					// 照合
					ClassifierImpl classifier = entity.getClassifier();
					int cSignum = classifier.signum(answer);
					int hSignum = classifier.signum(correct);

					// 集計
					Map<String, Integer> counter = map.get(name);
					if ((cSignum == 1) && (hSignum == 1)) {
						int count = counter.get("true_true");
						counter.put("true_true", ++count);
					} else if ((cSignum == 1) && (hSignum == -1)) {
						int count = counter.get("true_not");
						counter.put("true_not", ++count);
					} else if ((cSignum == -1) && (hSignum == 1)) {
						int count = counter.get("not_true");
						counter.put("not_true", ++count);
					} else if ((cSignum == -1) && (hSignum == -1)) {
						int count = counter.get("not_not");
						counter.put("not_not", ++count);
					}

					// ブースティング
					Double reliabilitiy = reliabilities.get(name);
					hypothesis += cSignum * reliabilitiy;
				}

				// ブースティングによる判定
				ClassifierImpl classifier = classifiedType.getClassifier();
				int hSignum = classifier.signum(correct);
				int cSignum = -1;
				if (hypothesis > 0.0) cSignum = 1;

				// 集計
				Map<String, Integer> counter = map.get("learner");
				if ((cSignum == 1) && (hSignum == 1)) {
					int count = counter.get("true_true");
					counter.put("true_true", ++count);
				} else if ((cSignum == 1) && (hSignum == -1)) {
					int count = counter.get("true_not");
					counter.put("true_not", ++count);
				} else if ((cSignum == -1) && (hSignum == 1)) {
					int count = counter.get("not_true");
					counter.put("not_true", ++count);
				} else if ((cSignum == -1) && (hSignum == -1)) {
					int count = counter.get("not_not");
					counter.put("not_not", ++count);
				}
			}

			return map;

		} finally {
			if (executor != null) executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 割り算をする.
	 * </p>
	 * @param dividend 被除数
	 * @param divisor 除数
	 * @return 商
	 */
	private static double divide(double dividend, double divisor) {
		Double quotient = dividend / divisor;
		if (quotient.isNaN()) {
			return 0;
		} else {
			return quotient;
		}
	}
}
