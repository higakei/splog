package jp.co.hottolink.splogfilter.takeda.copycontent.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.constants.ConstantWords;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;
import jp.co.hottolink.splogfilter.takeda.copycontent.CopyContentAnalyze;
import jp.co.hottolink.splogfilter.takeda.copycontent.CopyContentCreator;
import jp.co.hottolink.splogfilter.takeda.copycontent.entity.AuthorCopyContentScoreEntity;
import jp.co.hottolink.splogfilter.takeda.copycontent.entity.CopyContentEntity;
import jp.co.hottolink.splogfilter.takeda.copycontent.entity.CopyContentScoreEntity;

import org.apache.log4j.Logger;

/**
 * <p>
 * コピーコンテンツのスコアクラス.
 * </p>
 * @author higa
 */
public class CopyContentScore {

	/**
	 * <p>
	 * 文書の総スコアの条件定数1.
	 * </p>
	 */
	private static final double CONDITION_TOTAL_DOCUMENT_SCORE1 = 0.01;

	/**
	 * <p>
	 * 文書の総スコアの条件定数2.
	 * </p>
	 */
	private static final double CONDITION_TOTAL_DOCUMENT_SCORE2 = 0.93;

	/**
	 * <p>
	 * 総コピーコンテンツ長の条件定数.
	 * </p>
	 */
	private static final int CONDITION_TOTAL_COPY_CONTENT_LENGTH = 80;

	/**
	 * <p>
	 * 投稿者のコピー率の条件定数1.
	 * </p>
	 */
	private static final double CONDITION_TOTAL_COPY_RATE1 = 0.1;

	/**
	 * <p>
	 * 投稿者のコピー率の条件定数2.
	 * </p>
	 */
	private static final double CONDITION_TOTAL_COPY_RATE2 = 0.75;

	/**
	 * <p>
	 * コピーコンテンツ長の平均の条件定数.
	 * </p>
	 */
	private static final int CONDITION_AVERAGE_TOTAL_COPY_CONTENT_LENGTH = 160;

	/**
	 * <p>
	 * ブロガーのスコア.
	 * </p>
	 */
	private static final int SCORE_IS_BLOGGER = 10000;

	/**
	 * <p>
	 * 投稿者のコピー率のスコア定数.
	 * </p>
	 */
	private static final int SCORE_TOTAL_COPY_RATE = 10000;

	/**
	 * <p>
	 * コピーコンテンツ長の平均のスコア定数.
	 * </p>
	 */
	private static final int SCORE_AVERAGE_TOTAL_COPY_CONTENT_LENGTH = 10;

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(CopyContentScore.class);

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param blogFeeds ブログフィード
	 */
	public CopyContentScore(List<BlogFeedEntity> blogFeeds) {
		this.blogFeeds = blogFeeds;
	}

	/**
	 * <p>
	 * ブログフィード.
	 * </p>
	 */
	private List<BlogFeedEntity> blogFeeds = new ArrayList<BlogFeedEntity>();

	/**
	 * <p>
	 * スコアを取得する.
	 * </p>
	 * @return 投稿者のスコア
	 */
	public List<AuthorCopyContentScoreEntity> getScore() {
		long time = System.currentTimeMillis();
		logger.info("----- start analyze -----");
		// 1. 本文の分析
		List<CopyContentEntity> statistics = analyze(blogFeeds, SplogFilterConstants.COPY_CONTENT_LENGTH, ConstantWords.period);
		logger.info("----- end analyze -----");
		logger.info("analyze:" + getTime(time) + "sec");

		// 2. ブログのスコアを取得
		List<CopyContentScoreEntity> documents = scoreDocument(statistics);
		//logger.info("\t" + "authorId" + "\t" + "documentId" + "\t" + "rate");
		//for (CopyContentScoreEntity document: documents) {
		//	logger.info("\t" + document.getAuthorId()
		//			+ "\t" + document.getDocumentId()
		//			+ "\t" + document.getCopyRate()
		//	);
		//}

		// 3. 投稿者のスコアを取得
		List<AuthorCopyContentScoreEntity> authors = scoreAuthor(documents);

		return authors;
	}

	/**
	 * <p>
	 * 本文を分析する.
	 * </p>
	 * @param blogFeeds ブログフィード
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @return 分析結果
	 */
	public List<CopyContentEntity> analyze(List<BlogFeedEntity> blogFeeds, int length, String delimitor) {
		
		List<CopyContentEntity> list = new ArrayList<CopyContentEntity>();
		if (blogFeeds == null) {
			return list;
		}
		long time = System.currentTimeMillis();
		// 1. 集計データの作成
		CopyContentCreator creator = new CopyContentCreator(blogFeeds);
		list = creator.createByClearBody(length);
		logger.info("create statistics:" + getTime(time) + "sec");
		// 2. 分析の分割
		int size = list.size();
		int width = size;
		int partition = (int)Math.ceil((double)size / width);
		logger.info("analyze partition:" + partition);
		// 3.1 分割しない場合
		if (partition <= 1) {
			analyze(list, length, delimitor);
			return list;
		}

		// 3.2 総当たり分析
		int fromIndex = 0;
		int toIndex = 0;
		for (int i = 0; i < partition; i++) {
			fromIndex = width * i;
			toIndex = width * (i + 1);
			if (toIndex > list.size()) toIndex = size;
			List<CopyContentEntity> list1 = list.subList(fromIndex, toIndex);
			for (int j = i + 1; j < partition; j++) {
				fromIndex = width * j;
				toIndex = width * (j + 1);
				if (toIndex > list.size()) toIndex = size;
				List<CopyContentEntity> list2 = list.subList(fromIndex, toIndex);
				time = System.currentTimeMillis();
				analyze(list1, list2, length, delimitor);
				logger.info("analyze(" + i + "-" + j + "):"+ getTime(time) + "sec");
			}
		}
		
		return list;
	}

	/**
	 * <p>
	 * 文書のスコアを取得する.
	 * </p>
	 * @param documents 文書
	 * @return スコア
	 */
	private List<CopyContentScoreEntity> scoreDocument(List<CopyContentEntity> documents) {
	
		List<CopyContentScoreEntity> list = new ArrayList<CopyContentScoreEntity>();
		if (documents == null) {
			return list;
		}
		
		for (CopyContentEntity document: documents) {
			if (document == null) continue;
	
			// スコア算出
			int contentLength = document.getContentLength(); 
			int copyContentLength = document.getCopyContentLength();
			double copyRate = document.calculateCopyRate();
			double score = copyRate * copyRate * copyRate;
	
			// リストに追加
			CopyContentScoreEntity entity = new CopyContentScoreEntity(document.getAuthorId(), document.getDocumentId());
			entity.setContentLength(contentLength);
			entity.setCopyContentLength(copyContentLength);
			entity.setScore(score);
			entity.setStatistics(document);
			entity.setCopyRate(copyRate);
			list.add(entity);
		}
	
		return list;
	}

	/**
	 * <p>
	 * 投稿者のスコアを取得する.
	 * </p>
	 * @param documents 文書
	 * @return スコア
	 */
	private List<AuthorCopyContentScoreEntity> scoreAuthor(List<CopyContentScoreEntity> documents) {
	
		Map<String, AuthorCopyContentScoreEntity> map = createAuthorMap(documents);
		if (documents == null) {
			return new ArrayList<AuthorCopyContentScoreEntity>(map.values());
		}
		
		for (AuthorCopyContentScoreEntity author: map.values()) {
			author.setScore(getScore(author));
		}
	
		return new ArrayList<AuthorCopyContentScoreEntity>(map.values());
	}

	/**
	 * <p>
	 * 投稿者の文書を集計する.
	 * </p>
	 * @param documents 文書
	 * @return 集計結果
	 */
	private Map<String, AuthorCopyContentScoreEntity> createAuthorMap(List<CopyContentScoreEntity> documents) {
	
		Map<String, AuthorCopyContentScoreEntity> map = new HashMap<String, AuthorCopyContentScoreEntity>();
		if (documents == null) {
			return map;
		}

		// 集計
		for (CopyContentScoreEntity document: documents) {
			if (document == null) continue;
			String authorId = document.getAuthorId();
			AuthorCopyContentScoreEntity author = map.get(authorId);

			if (author == null) {
				author = new AuthorCopyContentScoreEntity(authorId);
				author.addDocument(document);
				map.put(authorId, author);
			} else {
				author.addDocument(document);
			}
		}
	
		return map;
	}

	/**
	 * <p>
	 * 投稿者のスコアをつける.
	 * </p>
	 * @param author 投稿者
	 * @return スコア
	 */
	private double getScore(AuthorCopyContentScoreEntity author) {

		double score = 0;
		if (author == null) {
			return score;
		}
		
		int totalContentLength = author.getTotalContentLength();
		int totalCopyContentLength = author.getTotalCopyContentLength();
		double totalDocumentScore = author.getTotalDocumentScore();
		int documentCount = author.getDocumentCount();
		
		if ((1 < documentCount)
				|| totalDocumentScore <= CONDITION_TOTAL_DOCUMENT_SCORE1
				|| ( (totalCopyContentLength < CONDITION_TOTAL_COPY_CONTENT_LENGTH) && ((totalCopyContentLength / totalContentLength) < CONDITION_TOTAL_COPY_RATE1) )
		) {
			score += SCORE_IS_BLOGGER;
		}


		if (1 <= documentCount && 1 <= totalContentLength) {
			if ((CONDITION_AVERAGE_TOTAL_COPY_CONTENT_LENGTH < (totalCopyContentLength / documentCount) && (CONDITION_TOTAL_DOCUMENT_SCORE2 < totalDocumentScore))
					|| (CONDITION_TOTAL_COPY_RATE2 < (totalCopyContentLength / totalContentLength))
			) {
				score += -(SCORE_TOTAL_COPY_RATE * (totalCopyContentLength / totalContentLength) + SCORE_AVERAGE_TOTAL_COPY_CONTENT_LENGTH * (totalCopyContentLength / documentCount));
				logger.debug("authorId=" + author.getAuthorId() + "\t" + "score=" + score);
			}
		}

		return score;
	}

	/**
	 * <p>
	 * 処理時間を秒で取得する.
	 * </p>
	 * @param start 開始時間
	 * @return 処理時間
	 */
	private double getTime(long start) {
		long end = System.currentTimeMillis();
		double time = end - start;
		return time / 1000;
	}

	/**
	 * <p>
	 * 指定した２つのブログリストの分析を行う.
	 * </p>
	 * @param list1 リスト1
	 * @param list2 リスト2
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 */
	private void analyze(List<CopyContentEntity> list1, List<CopyContentEntity> list2, int length, String delimitor) {
		List<CopyContentEntity> list = new ArrayList<CopyContentEntity>(list1.size() + list2.size());
		list.addAll(list1);
		list.addAll(list2);
		analyze(list, length, delimitor);
	}

	/**
	 * <p>
	 * 本文を分析する.
	 * </p>
	 * @param documents 集計データ
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 */
	private void analyze(List<CopyContentEntity> documents, int length, String delimitor) {
		CopyContentAnalyze analyzer = new CopyContentAnalyze(documents);
		analyzer.analyze(length, delimitor);
	}
}
