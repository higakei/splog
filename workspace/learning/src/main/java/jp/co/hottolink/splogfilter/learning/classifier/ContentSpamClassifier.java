package jp.co.hottolink.splogfilter.learning.classifier;

import java.util.Map;

import jp.co.hottolink.splogfilter.common.extension.jp.co.hottolink.fusion.core.util.string.HtmlAnarizer;
import jp.co.hottolink.splogfilter.learning.boosting.entity.AnswerEntity;
import jp.co.hottolink.splogfilter.takeda.score.ContentScore;

/**
 * <p>
 * コンテンツフィルタのスパム判別器クラス.
 * </p>
 * @author higa
 */
public class ContentSpamClassifier extends SpamClassifier {

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.classifier.SpamClassifier#classifyBlog(java.util.Map)
	 */
	@Override
	public AnswerEntity classifyBlog(Map<String, Object> data) throws Exception {

		if (data == null) {
			return null;
		}

		// 本文の取得
		String content = (String)data.get("body");
		content = HtmlAnarizer.toPlaneText(content);

		// 解答
		ContentScore scorer = new ContentScore(content);
		double score = scorer.getScore();
		String answer = answer(score);

		// 結果の作成
		AnswerEntity entity = new AnswerEntity();
		entity.setAnswer(answer);
		entity.setSignum(isSpam(answer));

		// 答え合わせ
		String correct = (String)data.get("correct");
		if (correct != null) {
			entity.setMatching(check(answer, correct));
		}

		return entity;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.classifier.SpamClassifier#classifyComment(java.util.Map)
	 */
	@Override
	public AnswerEntity classifyComment(Map<String, Object> data) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>
	 * スコアから解答を求める.
	 * </p>
	 * @param score スコア
	 * @return 解答
	 */
	private String answer(double score) {
		if (score < -3800) {
			return ANSWER_SPAM;
		} else {
			return ANSWER_NOT_SPAM;
		}
	}
}
