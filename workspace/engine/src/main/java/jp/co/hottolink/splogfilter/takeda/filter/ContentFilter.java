package jp.co.hottolink.splogfilter.takeda.filter;

import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.dao.AuthorResultDao;
import jp.co.hottolink.splogfilter.dao.BlogResultDao;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;
import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.BlogResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.UserContentEntity;
import jp.co.hottolink.splogfilter.takeda.output.AuthorOutputResult;
import jp.co.hottolink.splogfilter.takeda.output.BlogOutputResult;
import jp.co.hottolink.splogfilter.takeda.output.OutputResult;
import jp.co.hottolink.splogfilter.takeda.score.ContentScore;

/**
 * <p>
 * コンテンツフィルタクラス.
 * </p>
 * @author higa
  */
public class ContentFilter extends BlogFeedFilter {

	/**
	 * <p>
	 * 投稿者のスコア定数.
	 * </p>
	 */
	private static final int SCORE_AUTHOR_CONST  = 10;

	/**
	 * <p>
	 * ブログのスコア定数.
	 * </p>
	 */
	private static final int SCORE_BLOG_CONST = 100; 

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 */
	public ContentFilter() {}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param blogFeeds ブログフィード
	 */
	public ContentFilter(List<BlogFeedEntity> blogFeeds) {
		super(blogFeeds);
	}

	/**
	 * <p>
	 * コンテンツフィルタを行う.
	 * </p><pre><dl>
	 * <dt>投稿者モード</dt><dd>doAuthorFilter</dd>
	 * <dt>ブログモード</dt><dd>doDocumentFilter</dd>
	 * </dl></pre>
	 * @param executor SQL実行クラス
	 * @see jp.co.hottolink.splogfilter.takeda.filter.BlogFeedFilter#doFilter(jp.co.hottolink.splogfilter.common.db.SQLExecutor)
	 */
	@Override
	public OutputResult doFilter(SQLExecutor executor) {
		if (MODE_BLOG.equals(mode)) {
			// ブログモード
			List<BlogResultEntity> results = doBlogFilter(executor);
			OutputResult output = new BlogOutputResult(results);
			return output;
		} else {
			// 投稿者モード
			List<AuthorResultEntity> results = doAuthorFilter(executor);
			OutputResult output = new AuthorOutputResult(results);
			return output;
		}
	}

	/**
	 * <p>
	 * 投稿者のコンテンツフィルタを行う
	 * </p><pre>
	 * 投稿者のスコアの算出方法
	 *  1. コンテンツがスパムの場合(score < -9000)
	 *   author_result += score/(投稿者のブログ数の平方根)
	 *  2. 上記以外の場合
	 *   author_result += 10 + score/100
	 *
	 *  score: コンテンツフィルタのスコア
	 *  author_result:投稿者のスコア  
	 * </pre>
	 * @see jp.co.hottolink.splogfilter.takeda.filter.BlogFeedFilter#doAuthorFilter(jp.co.hottolink.splogfilter.common.db.SQLExecutor, jp.co.hottolink.splogfilter.takeda.entity.UserContentEntity)
	 */
	@Override
	public AuthorResultEntity doAuthorFilter(SQLExecutor executor, UserContentEntity userContent) {

		double authorScore = 0;
		if (userContent == null) {
			return null;
		}

		// 投稿者のブログごとにスコアをつける
		for (BlogFeedEntity blogFeed: userContent.getBlogFeeds()) {
			String documentId = blogFeed.getDocumentId();
			logger.debug("documentId=" + documentId);

			// ブログのスコアの取得
			ContentScore scorer = new ContentScore(blogFeed);
			double blogScore = scorer.getScore();
			if (executor != null) {
				BlogResultDao dao = new BlogResultDao(executor);
				dao.setScore(documentId, blogScore);
			}

			// ブログのスコアの設定
			BlogResultEntity blogResult = new BlogResultEntity(documentId);
			blogResult.setScore((int)blogScore);
			blogResult.setCauses(scorer.getCauses());
			userContent.addBlogResult(blogResult);

			// 投稿者のスコアの算出
			if (blogScore < SplogFilterConstants.SCORE_CONTENT_IS_SPAM) {
				int sqrt = (int)Math.sqrt(userContent.getBlogFeeds().size());
				authorScore += (blogScore / sqrt);
			} else {
				authorScore += SCORE_AUTHOR_CONST + (blogScore / SCORE_BLOG_CONST);
			}
		}

		// 投稿者のスコアの設定
		authorScore = adjustScore(authorScore);
		userContent.setContentScore((int)authorScore);
		if (executor == null) {
			return userContent.getAuthorResult();
		}

		// DBに登録
		AuthorResultDao dao = new AuthorResultDao(executor);
		AuthorResultEntity result = dao.setContent(userContent.getAuthorId(), authorScore);
		userContent.setAuthorResult(result);
		
		return result;
	}

	/**
	 * <p>
	 * ブログのコンテンツフィルタを行う.
	 * </p>
	 * @param executor SQL実行クラス
	 * @param blogFeed ブログフィード
	 * @return フィルタ結果
	 */
	public BlogResultEntity doBlogFilter(SQLExecutor executor, BlogFeedEntity blogFeed) {

		if (blogFeed == null) {
			return null;
		}

		// スコアの取得
		String documentId = blogFeed.getDocumentId();
		ContentScore scorer = new ContentScore(blogFeed);
		double score = scorer.getScore();
		if (executor != null) {
			BlogResultDao dao = new BlogResultDao(executor);
			dao.setScore(documentId, score);
		}

		// スコアの作成
		BlogResultEntity blogResult = new BlogResultEntity(documentId);
		blogResult.setScore((int)score);
		blogResult.setCauses(scorer.getCauses());

		return blogResult;
	}

	/**
	 * <p>
	 * ブログのコンテンツフィルタを行う.
	 * </p>
	 * @param blogFeed ブログフィード
	 * @return フィルタ結果
	 */
	public BlogResultEntity doBlogFilter(BlogFeedEntity blogFeed) {
		return doBlogFilter(null, blogFeed);
	}

	/**
	 * <p>
	 * ブログのコンテンツフィルタを行う.
	 * </p>
	 * @param executor SQL実行クラス
	 * @return フィルタ結果
	 */
	public List<BlogResultEntity> doBlogFilter(SQLExecutor executor) {

		List<BlogResultEntity> results = new ArrayList<BlogResultEntity>();
		if (blogFeeds == null) {
			return results;
		}

		for (BlogFeedEntity blogFeed: blogFeeds) {
			logger.info("documentId=" + blogFeed.getDocumentId());
			BlogResultEntity result = doBlogFilter(executor, blogFeed);
			results.add(result);
		}

		return results;
	}

	/**
	 * <p>
	 * ブログのコンテンツフィルタを行う.
	 * </p>
	 * @return フィルタ結果
	 */
	public List<BlogResultEntity> doBlogFilter() {
		return doBlogFilter((SQLExecutor)null);
	}
}
