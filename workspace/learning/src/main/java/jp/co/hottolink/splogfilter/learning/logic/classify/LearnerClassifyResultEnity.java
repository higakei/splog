package jp.co.hottolink.splogfilter.learning.logic.classify;

import java.io.Serializable;
import java.util.List;

import jp.co.hottolink.splogfilter.learning.boosting.entity.HypothesisEntity;

/**
 * <p>
 * 学習機械の判定結果のEntityクラス.
 * </p>
 * @author higa
 */
public class LearnerClassifyResultEnity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 9106987418703950340L;

	/**
	 * <p>
	 * 学習機械情報.
	 * </p>
	 */
	private List<HypothesisEntity> learner = null;

	/**
	 * <p>
	 * 判定結果.
	 * </p>
	 */
	private List<DataLearnerClassifyResultEntity> classifieds = null;

	/**
	 * <p>
	 * 学習機械情報を取得する.
	 * </p>
	 * @return 学習機械情報
	 */
	public List<HypothesisEntity> getLearner() {
		return learner;
	}

	/**
	 * <p>
	 * 学習機械情報を設定する.
	 * </p>
	 * @param learner 学習機械情報
	 */
	public void setLearner(List<HypothesisEntity> learner) {
		this.learner = learner;
	}

	/**
	 * <p>
	 * 判定結果を取得する.
	 * </p>
	 * @return 判定結果
	 */
	public List<DataLearnerClassifyResultEntity> getClassifieds() {
		return classifieds;
	}

	/**
	 * <p>
	 * 判定結果を設定する.
	 * </p>
	 * @param classifieds 判定結果
	 */
	public void setClassifieds(List<DataLearnerClassifyResultEntity> classifieds) {
		this.classifieds = classifieds;
	}
}
