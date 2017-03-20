package jp.co.hottolink.splogfilter.learning.boosting.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import jp.co.hottolink.splogfilter.learning.boosting.classifier.ClassifierImpl;

/**
 * <p>
 * 判別器のEntityクラス.
 * </p>
 * @author higa
 */
public class ClassifierEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 2427417914127637453L;

	/**
	 * <p>
	 * 名称.
	 * </p>
	 */
	private String name = null;

	/**
	 * <p>
	 * ラベル.
	 * </p>
	 */
	private String label = null;

	/**
	 * <p>
	 * クラス名.
	 * </p>
	 */
	private String className = null;

	/**
	 * <p>
	 * 判別種別.
	 * </p>
	 */
	private String classifiedType = null;

	/**
	 * <p>
	 * ラベルを取得する.
	 * </p>
	 * @return ラベル
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * <p>
	 * ラベルを設定する.
	 * </p>
	 * @param label ラベル
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * <p>
	 * 判別器名を取得する.
	 * </p>
	 * @return 判別器名
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>
	 * 判別器名を設定する.
	 * </p>
	 * @param name 判別器名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p>
	 * クラス名を取得する.
	 * </p>
	 * @return クラス名
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * <p>
	 * クラス名を設定する.
	 * </p>
	 * @param className クラス名
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * <p>
	 * 判別器クラスを取得する.
	 * </p>
	 * @return 判別器クラス
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 */
	public ClassifierImpl getClassifier() throws IllegalArgumentException,
			SecurityException, InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException {
		return (ClassifierImpl)Class.forName(className).getConstructor().newInstance();
	}

	/**
	 * <p>
	 * 判別種別を取得する.
	 * </p>
	 * @return 判別種別
	 */
	public String getClassifiedType() {
		return classifiedType;
	}

	/**
	 * <p>
	 * 判別種別を設定する.
	 * </p>
	 * @param classifiedType 判別種別
	 */
	public void setClassifiedType(String classifiedType) {
		this.classifiedType = classifiedType;
	}
}
