package jp.co.hottolink.splogfilter.learning.logic.classify;

import java.io.Serializable;

import jp.co.hottolink.splogfilter.learning.boosting.entity.AnswerEntity;

/**
 * <p>
 * 学習機械で判定されたデータの判定結果のEntityクラス.
 * </p>
 * @author higa
 */
public class DataLearnerClassifyResultEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -7419067770372804346L;

	/**
	 * <p>
	 * データID.
	 * </p>
	 */
	private String id = null;

	/**
	 * <p>
	 * 正解.
	 * </p>
	 */
	private String correct = null;

	/**
	 * <p>
	 * 判定.
	 * </p>
	 */
	private AnswerEntity answer = null;

	/**
	 * <p>
	 * データIDを取得する.
	 * </p>
	 * @return データID
	 */
	public String getId() {
		return id;
	}

	/**
	 * <p>
	 * データIDを設定する.
	 * </p>
	 * @param id データID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * <p>
	 * 正解を取得する.
	 * </p>
	 * @return 正解
	 */
	public String getCorrect() {
		return correct;
	}

	/**
	 * <p>
	 * 正解を設定する.
	 * </p>
	 * @param correct 正解
	 */
	public void setCorrect(String correct) {
		this.correct = correct;
	}

	/**
	 * <p>
	 * 判定を取得する.
	 * </p>
	 * @return 判定
	 */
	public AnswerEntity getAnswer() {
		return answer;
	}

	/**
	 * <p>
	 * 判定を設定する.
	 * </p>
	 * @param answer 判定
	 */
	public void setAnswer(AnswerEntity answer) {
		this.answer = answer;
	}
}
