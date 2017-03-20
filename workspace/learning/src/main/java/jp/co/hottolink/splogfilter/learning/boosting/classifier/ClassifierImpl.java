package jp.co.hottolink.splogfilter.learning.boosting.classifier;

import java.util.Map;

import jp.co.hottolink.splogfilter.learning.boosting.entity.AnswerEntity;

/**
 * <p>
 * 判別器のインターフェイス.
 * </p>
 * @author higa
 */
public interface ClassifierImpl {

	/**
	 * <p>
	 * データを判別する.
	 * </p>
	 * @param data データ
	 * @param type データ種別
	 * @return 判別結果
	 * @throws Exception
	 */
	public AnswerEntity classify(Map<String, Object> data, String type) throws Exception;

	/**
	 * <p>
	 * 解答のラベルを取得する.
	 * </p>
	 * @param answer 解答
	 * @return ラベル
	 */
	public int signum(String answer);
}
