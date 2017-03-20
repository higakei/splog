package jp.co.hottolink.splogfilter.takeda.copycontent.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 投稿者のコピーコンテンツのスコアのEntityクラス.
 * </p>
 * @author higa
 */
public class AuthorCopyContentScoreEntity implements Serializable {

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
	 */
	public AuthorCopyContentScoreEntity(String authorId) {
		this.authorId = authorId;
	}

	/**
	 * <p>
	 * 投稿者ID.
	 * </p>
	 */
	private String authorId = null; 

	/**
	 * <p>
	 * 文書.
	 * </p>
	 */
	private List<CopyContentScoreEntity> documents = new ArrayList<CopyContentScoreEntity>();

	/**
	 * <p>
	 * 総コンテンツ長.
	 * </p>
	 */
	private int totalContentLength = 0;

	/**
	 * <p>
	 * 総コピーコンテンツ長.
	 * </p>
	 */
	private int totalCopyContentLength = 0;

	/**
	 * <p>
	 * 総スコア.
	 * </p><pre>
	 * 文書のスコアの合計
	 * </pre>
	 */
	private double totalDocumentScore = 0;

	/**
	 * <p>
	 * 文書数.
	 * </p>
	 */
	private int documentCount = 0;

	/**
	 * <p>
	 * スコア.
	 * </p>
	 */
	private double score = 0;

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
	 * 文書を取得する.
	 * </p>
	 * @return 文書
	 */
	public List<CopyContentScoreEntity> getDocuments() {
		return documents;
	}

	/**
	 * <p>
	 * 文書を設定する.
	 * </p>
	 * @param documents 文書
	 */
	public void setDocuments(List<CopyContentScoreEntity> documents) {
		this.documents = documents;
	}

	/**
	 * <p>
	 * 文書を追加する.
	 * </p>
	 * @param document 文書
	 */
	public void addDocument(CopyContentScoreEntity document) {
		if (documents == null) documents = new ArrayList<CopyContentScoreEntity>();
		//documents.add(document);
		totalContentLength += document.getContentLength();
		totalCopyContentLength += document.getCopyContentLength();
		totalDocumentScore += document.getScore();
		++documentCount;
	}

	/**
	 * <p>
	 * 総コンテンツ長を取得する.
	 * </p>
	 * @return 総コンテンツ長
	 */
	public int getTotalContentLength() {
		return totalContentLength;
	}

	/**
	 * <p>
	 * 総コンテンツ長を設定する.
	 * </p>
	 * @param totalContentLength 総コンテンツ長
	 */
	public void setTotalContentLength(int totalContentLength) {
		this.totalContentLength = totalContentLength;
	}

	/**
	 * <p>
	 * 総コピーコンテンツ長を取得する.
	 * </p>
	 * @return 総コピーコンテンツ長
	 */
	public int getTotalCopyContentLength() {
		return totalCopyContentLength;
	}

	/**
	 * <p>
	 * 総コピーコンテンツ長を設定する.
	 * </p>
	 * @param totalCopyContentLength 総コピーコンテンツ長
	 */
	public void setTotalCopyContentLength(int totalCopyContentLength) {
		this.totalCopyContentLength = totalCopyContentLength;
	}

	/**
	 * <p>
	 * 総スコアを取得する.
	 * </p>
	 * @return 総スコア
	 */
	public double getTotalDocumentScore() {
		return totalDocumentScore;
	}

	/**
	 * <p>
	 * 総スコアを設定する.
	 * </p>
	 * @param totalDocumentScore 総スコア
	 */
	public void setTotalDocumentScore(double totalDocumentScore) {
		this.totalDocumentScore = totalDocumentScore;
	}

	/**
	 * <p>
	 * 文書数を取得する.
	 * </p>
	 * @return 文書数
	 */
	public int getDocumentCount() {
		return documentCount;
	}

	/**
	 * <p>
	 * 文書数を設定する.
	 * </p>
	 * @param documentCount 文書数
	 */
	public void setDocumentCount(int documentCount) {
		this.documentCount = documentCount;
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
}
