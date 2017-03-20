package jp.co.hottolink.splogfilter.takeda.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * blog_resultテーブルのEntityクラス.
 * </p>
 * @author higa
 */
public class BlogResultEntity implements Serializable {

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
	 * @param documentId 文書ID
	 */
	public BlogResultEntity(String documentId) {
		this.documentId = documentId;
	}

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
	private int score = 0;

	/**
	 * <p>
	 * 理由.
	 * </p>
	 */
	private List<String> causes = null;

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
	 * スコアを取得する.
	 * </p>
	 * @return スコア
	 */
	public int getScore() {
		return score;
	}

	/**
	 * <p>
	 * スコアを設定する.
	 * </p>
	 * @param score スコア
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * <p>
	 * 理由を取得する.
	 * </p>
	 * @return 理由
	 */
	public List<String> getCauses() {
		return causes;
	}

	/**
	 * <p>
	 * 理由を設定する.
	 * </p>
	 * @param causes 理由
	 */
	public void setCauses(List<String> causes) {
		this.causes = causes;
	}
}
