package jp.co.hottolink.splogfilter.learning.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;
import jp.co.hottolink.splogfilter.learning.master.entity.ClassifiedTypeEntity;

/**
 * <p>
 * 判別種別マスタークラス.
 * </p>
 * @author higa
 */
public class ClassifiedTypeMaster {

	/**
	 * <p>
	 * 判別種別マスターデータ.
	 * </p>
	 */
	private static final Object[][] DATA = {
		{ "name", "label", "classifier" }
		, { "spam", "スパム判定", "jp.co.hottolink.splogfilter.learning.classifier.LearnerSpamClassifier" }
	};

	/**
	 * <p>
	 * 判別種別のマスターテーブルを作成する.
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

			// 判別器の取得
			String classifiedType = (String)record.get("name");
			List<ClassifierEntity> classifiers = getClassifiers(classifiedType);

			ClassifiedTypeEntity entity = new ClassifiedTypeEntity();
			entity.setName(classifiedType);
			entity.setLabel((String)record.get("label"));
			entity.setClassName((String)record.get("classifier"));
			entity.setClassifiers(classifiers);

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
	private static List<ClassifierEntity> getClassifiers(String classifiedType) {

		List<ClassifierEntity> list = new ArrayList<ClassifierEntity>();

		List<ClassifierEntity> classifiers = MasterDataCache.getClassifierList();
		for (ClassifierEntity classifier: classifiers) {
			if (classifier.getClassifiedType().equals(classifiedType)) {
				list.add(classifier);
			}
		}

		return list;
	}
}
