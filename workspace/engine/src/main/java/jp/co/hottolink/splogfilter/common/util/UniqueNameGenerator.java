package jp.co.hottolink.splogfilter.common.util;

import java.util.Date;


/**
 * <p>
 * 一意な名前を生成するクラス.
 * </p>
 * @author higa
 */
public class UniqueNameGenerator {

	/**
	 * <p>
	 * 接頭辞から始まる一意な名前を生成する.
	 * </p>
	 * @param preffix 接頭辞
	 * @return 一意な名前
	 */
	public static String generate(String preffix) {

		StringBuffer buffer = new StringBuffer();
		if (preffix != null) {
			buffer.append(preffix);
			buffer.append("_");
		}

		buffer.append(getCurrentTime());
		return buffer.toString();
	}

	/**
	 * <p>
	 * 現在時間を取得する.
	 * </p>
	 * @return 現在時間
	 */
	private static synchronized String getCurrentTime() {
		Date date = null;
		date = new Date();
		try { Thread.sleep(10); } catch (InterruptedException e) {}
		date.setTime(System.currentTimeMillis());
		return DateUtil.toString(date, "yyyyMMDDHHmmssS");
	}
}
