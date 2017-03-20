package jp.co.hottolink.splogfilter.learning.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;
import jp.co.hottolink.splogfilter.learning.master.entity.ClassifiedTypeEntity;
import jp.co.hottolink.splogfilter.learning.master.entity.DataTypeEntity;

/**
 * <p>
 * マスターデータのキャッシュクラス.
 * </p>
 * @author higa
 */
public class MasterDataCache {

	/**
	 * <p>
	 * テーブル名:データ種別.
	 * </p>
	 */
	private static final String TABLE_NAME_DATA_TYPE = "data_type";

	/**
	 * <p>
	 * テーブル名:判別器.
	 * </p>
	 */
	private static final String TABLE_NAME_CLASSIFIER = "classifier";

	/**
	 * <p>
	 * テーブル名:判別種別.
	 * </p>
	 */
	private static final String TABLE_NAME_CLASSIFIED_TYPE = "classified_type";

	/**
	 * <p>
	 * マスターデータのキャッシュ.
	 * </p>
	 */
	private static Map<String, Map<String,Object>> cache = new HashMap<String, Map<String,Object>>();

	/**
	 * <p>
	 * ロック中のテーブル.
	 * </p>
	 */
	private static Set<String> lockedTables = new HashSet<String>();

	/**
	 * <p>
	 * データ種別リストを取得する.
	 * </p>
	 * @return マスターテーブルリスト
	 */
	public static List<DataTypeEntity> getDataTypeList() {

		Map<String, Object> table = getTable(TABLE_NAME_DATA_TYPE, null);
		List<DataTypeEntity> list = new ArrayList<DataTypeEntity>();

		for (Object record: table.values()) {
			list.add((DataTypeEntity)record);
		}

		return list;
	}

	/**
	 * <p>
	 * 判別器リストを取得する.
	 * </p>
	 * @return マスターテーブルリスト
	 */
	public static List<ClassifierEntity> getClassifierList() {

		Map<String, Object> table = getTable(TABLE_NAME_CLASSIFIER, null);
		List<ClassifierEntity> list = new ArrayList<ClassifierEntity>();

		for (Object record: table.values()) {
			list.add((ClassifierEntity)record);
		}

		return list;
	}

	/**
	 * <p>
	 * 判別種別リストを取得する.
	 * </p>
	 * @return マスターテーブルリスト
	 */
	public static List<ClassifiedTypeEntity> getClassifiedTypeList() {

		Map<String, Object> table = getTable(TABLE_NAME_CLASSIFIED_TYPE, null);
		List<ClassifiedTypeEntity> list = new ArrayList<ClassifiedTypeEntity>();

		for (Object record: table.values()) {
			list.add((ClassifiedTypeEntity)record);
		}

		return list;
	}

	/**
	 * <p>
	 * データ種別データを取得する.
	 * </p>
	 * @param key ユニークキー
	 * @return マスターデータ
	 */
	public static DataTypeEntity getDataType(String key) {
		Map<String, Object> table = getTable(TABLE_NAME_DATA_TYPE, null);
		return (DataTypeEntity)table.get(key);
	}

	/**
	 * <p>
	 * 判別器データを取得する.
	 * </p>
	 * @param key ユニークキー
	 * @return テーブルデータ
	 */
	public static ClassifierEntity getClassifier(String key) {
		Map<String, Object> table = getTable(TABLE_NAME_CLASSIFIER, null);
		return (ClassifierEntity)table.get(key);
	}

	/**
	 * <p>
	 * 判別種別データを取得する.
	 * </p>
	 * @param key ユニークキー
	 * @return テーブルデータ
	 */
	public static ClassifiedTypeEntity getClassifiedType(String key) {
		Map<String, Object> table = getTable(TABLE_NAME_CLASSIFIED_TYPE, null);
		return (ClassifiedTypeEntity)table.get(key);
	}

	/**
	 * <p>
	 * マスターテーブルリストを取得する.
	 * </p>
	 * @param name テーブル名
	 * @param data マスターデータ
	 * @return マスターテーブルリスト
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getTableList(String name, Object[][] data) {

		Map<String, Object> table = getTable(name, data);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (Object record: table.values()) {
			list.add((Map<String, Object>)record);
		}

		return list;
	}

	/**
	 * <p>
	 * マスターテーブルのレコードを取得する.
	 * </p>
	 * @param key ユニークキー
	 * @param name テーブル名
	 * @param data マスターデータ
	 * @return レコード
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getRecord(String key, String name, Object[][] data) {
		Map<String, Object> table = getTable(name, data);
		return (Map<String, Object>)table.get(key);
	}

	/**
	 * <p>
	 * マスターテーブルを取得する.
	 * </p>
	 * @param name テーブル名
	 * @param data マスターデータ
	 * @return マスターテーブル
	 */
	private static synchronized Map<String, Object> getTable(String name, Object[][] data) {

		Map<String, Object> table = null;

		// テーブルロックが解除するまでループ
		for(;;) {
			synchronized (lockedTables) {
				if (!lockedTables.contains(name)) {
					lockedTables.add(name);
					break;
				}
			}
		}

		// キャッシュから取得
		table = cache.get(name);
		if (table != null) {
			lockedTables.remove(name);
			return table;
		}

		// マスターテーブル作成
		if (TABLE_NAME_CLASSIFIER.equals(name)) {
			table = ClassifierMaster.createTable();
		} else if (TABLE_NAME_CLASSIFIED_TYPE.equals(name)) {
			table = ClassifiedTypeMaster.createTable();
		} else if (TABLE_NAME_DATA_TYPE.equals(name)) {
			table = DataTypeMaster.createTable();
		} else {
			table = createTable(data);
		}

		// キャッシュに追加
		cache.put(name, table);
		lockedTables.remove(name);

		return table;
	}

	/**
	 * <p>
	 * マスターテーブルを作成する.
	 * </p>
	 * @param data マスターデータ
	 * @return マスターテーブル
	 */
	private static Map<String, Object> createTable(Object[][] data) {

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String[] columnName = null;

		for (int i = 0; i < data.length; i++) {
			// カラム名の取得
			if (i == 0) {
				columnName = new String[data[i].length];
				for (int j = 0; j < data[i].length; j++) {
					columnName[j] = (String)data[i][j];
				}
				continue;
			}

			// データの取得
			Map<String, Object> record = new HashMap<String, Object>();
			for (int j = 0; j < columnName.length; j++) {
				record.put(columnName[j], data[i][j]);
			}

			String key = (String)record.get(columnName[0]);
			map.put(key, record);
		}

		return map;
	}
}
