package jp.co.hottolink.splogfilter.learning.classifier;

import java.util.Map;

import jp.co.hottolink.splogfilter.learning.boosting.classifier.ClassifierImpl;
import jp.co.hottolink.splogfilter.learning.boosting.entity.AnswerEntity;

/**
 * <p>
 * スパム判別器の抽象クラス.
 * </p>
 * @author higa
 */
public abstract class SpamClassifier implements ClassifierImpl {

	/**
	 * <p>
	 * スパム判定の解答.
	 * </p>
	 */
	protected static final String ANSWER_SPAM = "spam";

	/**
	 * <p>
	 * 非スパム判定の解答.
	 * </p>
	 */
	protected static final String ANSWER_NOT_SPAM = "safe";

	/**
	 * <p>
	 * ブログデータをスパム判定する.
	 * </p>
	 * @param data データ
	 * @return 判別結果
	 * @throws Exception
	 */
	public abstract AnswerEntity classifyBlog(Map<String, Object> data) throws Exception;

	/**
	 * <p>
	 * コメントデータをスパム判定する.
	 * </p>
	 * @param data データ
	 * @return 判別結果
	 * @throws Exception
	 */
	public abstract AnswerEntity classifyComment(Map<String, Object> data) throws Exception;

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.boosting.classifier.ClassifierImpl#signum(java.lang.String)
	 */
	@Override
	public int signum(String answer) {
		if (isSpam(answer)) {
			return 1;
		} else {
			return -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.boosting.classifier.ClassifierImpl#classify(java.util.Map, java.lang.String)
	 */
	@Override
	public AnswerEntity classify(Map<String, Object> data, String type) throws Exception {

		if (data == null) {
			return null;
		}

		if ("blog".equals(type)) {
			return classifyBlog(data);
		} else if ("comment".equals(type)) {
			return classifyComment(data);
		} else {
			throw new RuntimeException("データ種別が不正です。");
		}
	}

	/**
	 * <p>
	 * 解答の答え合わせをする.
	 * </p>
	 * @param answer 解答
	 * @param correct 正解
	 * @return <code>true</code>:正解, <code>false</code>:不正解
	 */
	protected static boolean check(String answer, String correct) {
		if (isSpam(answer) && isSpam(correct)) {
			return true;
		} if (!isSpam(answer) && !isSpam(correct)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * 解答をスパム判別する.
	 * </p>
	 * @param answer 解答
	 * @return <code>true</code>:スパムである, <code>false</code>:スパムでない
	 */
	protected static boolean isSpam(String answer) {
		if (ANSWER_SPAM.equals(answer)) {
			return true;
		} else {
			return false;
		}
	}
}
