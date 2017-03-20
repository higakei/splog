package jp.co.hottolink.splogfilter.entity;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Timestamp;

/**
 * <p>
 * blogfeedテーブルのEntityクラス.
 * </p>
 * @author higa
 */
public class BlogFeedEntity implements Serializable, Comparable<BlogFeedEntity> {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * ID.
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
	 * ブログID.
	 * </p>
	 */
	private String documentId = null;

	/**
	 * <p>
	 * 投稿者ID.
	 * </p>
	 */
	private String authorId = null; 

	/**
	 * <p>
	 * 投稿日.
	 * </p>
	 */
	private Timestamp date = null;

	/**
	 * <p>
	 * 本文.
	 * </p>
	 */
	private String body = null;

	/**
	 * <p>
	 * 登録日時.
	 * </p>
	 */
	private Timestamp creationDate = null;

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(BlogFeedEntity entity) {

		Timestamp thisDate = new Timestamp(0);
		if (date != null) thisDate = date;

		Timestamp givenDate = new Timestamp(0);
		if (entity != null) {
			givenDate = entity.getDate();
			if (givenDate == null) givenDate = new Timestamp(0);
		}

		return thisDate.compareTo(givenDate);
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
	 * 投稿日を取得する.
	 * </p>
	 * @return 投稿日
	 */
	public Timestamp getDate() {
		return date;
	}

	/**
	 * <p>
	 * 投稿日を設定する.
	 * </p>
	 * @param date 投稿日
	 */
	public void setDate(Timestamp date) {
		this.date = date;
	}

	/**
	 * <p>
	 * ブログIDを取得する.
	 * </p>
	 * @return ブログID
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * <p>
	 * ブログIDを設定する.
	 * </p>
	 * @param documentId ブログID
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * <p>
	 * IDを取得する.
	 * </p>
	 * @return ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * <p>
	 * IDを設定する.
	 * </p>
	 * @param id ID
	 */
	public void setId(int id) {
		this.id = id;
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

	public String toString() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("documentId=" + documentId);
		pw.println("authorId=" + authorId);
		pw.println("date=" + date);
		pw.println("title=" + title);
		pw.println("url=" + url);
		pw.println("body=" + body);
		pw.println("creation_date=" + creationDate);
		pw.close();
		return sw.toString();
	}

	/**
	 * <p>
	 * 登録日時を取得する.
	 * </p>
	 * @return 登録日時
	 */
	public Timestamp getCreationDate() {
		return creationDate;
	}

	/**
	 * <p>
	 * 登録日時を設定する.
	 * </p>
	 * @param creationDate 登録日時
	 */
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
}
