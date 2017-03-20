package jp.co.hottolink.splogfilter.takeda.score;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import jp.co.hottolink.splogfilter.common.util.StringUtil;
import jp.co.hottolink.splogfilter.takeda.constants.ConstantWords;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;
import jp.co.hottolink.splogfilter.takeda.entity.SuffixArrayEntity;
import jp.co.hottolink.splogfilter.takeda.entity.UserContentEntity;
import jp.co.hottolink.splogfilter.takeda.statistics.TitleSpamSignStatistics;
import jp.co.hottolink.splogfilter.takeda.statistics.TitleStatistics;
import jp.co.hottolink.splogfilter.takeda.statistics.UserTitleStatistics;
import jp.co.hottolink.splogfilter.takeda.statistics.WordCountStatistics;
import jp.co.hottolink.splogfilter.takeda.util.SuffixArrayUtil;

/**
 * <p>
 * タイトルのスコアクラス.
 * </p>
 * @author higa
 */
public class TitleScore {

	/**
	 * <p>
	 * タイトルに含まれるスパムワードの文字数の条件定数.
	 * </p>
	 */
	private static final int CONDITION_SPAM_WORD_LENGTH = 4;

	/**
	 * <p>
	 * タイトルに含まれるスパムワードの総文字数の平均の条件定数.
	 * </p>
	 */
	private static final int CONDITION_SPAM_WORDS_LENGTH_AVERAGE = 5;

	/**
	 * <p>
	 * 投稿者のブログに対するタイトルにスパムワードを含むブログの割合の条件定数.
	 * </p>
	 */
	private static final double CONDITION_SPAM_WORD_BLOG_RATE = 0.8;

	/**
	 * <p>
	 * スパムワードが含まれるブログ数の条件定数.
	 * </p>
	 */
	private static final int CONDITION_SPAM_WORD_BLOG_COUNT = 1;

	/**
	 * <p>
	 * スパムワードがタイトルに多く含まれるブログ数の条件定数1.
	 * </p>
	 */
	private static final int CONDITION_MORE_SPAM_WORD_BLOG_COUNT1 = 1;

	/**
	 * <p>
	 * スパムワードがタイトルに多く含まれるブログ数の条件定数2.
	 * </p>
	 */
	private static final int CONDITION_MORE_SPAM_WORD_BLOG_COUNT2 = 0;

	/**
	 * <p>
	 * タイトルに含まれるスパムワードの出現頻度の条件定数.
	 * </p>
	 */
	private static final int CONDITION_SPAM_WORD_FREQUENCY = 1;

	/**
	 * <p>
	 * タイトルに含まれるスパム記号の出現頻度の条件定数.
	 * </p>
	 */
	private static final double CONDITION_SPAM_SIGN_FREQUENCY = 4.9;

	/**
	 * <p>
	 * タイトルに含まれるスペースの出現頻度の条件定数1.
	 * </p>
	 */
	private static final double CONDITION_SPACE_FREQUENCY1 = 1.1;

	/**
	 * <p>
	 * タイトルに含まれるスペースの出現頻度の条件定数2.
	 * </p>
	 */
	private static final double CONDITION_SPACE_FREQUENCY2 = 3.1;

	/**
	 * <p>
	 * タイトル中に最も多く出現した文字列の出現したブログ数の条件定数.
	 * </p>
	 */
	private static final int CONDITION_MOST_FREQUENCE_WORD_BLOG_COUNT = 2;

	/**
	 * <p>
	 * タイトル中に最も多く出現した文字列の出現したブログ数の割合の条件定数.
	 * </p>
	 */
	private static final double CONDITION_MOST_FREQUENCE_WORD_BLOG_COUNT_RATE = 0.7;

	
	/**
	 * <p>
	 * タイトル中に最も多く出現した文字列の文字数の条件定数.
	 * </p>
	 */
	private static final int CONDITION_MOST_FREQUENCE_WORD_LENGTH = 1;

	
	/**
	 * <p>
	 * タイトルの総文字数の条件定数.
	 * </p>
	 */
	private static final double CONDITION_TITLES_LENGTH = 1.1;

	/**
	 * <p>
	 * スパムタイトルのスコア.
	 * </p>
	 */
	private static final int SCORE_TITLE_IS_SPAM = -99999;

	/**
	 * <p>
	 * (ブログ数)×(文字数)のスコア定数.
	 * </p>
	 */
	private static final int SCORE_BLOG_COUNT_TIMES_WORD_LENGTH = 1000;

	/**
	 * <p>
	 * 接尾辞の文字数.
	 * </p>
	 */
	private static final int SUFFIX_LENGTH = 2;

	/**
	 * <p>
	 * 接尾辞のブログ数.
	 * </p>
	 */
	private static final int SUFFIX_BLOG_COUNT = 2;

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(TitleScore.class);

	/**
	 * <p>
	 * タイトル.
	 * </p>
	 */
	private List<String> titles = new ArrayList<String>();

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
	 * @param userContent UserContent
	 */
	public TitleScore(UserContentEntity userContent) {
		if (userContent != null) {
			titles = userContent.getTitles();
			authorId = userContent.getAuthorId();
		}
		causes = new ArrayList<String>();
	}

	/**
	 * <p>
	 * 投稿者のスコアを取得する.
	 * </p><pre>
	 * 1. スパムワード
	 * 以下の条件のいずれかを満たす場合
	 * score = -99999
	 * (1)スパムワードがタイトルに含まれるブログ数が 1 より大きい、かつ、(スパムワードがタイトルに含まれるブログ数)が(投稿者のブログ数)の 80% より大きい
	 * (2)スパムワードがタイトルに多く含まれるブログ数が 1 より大きい
	 * (3)タイトルに含まれるスパムワードの総文字数の平均が 5 より大きい
	 * (4)スパムワードがタイトルに多く含まれるブログ数が 0 より大きい、かつ、タイトルに含まれるスパムワードの出現頻度が 1 より大きい
	 * 
	 * 2. ワードサラダ
	 * 以下の条件のいずれかを満たす場合
	 * score = -99999
	 * (1)タイトルに含まれるスパム記号の出現頻度が 4.9 より大きい、かつ、タイトルに含まれるスペースの出現頻度が 1.1 より大きい
	 * (2)タイトルに含まれるスペースの出現頻度が 3.1 より大きい
	 * 
	 * 3. 出現頻度1
	 * 以下の条件を全て満たす場合
	 * score = -1000×(タイトル中に最も多く出現した単語の出現したブログ数)×(タイトル中に最も多く出現した単語の文字数)
	 * (1)タイトル中に最も多く出現した単語の出現したブログ数が 3 より大きい
	 * (2)タイトル中に最も多く出現した単語の出現したブログ数が、投稿者のブログの 70% より大きい
	 * (3)タイトル中に最も多く出現した単語の文字数が 1 より大きい
	 * (4)投稿者のブログ数よりも、(タイトルに含まれるスパムワード数 + タイトルに含まれるスパムワードの総文字数 + タイトルに含まれるスペースの出現数) が大きい
	 * 
	 * 4. 出現頻度2
	 * 以下の条件を満たす場合
	 * score = -99999
	 * (1)(タイトルの総文字数)×1.1 より タイトル中に出現した単語の(出現回数×文字数)の総数 が大きい
	 * </pre>
	 * @return スコア
	 */
	public double getUserScore() {

		// タイトルの分析
		UserTitleStatistics statistics = analyzeUserTitle(titles);
		
		/*
		 * 変数定義(package\SplogFilter.java:915)
		 * spamcount[0]：タイトルに含まれるスパムワード数
		 * spamcount[1]：タイトルに含まれるスパムワードの総文字数（スペース区切り）
		 * spamcount[4]：スパムワードがタイトルに含まれるブログ数
		 * spamcount[5]：スパムワードがタイトルに多く(4文字以上)含まれるブログ数
		 * score1[3]：ブログ数
		 */
		int spamWordCount = statistics.getSpamWordCount();													// spamcount[0](=score2[0])
		int spamWordsLength1 = statistics.getSpamWordsLength(true);										// spamcount[1]
		int spamWordBlogCount = statistics.getSpamWordBlogCount();											// spamcount[4]
		int moreSpamWordBlogCount = statistics.getMoreSpamWordBlogCount(CONDITION_SPAM_WORD_LENGTH, true);	// spamcount[5]
		int blogCount = statistics.getBlogCount();															// score1[3]

		// 1. スパムワードチェック(SplogFilter.java:961)
		if ((CONDITION_SPAM_WORD_BLOG_COUNT < spamWordBlogCount && (CONDITION_SPAM_WORD_BLOG_RATE * blogCount) < spamWordBlogCount)
				|| (CONDITION_MORE_SPAM_WORD_BLOG_COUNT1 < moreSpamWordBlogCount)
				|| CONDITION_SPAM_WORDS_LENGTH_AVERAGE < (spamWordsLength1 / blogCount)
				|| ((CONDITION_MORE_SPAM_WORD_BLOG_COUNT2 < moreSpamWordBlogCount) && CONDITION_SPAM_WORD_FREQUENCY < (spamWordCount / blogCount))
		) {
			double score = SCORE_TITLE_IS_SPAM;
			logger.debug("スパムワードチェック" + "\t" + "score=" + score
					+ "\t" + "authorId=" + authorId
					+ "\t" + "spamTitleCount=" + spamWordCount
					+ "\t" + "spamTitlesLength1=" + spamWordsLength1
					+ "\t" + "spamTitleBlogCount=" + spamWordBlogCount
					+ "\t" + "moreSpamTitleBlogCount=" + moreSpamWordBlogCount
					+ "\t" + "blogCount=" + blogCount
			);
			causes.add("タイトルスパムワード(" + "スパムワード数:" + spamWordCount
					+ ", " + "スパムワードの総文字数:" + spamWordsLength1
					+ ", " + "スパムワードが含まれるブログ数:" + spamWordBlogCount
					+ ", " + "スパムワードが多く含まれるブログ数:" + moreSpamWordBlogCount
					+ ", " + "投稿者のブログ数:" + blogCount
					+ ")");

			return score;
		}

		/*
		 * 変数定義(package\SplogFilter.java:974)
		 * score2[1]：タイトルに含まれるスパムワードの総文字数
		 * score2[3]：タイトルに含まれるスペースの出現数
		 * score2[6]：タイトルに含まれるスパム記号の出現数
		 */
		int spamWordsLength2 = statistics.getSpamWordsLength(false);		// score2[1]
		int spaceCount = statistics.getSpaceCount();						// score2[3](=spamcount[3])
		int spamSignsCount = statistics.getSpamSignsCount();				// score2[6](=spamcount[6])

		/*
		 * 変数定義(package\SplogFilter.java:973)
		 * score1[0]：タイトル中に最も多く出現した単語の出現したブログ数
		 * score1[1]：タイトル中に最も多く出現した単語の文字数
		 * score1[2]：タイトル中に出現した単語の（出現回数×文字数）の総数
		 * score1[3]：タイトルの総文字数
		 */
		int mostFrequenceWordBlogCount = 0;	// score1[0]
		int mostFrequenceWordLength = 0;	// score1[1]
		int blogCountTimesWordLength = 0;	// score1[2]
		int titlesLength = 0;				// score1[3]
		if (blogCount > 1) {
			mostFrequenceWordBlogCount = statistics.getMostFrequenceWordBlogCount();
			mostFrequenceWordLength = statistics.getMostFrequenceWordLength();
			blogCountTimesWordLength = statistics.getBlogCountTimesWordLength();
			titlesLength = statistics.getTitlesLength();
		}

		// 2. ワードサラダチェック(package\SplogFilter.java:983)
		if (( (((double) spamSignsCount / blogCount) > CONDITION_SPAM_SIGN_FREQUENCY) && (((double) spaceCount / blogCount) > CONDITION_SPACE_FREQUENCY1) )
				|| (((double) spaceCount / blogCount) > CONDITION_SPACE_FREQUENCY2)
		) {
			double score = SCORE_TITLE_IS_SPAM;
			logger.debug("タイトルのワードサラダチェック" + "\t" + "score=" + score
					+ "\t" + "authorId=" + authorId
					+ "\t" + "spamSign=" + ((double) spamSignsCount / blogCount)
					+ "\t" + "space=" + spaceCount
			);
			causes.add("タイトルのワードサラダ(" + "スパム記号の出現頻度:" + ((double) spamSignsCount / blogCount)
					+ ", " + "スペースの出現頻度:" + spaceCount
					+ ")");

			return score;
		}

		// 3. 出現頻度チェック1(package\SplogFilter.java:996)
		if (CONDITION_MOST_FREQUENCE_WORD_BLOG_COUNT < mostFrequenceWordBlogCount
					&& ((CONDITION_MOST_FREQUENCE_WORD_BLOG_COUNT_RATE * blogCount) < mostFrequenceWordBlogCount
					&& CONDITION_MOST_FREQUENCE_WORD_LENGTH < mostFrequenceWordLength
					&& (blogCount < (spamWordCount + spamWordsLength2 + spaceCount)))
		) {
			double score = -(SCORE_BLOG_COUNT_TIMES_WORD_LENGTH * mostFrequenceWordBlogCount * mostFrequenceWordLength);
			logger.debug("タイトルの出現頻度チェック1" + "\t" + "score=" + score
					+ "\t" + "authorId=" + authorId
					+ "\t" + "mostFrequenceWordBlogCount=" + mostFrequenceWordBlogCount
					+ "\t" + "blogCount=" + blogCount
					+ "\t" + "mostFrequenceWordLength=" + mostFrequenceWordLength
					+ "\t" + "spamTitleCount=" + spamWordCount
					+ "\t" + "spamTitlesLength2=" + spamWordsLength2
					+ "\t" + "spaceCount=" + spaceCount
			);
			causes.add("タイトルの出現頻度1(" + "タイトル中に最も多く出現した単語の出現したブログ数:" + mostFrequenceWordBlogCount
					+ ", " + "投稿者のブログ数:" + blogCount
					+ ", " + "タイトル中に最も多く出現した単語の文字数:" + mostFrequenceWordLength
					+ ", " + "タイトルに含まれるスパムワード数:" + spamWordCount
					+ ", " + "タイトルに含まれるスパムワードの総文字数:" + spamWordsLength2
					+ ", " + "タイトルに含まれるスペースの出現数:" + spaceCount
					+ ")");

			if (score < SplogFilterConstants.SCORE_MIN_VALUE) score = SplogFilterConstants.SCORE_MIN_VALUE;
			return score;
		}

		// 4. 出現頻度チェック2(package\SplogFilter.java:1007)
		if (titlesLength * CONDITION_TITLES_LENGTH < blogCountTimesWordLength) {
			double score = SCORE_TITLE_IS_SPAM;
			logger.debug("タイトルの出現頻度チェック2" + "\t" + "score=" + score
					+ "\t" + "authorId=" + authorId
					+ "\t" + "titlesLength=" + titlesLength
					+ "\t" + "blogCountTimesWordLength=" + blogCountTimesWordLength
			);
			causes.add("タイトルの出現頻度2(" + "タイトルの総文字数:" + titlesLength
					+ ", " + "タイトル中に出現した単語の(出現回数×文字数)の総数:" + blogCountTimesWordLength
					+ ")");
			return score;
		}

		return SplogFilterConstants.SCORE_NOMAL_BLOG_FEED;
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
	 * 投稿者のタイトルを分析する.
	 * </p><pre>
	 * 1. タイトルスパムワード(type=15)
	 * 投稿者の各タイトルごとに、スパムワードを取得する。
	 * 
	 * 2. タイトルスパム記号(type=25)
	 * 投稿者の各タイトルごとに、タイトルスパム記号の出現数を取得する。
	 * ※1文字間隔をあけて出現数を集計する。
	 * 
	 * 3. スペース
	 * 投稿者の各タイトルごとに、スペースの出現数を取得する。
	 * ※半角スペースは、2文字間隔をあけて出現数を集計する。
	 * ※全角スペースは、1文字間隔をあけて出現数を集計する。
	 * 
	 * 4. 単語の出現数
	 * 投稿者のタイトル中、2回以上（ブログ数）出現する、2文字以上の単語のブログ数を取得する。
	 * </pre>
	 * @param titles タイトル
	 * @return 統計データ
	 */
	private UserTitleStatistics analyzeUserTitle(List<String> titles) {
		UserTitleStatistics statistics = new UserTitleStatistics();
		statistics.setTitles(analyzeTitle(titles));
		statistics.setWords(getWordCount(titles));
		return statistics;
	}

	/**
	 * <p>
	 * タイトルリストに含まれる単語の出現数を取得する.
	 * </p><pre>
	 * タイトルリストに2回（タイトル数）以上出現する、2文字以上の単語のタイトル数を取得する
	 * </pre>
	 * @param titles タイトルリスト
	 * @return 出現数
	 */
	private List<WordCountStatistics> getWordCount(List<String> titles) {
		
		List<WordCountStatistics> result = new ArrayList<WordCountStatistics>();

		// 単語と出現数の取得
		List<SuffixArrayEntity> list = SuffixArrayUtil.getSuffixArrayList(titles, SUFFIX_LENGTH, ConstantWords.period, SUFFIX_BLOG_COUNT);
		//List<SuffixArrayEntity> list = SuffixArrayUtil.getFrequencyTerms(titles, SUFFIX_LENGTH, ConstantWords.period);

		loop: for (SuffixArrayEntity element: list) {
			String suffix = element.getSuffix();
			for (String word: ConstantWords.titleExcludeWords) {
				suffix = suffix.replaceAll(word, "");
			}
			suffix = suffix.replaceAll(ConstantWords.titleExcludeSignPattern, "");
			suffix = suffix.replaceAll("\\p{Space}|\\p{Punct}|\\p{Digit}", "");
			if (suffix.length() < 2) {
				continue;
			}

			List<String> strdiv = StringUtil.divide(ConstantWords.hirakana + suffix);
			if (strdiv.size() > 1) {
				for (int i = 0; i < result.size(); i++) {
					WordCountStatistics word = result.get(i);
					if (word.getBlogCount() <= element.getIndexCount()) {
						if (suffix.indexOf(word.getWord()) >= 0) {
							result.remove(i);
							--i;
							continue;
						}
					}
					if (word.getWord().indexOf(suffix) >= 0) {
						continue loop;
					}
				}
			
				WordCountStatistics word = new WordCountStatistics();
				word.setWord(suffix);
				word.setBlogCount(element.getIndexCount());
				result.add(word);
			}
			
		}

		for (WordCountStatistics word: result) {
			logger.debug(word.getWord() + "\t" + word.getBlogCount());
		}

		return result;
	}

	/**
	 * <p>
	 * タイトルごとに分析する.
	 * </p>
	 * @param titles タイトル
	 * @return 統計データ
	 */
	private List<TitleStatistics> analyzeTitle(List<String> titles) {
		List<TitleStatistics> list = new ArrayList<TitleStatistics>();
		for (String title: titles) {
			TitleStatistics element = new TitleStatistics(title);
			element.setSpamWords(StringUtil.getWordList(title, ConstantWords.titleSpamWords));
			element.setSpamSigns(getTitleSpamSigns(title));
			element.setSpaceCount(getSpaceCount(title));
			list.add(element);
		}
		return list;
	}
	
	/**
	 * <p>
	 * 指定したタイトルのスパム記号の統計データを取得する.
	 * </p><pre>
	 * 1文字間隔をあけて出現数を集計する
	 * </pre>
	 * @param titles タイトル
	 * @return 統計データ
	 */
	private List<TitleSpamSignStatistics> getTitleSpamSigns(String title) {
		List<TitleSpamSignStatistics> list = new ArrayList<TitleSpamSignStatistics>();
		List<String> spamSigns = StringUtil.getWordList(title, ConstantWords.titleSpamSigns);
		for (String spamSign: spamSigns) {
			TitleSpamSignStatistics element = new TitleSpamSignStatistics(spamSign);
			element.setCount(StringUtil.getWordCount(title, spamSign, 1));
			list.add(element);
		}
		return list;
	}

	/**
	 * <p>
	 * 指定したタイトルのスペースの出現数を取得する.
	 * </p><pre>
	 * 半角スペースは、2文字間隔をあけて出現数を集計する
	 * 全角スペースは、1文字間隔をあけて出現数を集計する
	 * </pre>
	 * @param title タイトル.
	 * @return 出現数
	 */
	private int getSpaceCount(String title) {
		int count = 0;
		count += StringUtil.getWordCount(title, " ", 2);
		count += StringUtil.getWordCount(title, ConstantWords.space, 1);
		return count;
	}
}
