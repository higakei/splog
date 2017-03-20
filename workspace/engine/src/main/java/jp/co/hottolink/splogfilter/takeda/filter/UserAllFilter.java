package jp.co.hottolink.splogfilter.takeda.filter;

import java.util.List;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.UserContentEntity;
import jp.co.hottolink.splogfilter.takeda.output.AuthorOutputResult;
import jp.co.hottolink.splogfilter.takeda.output.OutputResult;

/**
 * <p>
 * 全てのフィルタを行うクラス.
 * </p>
 * @author higa
 */
public class UserAllFilter extends BlogFeedFilter {

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 */
	public UserAllFilter() {}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param blogFeeds ブログフィード
	 */
	public UserAllFilter(List<BlogFeedEntity> blogFeeds) {
		super(blogFeeds);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.filter.BlogFeedFilter#doFilter(jp.co.hottolink.splogfilter.db.SQLExecutor)
	 */
	@Override
	public OutputResult doFilter(SQLExecutor executor) {
		List<AuthorResultEntity> results = doAuthorFilter(executor);
		OutputResult output = new AuthorOutputResult(results);
		return output;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.filter.BlogFeedFilter#doAuthorFilter(jp.co.hottolink.splogfilter.db.SQLExecutor, jp.co.hottolink.splogfilter.entity.UserContentEntity)
	 */
	@Override
	public AuthorResultEntity doAuthorFilter(SQLExecutor executor, UserContentEntity userContent) {

		// 投稿間隔フィルタ
		UserIntervalFilter interval = new UserIntervalFilter();
		interval.doAuthorFilter(executor, userContent);

		// タイトルフィルタ
		TitleFilter title = new TitleFilter();
		title.doAuthorFilter(executor, userContent);

		// コンテンツフィルタ
		ContentFilter contentFilter = new ContentFilter();
		contentFilter.doAuthorFilter(executor, userContent);

		return userContent.getAuthorResult();
	}
}
