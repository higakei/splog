package jp.co.hottolink.splogfilter.tools.copyfilter.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * コピーフィルターの検証結果のEntityクラス.
 * </p>
 * @author higa
 */
public class CopyFilterReportEntity implements Serializable{

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -7263958799257900944L;

	/**
	 * <p>
	 * 文書ID.
	 * </p>
	 */
	private String documentId = null;

	/**
	 * <p>
	 * コピー率.
	 * </p>
	 */
	private Double copyRate = null;

	/**
	 * <p>
	 * splogフィルタ(強).
	 * </p>
	 */
	private boolean splogHard = false;

	/**
	 * <p>
	 * splogフィルタ(中).
	 * </p>
	 */
	private boolean splogMedium = false;

	/**
	 * <p>
	 * splogフィルタ(弱).
	 * </p>
	 */
	private boolean splogSoft = false;

	/**
	 * <p>
	 * 本文重複フィルタ.
	 * </p>
	 */
	private boolean spamBodyDeplicated = false;

	/**
	 * <p>
	 * URL.
	 * </p>
	 */
	private String url = null;

	/**
	 * <p>
	 * splogフィルタ(強)を取得する.
	 * </p>
	 * @return splogフィルタ(強)
	 */
	public boolean isSplogHard() {
		return splogHard;
	}

	/**
	 * <p>
	 * splogフィルタ(強)を設定する.
	 * </p>
	 * @param splogHard splogフィルタ(強)
	 */
	public void setSplogHard(boolean splogHard) {
		this.splogHard = splogHard;
	}

	/**
	 * <p>
	 * splogフィルタ(中)を取得する.
	 * </p>
	 * @return splogフィルタ(中)
	 */
	public boolean isSplogMedium() {
		return splogMedium;
	}

	/**
	 * <p>
	 * splogフィルタ(中)を設定する.
	 * </p>
	 * @param splogMedium splogフィルタ(中)
	 */
	public void setSplogMedium(boolean splogMedium) {
		this.splogMedium = splogMedium;
	}

	/**
	 * <p>
	 * splogフィルタ(弱)を取得する.
	 * </p>
	 * @return splogフィルタ(弱)
	 */
	public boolean isSplogSoft() {
		return splogSoft;
	}

	/**
	 * <p>
	 * splogフィルタ(弱)を設定する.
	 * </p>
	 * @param splogSoft splogフィルタ(弱)
	 */
	public void setSplogSoft(boolean splogSoft) {
		this.splogSoft = splogSoft;
	}

	/**
	 * <p>
	 * spamBodyDeplicatedを取得する.
	 * </p>
	 * @return spamBodyDeplicated
	 */
	public boolean isSpamBodyDeplicated() {
		return spamBodyDeplicated;
	}

	/**
	 * <p>
	 * spamBodyDeplicatedを設定する.
	 * </p>
	 * @param spamBodyDeplicated spamBodyDeplicated
	 */
	public void setSpamBodyDeplicated(boolean spamBodyDeplicated) {
		this.spamBodyDeplicated = spamBodyDeplicated;
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
	 * コピー率を取得する.
	 * </p>
	 * @return コピー率
	 */
	public Double getCopyRate() {
		return copyRate;
	}

	/**
	 * <p>
	 * コピー率を設定する.
	 * </p>
	 * @param copyRate コピー率
	 */
	public void setCopyRate(Double copyRate) {
		this.copyRate = copyRate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("documentId", documentId);
		map.put("copyRate", copyRate);
		map.put("url", url);
		map.put("splogHard", splogHard);
		map.put("splogMedium", splogMedium);
		map.put("splogSoft", splogSoft);
		map.put("spamBodyDeplicated", spamBodyDeplicated);
		return map.toString();
	}
}
