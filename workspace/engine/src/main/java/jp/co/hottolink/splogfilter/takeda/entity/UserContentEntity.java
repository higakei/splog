package jp.co.hottolink.splogfilter.takeda.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;

/**
 * <p>
 * ユーザーコンテンツのEntityクラス.
 * </p>
 * @author higa
 */
public class UserContentEntity implements Serializable {

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
	 */
	public UserContentEntity() {}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param blogFeed ブログ
	 */
	public UserContentEntity(BlogFeedEntity blogFeed) {
		authorId = blogFeed.getAuthorId();
		blogFeeds.add(blogFeed);
	}

	/**
	 * <p>
	 * 投稿者ID.
	 * </p>
	 */
	private String authorId = null;

	/**
	 * <p>
	 * ブログ.
	 * </p>
	 */
	private List<BlogFeedEntity> blogFeeds = new ArrayList<BlogFeedEntity>(0);

	/**
	 * <p>
	 * 投稿者の結果.
	 * </p>
	 */
	private AuthorResultEntity authorResult = null;

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
	 * ブログを取得する.
	 * </p>
	 * @return ブログ
	 */
	public List<BlogFeedEntity> getBlogFeeds() {
		if (blogFeeds == null) {
			return new ArrayList<BlogFeedEntity>(0);
		} else {
			return blogFeeds;
		}
	}

	/**
	 * <p>
	 * ブログを設定する.
	 * </p>
	 * @param blogFeeds ブログ
	 */
	public void setBlogFeeds(List<BlogFeedEntity> blogFeeds) {
		this.blogFeeds = blogFeeds;
	}

	/**
	 * <p>
	 * ブログを追加する
	 * </p>
	 * @param blogFeed ブログ
	 */
	public void addBlogFeeds(BlogFeedEntity blogFeed) {
		blogFeeds.add(blogFeed);
	}

	/**
	 * <p>
	 * タイトルを取得する.
	 * </p>
	 * @return タイトル
	 */
	public List<String> getTitles() {
		List<String> list = new ArrayList<String>(0);
		for (BlogFeedEntity blogFeed: blogFeeds) {
			list.add(blogFeed.getTitle());
		}
		return list;
	}

	/**
	 * <p>
	 * 投稿者の結果を取得する.
	 * </p>
	 * @return 投稿者の結果
	 */
	public AuthorResultEntity getAuthorResult() {
		return authorResult;
	}

	/**
	 * <p>
	 * 投稿者の結果を設定する.
	 * </p>
	 * @param authorResult 投稿者の結果
	 */
	public void setAuthorResult(AuthorResultEntity authorResult) {
		this.authorResult = authorResult;
	}

	/**
	 * <p>
	 * ブログの結果を追加する.
	 * </p>
	 * @param blogResult ブログの結果
	 */
	public void addBlogResult(BlogResultEntity blogResult) {
		if (authorResult == null) authorResult = new AuthorResultEntity(authorId);
		authorResult.addBlogResult(blogResult);
	}

	/**
	 * <p>
	 * タイトルフィルタのスコアを設定する.
	 * </p>
	 * @param score スコア
	 */
	public void setTitleScore(int score) {
		if (authorResult == null) authorResult = new AuthorResultEntity(authorId);
		authorResult.setTitleScore(score);
	}

	/**
	 * <p>
	 * 投稿間隔フィルタのスコアを設定する.
	 * </p>
	 * @param score スコア
	 */
	public void setIntervalScore(int score) {
		if (authorResult == null) authorResult = new AuthorResultEntity(authorId);
		authorResult.setIntervalsScore(score);
	}

	/**
	 * <p>
	 * コンテンツフィルタのスコアを設定する.
	 * </p>
	 * @param score スコア
	 */
	public void setContentScore(int score) {
		if (authorResult == null) authorResult = new AuthorResultEntity(authorId);
		authorResult.setContentScore(score);
	}

	/**
	 * <p>
	 * 理由を追加する.
	 * </p>
	 * @param causes 理由
	 * @see jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity#addCauses(java.util.List)
	 */
	public void addCauses(List<String> causes) {
		authorResult.addCauses(causes);
	}
}
