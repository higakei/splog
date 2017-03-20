package jp.co.hottolink.splogfilter.learning.logic.training;

import java.util.HashMap;
import java.util.Map;

import jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl;
import jp.co.hottolink.splogfilter.learning.boosting.AdaBoost;
import jp.co.hottolink.splogfilter.learning.writer.RecordWriterImpl;

/**
 * <p>
 * 学習機械を訓練するモデル.
 * </p>
 * @author higa
 */
public class LearnerTrainer {

	/**
	 * <p>
	 * 訓練情報.
	 * </p>
	 */
	private TrainingEntity training = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param training 訓練情報
	 */
	public LearnerTrainer(TrainingEntity training) {
		this.training = training;
	}

	/**
	 * <p>
	 * 学習機械を訓練する.
	 * </p>
	 * @param loader データローダ
	 * @return 学習機械
	 * @throws Exception
	 */
	public Map<String, Double> train(DataLoaderImpl loader) throws Exception {
		AdaBoost adaBoost = new AdaBoost();
		Map<String, Double> learner = adaBoost.train(loader, training.getClassifiers());
		training.setLearner(learner);
		return learner;
	}

	/**
	 * <p>
	 * 学習機械を出力する.
	 * </p>
	 * @param writer レコードライター
	 * @throws Exception 
	 */
	public void output(RecordWriterImpl writer) throws Exception {

		try {
			// 学習機械の取得
			Map<String, Double> learner = training.getLearner();
			if (learner == null) {
				return;
			}

			// 学習機械の出力
			writer.open();
			for (String key: learner.keySet()) {
				Map<String, Object> record = new HashMap<String, Object>();
				record.put("training_id", training.getId());
				record.put("classifier", key);
				record.put("reliability", learner.get(key));
				writer.println(record);
			}

		} finally {
			if (writer != null) writer.close();
		}
	}
}
