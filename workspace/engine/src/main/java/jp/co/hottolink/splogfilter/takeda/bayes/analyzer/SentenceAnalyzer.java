package jp.co.hottolink.splogfilter.takeda.bayes.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.common.util.SenUtil;
import jp.co.hottolink.splogfilter.takeda.bayes.config.BayesFilterConfig;
import jp.co.hottolink.splogfilter.takeda.bayes.entity.MorphemeResultEntity;
import jp.co.hottolink.splogfilter.takeda.bayes.entity.SentenceResultEntity;

import net.java.sen.Token;

/**
 * <p>
 * 文章の分析クラス.
 * </p>
 * @author higa
 */
public class SentenceAnalyzer {

	/**
	 * <p>
	 * 分析を行う.
	 * </p>
	 * @param sentence 文章
	 * @return 分析結果
	 * @throws IOException
	 */
	public SentenceResultEntity analyze(String sentence) throws IOException {

		if (sentence == null) {
			return null;
		}

		// 条件付き確率の取得
		List<MorphemeResultEntity> morphemes = getLikelihoods(sentence);

		// 分析結果を求める
		Double splogClassify = getSplogClassify(morphemes);
		Double blogClassify = getBlogClassify(morphemes);

		// 分析結果オブジェクトに格納
		SentenceResultEntity result = new SentenceResultEntity();
		result.setMorphemes(morphemes);
		result.setSplogClassify(splogClassify);
		result.setBlogClassify(blogClassify);
		result.setSentence(sentence);

		return result;
	}

	/**
	 * <p>
	 * 文章を形態素解析し、形態素の条件確率を取得する.
	 * </p>
	 * @param sentence 文章
	 * @return 形態素の条件確率
	 * @throws IOException
	 */
	private List<MorphemeResultEntity> getLikelihoods(String sentence) throws IOException {

		List<MorphemeResultEntity> list = new ArrayList<MorphemeResultEntity>();
		if (sentence == null) {
			return list;
		}

		// 形態素解析
		List<Token> morphemes = SenUtil.getMorphemes(sentence);

		// 形態素の条件確率の取得
		for (Token morpheme: morphemes) {
			// 条件付き確率の取得
		    String surface = morpheme.getSurface();
			Double splogLikelihood = getSplogLikelihood(surface);
			Double blogLikelihood = getBlogLikelihood(surface);
			// 分析結果オブジェクトに格納
		    MorphemeResultEntity entity = new MorphemeResultEntity(morpheme);
		    entity.setSplogLikelihood(splogLikelihood);
		    entity.setBlogLikelihood(blogLikelihood);
		    // リストに追加
		    list.add(entity);
		}

		return list;
	}

	/**
	 * <p>
	 * スプログの条件付き確率を求める.
	 * </p>
	 * @param surface 形態素
	 * @return スプログの条件付き確率
	 */
	private Double getSplogLikelihood(String surface) {

		Double likelihood = BayesFilterConfig.splogDefaultLikelihood;
		if (surface == null) {
			return null;
		}

		if (!BayesFilterConfig.splogLikelihoods.containsKey(surface)) {
			return likelihood;
		}

		likelihood = BayesFilterConfig.splogLikelihoods.get(surface);
		if (likelihood == null) {
			return null;
		}

		return likelihood;
	}

	/**
	 * <p>
	 * ブログの条件付き確率を求める.
	 * </p>
	 * @param surface 形態素
	 * @return ブログの条件付き確率
	 */
	private Double getBlogLikelihood(String surface) {

		Double likelihood = BayesFilterConfig.blogDefaultLikelihood;
		if (surface == null) {
			return null;
		}

		if (!BayesFilterConfig.blogLikelihoods.containsKey(surface)) {
			return likelihood;
		}

		likelihood = BayesFilterConfig.blogLikelihoods.get(surface);
		if (likelihood == null) {
			return null;
		}

		return likelihood;
	}

	/**
	 * <p>
	 * スプログの確率を求める.
	 * </p>
	 * @param tokens 形態素の確率
	 * @return スプログの確率
	 */
	private Double getSplogClassify(List<MorphemeResultEntity> morphemes) {

		double classify = Math.log10(BayesFilterConfig.splogPrior);
		if (morphemes == null) {
			return null;
		}
		
		for (MorphemeResultEntity morpheme: morphemes) {
			classify += Math.log10(morpheme.getSplogLikelihood());
		}

		return classify;
	}

	/**
	 * <p>
	 * ブログの確率を求める.
	 * </p>
	 * @param tokens 形態素の確率
	 * @return ブログの確率
	 */
	private Double getBlogClassify(List<MorphemeResultEntity> morphemes) {

		double classify = Math.log10(BayesFilterConfig.blogPrior);
		if (morphemes == null) {
			return null;
		}
		
		for (MorphemeResultEntity morpheme: morphemes) {
			classify += Math.log10(morpheme.getBlogLikelihood());
		}

		return classify;
	}
}
