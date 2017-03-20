package jp.co.hottolink.splogfilter.takeda.score;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.dao.BlogFeedDao;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;
import jp.co.hottolink.splogfilter.takeda.entity.UserContentEntity;

/**
 * <p>
 * 投稿間隔のスコアクラス.
 * </p>
 * @author higa
 */
public class UserIntervalScore {

	/**
	 * <p>
	 * 投稿間隔のスパム判定ブログ数.
	 * </p>
	 */
	private static final int INTERVAL_WIDTH = 4;

	/**
	 * <p>
	 * 投稿間隔のスパム判定時間.
	 * </p>
	 */
	private static final int TOO_DENSE_INTERVAL_TIME = 500;

	/**
	 * <p>
	 * 投稿間隔のスコア.
	 * </p>
	 */
	private static final int SCORE_USER_INTERVAL = -10000;

	/**
	 * <p>
	 * 1秒（ミリ秒）
	 * </p>
	 */
	private static final int ONE_SECOND = 1000; 

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(UserIntervalScore.class);

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param blogFeeds ブログ
	 */
	public UserIntervalScore(List<BlogFeedEntity> blogFeeds) {
		if (blogFeeds != null) {
			this.blogFeeds = sortDate(blogFeeds);
		}
		causes = new ArrayList<String>();
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param userContent ユーザーコンテンツ
	 */
	public UserIntervalScore(UserContentEntity userContent) {
		if (userContent != null) {
			List<BlogFeedEntity> blogFeeds = userContent.getBlogFeeds(); 
			if (blogFeeds != null) {
				this.blogFeeds = sortDate(blogFeeds);
			}
		}
		causes = new ArrayList<String>();
	}

	/**
	 * <p>
	 * ブログ.
	 * </p>
	 */
	private List<BlogFeedEntity> blogFeeds = new ArrayList<BlogFeedEntity>();

	/**
	 * <p>
	 * 理由.
	 * </p>
	 */
	private List<String> causes = null;

	/**
	 * <p>
	 * 投稿間隔のスコアを取得する.
	 * </p><pre>
	 * 1. 投稿者のブログを投稿時間順に並べる
	 * 
	 * 2. 3.以降の処理を繰り返す
	 * (A - B)回繰り返す
	 * A: 投稿者のブログ数
	 * B: 投稿間隔のスパム判定ブログ数(4)
	 * 
	 * 3. 投稿間隔のスパム判定ブログ数分、投稿時間順に連続したブログリストを取得する
	 * 
	 * 4. 投稿間隔の平均を求める
	 * (投稿間隔の平均) = (A - B) / C
	 * A: 3.で取得したブログリストの最新の投稿時間
	 * B: 3.で取得したブログリストの最古の投稿時間
	 * C: 3.で取得したブログリストのブログ数
	 * 
	 * 5. 判定
	 * (A < B) ならばスパム判定
	 * A: 投稿間隔の平均
	 * B: 投稿間隔のスパム判定時間（500秒）
	 * 
	 * 6. スコア
	 * score += -10000
	 * </pre>
	 * @return スコア
	 */
	public double getScore() {

		double score = SplogFilterConstants.SCORE_NOMAL_BLOG_FEED;

		for (int i = INTERVAL_WIDTH; i < blogFeeds.size(); i++) {

			// ブログの取得
			BlogFeedEntity startBlog = blogFeeds.get(i - INTERVAL_WIDTH);
			BlogFeedEntity endBlog = blogFeeds.get(i);
			if ((startBlog == null) || (endBlog == null)) {
				continue;
			}

			// 投稿時間の取得
			Timestamp startDate = startBlog.getDate();
			Timestamp endDate = endBlog.getDate();
			if ((startDate == null) || (endDate == null)) {
				continue;
			}

			// 投稿間隔の平均を求める
			long startTime = startDate.getTime();
			long endTime = endDate.getTime();
			long interval = ((endTime - startTime) / ONE_SECOND) / INTERVAL_WIDTH;
			logger.debug(getDetailInfo(startBlog, endBlog) + "\t" + interval + "sec");

			// 判定
			if (interval < TOO_DENSE_INTERVAL_TIME) {
				score += SCORE_USER_INTERVAL;
				logger.debug("投稿間隔チェック" + "\t" + "score=" + score
						+ "\t" + "authorId=" + startBlog.getAuthorId()
						+ "\t" + "interval=" + interval
						+ "\t" + "blogfeedId=" + getDetailInfo(startBlog, endBlog)
				);
			}
		}

		// スコアの最小値を超えた場合
		if (score < SplogFilterConstants.SCORE_MIN_VALUE) {
			return SplogFilterConstants.SCORE_MIN_VALUE;
		}

		return score;
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
	 * ブログリストを投稿時間でソートする.
	 * </p>
	 * @param blogFeeds ソートするブログリスト
	 * @return ソートしたブログリスト
	 */
	private List<BlogFeedEntity> sortDate(List<BlogFeedEntity> blogFeeds) {
		if (blogFeeds == null) return null;
		Collections.sort(blogFeeds);
		return blogFeeds;
	}

	/**
	 * <p>
	 * 詳細情報を取得する.
	 * </p>
	 * @param startBlog 開始ブログ
	 * @param endBlog 終了ブログ
	 * @return 詳細情報
	 */
	private String getDetailInfo(BlogFeedEntity startBlog, BlogFeedEntity endBlog) {
		if ((startBlog == null) || (endBlog == null)) return "";
		return "[" + startBlog.getId() + "-" + endBlog.getId() + "]";
	}

	public static void main(String[] args) {

		SQLExecutor executor = new SQLExecutor(SplogFilterConstants.DB_BLOG_FEEDS);
		BlogFeedDao blogFeedDao = new BlogFeedDao(executor);
		List<BlogFeedEntity> blogFeeds = blogFeedDao.selectAll();
		executor.finalize();

		UserIntervalScore userInterval = new UserIntervalScore(blogFeeds);
		double score = userInterval.getScore();
		System.out.println("score=" + score);
	}
}
