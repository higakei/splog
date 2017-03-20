package jp.co.hottolink.splogfilter.takeda.filter;

import java.util.List;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.dao.AuthorResultDao;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.UserContentEntity;
import jp.co.hottolink.splogfilter.takeda.output.AuthorOutputResult;
import jp.co.hottolink.splogfilter.takeda.output.OutputResult;
import jp.co.hottolink.splogfilter.takeda.score.UserIntervalScore;

/**
 * <p>
 * 投稿間隔フィルタクラス.
 * </p>
 * @author higa
  */
public class UserIntervalFilter extends BlogFeedFilter {

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 */
	public UserIntervalFilter() {}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param blogFeeds ブログフィード
	 */
	public UserIntervalFilter(List<BlogFeedEntity> blogFeeds) {
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

		// スコアの取得
		UserIntervalScore scorer = new UserIntervalScore(userContent);
		double interval = scorer.getScore();
		List<String> causes = scorer.getCauses();
		userContent.setIntervalScore((int)interval);
		if (executor == null) {
			userContent.addCauses(causes);
			return userContent.getAuthorResult();
		}
		
		// DBに登録
		AuthorResultDao dao = new AuthorResultDao(executor);
		AuthorResultEntity result = dao.setIntervals(userContent.getAuthorId(), interval);
		result.addCauses(causes);
		userContent.setAuthorResult(result);
		
		return result;
	}
}
