package jp.co.hottolink.splogfilter.takeda.bayes.analyzer;

import java.io.InputStream;
import java.util.List;

import jp.co.hottolink.splogfilter.takeda.bayes.entity.BayesFilterAnalysisEntity;

/**
 * <p>
 * ベイズフィルターの分析インターフェイス.
 * </p>
 * @author higa
 */
public interface BayesFilterAnalyzer {

	/**
	 * <p>
	 * 分析データをロードする.
	 * </p>
	 * @param stream 入力ストリーム
	 * @throws Exception
	 */
	public void loadData(InputStream stream) throws Exception;

	/**
	 * <p>
	 * 分析を行う.
	 * </p>
	 * @return 分析結果
	 * @throws Exception
	 */
	public List<BayesFilterAnalysisEntity> analyze() throws Exception;

	/**
	 * <p>
	 * 分析データを取得する.
	 * </p>
	 * @return 分析データ
	 */
	public Object getData();

	/**
	 * <p>
	 * 分析データ数を取得する.
	 * </p>
	 * @return 分析データ数
	 */
	public int getDataSize();

	/**
	 * <p>
	 * ロードバリデートフラグを取得する.
	 * </p>
	 * @return ロードバリデートフラグ
	 */
	public boolean isValidateOnLoad();

	/**
	 * <p>
	 * ロードバリデートフラグを設定する.
	 * </p>
	 * @param isValidateOnLoad ロードバリデートフラグ
	 */
	public void setValidateOnLoad(boolean isValidateOnLoad);
}
