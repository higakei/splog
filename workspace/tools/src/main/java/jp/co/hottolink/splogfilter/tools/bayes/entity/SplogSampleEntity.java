package jp.co.hottolink.splogfilter.tools.bayes.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * スプログサンプルテーブルのEntityクラス.
 * </p><pre>
 * 人間がスプログ判定したブログデータ
 * </pre>
 * @author higa
 */
public class SplogSampleEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -8898445189142779582L;

	/**
	 * <p>
	 * サンプルID.
	 * </p>
	 */
	private int id = 0;

	/**
	 * <p>
	 * URL.
	 * </p>
	 */
	private String url = null;

	/**
	 * <p>
	 * タイトル.
	 * </p>
	 */
	private String title = null;

	/**
	 * <p>
	 * 本文.
	 * </p>
	 */
	private String body = null;

	/**
	 * <p>
	 * スプログフラグ.
	 * </p><pre>
	 * true:スプログ, false:ブログ
	 * </pre>
	 */
	private boolean isSplog = false;

	/**
	 * <p>
	 * サンプルIDを取得する.
	 * </p>
	 * @return サンプルID
	 */
	public int getId() {
		return id;
	}

	/**
	 * <p>
	 * サンプルIDを設定する.
	 * </p>
	 * @param id サンプルID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * <p>
	 * URLを取得する.
	 * </p>
	 * @return URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * <p>
	 * URLを設定する.
	 * </p>
	 * @param url URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * <p>
	 * タイトルを取得する.
	 * </p>
	 * @return タイトル
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * <p>
	 * タイトルを設定する.
	 * </p>
	 * @param title タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * <p>
	 * 本文を取得する.
	 * </p>
	 * @return 本文
	 */
	public String getBody() {
		return body;
	}

	/**
	 * <p>
	 * 本文を設定する.
	 * </p>
	 * @param body 本文
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * <p>
	 * スプログフラグを取得する.
	 * </p>
	 * @return スプログフラグ
	 */
	public boolean isSplog() {
		return isSplog;
	}

	/**
	 * <p>
	 * スプログフラグを設定する.
	 * </p>
	 * @param isSplog スプログフラグ
	 */
	public void setSplog(boolean isSplog) {
		this.isSplog = isSplog;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("url", url);
		map.put("title", title);
		map.put("body", getSummary(body, 20));
		map.put("isSplog", isSplog);
		return map.toString();
	}

	/**
	 * <p>
	 * 本文を指定した文字数に短縮する.
	 * </p>
	 * @param body 本文
	 * @param length 文字数
	 * @return
	 */
	private static String getSummary(String body, int length) {

		if (body == null) {
			return null;
		}

		body = body.replaceAll("\\n", "");
		if (body.length() <= length) {
			return body;
		} else {
			return body.substring(0, length) + "…";
		}
	}
}
