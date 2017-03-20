package jp.co.hottolink.splogfilter.learning.logic.matching;

import java.io.Serializable;

import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;

/**
 * <p>
 * 判別器の照合結果のEntityクラス.
 * </p>
 * @author higa
 */
public class ClassifierMatchingEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 4933805548239160961L;

	/**
	 * <p>
	 * 判別器情報.
	 * </p>
	 */
	private ClassifierEntity classifier = null;

	/**
	 * <p>
	 * 解答数.
	 * </p>
	 */
	private int answer = 0;

	/**
	 * <p>
	 * 正解数.
	 * </p>
	 */
	private int correct = 0;

	/**
	 * <p>
	 * 判別器情報を取得する.
	 * </p>
	 * @return 判別器情報
	 */
	public ClassifierEntity getClassifier() {
		return classifier;
	}

	/**
	 * <p>
	 * 判別器情報を設定する.
	 * </p>
	 * @param classifier 判別器情報
	 */
	public void setClassifier(ClassifierEntity classifier) {
		this.classifier = classifier;
	}

	/**
	 * <p>
	 * 解答数を取得する.
	 * </p>
	 * @return 解答数
	 */
	public int getAnswer() {
		return answer;
	}

	/**
	 * <p>
	 * 解答数を設定する.
	 * </p>
	 * @param answer 解答数
	 */
	public void setAnswer(int answer) {
		this.answer = answer;
	}

	/**
	 * <p>
	 * 正解数を取得する.
	 * </p>
	 * @return 正解数
	 */
	public int getCorrect() {
		return correct;
	}

	/**
	 * <p>
	 * 正解数を設定する.
	 * </p>
	 * @param correct 正解数
	 */
	public void setCorrect(int correct) {
		this.correct = correct;
	}

	/**
	 * <p>
	 * 正解率を取得する.
	 * </p>
	 * @return 正解率
	 */
	public double getRate() {
		return (double)correct / answer;
	}

	/**
	 * <p>
	 * 判別器のラベルを取得する.
	 * </p>
	 * @return 判別器のラベル
	 * @see jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity#getLabel()
	 */
	public String getClassifierLabel() {
		return classifier.getLabel();
	}
}
