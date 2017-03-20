package jp.co.hottolink.splogfilter.tools.bayes.entity;

import java.io.Serializable;

/**
 * <p>
 * 形態素頻度テーブルのEntityクラス.
 * </p>
 * @author higa
 */
public class MorphemeCountEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -6472769093987292031L;

	/**
	 * <p>
	 * 形態素ID.
	 * </p>
	 */
	private int id = 0;

	/**
	 * <p>
	 * 表層形.
	 * </p>
	 */
	private String surface = null;

	/**
	 * <p>
	 * 品詞.
	 * </p>
	 */
	private String pos = null;

	/**
	 * <p>
	 * スプログのタイトル出現頻度.
	 * </p>
	 */
	private int splogTitleTF = 0;

	/**
	 * <p>
	 * スプログのタイトルの文書頻度.
	 * </p>
	 */
	private int splogTitleDF = 0;

	/**
	 * <p>
	 * スプログの本文出現頻度.
	 * </p>
	 */
	private int splogContentTF = 0;

	/**
	 * <p>
	 * スプログの本文文書頻度.
	 * </p>
	 */
	private int splogContentDF = 0;

	/**
	 * <p>
	 * ブログのタイトル出現頻度.
	 * </p>
	 */
	private int blogTitleTF = 0;

	/**
	 * <p>
	 * ブログのタイトルの文書頻度.
	 * </p>
	 */
	private int blogTitleDF = 0;

	/**
	 * <p>
	 * ブログの本文出現頻度.
	 * </p>
	 */
	private int blogContentTF = 0;

	/**
	 * <p>
	 * ブログの本文文書頻度.
	 * </p>
	 */
	private int blogContentDF = 0;

	/**
	 * <p>
	 * スプログのタイトル出現頻度を取得する.
	 * </p>
	 * @return スプログのタイトル出現頻度
	 */
	public int getSplogTitleTF() {
		return splogTitleTF;
	}

	/**
	 * <p>
	 * スプログのタイトル出現頻度を設定する.
	 * </p>
	 * @param splogTitleTF スプログのタイトル出現頻度
	 */
	public void setSplogTitleTF(int splogTitleTF) {
		this.splogTitleTF = splogTitleTF;
	}

	/**
	 * <p>
	 * スプログのタイトル文書頻度を取得する.
	 * </p>
	 * @return スプログのタイトル文書頻度
	 */
	public int getSplogTitleDF() {
		return splogTitleDF;
	}

	/**
	 * <p>
	 * スプログのタイトル文書頻度を設定する.
	 * </p>
	 * @param splogTitleDF スプログのタイトル文書頻度
	 */
	public void setSplogTitleDF(int splogTitleDF) {
		this.splogTitleDF = splogTitleDF;
	}

	/**
	 * <p>
	 * スプログの本文出現頻度を取得する.
	 * </p>
	 * @return スプログの本文出現頻度
	 */
	public int getSplogContentTF() {
		return splogContentTF;
	}

	/**
	 * <p>
	 * スプログの本文出現頻度を設定する.
	 * </p>
	 * @param splogContentTF スプログの本文出現頻度
	 */
	public void setSplogContentTF(int splogContentTF) {
		this.splogContentTF = splogContentTF;
	}

	/**
	 * <p>
	 * スプログの本文文書頻度を取得する.
	 * </p>
	 * @return スプログの本文文書頻度
	 */
	public int getSplogContentDF() {
		return splogContentDF;
	}

	/**
	 * <p>
	 * スプログの本文文書頻度を設定する.
	 * </p>
	 * @param splogContentDF スプログの本文文書頻度
	 */
	public void setSplogContentDF(int splogContentDF) {
		this.splogContentDF = splogContentDF;
	}

	/**
	 * <p>
	 * 形態素IDを取得する.
	 * </p>
	 * @return 形態素ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * <p>
	 * 形態素IDを設定する.
	 * </p>
	 * @param id 形態素ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * <p>
	 * 表層形を取得する.
	 * </p>
	 * @return 表層形
	 */
	public String getSurface() {
		return surface;
	}

	/**
	 * <p>
	 * 表層形を設定する.
	 * </p>
	 * @param surface 表層形
	 */
	public void setSurface(String surface) {
		this.surface = surface;
	}

	/**
	 * <p>
	 * 品詞を取得する.
	 * </p>
	 * @return 品詞
	 */
	public String getPos() {
		return pos;
	}

	/**
	 * <p>
	 * 品詞を設定する.
	 * </p>
	 * @param pos 品詞
	 */
	public void setPos(String pos) {
		this.pos = pos;
	}

	/**
	 * <p>
	 * スプログのタイトル出現頻度を加算する.
	 * </p>
	 * @param splogTitleTF 加算するスプログのタイトル出現頻度
	 */
	public void addSplogTitleTF(int splogTitleTF) {
		this.splogTitleTF += splogTitleTF;
	}

	/**
	 * <p>
	 * スプログのタイトル文書頻度を加算する.
	 * </p>
	 * @param splogTitleTF 加算するスプログのタイトル文書頻度
	 */
	public void addSplogTitleDF(int splogTitleTF) {
		this.splogTitleDF += splogTitleTF;
	}

	/**
	 * <p>
	 * スプログの本文出現頻度を加算する.
	 * </p>
	 * @param splogContentTF 加算するスプログの本文出現頻度
	 */
	public void addSplogContentTF(int splogContentTF) {
		this.splogContentTF += splogContentTF;
	}

	/**
	 * <p>
	 * スプログの本文文書頻度を加算する.
	 * </p>
	 * @param splogContentDF 加算するスプログの本文文書頻度
	 */
	public void addSplogContentDF(int splogContentDF) {
		this.splogContentDF += splogContentDF;
	}

	/**
	 * <p>
	 * スプログのタイトル出現頻度を加算する.
	 * </p>
	 * @param blogTitleTF 加算するスプログのタイトル出現頻度
	 */
	public void addBlogTitleTF(int blogTitleTF) {
		this.blogTitleTF += blogTitleTF;
	}

	/**
	 * <p>
	 * スプログのタイトル文書頻度を加算する.
	 * </p>
	 * @param blogTitleTF 加算するスプログのタイトル文書頻度
	 */
	public void addBlogTitleDF(int blogTitleTF) {
		this.blogTitleDF += blogTitleTF;
	}

	/**
	 * <p>
	 * スプログの本文出現頻度を加算する.
	 * </p>
	 * @param blogContentTF 加算するスプログの本文出現頻度
	 */
	public void addBlogContentTF(int blogContentTF) {
		this.blogContentTF += blogContentTF;
	}

	/**
	 * <p>
	 * スプログの本文文書頻度を加算する.
	 * </p>
	 * @param blogContentDF 加算するスプログの本文文書頻度
	 */
	public void addBlogContentDF(int blogContentDF) {
		this.blogContentDF += blogContentDF;
	}
	
	/**
	 * <p>
	 * ブログのタイトル出現頻度を取得する.
	 * </p>
	 * @return ブログのタイトル出現頻度
	 */
	public int getBlogTitleTF() {
		return blogTitleTF;
	}

	/**
	 * <p>
	 * ブログのタイトル出現頻度を設定する.
	 * </p>
	 * @param blogTitleTF ブログのタイトル出現頻度
	 */
	public void setBlogTitleTF(int blogTitleTF) {
		this.blogTitleTF = blogTitleTF;
	}

	/**
	 * <p>
	 * ブログのタイトルの文書頻度を取得する.
	 * </p>
	 * @return ブログのタイトルの文書頻度
	 */
	public int getBlogTitleDF() {
		return blogTitleDF;
	}

	/**
	 * <p>
	 * ブログのタイトルの文書頻度を設定する.
	 * </p>
	 * @param blogTitleDF ブログのタイトルの文書頻度
	 */
	public void setBlogTitleDF(int blogTitleDF) {
		this.blogTitleDF = blogTitleDF;
	}

	/**
	 * <p>
	 * ブログの本文出現頻度を取得する.
	 * </p>
	 * @return ブログの本文出現頻度
	 */
	public int getBlogContentTF() {
		return blogContentTF;
	}

	/**
	 * <p>
	 * ブログの本文出現頻度を設定する.
	 * </p>
	 * @param blogContentTF ブログの本文出現頻度
	 */
	public void setBlogContentTF(int blogContentTF) {
		this.blogContentTF = blogContentTF;
	}

	/**
	 * <p>
	 * ブログの本文文書頻度を取得する.
	 * </p>
	 * @return ブログの本文文書頻度
	 */
	public int getBlogContentDF() {
		return blogContentDF;
	}

	/**
	 * <p>
	 * ブログの本文文書頻度を設定する.
	 * </p>
	 * @param blogContentDF ブログの本文文書頻度
	 */
	public void setBlogContentDF(int blogContentDF) {
		this.blogContentDF = blogContentDF;
	}
}
