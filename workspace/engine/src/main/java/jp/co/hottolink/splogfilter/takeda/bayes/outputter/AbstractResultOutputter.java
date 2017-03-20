package jp.co.hottolink.splogfilter.takeda.bayes.outputter;

import java.util.List;

import jp.co.hottolink.splogfilter.takeda.bayes.entity.BayesFilterAnalysisEntity;

/**
 * <p>
 * ベイズフィルターの分析結果出力の抽象クラス.
 * </p>
 * @author higa
 */
public abstract class AbstractResultOutputter implements BayesFilterResultOutputter {

	/**
	 * <p>
	 * 分析情報.
	 * </p>
	 */
	protected List<BayesFilterAnalysisEntity> analyses = null;

	/**
	 * <p>
	 * 詳細表示フラグ.
	 * </p>
	 */
	protected boolean isShowDetail = false;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param analyses 分析情報
	 */
	public AbstractResultOutputter(List<BayesFilterAnalysisEntity> analyses) {
		this.analyses = analyses;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.outputter.impl.BayesFilterResultOutputter#isShowDetail()
	 */
	public boolean isShowDetail() {
		return isShowDetail;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.outputter.impl.BayesFilterResultOutputter#setShowDetail(boolean)
	 */
	public void setShowDetail(boolean isShowDetail) {
		this.isShowDetail = isShowDetail;
	}
}
