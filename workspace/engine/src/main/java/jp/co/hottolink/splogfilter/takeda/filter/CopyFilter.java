package jp.co.hottolink.splogfilter.takeda.filter;

import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.dao.AuthorResultDao;
import jp.co.hottolink.splogfilter.dao.BlogFeedDao;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.ConstantWordSetter;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;
import jp.co.hottolink.splogfilter.takeda.copycontent.entity.AuthorCopyContentScoreEntity;
import jp.co.hottolink.splogfilter.takeda.copycontent.score.CopyContentScore;
import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.UserContentEntity;
import jp.co.hottolink.splogfilter.takeda.output.AuthorOutputResult;
import jp.co.hottolink.splogfilter.takeda.output.OutputResult;

/**
 * <p>
 * コピーフィルタクラス.
 * </p>
 * @author higa
 */
public class CopyFilter extends BlogFeedFilter {

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 */
	public CopyFilter() {}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param blogFeeds ブログフィード
	 */
	public CopyFilter(List<BlogFeedEntity> blogFeeds) {
		super(blogFeeds);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.filter.BlogFeedFilter#doAuthorFilter(jp.co.hottolink.splogfilter.db.SQLExecutor, jp.co.hottolink.splogfilter.entity.UserContentEntity)
	 */
	@Deprecated
	public AuthorResultEntity doAuthorFilter(SQLExecutor executor, UserContentEntity userContent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.filter.BlogFeedFilter#doAuthorFilter(jp.co.hottolink.splogfilter.db.SQLExecutor)
	 */
	@Deprecated
	public List<AuthorResultEntity> doAuthorFilter(SQLExecutor executor) {
		return null;
	}

	/* (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.BlogFeedFilter#doAuthorFilter()
	 */
	@Deprecated
	public List<AuthorResultEntity> doAuthorFilter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.filter.BlogFeedFilter#doFilter(jp.co.hottolink.splogfilter.db.SQLExecutor)
	 */
	@Override
	public OutputResult doFilter(SQLExecutor executor) {

		// 1. スコアの取得
		CopyContentScore scorer = new CopyContentScore(blogFeeds);
		List<AuthorCopyContentScoreEntity> authorCopyContents = scorer.getScore();

		// 2. 結果の作成
		List<AuthorResultEntity> authorResults = new ArrayList<AuthorResultEntity>();
		for (AuthorCopyContentScoreEntity authorCopyContent: authorCopyContents) {
			String authorId = authorCopyContent.getAuthorId();
			double score = authorCopyContent.getScore();
			AuthorResultEntity authorResult = new AuthorResultEntity(authorId);
			authorResult.setScore((int)score);
			authorResults.add(authorResult);
		}
		OutputResult result = new AuthorOutputResult(authorResults);
		if (executor == null) {
			return result;
		}
		
		// 2. スコアをDBに登録
		AuthorResultDao authorResultDao = new AuthorResultDao(executor);
		for (AuthorResultEntity authorResult: authorResults) {
			authorResultDao.setScore(authorResult);
		}

		return result;
	}

	public static void main(String[] args) {

		SQLExecutor executor = null;

		try {
			Integer size = null;
			try {
				size = Integer.parseInt(args[0]);
			} catch (Exception e) {	}

			// 1. スパム判定語の取得
			executor = new SQLExecutor(SplogFilterConstants.DB_BLOG_FEEDS);
			ConstantWordSetter.setFromDB(executor);

			// 2. ブログデータの取得
			BlogFeedDao blogFeedDao = new BlogFeedDao(executor);
			List<BlogFeedEntity> blogFeeds = blogFeedDao.selectAll(size);
			logger.info("blogfeeds size:" + blogFeeds.size());

			// 3. フィルターを行う
			CopyFilter filter = new CopyFilter(blogFeeds);
			OutputResult result = filter.doFilter();

			// 4. 結果の出力
			result.outputtoXML(System.out);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (executor != null) executor.finalize();
		}
	}
}
