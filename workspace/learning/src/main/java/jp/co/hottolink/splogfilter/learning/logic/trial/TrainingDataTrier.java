package jp.co.hottolink.splogfilter.learning.logic.trial;

import java.util.HashMap;
import java.util.Map;

import jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl;
import jp.co.hottolink.splogfilter.learning.boosting.classifier.ClassifierImpl;
import jp.co.hottolink.splogfilter.learning.boosting.entity.AnswerEntity;
import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;
import jp.co.hottolink.splogfilter.learning.master.entity.DataTypeEntity;
import jp.co.hottolink.splogfilter.learning.writer.TrialResultDBWriter;

/**
 * <p>
 * 学習データのトライアルを行うクラス.
 * </p>
 * @author higa
 */
public class TrainingDataTrier {

	/**
	 * <p>
	 * トライアル情報.
	 * </p>
	 */
	private TrialEntity trial = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param trial トライアル情報
	 */
	public TrainingDataTrier(TrialEntity trial) {
		this.trial = trial;
	}

	/**
	 * <p>
	 * 学習データのトライアルを行う.
	 * </p>
	 * @param loader データローダ
	 * @param writer レコードライター
	 * @throws Exception
	 */
	public void doTrial(DataLoaderImpl loader, TrialResultDBWriter writer) throws Exception {

		try {
			// ライターのオープン
			writer.open();
			// データローダのオープン
			loader.open();

			// データがなくなるまで訓練
			for (int i = 0;;i++) {
				// データの取得
				Map<String, Object> data = loader.fetch();
				if (data == null) {
					break;
				}

				// データ項目名を判別項目名に置換
				Map<String, String> itemNames = trial.getItemNames();
				for (String itemName: itemNames.keySet()) {
					String dataName = itemNames.get(itemName);
					Object value = data.remove(dataName);
					data.put(itemName, value);
				}

				// データ種別の取得
				DataTypeEntity dataType = trial.getDataType();
				String type = dataType.getName();

				// トライアル
				for (ClassifierEntity entity: trial.getClassifiers()) {
					// 判別
					ClassifierImpl classifier = entity.getClassifier();
					AnswerEntity answer = classifier.classify(data, type);

					// データIDの取得
					Integer dataId = (Integer)data.get("id");
					if (dataId == null) dataId = (i + 1);

					// 結果の作成
					Map<String, Object> record = new HashMap<String, Object>();
					record.put("classifier", entity.getName());
					record.put("data_id", dataId);
					record.put("identification", data.get("identification"));
					record.put("correct", data.get("correct"));
					record.put("answer", answer.toString());
					record.put("matching", String.valueOf(answer.isMatched()));

					// 結果を書き込む
					writer.println(record);
				}
			}

		} finally {
			if (writer != null) writer.close();
			if (loader != null) try { loader.close(); } catch (Exception e) {}
		}
	}

	/**
	 * <p>
	 * 学習データのトライアルを行う.
	 * </p>
	 * @param writer レコードライター
	 * @param data 学習データ
	 * @throws Exception
	 */
	public void doTraial(TrialResultDBWriter writer, Map<String, Object> data) throws Exception {

		// データ項目名を判別項目名に置換
		Map<String, String> itemNames = trial.getItemNames();
		for (String itemName: itemNames.keySet()) {
			String dataName = itemNames.get(itemName);
			Object value = data.remove(dataName);
			data.put(itemName, value);
		}

		// データ種別の取得
		DataTypeEntity dataType = trial.getDataType();
		String type = dataType.getName();

		// トライアル
		for (ClassifierEntity entity: trial.getClassifiers()) {
			// 判別
			ClassifierImpl classifier = entity.getClassifier();
			AnswerEntity answer = classifier.classify(data, type);

			// 結果の作成
			Map<String, Object> record = new HashMap<String, Object>();
			record.put("classifier", entity.getName());
			record.put("data_id", data.get("data_id"));
			record.put("identification", data.get("identification"));
			record.put("correct", data.get("correct"));
			record.put("answer", answer.toString());
			record.put("matching", String.valueOf(answer.isMatched()));

			// 結果を書き込む
			writer.println(record);
		}
	}
}
