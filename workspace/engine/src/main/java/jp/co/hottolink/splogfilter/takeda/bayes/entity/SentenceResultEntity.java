package jp.co.hottolink.splogfilter.takeda.bayes.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 文章の分析結果のEntityクラス.
 * </p>
 * @author higa
 */
public class SentenceResultEntity implements Serializable {


	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -4991311346266092489L;

	/**
	 * <p>
	 * 文章.
	 * </p>
	 */
	private String sentence = null;

	/**
	 * <p>
	 * 形態素の分析結果.
	 * </p>
	 */
	private List<MorphemeResultEntity> morphemes = null;

	/**
	 * <p>
	 * スプログの確率.
	 * </p>
	 */
	private Double splogClassify = null;

	/**
	 * <p>
	 * ブログの確率.
	 * </p>
	 */
	private Double blogClassify = null;

	/**
	 * <p>
	 * 形態素の分析結果を取得する.
	 * </p>
	 * @return 形態素の分析結果
	 */
	public List<MorphemeResultEntity> getMorphemes() {
		return morphemes;
	}

	/**
	 * <p>
	 * 形態素の分析結果を設定する.
	 * </p>
	 * @param morphemes 形態素の分析結果
	 */
	public void setMorphemes(List<MorphemeResultEntity> morphemes) {
		this.morphemes = morphemes;
	}

	/**
	 * <p>
	 * スプログの確率を取得する.
	 * </p>
	 * @return ブログの確率
	 */
	public Double getSplogClassify() {
		return splogClassify;
	}

	/**
	 * <p>
	 * スプログの確率を設定する.
	 * </p>
	 * @param splogClassify スプログの確率
	 */
	public void setSplogClassify(Double splogClassify) {
		this.splogClassify = splogClassify;
	}

	/**
	 * <p>
	 * ブログの確率を取得する.
	 * </p>
	 * @return ブログの確率
	 */
	public Double getBlogClassify() {
		return blogClassify;
	}

	/**
	 * <p>
	 * ブログの確率を設定する.
	 * </p>
	 * @param blogClassify ブログの確率
	 */
	public void setBlogClassify(Double blogClassify) {
		this.blogClassify = blogClassify;
	}

	/**
	 * <p>
	 * スプログ判定を行う.
	 * </p>
	 * @return 判定結果
	 */
	public boolean isSplog() {
		if (splogClassify > blogClassify) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * 文章を取得する.
	 * </p>
	 * @return 文章
	 */
	public String getSentence() {
		return sentence;
	}

	/**
	 * <p>
	 * 文章を設定する.
	 * </p>
	 * @param sentence 文章
	 */
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
}
