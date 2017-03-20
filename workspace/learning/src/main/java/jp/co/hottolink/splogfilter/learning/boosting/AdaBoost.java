package jp.co.hottolink.splogfilter.learning.boosting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl;
import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;
import jp.co.hottolink.splogfilter.learning.boosting.entity.HypothesisEntity;

public class AdaBoost {

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private Logger logger = Logger.getLogger(AdaBoost.class);

	/**
	 * <p>
	 * トライアル結果から判別器の信頼度を求める.
	 * </p>
	 * @param loader データローダ
	 * @param classifiers 判別器リスト
	 * @return 判別器の信頼度
	 * @throws Exception
	 */
	public Map<String, Double> train(DataLoaderImpl loader,
			List<ClassifierEntity> classifiers) throws Exception {

		Map<String, Double> reliabilities = new LinkedHashMap<String, Double>();
		Map<String, File> matchingFile = null;

		try {
			// トライアル結果のロード
			matchingFile = createTempFile(classifiers);
			int dataCount = load(loader, matchingFile);

			// データの重みを初期化
			double[] weights = new double[dataCount];
			for (int i = 0; i < dataCount; i++) {
				weights[i] = Math.pow(dataCount, -1);
			}

			for (ClassifierEntity classifier: classifiers) {
				String classifierNmae = classifier.getName();
				File file = matchingFile.get(classifier.getName());

				// 誤り率を計算
				double error = getError(file, weights);
				logger.info(classifierNmae + ":" + error);
				if (error >= 0.5) {
					// 誤り率が0.5以上なら、信頼度0.0
					logger.warn("εt=" + error + "(" + classifierNmae + ")");
					reliabilities.put(classifierNmae, 0.0);
					continue;
				}

				// 信頼度を計算
				double reliability = 0.0;
				if (error <= 0.0) {
					reliability = 0.5 * Math.log(Double.MAX_VALUE);
				} else { 
					reliability = 0.5 * Math.log((1 - error)/error);
				}
				reliabilities.put(classifierNmae, reliability);

				// 規格化因子を計算
				double nf = getNormalizationFactor(file, weights, reliability);

				// データの重みを更新
				updateWeight(file, weights, reliability, nf);
			}

			return reliabilities;

		} finally {
			deleteOnExit(matchingFile);
		}
	}

	/**
	 * <p>
	 * 学習機械の仮説を求める.
	 * </p>
	 * @param hypotheses 判定器の仮説リスト
	 * @return 仮説
	 */
	public boolean obtain(List<HypothesisEntity> hypotheses) {

		double hypothesis = 0;

		for (HypothesisEntity entity: hypotheses) {
			double reliability = entity.getReliability();
			int signum = entity.getSignum();
			hypothesis += reliability * signum;
		}

		return (hypothesis > 0.0);
	}

	/**
	 * <p>
	 * 判定器の誤り率を求める
	 * </p>
	 * @param file トライアル結果ファイル
	 * @param weights 学習データの重み
	 * @return 誤り率
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Double getError(File file, double[] weights) throws Exception {

		FileInputStream fis = null;
		double error = 0;

		try {
			// ファイルのオープン
			fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);

			for (int i = 0; i < weights.length; i++) {
				// 照合結果の取得
				Map<String, Object> data = (Map<String, Object>)ois.readObject();
				String matching = (String)data.get("matching");

				// 判別器の誤り率を計算
				if (!Boolean.valueOf(matching).booleanValue()) {
					error += weights[i];
				}
			}

			return error;

		} finally {
			if (fis != null) fis.close();
		}
	}

	/**
	 * <p>
	 * 規格化因子を求める.
	 * </p>
	 * @param file トライアル結果ファイル
	 * @param weights 学習データの重み
	 * @param reliability 判定器の信頼度
	 * @return 規格化因子
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private double getNormalizationFactor(File file, double[] weights,
			double reliability) throws Exception {

		FileInputStream fis = null;
		double nf = 0;

		try {
			// ファイルのオープン
			fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);

			for (int i = 0; i < weights.length; i++) {
				// 照合結果の取得
				Map<String, Object> data = (Map<String, Object>)ois.readObject();
				String matching = (String)data.get("matching");

				// 規格化因子を計算
				if (Boolean.valueOf(matching).booleanValue()) {
					nf += weights[i] * Math.exp(-reliability);
				} else {
					nf += weights[i] * Math.exp(reliability);
				}
			}

			return nf;

		} finally {
			if (fis != null) fis.close();
		}
	}

	/**
	 * <p>
	 * 学習データの重みを更新する.
	 * </p>
	 * @param file トライアル結果
	 * @param weights 学習データの重み
	 * @param reliability 判定器の信頼度
	 * @param nf 規格化因子
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void updateWeight(File file, double[] weights, double reliability,
			double nf) throws Exception {

		FileInputStream fis = null;

		try {
			// ファイルのオープン
			fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);

			for (int i = 0; i < weights.length; i++) {
				// 照合結果の取得
				Map<String, Object> data = (Map<String, Object>)ois.readObject();
				String matching = (String)data.get("matching");

				// 学習データの重みを更新
				if (Boolean.valueOf(matching).booleanValue()) {
					weights[i] = weights[i] * Math.exp(-reliability) / nf;
				} else {
					weights[i] = weights[i] * Math.exp(reliability) / nf;
				}
			}

		} finally {
			if (fis != null) fis.close();
		}
	}

	/**
	 * <p>
	 * データローダからトライアル結果を取得し、ファイルに保存する.
	 * </p>
	 * @param loader データローダ
	 * @param fileMap ファイル
	 * @return データ数
	 * @throws Exception
	 */
	private int load(DataLoaderImpl loader, Map<String, File> fileMap) throws Exception {

		try {
			// データローダのオープン
			loader.open();

			// ファイルのオープン
			Map<String, ObjectOutputStream> map = new HashMap<String, ObjectOutputStream>();
			for (String key: fileMap.keySet()) {
				File file = fileMap.get(key);
				FileOutputStream fos = new FileOutputStream(file);
		        ObjectOutputStream oos = new ObjectOutputStream(fos);
				map.put(key, oos);
			}

			// データがなくなるまで
			Set<Integer> dataCount = new HashSet<Integer>();
			for (;;) {

				// データの取得
				Map<String, Object> data = loader.fetch();
				if (data == null) {
					break;
				}

				// データ数をカウントする
				dataCount.add((Integer)data.get("data_id"));

				// データをファイルに保存
				String classifier = (String)data.get("classifier");
				ObjectOutputStream oos = map.get(classifier);
				oos.writeObject(data);
				oos.flush();
			}

			// ファイルのクローズ
			for (ObjectOutputStream oos: map.values()) {
				oos.close();
			}

			return dataCount.size();

		} finally {
			if (loader != null) try { loader.close(); } catch (Exception e) {}
		}
	}

	/**
	 * <p>
	 * トライアル結果の一時ファイルを作成する.
	 * </p>
	 * @param classifiers 判別器リスト
	 * @return 一時ファイル
	 * @throws IOException
	 */
	private Map<String, File> createTempFile(List<ClassifierEntity> classifiers) throws IOException {

		Map<String, File> map = new HashMap<String, File>();
		if (classifiers == null) {
			return null;
		}

		for (ClassifierEntity classifier: classifiers) {
			String name = classifier.getName();
			File file = File.createTempFile(name, null);
			map.put(name, file);
		}

		return map;
	}

	/**
	 * <p>
	 * 一時ファイルを削除する.
	 * </p>
	 * @param tempFile 一時ファイル
	 */
	private void deleteOnExit(Map<String, File> tempFile) {
		try {
			if (tempFile == null) {
				return;
			}

			for (File file: tempFile.values()) {
				file.deleteOnExit();
			}

		} catch (Exception e) {
			logger.warn("一時ファイルの削除に失敗しました。", e);
		}
	}
}
