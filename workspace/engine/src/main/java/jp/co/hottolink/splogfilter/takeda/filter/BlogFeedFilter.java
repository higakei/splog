package jp.co.hottolink.splogfilter.takeda.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;
import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.UserContentEntity;
import jp.co.hottolink.splogfilter.takeda.output.OutputResult;

import org.apache.log4j.Logger;

/**
 * <p>
 * ブログフィードフィルタの抽象クラス.
 * </p>
 * @author higa
 */
public abstract class BlogFeedFilter {

	/**
	 * <p>
	 * 投稿者モード.
	 * </p>
	 */
	public static final String MODE_AUTHOR = "author";

	/**
	 * <p>
	 * ブログモード.
	 * </p>
	 */
	public static final String MODE_BLOG = "blog";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	protected static Logger logger = Logger.getLogger(BlogFeedFilter.class);

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 */
	public BlogFeedFilter() {}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param blogFeeds ブログフィード
	 */
	public BlogFeedFilter(List<BlogFeedEntity> blogFeeds) {
		this.blogFeeds = blogFeeds;
	}

	/**
	 * <p>
	 * ブログフィード.
	 * </p>
	 */
	protected List<BlogFeedEntity> blogFeeds = null;

	/**
	 * <p>
	 * モード.
	 * </p><pre><dl>
	 * <dt>投稿者モード</dt><dd>投稿者単位にフィルタを行う</dd>
	 * <dt>ブログモード</dt><dd>ブログ単位にフィルタを行う</dd>
	 * </dl></pre>
	 */
	protected String mode = MODE_AUTHOR;

	/**
	 * <p>
	 * フィルタを実行する.
	 * </p>
	 * @param executor SQL実行クラス
	 * @return フィルタ結果
	 */
	public abstract OutputResult doFilter(SQLExecutor executor);

	/**
	 * <p>
	 * 指定した投稿者のフィルタを行う.
	 * </p>
	 * @param executor SQL実行クラス
	 * @param userContent ユーザーコンテンツ
	 * @return 投稿者のフィルタ結果
	 */
	public abstract AuthorResultEntity doAuthorFilter(SQLExecutor executor, UserContentEntity userContent);

	/**
	 * <p>
	 * フィルタを実行する.
	 * </p>
	 * @return フィルタ結果
	 */
	public OutputResult doFilter() {
		return doFilter(null);
	}

	/**
	 * <p>
	 * 投稿者のフィルタを行う.
	 * </p><pre>
	 * 1. ユーザーコンテンツの作成
	 * 投稿者ごとにユーザーコンテンツを作成する。
	 * 
	 * 2. 投稿者のフィルタ
	 * 投稿者のごとに、継承した各フィルタの処理を行う。
	 * </pre>
	 * @param executor SQL実行クラス
	 * @return 投稿者のフィルタ結果
	 */
	public List<AuthorResultEntity> doAuthorFilter(SQLExecutor executor) {

		// ユーザーコンテンツの作成
		List<UserContentEntity> userContents = createUserContent(blogFeeds);
		logger.info("author size=" + userContents.size());

		// 投稿者のフィルタ
		List<AuthorResultEntity> authorResults = new ArrayList<AuthorResultEntity>(userContents.size());
		for (UserContentEntity userContent: userContents) {
			logger.info("authorId=" + userContent.getAuthorId() + "\t blogfeed size="
					+ userContent.getBlogFeeds().size());
			// 継承した各フィルタの処理
			AuthorResultEntity authorResult = doAuthorFilter(executor, userContent);
			authorResults.add(authorResult);
		}

		return authorResults;
	}

	/**
	 * <p>
	 * 投稿者のフィルタを行う.
	 * </p>
	 * @return 投稿者のフィルタ結果
	 */
	public List<AuthorResultEntity> doAuthorFilter() {
		return doAuthorFilter(null);
	}

	/**
	 * <p>
	 * ブログフィードを設定する.
	 * </p>
	 * @param blogFeeds ブログフィード
	 */
	public void setBlogFeeds(List<BlogFeedEntity> blogFeeds) {
		this.blogFeeds = blogFeeds;
	}

	/**
	 * <p>
	 * モードを取得する.
	 * </p>
	 * @return モード
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * <p>
	 * モードを設定する.
	 * </p>
	 * @param mode モード
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * <p>
	 * スコアを調整する.
	 * </p><pre>
	 * スコアの最小値を超えるスコアはつけない
	 * </pre>
	 * @param score スコア
	 * @return スコア
	 */
	protected int adjustScore(double score) {
		if (score < SplogFilterConstants.SCORE_MIN_VALUE) {
			return SplogFilterConstants.SCORE_MIN_VALUE;
		} else {
			return (int)score;
		}
	}

	/**
	 * <p>
	 * ブログフィードからユーザーコンテンツを作成する.
	 * </p>
	 * @param blogFeeds ブログフィード
	 * @return UserContent ユーザーコンテンツ
	 */
	private List<UserContentEntity> createUserContent(List<BlogFeedEntity> blogFeeds) {

		Map<String, UserContentEntity> map = new HashMap<String, UserContentEntity>();

		if (blogFeeds == null) {
			return new ArrayList<UserContentEntity>(0);
		}
		
		for (BlogFeedEntity blogFeed: blogFeeds) {
			String authorId = blogFeed.getAuthorId();
			UserContentEntity userContent = map.get(authorId);
			if (userContent == null) {
				// ユーザーコンテンツの追加
				userContent = new UserContentEntity(blogFeed);
				map.put(authorId, userContent);
			} else {
				// ブログフィードの追加
				userContent.addBlogFeeds(blogFeed);
			}
		}

		return new ArrayList<UserContentEntity>(map.values());
	}
}
