package jp.co.hottolink.splogfilter.learning.master;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;

/**
 * <p>
 * 判別器マスタークラス.
 * </p>
 * @author higa
 */
public class ClassifierMaster {

	/**
	 * <p>
	 * 判別器のマスターデータ.
	 * </p>
	 */
	private static final Object[][] DATA = {
		{ "name", "label", "classified_type", "class" }
		, { "content", "コンテンツフィルタ", "spam", "jp.co.hottolink.splogfilter.learning.classifier.ContentSpamClassifier" }
		, { "waisetsu", "わいせつ語フィルタ", "spam", "jp.co.hottolink.splogfilter.learning.classifier.WaisetsuSpamClassifier" }
		, { "speech_balance", "品詞バランスフィルタ", "spam", "jp.co.hottolink.splogfilter.learning.classifier.SpeechBalanceSpamClassifier" }
		, { "naive_bayes", "NaiveBayesフィルタ", "spam", "jp.co.hottolink.splogfilter.learning.classifier.NaiveBayesSpamClassifier" }
	};

	/**
	 * <p>
	 * 判別器のマスターテーブルを作成する.
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

			ClassifierEntity entity = new ClassifierEntity();
			entity.setName((String)record.get("name"));
			entity.setLabel((String)record.get("label"));
			entity.setClassName((String)record.get("class"));
			entity.setClassifiedType((String)record.get("classified_type"));
			

			String key = (String)record.get(columnName[0]);
			map.put(key, entity);
		}

		return map;
	}
}
