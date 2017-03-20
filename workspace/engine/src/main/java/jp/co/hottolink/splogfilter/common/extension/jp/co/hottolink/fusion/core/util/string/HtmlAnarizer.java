package jp.co.hottolink.splogfilter.common.extension.jp.co.hottolink.fusion.core.util.string;

/**
 * <p>
 * fusion-utilsのバグ修正クラス.
 * </p><pre>
 * fusion-utilsが修正されたら削除
 * </pre>
 * @author higa
 *
 */
public class HtmlAnarizer {

	/**
	 * htmlテキストをプレーンテキストにする
	 * @param html
	 * @return
	 */
	public static String toPlaneText(String html){

		if (html == null) {
			return null;
		}

		String ret = null;
		ret = html.replaceAll("<[Bb][Rr][ ]*/?>", "\n");
		ret = ret.replaceAll("<.+?>", "");
		ret = ret.replaceAll("&nbsp;", " ");

		return ret;
	}
}
