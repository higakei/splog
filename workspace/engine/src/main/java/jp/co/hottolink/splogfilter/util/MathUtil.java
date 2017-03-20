package jp.co.hottolink.splogfilter.util;

import java.math.BigDecimal;

/**
 * <p>
 * 数値処理のUtilクラス.
 * </p>
 * @author higa
 */
public class MathUtil {

	/**
	 * <p>
	 * 常用対数を指数表示する.
	 * </p>
	 * @param logarithm 常用対数
	 * @return 指数表示
	 */
	public static String toExponent(double logarithm) {

		StringBuffer sb = new StringBuffer();

		// 仮数部
		int exponent = (int)logarithm;
		double mantissa = Math.pow(10, (logarithm - exponent));
		BigDecimal bd = new BigDecimal(mantissa);
		bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
    	sb.append(bd);


		// 基数部
		sb.append("E");

		// 指数部
		if (exponent >= 0) {
			sb.append("+" + exponent);
		} else {
			sb.append(exponent);
		}

		return sb.toString();
	}
}
