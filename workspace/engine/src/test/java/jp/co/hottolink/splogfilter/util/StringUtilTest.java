package jp.co.hottolink.splogfilter.util;

import jp.co.hottolink.splogfilter.common.util.StringUtil;

/**
 * <p>
 * StringUtilのテストクラス.
 * </p>
 * @author higa
 */
public class StringUtilTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		escapeXml(args[0]);
	}

	/**
	 * <p>
	 * escapeXmlのテストメソッド.
	 * </p>
	 * @param parameter パラメーター
	 */
	public static void escapeXml(String parameter) {
		System.out.println(StringUtil.escapeXml(parameter));
	}

}
