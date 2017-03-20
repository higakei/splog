package jp.co.hottolink.splogfilter.learning.boosting.entity;

import java.io.Serializable;

/**
 * <p>
 * 判別器の解答のEntityクラス.
 * </p>
 * @author higa
 */
public class AnswerEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -2392686359985778604L;

	/**
	 * <p>
	 * 解答.
	 * </p>
	 */
	private Object answer = null;

	/**
	 * <p>
	 * 照合結果.
	 * </p>
	 */
	private String matching = null;

	/**
	 * <p>
	 * 正誤.
	 * </p>
	 */
	private boolean isMatched = false;

	/**
	 * <p>
	 * ラベル.
	 * </p>
	 */
	private int signum = 0;

	/**
	 * <p>
	 * 詳細情報.
	 * </p>
	 */
	private Object detail = null;

	/**
	 * <p>
	 * 解答を取得する.
	 * </p>
	 * @return 解答
	 */
	public Object getAnswer() {
		return answer;
	}

	/**
	 * <p>
	 * 解答を設定する.
	 * </p>
	 * @param answer 解答
	 */
	public void setAnswer(Object answer) {
		this.answer = answer;
	}

	/**
	 * <p>
	 * 照合結果を取得する.
	 * </p>
	 * @return 正誤
	 */
	public String getMatching() {
		return matching;
	}

	/**
	 * <p>
	 * 照合結果を設定する.
	 * </p>
	 * @param isMatched <code>true</code>:正解, <code>false</code>:不正解
	 */
	public void setMatching(boolean isMatched) {
		this.isMatched = isMatched;
		matching = String.valueOf(isMatched);
	}

	/**
	 * <p>
	 * 正誤を取得する.
	 * </p>
	 * @return <code>true</code>:正解, <code>false</code>:不正解
	 */
	public boolean isMatched() {
		return isMatched;
	}

	/**
	 * <p>
	 * ラベルを取得する.
	 * </p>
	 * @return ラベル
	 */
	public int getSignum() {
		return signum;
	}

	/**
	 * <p>
	 * ラベルを設定する.
	 * </p>
	 * @param signum ラベル
	 */
	public void setSignum(boolean signum) {
		if (signum) {
			this.signum = 1;
		} else {
			this.signum = -1;
		}
	}

	/**
	 * <p>
	 * 詳細情報を取得する.
	 * </p>
	 * @return 詳細情報
	 */
	public Object getDetail() {
		return detail;
	}

	/**
	 * <p>
	 * 詳細情報を設定する.
	 * </p>
	 * @param detail 詳細情報
	 */
	public void setDetail(Object detail) {
		this.detail = detail;
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return answer.toString();
	}
}
