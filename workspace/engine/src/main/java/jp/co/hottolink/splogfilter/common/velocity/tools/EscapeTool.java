package jp.co.hottolink.splogfilter.common.velocity.tools;

import jp.co.hottolink.splogfilter.common.util.StringUtil;

/**
 * <p>
 * エスケープを行うVelocityToolクラス.
 * </p>
 * @author higa
 */
public class EscapeTool {

	/**
	 * <p>
	 * XMLのエスケープを行う.
	 * </p>
	 * @param string エスケープする文字列
	 * @return エスケープした文字列
	 */
	public String xml(Object string) {

		if (string == null) {
			return null;
		}

		return StringUtil.escapeXml(string.toString());
	}
}
