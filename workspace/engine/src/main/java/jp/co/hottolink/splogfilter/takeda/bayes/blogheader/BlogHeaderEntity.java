package jp.co.hottolink.splogfilter.takeda.bayes.blogheader;

import java.io.Serializable;

/**
 * <p>
 * ブログヘッダーのEntityクラス.
 * </p>
 * @author higa
 */
public class BlogHeaderEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 6658453165838575798L;

	/**
	 * <p>
	 * url.
	 * </p>
	 */
	private String url = null;

	/**
	 * <p>
	 * ブログヘッダー.
	 * </p>
	 */
	private String header = null;

	/**
	 * <p>
	 * urlを取得する.
	 * </p>
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * <p>
	 * urlを設定する
	 * </p>
	 * @param url url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * <p>
	 * ブログヘッダーを取得する.
	 * </p>
	 * @return ブログヘッダー
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * <p>
	 * ブログヘッダーを設定する
	 * </p>
	 * @param header ブログヘッダー
	 */
	public void setHeader(String header) {
		this.header = header;
	}
}
