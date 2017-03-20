package jp.co.hottolink.splogfilter.learning.logic.matching;

import java.io.Serializable;
import java.util.List;

import jp.co.hottolink.splogfilter.learning.logic.trial.TrialEntity;

/**
 * <p>
 * トライアルの照合結果のEntityクラス.
 * </p>
 * @author higa
 */
public class MatchingEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -844259426478590061L;

	/**
	 * <p>
	 * トライアル情報.
	 * </p>
	 */
	private TrialEntity trial = null;

	/**
	 * <p>
	 * 判別器の照合結果.
	 * </p>
	 */
	private List<ClassifierMatchingEntity> matchings = null;

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
	 * 判別器の照合結果を取得する.
	 * </p>
	 * @return 判別器の照合結果
	 */
	public List<ClassifierMatchingEntity> getMatchings() {
		return matchings;
	}

	/**
	 * <p>
	 * 判別器の照合結果を設定する.
	 * </p>
	 * @param matchings 判別器の照合結果
	 */
	public void setMatchings(List<ClassifierMatchingEntity> matchings) {
		this.matchings = matchings;
	}
}
