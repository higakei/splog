package jp.co.hottolink.splogfilter.takeda.copycontent.entity;

import java.io.Serializable;


/**
 * <p>
 * コピーコンテンツのスコアのEntityクラス.
 * </p>
 * @author higa
 */
public class CopyContentScoreEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param documentId 文書ID
	 */
	public CopyContentScoreEntity(String authorId, String documentId) {
		this.authorId = authorId;
		this.documentId = documentId;
	}

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
	 * スコア.
	 * </p>
	 */
	private double score = 0;

	/**
	 * <p>
	 * コンテンツ長.
	 * </p>
	 */
	private int contentLength = 0;

	/**
	 * <p>
	 * コピーコンテンツ長.
	 * </p>
	 */
	private int copyContentLength = 0;

	/**
	 * <p>
	 * コピーコンテンツの統計データ.
	 * </p>
	 */
	private CopyContentEntity statistics = null;

	/**
	 * <p>
	 * コピー率.
	 * </p>
	 */
	private double copyRate = 0;

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
	 * コンテンツ長を取得する.
	 * </p>
	 * @return コンテンツ長
	 */
	public int getContentLength() {
		return contentLength;
	}

	/**
	 * <p>
	 * コンテンツ長を設定する.
	 * </p>
	 * @param contentLength コンテンツ長
	 */
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * <p>
	 * コピーコンテンツ長を取得する.
	 * </p>
	 * @return コピーコンテンツ長
	 */
	public int getCopyContentLength() {
		return copyContentLength;
	}

	/**
	 * <p>
	 * コピーコンテンツ長を設定する.
	 * </p>
	 * @param copyContentLength コピーコンテンツ長
	 */
	public void setCopyContentLength(int copyContentLength) {
		this.copyContentLength = copyContentLength;
	}

	/**
	 * <p>
	 * スコアを取得する.
	 * </p>
	 * @return スコア
	 */
	public double getScore() {
		return score;
	}

	/**
	 * <p>
	 * スコアを設定する.
	 * </p>
	 * @param score スコア
	 */
	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * <p>
	 * コピーコンテンツの統計データを取得する.
	 * </p>
	 * @return 統計データ
	 */
	public CopyContentEntity getStatistics() {
		return statistics;
	}

	/**
	 * <p>
	 * コピーコンテンツの統計データを設定する.
	 * </p>
	 * @param statistics 統計データ
	 */
	public void setStatistics(CopyContentEntity statistics) {
		//this.statistics = statistics;
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
}
