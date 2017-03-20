package jp.co.hottolink.splogfilter.learning.master.entity;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import jp.co.hottolink.splogfilter.learning.boosting.classifier.ClassifierImpl;
import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;

/**
 * <p>
 * 判別種別のEntityクラス.
 * </p>
 * @author higa
 */
public class ClassifiedTypeEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -8027285236978968313L;

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
	 * 判別器情報.
	 * </p>
	 */
	private List<ClassifierEntity> classifiers = null;

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
	@SuppressWarnings("unchecked")
	public ClassifierImpl getClassifier(List hypotheses) throws IllegalArgumentException,
			SecurityException, InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException {
		Constructor<?> constructor = Class.forName(className).getConstructor(List.class);
		return (ClassifierImpl)constructor.newInstance(hypotheses);
	}

	/**
	 * <p>
	 * 判別器情報を取得する.
	 * </p>
	 * @return 判別器情報
	 */
	public List<ClassifierEntity> getClassifiers() {
		return classifiers;
	}

	/**
	 * <p>
	 * 判別器情報を設定する.
	 * </p>
	 * @param classifiers 判別器情報
	 */
	public void setClassifiers(List<ClassifierEntity> classifiers) {
		this.classifiers = classifiers;
	}
}
