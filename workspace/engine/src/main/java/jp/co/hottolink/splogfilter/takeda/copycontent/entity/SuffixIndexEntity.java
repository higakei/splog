package jp.co.hottolink.splogfilter.takeda.copycontent.entity;

import java.io.Serializable;

/**
 * <p>
 * 接尾辞インデックスのEntityクラス.
 * </p>
 * @author higa
 */
public class SuffixIndexEntity implements Serializable, Comparable<SuffixIndexEntity> {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * 接尾辞.
	 * </p>
	 */
	private String suffix = null;

	/**
	 * <p>
	 * 文書ID.
	 * </p>
	 */
	private int documentId = 0;

	/**
	 * <p>
	 * 開始位置.
	 * </p>
	 */
	private int position = 0;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param suffix 接尾辞
	 */
	public SuffixIndexEntity() {}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param suffix 接尾辞
	 */
	public SuffixIndexEntity(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * <p>
	 * 文書IDを取得する.
	 * </p>
	 * @return 文書ID
	 */
	public int getDocumentId() {
		return documentId;
	}

	/**
	 * <p>
	 * 文書IDを設定する.
	 * </p>
	 * @param documentId 文書ID
	 */
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	/**
	 * <p>
	 * 開始位置を取得する.
	 * </p>
	 * @return 開始位置
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * <p>
	 * 開始位置を設定する.
	 * </p>
	 * @param position 開始位置
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * <p>
	 * 接尾辞を取得する.
	 * </p>
	 * @return 接尾辞
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * <p>
	 * 接尾辞を設定する.
	 * </p>
	 * @param suffix 接尾辞
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SuffixIndexEntity entity) {
		return suffix.compareTo(entity.getSuffix());
	}
}
