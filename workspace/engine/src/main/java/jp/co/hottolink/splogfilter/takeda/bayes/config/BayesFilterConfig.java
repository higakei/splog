package jp.co.hottolink.splogfilter.takeda.bayes.config;

import java.util.Map;

/**
 * <p>
 * ベイズフィルターの設定クラス.
 * </p>
 * @author higa
 */
public class BayesFilterConfig {

	/**
	 * <p>
	 * スプログの事前確率.
	 * </p>
	 */
	public static Double splogPrior = null;

	/**
	 * <p>
	 * プログの事前確率.
	 * </p>
	 */
	public static Double blogPrior = null;

	/**
	 * <p>
	 * スプログの条件付き確率.
	 * </p>
	 */
	public static Map<String, Double> splogLikelihoods = null;

	/**
	 * <p>
	 * ブログの条件付き確率.
	 * </p>
	 */
	public static Map<String, Double> blogLikelihoods = null;

	/**
	 * <p>
	 * スプログの条件付き確率のデフォルト.
	 * </p>
	 */
	public static Double splogDefaultLikelihood = null;

	/**
	 * <p>
	 * プログの条件付き確率のデフォルト.
	 * </p>
	 */
	public static Double blogDefaultLikelihood = null;
}
