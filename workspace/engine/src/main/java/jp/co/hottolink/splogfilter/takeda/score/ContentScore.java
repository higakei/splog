package jp.co.hottolink.splogfilter.takeda.score;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import jp.co.hottolink.splogfilter.common.util.StringUtil;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.constants.ConstantWords;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;
import jp.co.hottolink.splogfilter.takeda.statistics.WordIntervalStatistics;
import jp.co.hottolink.splogfilter.takeda.util.WordUtil;

/**
 * <p>
 * コンテンツのスコアクラス.
 * </p>
 * @author higa
 */
public class ContentScore {

	/**
	 * <p>
	 * スパムフレーズ数の条件定数.
	 * </p>
	 */
	private static final int CONDITION_SPAM_PHRASE_COUNT = 0;

	/**
	 * <p>
	 * URLの出現数の条件定数.
	 * </p>
	 */
	private static final int CONDITION_URL_COUNT = 7;

	/**
	 * <p>
	 * URLの出現間隔の条件定数.
	 * </p>
	 */
	private static final int CONDITION_URL_INTERVAL = 200;

	/**
	 * <p>
	 * 改行の出現数の条件定数.
	 * </p>
	 */
	private static final int CONDITION_RETURN_COUNT = 2;

	/**
	 * <p>
	 * スペースの出現頻度の条件定数.
	 * </p>
	 */
	private static final double CONDITION_SPACE_FREQUENCY = 6.67;

	/**
	 * <p>
	 * 本文の文字数の平均の条件定数.
	 * </p>
	 */
	private static final int CONDITION_CONTENT_LENGTH_AVERAGE = 80;

	/**
	 * <p>
	 * スペースの出現間隔の条件定数.
	 * </p>
	 */
	private static final double CONDITION_SPACE_INTERVAL = 10.2;

	/**
	 * <p>
	 * 検索系ブログのスパムワードの出現数の条件定数.
	 * </p>
	 */
	private static final int CONDITION_SEARCH_BLOG_SPAM_WORD_COUNT = 1; 


	/**
	 * <p>
	 * 検索系ブログのスパムワードの条件定数.
	 * </p>
	 */
	private static final int CONDITION_SEARCH_BLOG_SPAM_WORD = 2; 

	/**
	 * <p>
	 * スパム文言のスコア定数.
	 * </p>
	 */
	private static final int SCORE_CONST_STRING = 95;

	/**
	 * <p>
	 * URLのスコア定数1.
	 * </p>
	 */
	private static final double SCORE_URL1 = 0.2;

	/**
	 * <p>
	 * スペースのスコア定数1.
	 * </p>
	 */
	private static final int SCORE_SPACE1 = 200;

	/**
	 * <p>
	 * スペースのスコア定数2.
	 * </p>
	 */
	private static final double SCORE_SPACE2 = 5;

	/**
	 * <p>
	 * スペースのスコア定数3.
	 * </p>
	 */
	private static final double SCORE_SPACE3 = 10000.0;

	/**
	 * <p>
	 * 検索系ブログのスパムワードのスコア定数1.
	 * </p>
	 */
	private static final int SCORE_SEARCH_BLOG_SPAM_WORD1 = 20; 

	/**
	 * <p>
	 * 検索系ブログのスパムワードのスコア定数2.
	 * </p>
	 */
	private static final int SCORE_SEARCH_BLOG_SPAM_WORD2 = 500; 

	/**
	 * <p>
	 * URLのスキップ数.
	 * </p>
	 */
	private static final int SKIP_URL = 70;

	/**
	 * <p>
	 * 改行のスキップ数.
	 * </p>
	 */
	private static final int SKIP_RETURN = 2;

	/**
	 * <p>
	 * スペースのスキップ数.
	 * </p>
	 */
	private static final int SKIP_SPACE = 2;

	/**
	 * <p>
	 * 検索系ブログのスパムワードのスキップ数.
	 * </p>
	 */
	private static final int SKIP_SEARCH_BLOG_SPAM_WORD = 40;

	/**
	 * <p>
	 * URLを表す単語.
	 * </p>
	 */
	private static final String URL_WORD = "http://"; 

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(ContentScore.class);

	/**
	 * <p>
	 * 本文.
	 * </p>
	 */
	private String content = null;

	/**
	 * <p>
	 * ブログID.
	 * </p>
	 */
	private String documentId = null;

	/**
	 * <p>
	 * 投稿者ID.
	 * </p>
	 */
	private String authorId = null;

	/**
	 * <p>
	 * 理由.
	 * </p>
	 */
	private List<String> causes = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param blogFeed ブログ
	 */
	public ContentScore(BlogFeedEntity blogFeed) {
		if (blogFeed != null) {
			content = blogFeed.getBody();
			documentId = blogFeed.getDocumentId();
			authorId = blogFeed.getAuthorId();
		}
		causes = new ArrayList<String>();
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param content コンテンツ
	 */
	public ContentScore(String content) {
		this.content = content;
		causes = new ArrayList<String>();
	}

	/**
	 * <p>
	 * コンテンツのスコアを取得する.
	 * </p><pre>
	 * 1. 本文の置換
	 * (1)句読文字をスペースに置換
	 * (2)ダブルクォートをスペースに置換
	 * (3)2個以上の連続したスペースを1個のスペースに置換
	 * (4)2個以上の連続した全角スペースを1個の全角スペースに置換
	 * (5)2個以上の連続した改行は1個の改行に置換
	 * 
	 * 2. スパムフレーズ(type=51)
	 *  2.1 スパムフレーズ数の取得
	 *   本文に含まれるスパムフレーズ数を取得する。
	 *  2.2 スパムフレーズチェック
	 *   本文にスパムフレーズが出現する場合
	 *   score -= 95^(スパムフレーズ数)
	 *   
	 * 3. URL
	 *  3.1 出現数の取得
	 *   本文に含まれるURL(http://)の出現数を取得する。
	 *   ※出現した位置から70文字は飛ばす
	 *  3.2 URLの出現数にスコアをつける
	 *   score += (URLの出現数)×0.2
	 *  3.3 出現間隔チェック
	 *   以下の条件をすべて満たす場合、以降の処理は行わない
	 *   score += -100000
	 *   (1)URLの出現数が 7 より大きい
	 *   (2)URLの出現間隔（文字数）が 200 より小さい
	 *   ※URLの出現間隔 = (本文の文字数) / (URLの出現数 + 1)
	 * 
	 * 4. ワードサラダ
	 *  4.1 改行とスペースの出現数の取得
	 *   ①本文に含まれる改行の出現数
	 *   ※連続した2回以上の（空白行を含めた）改行はすべて1回と数えている
	 *   ②本文に含まれる半角スペースの出現数（2文字以上の間隔をあける）
	 *   ③本文に含まれる全角スペースの出現数（2文字以上の間隔をあける）
	 *  4.2 ワードサラダチェック
	 *   以下のいずれかの条件を満たす場合
	 *   score -= (スペースの出現頻度)×200 + (本文の平均文字数)×5 + 10000÷(スペースの出現間隔)
	 *   (1)改行の出現数が 2 以上、かつ 、スペースの出現頻度（行単位）が 6.67 より大きい
	 *   (2)本文の平均文字数（行単位）が 80 より大きい、かつ 、スペースの出現間隔（文字数）が 10.2 より小さい
	 *   ※スペースの出現頻度 = (スペースの出現数) / (改行の出現数)
	 *   ※スペースの出現間隔 = (本文の文字数) / (スペースの出現数)
	 * 
	 * 5. 検索系ブログのスパムワード(type=14)
	 *  5.1 出現間隔の取得
	 *   本文に含まれる検索系ブログのスパムワードの出現間隔を取得する。
	 *   ※出現した位置から40文字は飛ばす
	 *  5.2 出現数にスコアをつける
	 *   score -= (検索系ブログのスパムワードの出現数)
	 *  5.3 出現間隔チェック
	 *   以下の条件を全てを満たす場合
	 *   score -= (検索系ブログのスパムワードの出現数)×20 + 500×(検索系ブログのスパムワードの平均出現間隔)÷(検索系ブログのスパムワードの最大出現間隔 + 1)
	 *   (1)検索系ブログのスパムワードの出現数が 1 より大きい
	 *   (2)検索系ブログのスパムワードの平均出現間隔の 2 倍が、検索系ブログのスパムワードが最大出現間隔よりも大きい
	 * </pre>
	 * @return スコア
	 */
	public double getScore() {

		double score = 0;
		double thisScore;

		// 1. 本文の置換(package\SplogFilter.java:1563)
		String content = replaceContent(this.content);

		// 2.1 スパムフレーズ数の取得(package\SplogFilter.java:1571)
		int spamPhraseCount = getSpamPhraseCount(content);

		// 2.2 スパムフレーズチェック(package\SplogFilter.java:1572)
		if (CONDITION_SPAM_PHRASE_COUNT < spamPhraseCount) {
			thisScore = Math.pow(SCORE_CONST_STRING, spamPhraseCount);
			score -= thisScore;
			logger.debug("スパムフレーズチェック" + "\t" + "score=" + (-thisScore)
					+ "\t" + "authorId=" + authorId
					+ "\t" + "documentId=" + documentId
					+ "\t" + "count=" + spamPhraseCount
			);
			causes.add("スパムフレーズ数:" + spamPhraseCount);

			// スコアの最小値を超えたら判定終了
			if (score <= SplogFilterConstants.SCORE_MIN_VALUE) {
				return SplogFilterConstants.SCORE_MIN_VALUE;
			}
		}

		// 3.1 URLの出現数の取得(package\SplogFilter.java:1577)
		int urlCount = StringUtil.getWordCount(content, URL_WORD, SKIP_URL);

		// 3.2 URLの出現数にスコアをつける(package\SplogFilter.java:1578)
		thisScore = (urlCount * SCORE_URL1);
		score += thisScore;
		if (thisScore > 0) {
			logger.debug("URLの出現数" + "\t" + "score=" + thisScore
					+ "\t" + "authorId=" + authorId
					+ "\t" + "documentId=" + documentId 
					+ "\t" + "count=" + urlCount
			);
		}

		// 3.3 URLの出現間隔チェック(package\SplogFilter.java:1579)
		if ((urlCount > CONDITION_URL_COUNT) && ((content.length() / (urlCount + 1)) < CONDITION_URL_INTERVAL)) {
			thisScore = SplogFilterConstants.SCORE_MIN_VALUE;
			score += thisScore;
			logger.debug("URLの出現間隔チェック" + "\t" + "score=" + thisScore
					+ "\t" + "authorId=" + authorId
					+ "\t" + "documentId=" + documentId 
					+ "\t" + "count=" + urlCount
					+ "\t" + "interval=" + (content.length() / (urlCount + 1))
			);
			causes.add("URLの出現間隔(" + "出現数:" + urlCount
					+ ", " + "出現間隔:" + (content.length() / (urlCount + 1))
					+ ")");
			// 判定終了
			return score;
		}

		// 4.1 改行とスペースの出現数の取得(package\SplogFilter.java:1588)
		int returnCount = 1 + StringUtil.getWordCount(content, "\n", SKIP_RETURN);
		int spaceCount1 = 1 + StringUtil.getWordCount(content, " ", SKIP_SPACE);
		int spaceCount2 = 1 + StringUtil.getWordCount(content, ConstantWords.space, SKIP_SPACE);

		// 4.2 ワードサラダチェック(package\SplogFilter.java:1592)
		if ( (CONDITION_RETURN_COUNT <= returnCount) && (CONDITION_SPACE_FREQUENCY < ((double) (spaceCount1 + spaceCount2) / (double) returnCount))
				|| (CONDITION_CONTENT_LENGTH_AVERAGE < ((double) content.length() / returnCount) && ((double) content.length() / (spaceCount1 + spaceCount2)) < CONDITION_SPACE_INTERVAL)
		) {
			thisScore = ((((double) (spaceCount1 + spaceCount2) / (double) returnCount) * SCORE_SPACE1
					+ ((double) content.length() / returnCount) * SCORE_SPACE2
					+ SCORE_SPACE3 / ((double) content.length() / (spaceCount1 + spaceCount2))));
			score -= thisScore;
			logger.debug("本文のワードサラダチェック" + "\t" + "score=" + (-thisScore)
					+ "\t" + "authorId=" + authorId
					+ "\t" + "documentId=" + documentId 
					+ "\t" + "return=" + returnCount + "\t" + "space=" + (spaceCount1 + spaceCount2)
					+ "\t" + "length=" + ((double) content.length() / returnCount)
					+ "\t" + "interval=" + ((double) content.length() / (spaceCount1 + spaceCount2))
					+ "\t" + "frequency=" + ((double) (spaceCount1 + spaceCount2) / (double) returnCount));
			causes.add("ワードサラダ("+ "改行:" + returnCount
					+ ", " + "スペース:" + (spaceCount1 + spaceCount2)
					+ ", " + "改行の出現間隔:" + ((double) content.length() / returnCount)
					+ ", " + "スペースの出現間隔:" + ((double) content.length() / (spaceCount1 + spaceCount2))
					+ ", " + "スペースの出現頻度:" + ((double) (spaceCount1 + spaceCount2) / (double) returnCount)
					+ ")");

			// スコアの最小値を超えたら判定終了
			if (score <= SplogFilterConstants.SCORE_MIN_VALUE) {
				return SplogFilterConstants.SCORE_MIN_VALUE;
			}
		}

		// 5. 検索系ブログのスパムワード(type=14)
		for (String searchBlogSpamWord: ConstantWords.searchBlogSpamWords) {
			// 5.1 出現間隔の取得(package\SplogFilter.java:1598)
			WordIntervalStatistics interval = WordUtil.getWordInterval(content, searchBlogSpamWord, SKIP_SEARCH_BLOG_SPAM_WORD);

			// 5.2 出現数にスコアをつける(package\SplogFilter.java:1599)
			thisScore = interval.getCount();
			score -= thisScore;
			if (thisScore > 0) {
				logger.debug("検索系ブログのスパムワードの出現数" + "\t" + "score=" + (-thisScore)
						+ "\t" + "authorId=" + authorId
						+ "\t" + "documentId=" + documentId 
						+ "\t" + "count=" + interval.getCount()
				);
				causes.add("検索系ブログのスパムワードの出現数:" + interval.getCount());
			}

			// 5.3 出現間隔チェック(package\SplogFilter.java:1602)
			if (interval.getCount() > CONDITION_SEARCH_BLOG_SPAM_WORD_COUNT) {
				if (((double) interval.getAverage() * CONDITION_SEARCH_BLOG_SPAM_WORD) > interval.getMax()) {
					thisScore = (interval.getCount() * SCORE_SEARCH_BLOG_SPAM_WORD1
							  + SCORE_SEARCH_BLOG_SPAM_WORD2 * (interval.getAverage() / (interval.getMax() + 1)));
					score -= thisScore;
					logger.debug("検索系ブログのスパムワードの出現間隔チェック" + "\t" + "score=" + (-thisScore)
							+ "\t" + "authorId=" + authorId
							+ "\t" + "documentId=" + documentId 
							+ "\t" + "blogSpam=" + searchBlogSpamWord 
							+ "\t" + "count=" + interval.getCount() 
							+ "\t" + "average=" + interval.getAverage()
							+ "\t" + "max=" + interval.getMax() 
							+ "\t" + "min=" + interval.getMin() 
							+ "\t" + "length=" + content.length() 
					);
					logger.debug("検索系ブログのスパムワードの出現間隔(" + "スパムワード:" + searchBlogSpamWord 
							+ ", " + "出現数:" + interval.getCount() 
							+ ", " + "平均:" + interval.getAverage()
							+ ", " + "最大:" + interval.getMax() 
							+ ", " + "最小:" + interval.getMin() 
							+ ")");
				}
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
	 * 本文を置換する.
	 * </p><pre>
	 * 1. 句読文字をスペースに置換
	 * 2. ダブルクォートをスペースに置換
	 * 3. 2個以上の連続したスペースを1個のスペースに置換
	 * 4. 2個以上の連続した全角スペースを1個の全角スペースに置換
	 * 5. 2個以上の連続した改行は1個の改行に置換
	 * </pre>
	 * @param content 置換する本文
	 * @return 置換された本文
	 */
	private String replaceContent(String content) {
		if (content == null) return "";
		content = content.trim().replaceAll("\\p{Punct}", " ");
		content = content.replaceAll("\"", " ");
		content = content.replaceAll(" +", " ");
		content = content.replaceAll(ConstantWords.space + "+", ConstantWords.space);
		content = content.replaceAll("\\n{2,}", ConstantWords.period + "\n");
		return content;
	}


	/**
	 * <p>
	 * 本文に含まれるスパムフレーズ(type=51)の数を取得する.
	 * </p>
	 * @param content 本文
	 * @return スパム文言数
	 */
	private int getSpamPhraseCount(String content) {
		return StringUtil.getWordList(content, ConstantWords.contentSpamPhrases).size();
	}
}
