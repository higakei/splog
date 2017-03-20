package jp.co.hottolink.splogfilter.learning.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.splogfilter.learning.master.entity.ClassifiedTypeEntity;
import jp.co.hottolink.splogfilter.learning.master.entity.DataTypeEntity;

/**
 * <p>
 * データ種別マスタークラス.
 * </p>
 * @author higa
 */
public class DataTypeMaster {

	/**
	 * <p>
	 * データ種別のマスターデータ.
	 * </p>
	 */
	private static final Object[][] DATA = {
		{ "name", "label" }
		, { "blog", "ブログ" }
		, { "comment", "コメント" }
	};

	/**
	 * <p>
	 * データ種別に紐付く判別種別.
	 * </p>
	 */
	private static final Object[][] CLASSIFIED_TYPE_BY_DATA_TYPE = {
		{ "data_type", "classified_type" }
		, { "blog", "spam" }
		, { "comment", "spam" }
	};

	/**
	 * <p>
	 * データ種別のマスターテーブルを作成する.
	 * </p>
	 * @return マスターテーブル
	 */
	public static Map<String, Object> createTable() {

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String[] columnName = null;

		for (int i = 0; i < DATA.length; i++) {
			// カラム名の取得
			if (i == 0) {
				columnName = new String[DATA[i].length];
				for (int j = 0; j < DATA[i].length; j++) {
					columnName[j] = (String)DATA[i][j];
				}
				continue;
			}

			// データの取得
			Map<String, Object> record = new HashMap<String, Object>();
			for (int j = 0; j < columnName.length; j++) {
				record.put(columnName[j], DATA[i][j]);
			}

			// 判別種別の取得
			String dataType = (String)record.get("name");
			List<ClassifiedTypeEntity> classifiedTypes = getClassifiedType(dataType);

			DataTypeEntity entity = new DataTypeEntity();
			entity.setName(dataType);
			entity.setLabel((String)record.get("label"));
			entity.setClassifiedTypes(classifiedTypes);

			String key = (String)record.get(columnName[0]);
			map.put(key, entity);
		}

		return map;
	}

	/**
	 * <p>
	 * データ種別に紐付く判別種別を取得する.
	 * </p>
	 * @param dataType データ種別
	 * @return 判別種別
	 */
	private static List<ClassifiedTypeEntity> getClassifiedType(String dataType) {

		List<ClassifiedTypeEntity> list = new ArrayList<ClassifiedTypeEntity>();

		for (int i = 1; i < CLASSIFIED_TYPE_BY_DATA_TYPE.length; i++) {
			if (dataType.equals(CLASSIFIED_TYPE_BY_DATA_TYPE[i][0])) {
				ClassifiedTypeEntity record = MasterDataCache.getClassifiedType((String)CLASSIFIED_TYPE_BY_DATA_TYPE[i][1]);
				list.add(record);
			}
		}

		return list;
	}
}
