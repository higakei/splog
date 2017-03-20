package jp.co.hottolink.splogfilter.takeda.bayes.blogheader;

import java.io.Serializable;

import jp.co.hottolink.splogfilter.takeda.bayes.entity.BayesFilterAnalysisEntity;
import jp.co.hottolink.splogfilter.takeda.bayes.entity.SentenceResultEntity;

/**
 * <p>
 * ブログヘッダーの分析情報のEntityクラス.
 * </p>
 * @author higa
 */
public class BlogHeaderAnalysisEntity implements BayesFilterAnalysisEntity, Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -7791608961852251553L;

	/**
	 * <p>
	 * 分析データ.
	 * </p>
	 */
	private BlogHeaderEntity data = null;

	/**
	 * <p>
	 * 分析結果.
	 * </p>
	 */
	private SentenceResultEntity result = null;

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.entity.BayesFilterAnalysisEntity#getData()
	 */
	public Object getData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.entity.AbstractAnalysisEntity#setData(java.lang.Object)
	 */
	public void setData(Object data) {
		this.data = (BlogHeaderEntity)data;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.entity.BayesFilterAnalysisEntity#setResult(java.lang.Object)
	 */
	public void setResult(Object result) {
		this.result = (SentenceResultEntity)result;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.entity.BayesFilterAnalysisEntity#getRsult()
	 */
	public Object getRsult() {
		return result;
	}
}
