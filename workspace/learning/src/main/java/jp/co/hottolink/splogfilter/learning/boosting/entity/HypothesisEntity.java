package jp.co.hottolink.splogfilter.learning.boosting.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import jp.co.hottolink.splogfilter.learning.boosting.classifier.ClassifierImpl;

/**
 * <p>
 * 判定器の仮説のEntityクラス.
 * </p>
 * @author higa
 */
public class HypothesisEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -3539439308024036706L;

	/**
	 * <p>
	 * 判定器情報.
	 * </p>
	 */
	private ClassifierEntity classifier = null;

	/**
	 * <p>
	 * 信頼度.
	 * </p>
	 */
	private double reliability = 0.0;

	/**
	 * <p>
	 * 仮説情報.
	 * </p>
	 */
	private AnswerEntity answer = null;

	/**
	 * <p>
	 * 判定器情報を取得する.
	 * </p>
	 * @return 判定器情報
	 */
	public ClassifierEntity getClassifier() {
		return classifier;
	}

	/**
	 * <p>
	 * 判定器情報を設定する.
	 * </p>
	 * @param classifier 判定器情報
	 */
	public void setClassifier(ClassifierEntity classifier) {
		this.classifier = classifier;
	}

	/**
	 * <p>
	 * 信頼度を取得する.
	 * </p>
	 * @return 信頼度
	 */
	public double getReliability() {
		return reliability;
	}

	/**
	 * <p>
	 * 信頼度を設定する.
	 * </p>
	 * @param reliability 信頼度
	 */
	public void setReliability(double reliability) {
		this.reliability = reliability;
	}

	/**
	 * <p>
	 * 仮説情報を取得する.
	 * </p>
	 * @return 仮説情報
	 */
	public AnswerEntity getAnswer() {
		return answer;
	}

	/**
	 * <p>
	 * 仮説情報を設定する.
	 * </p>
	 * @param answer 仮説情報
	 */
	public void setAnswer(AnswerEntity answer) {
		this.answer = answer;
	}

	/**
	 * <p>
	 * 判定器を取得する.
	 * </p>
	 * @return 判定器
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 * @see jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity#getClassifier()
	 */
	public ClassifierImpl getClassifierImpl() throws IllegalArgumentException,
			SecurityException, InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException {
		return classifier.getClassifier();
	}

	/**
	 * <p>
	 * 判定器の判定ラベルを取得する.
	 * </p>
	 * @return
	 * @see jp.co.hottolink.splogfilter.learning.boosting.entity.AnswerEntity#getSignum()
	 */
	public int getSignum() {
		return answer.getSignum();
	}

	/**
	 * <p>
	 * 判定器名を取得する.
	 * </p>
	 * @return 判定器名
	 * @see jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity#getName()
	 */
	public String getClassifierName() {
		return classifier.getName();
	}

	/**
	 * <p>
	 * 仮説のラベルを取得する.
	 * </p>
	 * @return
	 * @see jp.co.hottolink.splogfilter.learning.boosting.entity.AnswerEntity#toString()
	 */
	public String getAnswerLabel() {
		return answer.getAnswer().toString();
	}

	/**
	 * <p>
	 * 判別器のラベルを取得する.
	 * </p>
	 * @return
	 * @see jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity#getLabel()
	 */
	public String getLabel() {
		return classifier.getLabel();
	}

	/**
	 * <p>
	 * 判別名を取得する.
	 * </p>
	 * @return
	 * @see jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity#getName()
	 */
	public String getName() {
		return classifier.getName();
	}
}
