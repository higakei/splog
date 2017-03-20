package jp.co.hottolink.splogfilter.learning.logic.classify;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.splogfilter.learning.boosting.entity.HypothesisEntity;
import jp.co.hottolink.splogfilter.learning.master.entity.ClassifiedTypeEntity;

/**
 * <p>
 * LearnerClassifierのパラメータクラス.
 * </p>
 * @author higa
 */
public class LearnerClassifierParameter implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -513931202430967898L;

	/**
	 * <p>
	 * データ種別.
	 * </p>
	 */
	private String dataType = null;

	/**
	 * <p>
	 * データ項目名.
	 * </p>
	 */
	private Map<String, String> itemNames = null;

	/**
	 * <p>
	 * 判別器.
	 * </p>
	 */
	private ClassifiedTypeEntity classifier = null;

	/**
	 * <p>
	 * 学習機械.
	 * </p>
	 */
	private List<HypothesisEntity> learner = null;

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
	 * データ項目名を取得する.
	 * </p>
	 * @return 判定項目
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
	 * 判別機を取得する.
	 * </p>
	 * @return 判別機
	 */
	public ClassifiedTypeEntity getClassifier() {
		return classifier;
	}

	/**
	 * <p>
	 * 判別機を設定する.
	 * </p>
	 * @param classifier 判別機
	 */
	public void setClassifier(ClassifiedTypeEntity classifier) {
		this.classifier = classifier;
	}

	/**
	 * <p>
	 * 学習機械を取得する.
	 * </p>
	 * @return 学習機械
	 */
	public List<HypothesisEntity> getLearner() {
		return learner;
	}

	/**
	 * <p>
	 * 学習機械を設定する.
	 * </p>
	 * @param learner 学習機械
	 */
	public void setLearner(List<HypothesisEntity> learner) {
		this.learner = learner;
	}
}
