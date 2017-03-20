package jp.co.hottolink.splogfilter.learning.logic.classify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl;
import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;
import jp.co.hottolink.splogfilter.learning.boosting.entity.HypothesisEntity;
import jp.co.hottolink.splogfilter.learning.master.MasterDataCache;
import jp.co.hottolink.splogfilter.learning.master.entity.ClassifiedTypeEntity;

/**
 * <p>
 * 学習機械判定のリクエストを解析するクラス.
 * </p>
 * @author higa
 */
public class LearnerClassifyRequestParser {

	/**
	 * <p>
	 * データ種別.
	 * </p>
	 */
	private String dataType = null;

	/**
	 * <p>
	 * 判別種別.
	 * </p>
	 */
	private String classifiedType = null;

	/**
	 * <p>
	 * データ項目名.
	 * </p>
	 */
	private Map<String, String> itemNames = null;

	/**
	 * <p>
	 * リクエストを解析し、判定パラメータを作成する.
	 * </p>
	 * @param loader データローダ
	 * @return 判定パラメータ
	 * @throws Exception
	 */
	public LearnerClassifierParameter parse(DataLoaderImpl loader) throws Exception {

		// 判別種別の取得
		ClassifiedTypeEntity classifier = MasterDataCache.getClassifiedType(classifiedType);

		// 学習機械の取得
		Map<String, Double> reliabilities = getLearner(loader);

		// 学習機械情報に判別機情報を追加
		List<HypothesisEntity> learner = new ArrayList<HypothesisEntity>();
		for (String key: reliabilities.keySet()) {
			ClassifierEntity master = MasterDataCache.getClassifier(key);
			HypothesisEntity hypothesis = new HypothesisEntity();
			hypothesis.setClassifier(master);
			hypothesis.setReliability(reliabilities.get(key));
			learner.add(hypothesis);
		}

		LearnerClassifierParameter parameter = new LearnerClassifierParameter();
		parameter.setDataType(dataType);
		parameter.setItemNames(itemNames);
		parameter.setClassifier(classifier);
		parameter.setLearner(learner);

		return parameter;
	}

	/**
	 * <p>
	 * データ種別を取得する.
	 * </p>
	 * @return データ種別
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * <p>
	 * データ種別を設定する.
	 * </p>
	 * @param dataType データ種別
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * <p>
	 * 判別種別を取得する.
	 * </p>
	 * @return 判別種別
	 */
	public String getClassifiedType() {
		return classifiedType;
	}

	/**
	 * <p>
	 * 判別種別を設定する.
	 * </p>
	 * @param classifiedType 判別種別
	 */
	public void setClassifiedType(String classifiedType) {
		this.classifiedType = classifiedType;
	}

	/**
	 * <p>
	 * データ項目名を取得する.
	 * </p>
	 * @return データ項目名
	 */
	public Map<String, String> getItemNames() {
		return itemNames;
	}

	/**
	 * <p>
	 * データ項目名を設定する.
	 * </p>
	 * @param itemNames データ項目名
	 */
	public void setItemNames(Map<String, String> itemNames) {
		this.itemNames = itemNames;
	}

	/**
	 * <p>
	 * 学習機械を取得する.
	 * </p>
	 * @param loader データローダ
	 * @return 学習機械
	 * @throws Exception 
	 */
	private Map<String, Double> getLearner(DataLoaderImpl loader) throws Exception {

		try {
			// データローダのオープン
			loader.open();

			// データの取得
			Map<String, Double> learner = new HashMap<String, Double>();
			for (;;) {
				Map<String, Object> data = loader.fetch();
				if (data == null) {
					break;
				}

				String classifier = (String)data.get("classifier");
				Double reliability = (Double)data.get("reliability");
				learner.put(classifier, reliability);
			}

			return learner;

		} finally {
			if (loader != null) try { loader.close(); } catch (Exception e) {}
		}
	}
}
