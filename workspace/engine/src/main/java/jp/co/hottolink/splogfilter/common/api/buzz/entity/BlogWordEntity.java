package jp.co.hottolink.splogfilter.common.api.buzz.entity;

import java.io.Serializable;

/**
 * <p>
 * 話題語のEntityクラス.
 * </p><pre>
 * 話題語APIの結果を格納する.
 * </pre>
 * @author higa
 */
public class BlogWordEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * 話題語.
	 * </p>
	 */
	private String word = null;

	/**
	 * <p>
	 * 順位.
	 * </p>
	 */	
	private int rank = 0;

	/**
	 * <p>
	 * 記事数.
	 * </p>
	 */
	private int count = 0;

	/**
	 * <p>
	 * 記事数を取得する.
	 * </p>
	 * @return 記事数
	 */
	public int getCount() {
		return count;
	}

	/**
	 * <p>
	 * 記事数を設定する.
	 * </p>
	 * @param count 記事数
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * <p>
	 * 順位を取得する.
	 * </p>
	 * @return 順位
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * <p>
	 * 順位を設定する.
	 * </p>
	 * @param rank 順位
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * <p>
	 * 話題語を取得する.
	 * </p>
	 * @return 話題語
	 */
	public String getWord() {
		return word;
	}

	/**
	 * <p>
	 * 話題語を設定する.
	 * </p>
	 * @param word 話題語
	 */
	public void setWord(String word) {
		this.word = word;
	}
}
