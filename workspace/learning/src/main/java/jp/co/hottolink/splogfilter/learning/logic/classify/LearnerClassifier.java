package jp.co.hottolink.splogfilter.learning.logic.classify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl;
import jp.co.hottolink.splogfilter.learning.boosting.classifier.ClassifierImpl;
import jp.co.hottolink.splogfilter.learning.boosting.entity.AnswerEntity;

/**
 * <p>
 * 学習機械で判定するクラス.
 * </p>
 * @author higa
 */
public class LearnerClassifier {

	/**
	 * <p>
	 * 判定パラメータ.
	 * </p>
	 */
	private LearnerClassifierParameter parameter = null;

	
	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param parameter 判定パラメータ
	 */
	public LearnerClassifier(LearnerClassifierParameter parameter) {
		this.parameter = parameter;
	}

	/**
	 * <p>
	 * 学習機械で判定する.
	 * </p>
	 * @param loader データローダ
	 * @return 判定結果リスト
	 * @throws Exception
	 */
	public LearnerClassifyResultEnity classify(DataLoaderImpl loader) throws Exception {

		try {

			// データローダのオープン
			loader.open();

			// データがなくなるまで判定
			List<DataLearnerClassifyResultEntity> classifieds = new ArrayList<DataLearnerClassifyResultEntity>();
			for (int i = 1;;i++) {
				// データの取得
				Map<String, Object> data = loader.fetch();
				if (data == null) {
					break;
				}

				// データ項目名を判別項目名に置換
				Map<String, String> itemNames = parameter.getItemNames();
				for (String itemName: itemNames.keySet()) {
					String dataName = itemNames.get(itemName);
					Object value = data.remove(dataName);
					data.put(itemName, value);
				}

				// 判定
				ClassifierImpl classifier = parameter.getClassifier().getClassifier(parameter.getLearner());
				AnswerEntity answer = classifier.classify(data, parameter.getDataType());

				// データ情報を付加
				DataLearnerClassifyResultEntity classified = new DataLearnerClassifyResultEntity();
				classified.setId((String)data.get("identification"));
				classified.setCorrect((String)data.get("correct"));
				classified.setAnswer(answer);
				classifieds.add(classified);
				System.out.println(i);
			}

			LearnerClassifyResultEnity result = new LearnerClassifyResultEnity();
			result.setLearner(parameter.getLearner());
			result.setClassifieds(classifieds);

			return result;

		} finally {
			if (loader != null) try { loader.close(); } catch (Exception e) {}
		}
	}

	/**
	 * <p>
	 * データを学習機械で判定する.
	 * </p>
	 * @param data 学習機械
	 * @return 判定結果
	 * @throws Exception
	 */
	public DataLearnerClassifyResultEntity classify(Map<String, Object> data) throws Exception {

		// データ項目名を判別項目名に置換
		Map<String, String> itemNames = parameter.getItemNames();
		for (String itemName: itemNames.keySet()) {
			String dataName = itemNames.get(itemName);
			Object value = data.remove(dataName);
			data.put(itemName, value);
		}

		// 判定
		ClassifierImpl classifier = parameter.getClassifier().getClassifier(parameter.getLearner());
		AnswerEntity answer = classifier.classify(data, parameter.getDataType());

		// データ情報を付加
		DataLearnerClassifyResultEntity classified = new DataLearnerClassifyResultEntity();
		classified.setId((String)data.get("identification"));
		classified.setCorrect((String)data.get("correct"));
		classified.setAnswer(answer);

		return classified;
	}
}
