package jp.co.hottolink.splogfilter.learning.logic.trial;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;
import jp.co.hottolink.splogfilter.learning.logic.entity.DatasetEntity;
import jp.co.hottolink.splogfilter.learning.master.entity.ClassifiedTypeEntity;
import jp.co.hottolink.splogfilter.learning.master.entity.DataTypeEntity;

/**
 * <p>
 *  学習データのトライアル情報のEntityクラス.
 * </p>
 * @author higa
 */
public class TrialEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -4345083230240273577L;

	/**
	 * <p>
	 * トライアルID.
	 * </p>
	 */
	private Integer id = null;

	/**
	 * <p>
	 * トライアル結果名.
	 * </p>
	 */
	private String resultName = null;

	/**
	 * <p>
	 * データセット情報.
	 * </p>
	 */
	private DatasetEntity dataset = null;

	/**
	 * <p>
	 * データ種別.
	 * </p>
	 */
	private DataTypeEntity dataType = null;

	/**
	 * <p>
	 * 判別種別.
	 * </p>
	 */
	private ClassifiedTypeEntity classifiedType = null;

	/**
	 * <p>
	 * 判別器リスト.
	 * </p>
	 */
	private List<ClassifierEntity> classifiers = null;

	/**
	 * <p>
	 * データ項目名.
	 * </p>
	 */
	private Map<String, String> itemNames = null;

	/**
	 * <p>
	 * トライアルIDを取得する.
	 * </p>
	 * @return トライアルID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * <p>
	 * トライアルIDを設定する.
	 * </p>
	 * @param id トライアルID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * <p>
	 * トライアル結果名を取得する.
	 * </p>
	 * @return トライアル結果名
	 */
	public String getResultName() {
		return resultName;
	}

	/**
	 * <p>
	 * トライアル結果名を設定する.
	 * </p>
	 * @param resultName トライアル結果名
	 */
	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	/**
	 * <p>
	 * データセット情報を取得する.
	 * </p>
	 * @return データセット情報
	 */
	public DatasetEntity getDataset() {
		return dataset;
	}

	/**
	 * <p>
	 * データセット情報を設定する.
	 * </p>
	 * @param dataset データセット情報
	 */
	public void setDataset(DatasetEntity dataset) {
		this.dataset = dataset;
	}

	/**
	 * <p>
	 * 判別器リストを取得する.
	 * </p>
	 * @return 判別器リスト
	 */
	public List<ClassifierEntity> getClassifiers() {
		return classifiers;
	}

	/**
	 * <p>
	 * 判別器リストを設定する.
	 * </p>
	 * @param classifiers 判別器リスト
	 */
	public void setClassifiers(List<ClassifierEntity> classifiers) {
		this.classifiers = classifiers;
	}

	/**
	 * <p>
	 * データ種別を取得する.
	 * </p>
	 * @return データ種別
	 */
	public DataTypeEntity getDataType() {
		return dataType;
	}

	/**
	 * <p>
	 * データ種別を設定する.
	 * </p>
	 * @param dataType データ種別
	 */
	public void setDataType(DataTypeEntity dataType) {
		this.dataType = dataType;
	}

	/**
	 * <p>
	 * 判別種別を取得する.
	 * </p>
	 * @return 判別種別
	 */
	public ClassifiedTypeEntity getClassifiedType() {
		return classifiedType;
	}

	/**
	 * <p>
	 * 判別種別を設定する.
	 * </p>
	 * @param classifiedType 判別種別
	 */
	public void setClassifiedType(ClassifiedTypeEntity classifiedType) {
		this.classifiedType = classifiedType;
	}

	/**
	 * <p>
	 * データ種別名を取得する.
	 * </p>
	 * @return
	 * @see jp.co.hottolink.splogfilter.learning.master.entity.DataTypeEntity#getName()
	 */
	public String getDataTypeName() {
		return dataType.getName();
	}

	/**
	 * <p>
	 * データ項目名を取得する.
	 * </p>
	 * @return データ項目名
	 */
	public Map<String, String> getItemNames() {
		return itemNames;
	}

	/**
	 * <p>
	 * データ項目名を設定する.
	 * </p>
	 * @param itemNames データ項目名
	 */
	public void setItemNames(Map<String, String> itemNames) {
		this.itemNames = itemNames;
	}

	/**
	 * <p>
	 * データ種別のラベルを取得する.
	 * </p>
	 * @return データ種別のラベル
	 * @see jp.co.hottolink.splogfilter.learning.master.entity.DataTypeEntity#getLabel()
	 */
	public String getDataTypeLabel() {
		return dataType.getLabel();
	}

	/**
	 * <p>
	 * 判別種別のラベルを取得する.
	 * </p>
	 * @return 判別種別のラベル
	 * @see jp.co.hottolink.splogfilter.learning.master.entity.ClassifiedTypeEntity#getLabel()
	 */
	public String getClassifiedTypeLabel() {
		return classifiedType.getLabel();
	}
}
