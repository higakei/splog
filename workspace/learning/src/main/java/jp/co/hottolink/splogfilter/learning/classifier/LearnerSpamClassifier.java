package jp.co.hottolink.splogfilter.learning.classifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.splogfilter.learning.boosting.AdaBoost;
import jp.co.hottolink.splogfilter.learning.boosting.classifier.ClassifierImpl;
import jp.co.hottolink.splogfilter.learning.boosting.entity.AnswerEntity;
import jp.co.hottolink.splogfilter.learning.boosting.entity.HypothesisEntity;

/**
 * <p>
 * 学習機械によるスパム判別器クラス.
 * </p>
 * @author higa
 */
public class LearnerSpamClassifier implements ClassifierImpl {

	/**
	 * <p>
	 * 判別器情報.
	 * </p>
	 */
	private List<HypothesisEntity> hypotheses = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 */
	public LearnerSpamClassifier() {}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param hypotheses 判別器情報
	 */
	public LearnerSpamClassifier(List<HypothesisEntity> hypotheses) {
		this.hypotheses = hypotheses;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.boosting.classifier.ClassifierImpl#signum(java.lang.String)
	 */
	@Override
	public int signum(String answer) {
		if (SpamClassifier.isSpam(answer)) {
			return 1;
		} else {
			return -1;
		}
	}

	/*
	 * (非 Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.boosting.classifier.ClassifierImpl#classify(java.util.Map)
	 */
	@Override
	public AnswerEntity classify(Map<String, Object> data, String type) throws Exception {

		if (data == null) {
			return null;
		}

		// 判定器ごとに判定する
		for (HypothesisEntity hypothesis: hypotheses) {
			ClassifierImpl classifier = hypothesis.getClassifierImpl();
			AnswerEntity answer = classifier.classify(data, type);
			hypothesis.setAnswer(answer);
		}

		// 学習機械の判定を求める
		AdaBoost adaBoost = new AdaBoost();
		boolean isSpam = adaBoost.obtain(hypotheses);
		String answer = answer(isSpam);

		// 結果の作成
		AnswerEntity entity = new AnswerEntity();
		entity.setAnswer(answer);
		entity.setSignum(isSpam);

		// 答え合わせ
		String correct = (String)data.get("correct");
		if (correct != null) {
			entity.setMatching(SpamClassifier.check(answer, correct));
		}

		// 詳細情報の作成
		Map<String, String> detail = new HashMap<String, String>();
		for (HypothesisEntity hypothesis: hypotheses) {
			String key = hypothesis.getClassifierName();
			String value = hypothesis.getAnswerLabel();
			detail.put(key, value);
		}
		entity.setDetail(detail);

		return entity;
	}

	/**
	 * <p>
	 * スパム判定から解答を求める.
	 * </p>
	 * @param isSpam スパム判定
	 * @return 解答
	 */
	private String answer(boolean isSpam) {
		if (isSpam) {
			return SpamClassifier.ANSWER_SPAM;
		} else {
			return SpamClassifier.ANSWER_NOT_SPAM;
		}
	}
}
