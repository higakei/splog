package jp.co.hottolink.splogfilter.learning.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * クッキーのUtilクラス.
 * </p>
 * @author higa
 */
public class CookieUtil {

	/**
	 * <p>
	 * 指定した名前のクッキーを取得する.
	 * </p>
	 * @param request リクエスト
	 * @param name クッキー名
	 * @return クッキー
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {

		if (request == null) {
			return null;
		}

		Cookie[] cookies = request.getCookies();
		for (Cookie cookie: cookies) {
			if (cookie.getName().equals(name)) {
				return cookie;
			}
		}

		return null;
	}

	/**
	 * <p>
	 * 指定した名前のクッキーの値を取得する.
	 * </p>
	 * @param request リクエスト
	 * @param name クッキー名
	 * @return クッキーの値
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie cookie = getCookie(request, name);
		if (cookie == null) {
			return null;
		} else {
			return cookie.getValue();
		}
	}
}
