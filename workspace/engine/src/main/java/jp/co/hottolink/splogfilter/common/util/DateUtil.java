package jp.co.hottolink.splogfilter.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 日付のUtilクラス.
 * </p>
 * @author higa
 */
public class DateUtil {

	/**
	 * <p>
	 * デフォルトの日付パターン.
	 * </p>
	 */
	private static final String DEFAULT_DATE_PATTERN = "yyyy-M-d";

	/**
	 * <p>
	 * デフォルトの日時パターン.
	 * </p>
	 */
	private static final String DEFAULT_DATETIME_PATTERN = "yyyy-M-d H:m:s";

	/**
	 * <p>
	 * 1日のミリ秒数.
	 * </p>
	 */
	private static final int DAY_TO_MILLISECOND = 24 * 60 * 60 * 1000;

	/**
	 * <p>
	 * 文字列から日付に変換する.
	 * </p><p>
	 * <code>yyyy-M-D</code>のみ有効
	 * </p>
	 * @param string 文字列
	 * @return 日付
	 * @throws ParseException
	 */
	public static Date parseToDate(String string) throws ParseException {
		return parseToDate(string, DEFAULT_DATE_PATTERN);
	}

	/**
	 * <p>
	 * 文字列から日付に変換する.
	 * </p>
	 * @param string 文字列
	 * @param pattern 日付パターン
	 * @return 日付
	 * @throws ParseException
	 */
	public static Date parseToDate(String string, String pattern) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setLenient(false);
		return format.parse(string);
	}

	/**
	 * <p>
	 * 文字列からタイムスタンプに変換する.
	 * </p>
	 * @param string 文字列
	 * @param pattern 日付パターン
	 * @return タイムスタンプ
	 * @throws ParseException
	 */
	public static Timestamp parseToTimestamp(String string, String pattern) throws ParseException {
		Date date = parseToDate(string, pattern);
		return new Timestamp(date.getTime());
	}

	/**
	 * <p>
	 * 日付文字列のバリデートを行う.
	 * </p><pre>
	 * <code>yyyy-M-D</code>または<code>yyyy-M-D H:m:s</code>のみ有効
	 * </pre>
	 * @param dateString 日付文字列
	 * @return true:有効, false:無効
	 */
	public static boolean validate(String dateString) {
		if (validateDate(dateString) || validateDateTime(dateString)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * 日付文字列のバリデートを行う.
	 * </p><pre>
	 * <code>yyyy-M-D</code>のみ有効
	 * </pre>
	 * @param dateString 日付文字列
	 * @return true:有効, false:無効
	 */
	public static boolean validateDate(String dateString) {

		if (dateString == null) {
			return false;
		}

		if (!dateString.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
			return false;
		}

		try {
			parseToDate(dateString, DEFAULT_DATE_PATTERN);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * <p>
	 * 日付文字列のバリデートを行う.
	 * </p><pre>
	 * <code>yyyy-M-D H:m:s</code>のみ有効
	 * </pre>
	 * @param dateString 日付文字列
	 * @return true:有効, false:無効
	 */
	public static boolean validateDateTime(String dateString) {

		if (dateString == null) {
			return false;
		}

		if (!dateString.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
			return false;
		}

		try {
			parseToDate(dateString, DEFAULT_DATETIME_PATTERN);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * <p>
	 * 日付を文字列に変換する.
	 * </p>
	 * @param date 日付
	 * @param pattern 日付パターン
	 * @return 日付文字列
	 */
	public static String toString(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * <p>
	 * 1970年1月1日からの日数を求める.
	 * </p>
	 * @param date 日付
	 * @return 1970年1月1日からの日数
	 */
	public static Integer getOffsetDay(Date date) {

		if (date == null) {
			return null;
		}

		long time = date.getTime() - getTimezoneOffset();
		return (int)(time / DAY_TO_MILLISECOND);
	}

	/**
	 * <p>
	 * デフォルトのタイムゾーンのオフセットを取得する.
	 * </p>
	 * @return デフォルトのタイムゾーンオフセット
	 */
	public static long getTimezoneOffset() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		return calendar.getTimeInMillis();
	}

	/**
	 * <p>
	 * 1970年1月1日からの日数の日付を取得する.
	 * </p>
	 * @param day 1970年1月1日からの日数
	 * @return 日付
	 */
	public static Date getOffsetDate(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}
}
