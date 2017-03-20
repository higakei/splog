package jp.co.hottolink.splogfilter.takeda.filter;

import java.util.List;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.dao.AuthorResultDao;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.UserContentEntity;
import jp.co.hottolink.splogfilter.takeda.output.AuthorOutputResult;
import jp.co.hottolink.splogfilter.takeda.output.OutputResult;
import jp.co.hottolink.splogfilter.takeda.score.TitleScore;

/**
 * <p>
 * タイトルフィルタクラス.
 * </p>
 * @author higa
  */
public class TitleFilter extends BlogFeedFilter {

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 */
	public TitleFilter() {}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param blogFeeds ブログフィード
	 */
	public TitleFilter(List<BlogFeedEntity> blogFeeds) {
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
		TitleScore scorer = new TitleScore(userContent);
		double title = scorer.getUserScore();
		List<String> causes = scorer.getCauses();
		userContent.setTitleScore((int)title);
		if (executor == null) {
			userContent.addCauses(causes);
			return userContent.getAuthorResult();
		}

		// DBに登録
		AuthorResultDao dao = new AuthorResultDao(executor);
		AuthorResultEntity result = dao.setTitle(userContent.getAuthorId(), title);
		result.addCauses(causes);
		userContent.setAuthorResult(result);

		return result;
	}
}
