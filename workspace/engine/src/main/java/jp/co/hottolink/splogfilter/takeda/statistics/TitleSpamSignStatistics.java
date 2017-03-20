package jp.co.hottolink.splogfilter.takeda.statistics;

import java.io.Serializable;

/**
 * <p>
 * タイトルスパム記号(type=25)の統計データクラス.
 * </p>
 * @author higa
 */
public class TitleSpamSignStatistics implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * スパム記号(type=25).
	 * </p>
	 */
	private String spamSign = null;

	/**
	 * <p>
	 * 出現数.
	 * </p>
	 */
	private int count = 0;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param spamSign スパム記号
	 */
	public TitleSpamSignStatistics(String spamSign) {
		this.spamSign = spamSign;
	}

	/**
	 * <p>
	 * 出現数を取得する.
	 * </p>
	 * @return 出現数
	 */
	public int getCount() {
		return count;
	}

	/**
	 * <p>
	 * 出現数を設定する.
	 * </p>
	 * @param count 出現数
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * <p>
	 * スパム記号を取得する.
	 * </p>
	 * @return スパム記号
	 */
	public String getSpamSign() {
		return spamSign;
	}

	/**
	 * <p>
	 * スパム記号を設定する.
	 * </p>
	 * @param titleTag スパム記号
	 */
	public void setSpamSign(String titleTag) {
		this.spamSign = titleTag;
	}

	/**
	 * <p>
	 * スパム記号の文字数を取得する.
	 * </p>
	 * @return 文字数
	 */
	public int getSpamSignLength() {
		if (spamSign == null) {
			return 0;
		} else {
			return spamSign.length();
		}
	}
}
