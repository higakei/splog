package jp.co.hottolink.splogfilter.learning.master.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * データ種別のEntityクラス.
 * </p>
 * @author higa
 */
public class DataTypeEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 5311910127563740946L;

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
	 * 判別器情報.
	 * </p>
	 */
	private List<ClassifiedTypeEntity> classifiedTypes = null;

	/**
	 * <p>
	 * 名称を取得する.
	 * </p>
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>
	 * 名称を設定する.
	 * </p>
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}

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
	 * 判別器情報を取得する.
	 * </p>
	 * @return 判別器情報
	 */
	public List<ClassifiedTypeEntity> getClassifiedTypes() {
		return classifiedTypes;
	}

	/**
	 * <p>
	 * 判別種別を設定する.
	 * </p>
	 * @param classifiedTypes 判別器情報　
	 */
	public void setClassifiedTypes(List<ClassifiedTypeEntity> classifiedTypes) {
		this.classifiedTypes = classifiedTypes;
	}
}
