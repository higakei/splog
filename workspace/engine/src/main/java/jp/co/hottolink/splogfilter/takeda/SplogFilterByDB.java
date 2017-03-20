package jp.co.hottolink.splogfilter.takeda;

import java.util.List;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.DAOException;
import jp.co.hottolink.splogfilter.common.exception.DBConnectionException;
import jp.co.hottolink.splogfilter.dao.BlogFeedDao;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;
import jp.co.hottolink.splogfilter.takeda.filter.BlogFeedFilter;
import jp.co.hottolink.splogfilter.takeda.filter.ContentFilter;
import jp.co.hottolink.splogfilter.takeda.filter.TitleFilter;
import jp.co.hottolink.splogfilter.takeda.filter.UserAllFilter;
import jp.co.hottolink.splogfilter.takeda.filter.UserIntervalFilter;
import jp.co.hottolink.splogfilter.takeda.output.OutputResult;

import org.apache.log4j.Logger;

/**
 * <p>
 * DBによるSplogFilter実行クラス.
 * </p>
 * @author higa
 */
public class SplogFilterByDB {

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(SplogFilterByDB.class);

	/**
	 * <p>
	 * メイン処理.
	 * </p>
	 * @param args パラメータ
	 */
	public static void main(String[] args) {

		SQLExecutor executor = null;

		try {
			// 1. フィルタの設定
			BlogFeedFilter filter = new UserAllFilter();
			if (args.length < 1) {
				filter = new UserAllFilter();
			} else if ("interval".equals(args[0])) {
				// 投稿間隔フィルタ
				filter = new UserIntervalFilter();
			} else if ("title".equals(args[0])) {
				// タイトルフィルタ
				filter = new TitleFilter();
			} else if ("content".equals(args[0])) {
				// コンテンツフィルタ
				filter = new ContentFilter();
			} else if ("all".equals(args[0])) {
				// 全フィルタ
				filter = new UserAllFilter();
			} else {
				logger.error("指定したフィルタ(" + args[0] + ")が正しくありません。");
				System.err.println("500 error(Usage: SplogFilter {interval|title|content|all})");
				System.exit(1);
			}

			// 2. スパム判定語の取得
			executor = new SQLExecutor(SplogFilterConstants.DB_BLOG_FEEDS);
			ConstantWordSetter.setFromDB(executor);

			// 3. ブログフィードの取得
			BlogFeedDao blogFeedDao = new BlogFeedDao(executor);
			List<BlogFeedEntity> blogFeeds = blogFeedDao.selectAll();
			filter.setBlogFeeds(blogFeeds);
			logger.info("blogfeeds size=" + blogFeeds.size());

			// 4. フィルタの実行
			logger.info("----- start " + filter.getClass().getSimpleName() + " -----");
			OutputResult result = filter.doFilter(executor);
			logger.info("----- end " + filter.getClass().getSimpleName() + " -----");

			// 5. 結果の出力
			System.out.println("200 sucess");
			result.outputtoXML(System.out);
			System.exit(0);
			
		} catch (DBConnectionException e) {
			logger.error("DB接続エラー", e);
			System.err.println("500 error(DB接続エラー)");
			System.exit(1);
		} catch (DAOException e) {
			logger.error("DB接続アクセスエラー", e);
			System.err.println("500 error(DB接続アクセスエラー)");
			System.exit(1);
		} catch (Exception e) {
			logger.error("", e);
			String reason = e.getMessage();
			if (reason == null) reason = e.toString();
			System.err.println("500 error(" + reason + ")");
			System.exit(1);
		} finally {
			if (executor != null) executor.finalize();
		}
	}
}