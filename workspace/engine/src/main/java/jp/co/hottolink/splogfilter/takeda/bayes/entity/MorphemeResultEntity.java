package jp.co.hottolink.splogfilter.takeda.bayes.entity;

import java.io.Serializable;

import net.java.sen.Token;

/**
 * <p>
 * 形態素の分析結果のEntityクラス.
 * </p>
 * @author higa
 */
public class MorphemeResultEntity implements Serializable {


	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 8888074820359054182L;

	/**
	 * <p>
	 * 形態素.
	 * </p>
	 */
	private Token morpheme = null;

	/**
	 * <p>
	 * スプログの条件付き確率.
	 * </p>
	 */
	private Double splogLikelihood = null;

	/**
	 * <p>
	 * ブログの条件付き確率.
	 * </p>
	 */
	private Double blogLikelihood = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param morpheme 形態素
	 */
	public MorphemeResultEntity(Token morpheme) {
		this.morpheme = morpheme;
	}

	/**
	 * <p>
	 * 形態素を取得する.
	 * </p>
	 * @return 形態素
	 */
	public Token getMorpheme() {
		return morpheme;
	}

	/**
	 * <p>
	 * 形態素を設定する.
	 * </p>
	 * @param morpheme 形態素
	 */
	public void setMorpheme(Token morpheme) {
		this.morpheme = morpheme;
	}

	/**
	 * <p>
	 * スプログの条件付き確率を取得する.
	 * </p>
	 * @return splogLikelihood スプログの条件付き確率
	 */
	public Double getSplogLikelihood() {
		return splogLikelihood;
	}

	/**
	 * <p>
	 * スプログの条件付き確率を設定する.
	 * </p>
	 * @param splogLikelihood スプログの条件付き確率
	 */
	public void setSplogLikelihood(Double splogLikelihood) {
		this.splogLikelihood = splogLikelihood;
	}

	/**
	 * <p>
	 * ブログの条件付き確率を取得する.
	 * </p>
	 * @return ブログの条件付き確率
	 */
	public Double getBlogLikelihood() {
		return blogLikelihood;
	}

	/**
	 * <p>
	 * ブログの条件付き確率を設定する.
	 * </p>
	 * @param blogLikelihood ブログの条件付き確率
	 */
	public void setBlogLikelihood(Double blogLikelihood) {
		this.blogLikelihood = blogLikelihood;
	}
}
