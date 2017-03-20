package jp.co.hottolink.splogfilter.takeda.statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * タイトルの統計データクラス.
 * </p>
 * @author higa
 */
public class TitleStatistics implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * タイトル.
	 * </p>
	 */
	private String title = null;

	/**
	 * <p>
	 * スパムワード(type=15).
	 * </p>
	 */
	private List<String> spamWords = new ArrayList<String>();

	/**
	 * <p>
	 * スパム記号の統計データ.
	 * </p>
	 */
	private List<TitleSpamSignStatistics> spamSigns = new ArrayList<TitleSpamSignStatistics>();

	/**
	 * <p>
	 * スペースの出現数.
	 * </p>
	 */
	private int spaceCount = 0;

	/**
	 * <p>
	 * コンストラクタ.
	 * </p>
	 * @param title タイトル
	 */
	public TitleStatistics(String title) {
		this.title = title;
	}

	/**
	 * <p>
	 * スペースの出現数を取得する.
	 * </p>
	 * @return 出現数
	 */
	public int getSpaceCount() {
		return spaceCount;
	}

	/**
	 * <p>
	 * スペースの出現数を設定する.
	 * </p>
	 * @param spaceCount 出現数
	 */
	public void setSpaceCount(int spaceCount) {
		this.spaceCount = spaceCount;
	}

	/**
	 * <p>
	 * スパムワードを取得する.
	 * </p>
	 * @return スパムワード
	 */
	public List<String> getSpamWords() {
		return spamWords;
	}

	/**
	 * <p>
	 * スパムワードを設定する.
	 * </p>
	 * @param spamWords スパムワード
	 */
	public void setSpamWords(List<String> spamWords) {
		this.spamWords = spamWords;
	}

	/**
	 * <p>
	 * タイトルを取得する.
	 * </p>
	 * @return タイトル
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * <p>
	 * タイトルを設定する.
	 * </p>
	 * @param title タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * <p>
	 * スパム記号の統計データを取得する.
	 * </p>
	 * @return 統計データ
	 */
	public List<TitleSpamSignStatistics> getSpamSigns() {
		return spamSigns;
	}

	/**
	 * <p>
	 * スパム記号の統計データを設定する.
	 * </p>
	 * @param spamSigns 統計データ
	 */
	public void setSpamSigns(List<TitleSpamSignStatistics> spamSigns) {
		this.spamSigns = spamSigns;
	}

	/**
	 * <p>
	 * スパムワード数を取得する.
	 * </p>
	 * @return スパムワード数
	 */
	public int getSpamWordCount() {
		if (spamWords == null) {
			return 0;
		} else {
			return spamWords.size();
		}
	}

	/**
	 * <p>
	 * スパムワードの総文字数を取得する.
	 * </p><pre>
	 * スパムワードを連結したの文字列の文字数
	 * </pre>
	 * @param isSpace true:スペースで区切る, false:スペースで区切らない
	 * @return 総文字数
	 */
	public int getSpamWordsLength(boolean isSpace) {
		int length = 0;
		if (spamWords != null) {
			for (String spamTitle: spamWords) {
				if ((spamTitle != null) && !spamTitle.isEmpty()) {
					length += spamTitle.length();
					if (isSpace) ++length;
				}
			}
		}
		return length;
	}

	/**
	 * <p>
	 * タイトルの文字数を取得する.
	 * </p>
	 * @return 文字数
	 */
	public int getTitleLength() {
		if (title == null) {
			return 0;
		} else {
			return title.length();
		}
	}

	/**
	 * <p>
	 * スパム記号の総出現数を取得する.
	 * </p>
	 * @return 総出現数
	 */
	public int getSpamSignsCount() {
		int count = 0;
		if (spamSigns != null) {
			for (TitleSpamSignStatistics spamSign: spamSigns) {
				count += spamSign.getCount();
			}
		}
		return count;
	}

	/**
	 * <p>
	 * スパム記号の総文字数を取得する.
	 * </p>
	 * @return 総文字数
	 */
	public int getSpamSignsLength() {
		int length = 0;
		if (spamSigns != null) {
			for (TitleSpamSignStatistics spamSign: spamSigns) {
				length += spamSign.getSpamSignLength();
			}
		}
		return length;
	}	
}
