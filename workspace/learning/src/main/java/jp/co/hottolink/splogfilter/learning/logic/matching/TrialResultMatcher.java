package jp.co.hottolink.splogfilter.learning.logic.matching;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl;
import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;
import jp.co.hottolink.splogfilter.learning.logic.trial.TrialEntity;

/**
 * <p>
 * トライアル結果を照合するクラス.
 * </p>
 * @author higa
 */
public class TrialResultMatcher {

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private Logger logger = Logger.getLogger(TrialResultMatcher.class);

	/**
	 * <p>
	 * トライアル情報.
	 * </p>
	 */
	private TrialEntity trial = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param trial トライアル情報
	 */
	public TrialResultMatcher(TrialEntity trial) {
		this.trial = trial;
	}

	/**
	 * <p>
	 * トライアル結果を照合する.
	 * </p>
	 * @param loader トライアル結果のデータローダ
	 * @return 照合結果
	 * @throws Exception
	 */
	public MatchingEntity match(DataLoaderImpl loader) throws Exception {

		Map<String, File> trials = null;

		try {
			// トライアル結果のロード
			List<ClassifierEntity> classifiers = trial.getClassifiers();
			trials = createTempFile(classifiers);
			trials = load(loader, trials);

			// 照合
			List<ClassifierMatchingEntity> matchings = new ArrayList<ClassifierMatchingEntity>();
			for (ClassifierEntity classifier: classifiers) {
				String key = classifier.getName();
				File file = trials.get(key);
				ClassifierMatchingEntity matching = match(classifier, file);
				matchings.add(matching);
			}

			// 照合結果の作成
			MatchingEntity result = new MatchingEntity();
			result.setTrial(trial);
			result.setMatchings(matchings);

			return result;

		} finally {
			deleteOnExit(trials);
		}
	}

	/**
	 * <p>
	 * 判別器ごとに一時ファイルを作成する.
	 * </p>
	 * @param classifiers 判別器リスト
	 * @return 一時ファイル
	 * @throws IOException
	 */
	private Map<String, File> createTempFile(List<ClassifierEntity> classifiers)
			throws IOException {

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

	/**
	 * <p>
	 * データローダからトライアル結果を取得し、判別器ファイルに保存する.
	 * </p>
	 * @param loader データローダ
	 * @param classifiersFiles 判別器ファイル
	 * @return 判別器ファイル
	 * @throws Exception
	 */
	private Map<String, File> load(DataLoaderImpl loader,
			Map<String, File> classifiersFiles) throws Exception {

		try {
			// データローダのオープン
			loader.open();

			// ファイルのオープン
			Map<String, ObjectOutputStream> map = new HashMap<String, ObjectOutputStream>();
			for (String key: classifiersFiles.keySet()) {
				File file = classifiersFiles.get(key);
				FileOutputStream fos = new FileOutputStream(file);
		        ObjectOutputStream oos = new ObjectOutputStream(fos);
				map.put(key, oos);
			}

			// データがなくなるまで
			for (;;) {
				// データの取得
				Map<String, Object> data = loader.fetch();
				if (data == null) {
					break;
				}

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

			return classifiersFiles;

		} finally {
			if (loader != null) try { loader.close(); } catch (Exception e) {}
		}
	}

	/**
	 * <p>
	 * 判別器の照合を行う.
	 * </p>
	 * @param classifier 判別器
	 * @param trial トライアル結果ファイル
	 * @return 照合結果
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private ClassifierMatchingEntity match(ClassifierEntity classifier,
			File trial) throws IOException, ClassNotFoundException {

		FileInputStream fis = null;

		try {
			// ファイルのオープン
			fis = new FileInputStream(trial);
			ObjectInputStream ois = new ObjectInputStream(fis);

			// 照合
			int answer = 0;
			int correct = 0;
			for (;;) {
				// トライアル結果の取得
				Map<String, Object> data = null;
				try {
					data = (Map<String, Object>)ois.readObject();
					answer += 1;
				} catch (EOFException e) {
					break;
				}

				// 正解数をカウントアップ
				String matching = (String)data.get("matching");
				if (Boolean.valueOf(matching).booleanValue()) {
					correct += 1;
				}
			}

			// 照合結果の作成
			ClassifierMatchingEntity matching = new ClassifierMatchingEntity();
			matching.setClassifier(classifier);
			matching.setAnswer(answer);
			matching.setCorrect(correct);

			return matching;

		} finally {
			if (fis != null) fis.close();
		}
	}

}
