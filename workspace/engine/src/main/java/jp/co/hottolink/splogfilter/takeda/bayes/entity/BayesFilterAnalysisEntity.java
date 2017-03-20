package jp.co.hottolink.splogfilter.takeda.bayes.entity;


/**
 * <p>
 * ベイズフィルターの分析情報のインターフェイス.
 * </p>
 * @author higa
 */
public interface BayesFilterAnalysisEntity {

	/**
	 * <p>
	 * 分析データを取得する.
	 * </p>
	 * @return 分析データ
	 */
	public Object getData();

	/**
	 * <p>
	 * 分析データを設定する.
	 * </p>
	 * @param data 分析データ
	 */
	public void setData(Object data);

	/**
	 * <p>
	 * 分析結果を取得する.
	 * </p>
	 * @return 分析結果
	 */
	public Object getRsult();

	/**
	 * <p>
	 * 分析結果を設定する.
	 * </p>
	 * @param result 分析結果
	 */
	public void setResult(Object result);
}
