package jp.co.hottolink.splogfilter.learning.logic.training;

import java.util.List;
import java.util.Map;

import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;
import jp.co.hottolink.splogfilter.learning.logic.trial.TrialEntity;

/**
 * <p>
 * 訓練情報のEntityクラス.
 * </p>
 * @author higa
 */
public class TrainingEntity {

	/**
	 * <p>
	 * 訓練ID.
	 * </p>
	 */
	private Integer id = null;

	/**
	 * <p>
	 * トライアル情報.
	 * </p>
	 */
	private TrialEntity trial = null;

	/**
	 * <p>
	 * 判別器リスト.
	 * </p>
	 */
	private List<ClassifierEntity> classifiers = null;

	/**
	 * <p>
	 * 学習機械.
	 * </p>
	 */
	private Map<String, Double> learner = null;

	/**
	 * <p>
	 * 訓練IDを取得する.
	 * </p>
	 * @return 訓練ID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * <p>
	 * 訓練IDを設定する.
	 * </p>
	 * @param id 訓練ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * <p>
	 * トライアル情報を取得する.
	 * </p>
	 * @return トライアル情報
	 */
	public TrialEntity getTrial() {
		return trial;
	}

	/**
	 * <p>
	 * トライアル情報を設定する.
	 * </p>
	 * @param trial トライアル情報
	 */
	public void setTrial(TrialEntity trial) {
		this.trial = trial;
	}

	/**
	 * <p>
	 * 判別器リストを取得する.
	 * </p>
	 * @return 判別器リスト
	 */
	public List<ClassifierEntity> getClassifiers() {
		return classifiers;
	}

	/**
	 * <p>
	 * 判別器リストを設定する.
	 * </p>
	 * @param classifiers 判別器リスト
	 */
	public void setClassifiers(List<ClassifierEntity> classifiers) {
		this.classifiers = classifiers;
	}

	/**
	 * <p>
	 * 学習機械を取得する.
	 * </p>
	 * @return learner
	 */
	public Map<String, Double> getLearner() {
		return learner;
	}

	/**
	 * <p>
	 * 学習機械を設定する.
	 * </p>
	 * @param learner 学習機械
	 */
	public void setLearner(Map<String, Double> learner) {
		this.learner = learner;
	}
}
