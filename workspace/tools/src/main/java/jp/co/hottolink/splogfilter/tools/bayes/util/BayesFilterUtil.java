package jp.co.hottolink.splogfilter.tools.bayes.util;

/**
 * <p>
 * ベイズフィルターのUtilクラス.
 * </p>
 * @author higa
 *
 */
public class BayesFilterUtil {

	/**
	 * <p>
	 * カテゴリを取得する.
	 * </p>
	 * @param isSplog スプログフラグ
	 * @return カテゴリ
	 */
	public static String getCategory(boolean isSplog) {
		if (isSplog) {
			return "splog";
		} else {
			return "blog";
		}
	}

	/**
	 * <p>
	 * カテゴリがスプログかどうか判定する.
	 * </p>
	 * @param category カテゴリ
	 * @return true:スプログ, false:ブログ
	 */
	public static boolean isSplog(String category) {
		if ("splog".equals(category)) {
			return true;
		} else {
			return false;
		}
	}

}
