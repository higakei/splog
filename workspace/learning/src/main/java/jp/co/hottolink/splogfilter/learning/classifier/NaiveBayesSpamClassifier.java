package jp.co.hottolink.splogfilter.learning.classifier;

import java.util.Map;

import jp.co.hottolink.splogfilter.common.extension.jp.co.hottolink.fusion.core.util.string.HtmlAnarizer;
import jp.co.hottolink.splogfilter.learning.boosting.entity.AnswerEntity;
import jp.co.hottolink.splogfilter.santi.bayes.NaiveBayesAnalyzer;

/**
 * <p>
 * NaiveBayesフィルタのスパム判別器クラス.
 * </p>
 * @author higa
 */
public class NaiveBayesSpamClassifier extends SpamClassifier {

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.classifier.SpamClassifier#classifyBlog(java.util.Map)
	 */
	@Override
	public AnswerEntity classifyBlog(Map<String, Object> data) throws Exception {

		if (data == null) {
			return null;
		}

		// タイトルと本文の取得
		String title = HtmlAnarizer.toPlaneText((String)data.get("title"));
		String body = HtmlAnarizer.toPlaneText((String)data.get("body"));

		// 解答
		NaiveBayesAnalyzer analyzer = new NaiveBayesAnalyzer();
		Map<String, Object> response = analyzer.analyze(title, body);
		String judge = (String)response.get("judge");
		String answer = answer(judge);

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
	 * APIの判定から解答を求める.
	 * </p>
	 * @param judge 判定
	 * @return 解答
	 */
	private String answer(String judge) {
		if (ANSWER_SPAM.equals(judge)) {
			return ANSWER_SPAM;
		} else {
			return ANSWER_NOT_SPAM;
		}
	}
}
