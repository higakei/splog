package jp.co.hottolink.splogfilter.takeda.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * author_resultテーブルのEntityクラス.
 * </p>
 * @author higa
 */
public class AuthorResultEntity implements Serializable {

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
	public AuthorResultEntity(String authorId) {
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
	 * スコア.
	 * </p>
	 */
	private int score = 0;

	/**
	 * <p>
	 * タイトルフィルタのスコア.
	 * </p>
	 */
	private Integer title = null;

	/**
	 * <p>
	 * コンテンツフィルタのスコア.
	 * </p>
	 */
	private Integer content = null;

	/**
	 * <p>
	 * 投稿間隔フィルタのスコア.
	 * </p>
	 */
	private Integer intervals = null;

	/**
	 * <p>
	 * 投稿者の文書のスコア.
	 * </p>
	 */
	private List<BlogResultEntity> blogResults = new ArrayList<BlogResultEntity>(0);

	/**
	 * <p>
	 * 理由.
	 * </p>
	 */
	private List<String> causes = null;

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
	 * コンテンツフィルタのスコアを取得する.
	 * </p>
	 * @return スコア
	 */
	public Integer getContent() {
		return content;
	}

	/**
	 * <p>
	 * コンテンツフィルタのスコアを設定する.
	 * </p>
	 * @param content スコア
	 */
	public void setContent(Integer content) {
		this.content = content;
	}

	/**
	 * <p>
	 * コンテンツフィルタのスコアを設定する.
	 * </p><pre>
	 * 投稿者のスコアを再計算して設定する
	 * </pre>
	 * @param content スコア
	 */
	public void setContentScore(Integer content) {
		this.content = content;
		score = getAuthorScore();
	}

	/**
	 * <p>
	 * タイトルフィルタのスコアを取得する.
	 * </p>
	 * @return スコア
	 */
	public Integer getTitle() {
		return title;
	}

	/**
	 * <p>
	 * タイトルフィルタのスコアを設定する.
	 * </p>
	 * @param title スコア
	 */
	public void setTitle(Integer title) {
		this.title = title;
	}

	/**
	 * <p>
	 * タイトルフィルタのスコアを設定する.
	 * </p><pre>
	 * 投稿者のスコアを再計算して設定する
	 * </pre>
	 * @param title スコア
	 */
	public void setTitleScore(Integer title) {
		this.title = title;
		score = getAuthorScore();
	}

	/**
	 * <p>
	 * 投稿者のスコアを取得する.
	 * </p><pre>
	 * 1. 各フィルタのスコアが 0 以上の場合、各フィルタの最大値
	 * 2. 上記以外の場合、各フィルタの最小値
	 * </pre>
	 * @return スコア
	 */
	public int getAuthorScore() {

		int title = 0;
		if (this.title != null) {
			title = this.title;
		}

		int content = 0;
		if (this.content != null) {
			content = this.content;
		}
		
		int intervals = 0;
		if (this.intervals != null) {
			intervals = this.intervals;
		}
		
		if ((title >= 0) && (content >= 0) && (intervals >= 0)) {
			return Math.max(title, Math.max(content, intervals));
		} else {
			return Math.min(title, Math.min(content, intervals));
		}
	}

	/**
	 * <p>
	 * 投稿間隔フィルタのスコアを取得する.
	 * </p>
	 * @return スコア
	 */
	public Integer getIntervals() {
		return intervals;
	}

	/**
	 * <p>
	 * 投稿間隔フィルタのスコアを設定する.
	 * </p>
	 * @param interval スコア
	 */
	public void setIntervals(Integer interval) {
		this.intervals = interval;
	}

	/**
	 * <p>
	 * 投稿間隔フィルタのスコアを設定する.
	 * </p><pre>
	 * 投稿者のスコアを再計算して設定する
	 * </pre>
	 * @param interval スコア
	 */
	public void setIntervalsScore(Integer interval) {
		this.intervals = interval;
		score = getAuthorScore();
	}

	/**
	 * <p>
	 * 投稿者の文書のスコアを取得する.
	 * </p>
	 * @return 投稿者の文書のスコア
	 */
	public List<BlogResultEntity> getBlogResults() {
		return blogResults;
	}

	/**
	 * <p>
	 * 投稿者の文書のスコアを設定する.
	 * </p>
	 * @param blogResults 投稿者の文書のスコア
	 */
	public void setBlogResults(List<BlogResultEntity> blogResults) {
		this.blogResults = blogResults;
	}

	/**
	 * <p>
	 * ブログの結果を追加する.
	 * </p>
	 * @param blogResult ブログの結果
	 */
	public void addBlogResult(BlogResultEntity blogResult) {
		if (blogResults == null) blogResults = new ArrayList<BlogResultEntity>(0);
		blogResults.add(blogResult);
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

	/**
	 * <p>
	 * 理由を追加する.
	 * </p>
	 * @param causes 理由
	 */
	public void addCauses(List<String> causes) {
		if (causes == null) return;
		if (this.causes == null) this.causes = new ArrayList<String>();
		this.causes.addAll(causes);
	}
}
