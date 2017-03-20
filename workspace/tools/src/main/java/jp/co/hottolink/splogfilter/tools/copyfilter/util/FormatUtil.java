package jp.co.hottolink.splogfilter.tools.copyfilter.util;

import java.text.DecimalFormat;

/**
 * <p>
 * フォーマットのUtilクラス.
 * </p>
 * @author higa
 */
public class FormatUtil {

	/**
	 * <p>
	 * スプログ判定をフォーマットする.
	 * </p>
	 * @param isSplog スプログ判定
	 * @return フォーマットした文字列
	 */
	public static String formatIsSplog(boolean isSplog) {
		if (isSplog) {
			return "splog";
		} else {
			return "blog";
		}
	}

	/**
	 * <p>
	 * 10進数のフォーマットを行う.
	 * </p>
	 * @param number 10進数
	 * @param pattern 表示形式
	 * @return フォーマットした文字列
	 */
	public static String formatDecimal(double number, String pattern) {
		DecimalFormat formatter = new DecimalFormat(pattern);
		return formatter.format(number);
	}
}
