package jp.co.hottolink.splogfilter.takeda.statistics;

import java.io.Serializable;

/**
 * <p>
 * 単語の出現数の統計データクラス.
 * </p>
 * @author higa
 */
public class WordCountStatistics implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * 単語.
	 * </p>
	 */
	private String word = null;

	/**
	 * <p>
	 * 出現するブログ数.
	 * </p>
	 */
	private int blogCount = 0;

	/**
	 * <p>
	 * 出現するブログ数を取得する.
	 * </p>
	 * @return ブログ数
	 */
	public int getBlogCount() {
		return blogCount;
	}

	/**
	 * <p>
	 * 出現するブログ数を設定する.
	 * </p>
	 * @param blogCount ブログ数
	 */
	public void setBlogCount(int blogCount) {
		this.blogCount = blogCount;
	}

	/**
	 * <p>
	 * 単語を取得する.
	 * </p>
	 * @return 単語
	 */
	public String getWord() {
		return word;
	}

	/**
	 * <p>
	 * 単語を設定する.
	 * </p>
	 * @param word 単語
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * <p>
	 * 単語の文字数を取得する.
	 * </p>
	 * @return 文字数
	 */
	public int getWordLength() {
		if (word == null) {
			return 0;
		} else {
			return word.length();
		}
	}
}
