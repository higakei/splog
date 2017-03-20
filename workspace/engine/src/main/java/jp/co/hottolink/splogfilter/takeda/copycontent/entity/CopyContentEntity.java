package jp.co.hottolink.splogfilter.takeda.copycontent.entity;

import java.io.Serializable;

/**
 * <p>
 * コピーコンテンツのEntityクラス.
 * </p>
 * @author higa
 */
public class CopyContentEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * 投稿者ID.
	 * </p>
	 */
	private String authorId = null;

	/**
	 * <p>
	 * 文書ID.
	 * </p>
	 */
	private String documentId = null;

	/**
	 * <p>
	 * コンテンツ.
	 * </p>
	 */
	private String content = null;

	/**
	 * <p>
	 * コピー検出のマーク領域.
	 * </p>
	 */
	private boolean[] fill = null;

	/**
	 * <p>
	 * URL.
	 * </p>
	 */
	private String url = null;

	/**
	 * <p>
	 * コピー率.
	 * </p>
	 */
	private double copyRate = 0;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 */
	public CopyContentEntity() {}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param documentId 文書ID
	 */
	public CopyContentEntity(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * <p>
	 * 投稿者IDを取得する.
	 * </p>
	 * @return 投稿者ID
	 */
	public String getAuthorId() {
		return authorId;
	}

	/**
	 * <p>
	 * 投稿者IDを設定する.
	 * </p>
	 * @param authorId 投稿者ID
	 */
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	/**
	 * <p>
	 * 文書IDを取得する.
	 * </p>
	 * @return 文書ID
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * <p>
	 * 文書IDを設定する.
	 * </p>
	 * @param documentId 文書ID
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * <p>
	 * コンテンツを取得する.
	 * </p>
	 * @return コンテンツ
	 */
	public String getContent() {
		return content;
	}

	/**
	 * <p>
	 * コンテンツを設定する.
	 * </p>
	 * @param content コンテンツ
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * <p>
	 * コンテンツを設定する.
	 * </p>
	 * @param content コンテンツ
	 */
	public void setContentAndCreateFill(String content) {
		this.content = content;
		createFill(content);
	}

	/**
	 * <p>
	 * コピー検出のマーク領域を取得する.
	 * </p>
	 * @return マーク領域
	 */
	public boolean[] getFill() {
		return fill;
	}

	/**
	 * <p>
	 * コピー検出のマーク領域を設定する.
	 * </p>
	 * @param fill マーク領域
	 */
	public void setFill(boolean[] fill) {
		this.fill = fill;
	}

	/**
	 * <p>
	 * コンテンツのコピー検出のマーク領域を作成する.
	 * </p>
	 * @param content コンテンツ
	 */
	public void createFill(String content) {
		if (content == null) {
			fill = new boolean[0];
		} else {
			fill = new boolean[content.length()];
		}
	}

	/**
	 * <p>
	 * コピー率を算出する.
	 * </p>
	 * @return コピー率
	 */
	public double calculateCopyRate() {

		int total = getContentLength();
		if (total == 0) {
			return 0;
		}

		int copy = getCopyContentLength();
		return (double)copy / total;
	}

	/**
	 * <p>
	 * コンテンツ長を取得する.
	 * </p>
	 * @return コンテンツ長
	 */
	public int getContentLength() {
		if (fill == null) {
			return 0;
		} else {
			return fill.length;
		}
	}

	/**
	 * <p>
	 * コピーコンテンツ長を取得する.
	 * </p>
	 * @return コンテンツ長
	 */
	public int getCopyContentLength() {

		if (fill == null) {
			return 0;
		}

		int count = 0;
		for (int i = 0; i < fill.length; i++) {
			if (fill[i]) {
				++count;
			}
		}

		return count;
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
	 * コピー率を取得する.
	 * </p>
	 * @return コピー率
	 */
	public double getCopyRate() {
		return copyRate;
	}

	/**
	 * <p>
	 * コピー率を設定する.
	 * </p>
	 * @param copyRate コピー率
	 */
	public void setCopyRate(double copyRate) {
		this.copyRate = copyRate;
	}

	/**
	 * <p>
	 * コピー率を設定する.
	 * </p>
	 * @param copyRate コピー率
	 */
	public void setCopyRate() {
		copyRate = calculateCopyRate();
	}
}
