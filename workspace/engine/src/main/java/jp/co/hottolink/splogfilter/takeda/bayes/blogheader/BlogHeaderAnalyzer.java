package jp.co.hottolink.splogfilter.takeda.bayes.blogheader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.takeda.bayes.analyzer.BayesFilterAnalyzer;
import jp.co.hottolink.splogfilter.takeda.bayes.analyzer.SentenceAnalyzer;
import jp.co.hottolink.splogfilter.takeda.bayes.entity.BayesFilterAnalysisEntity;
import jp.co.hottolink.splogfilter.takeda.bayes.entity.SentenceResultEntity;

/**
 * <p>
 * ブログヘッダーの分析クラス.
 * </p>
 * @author higa
 */
public class BlogHeaderAnalyzer implements BayesFilterAnalyzer {

	/**
	 * <p>
	 * 分析データ.
	 * </p>
	 */
	private List<BlogHeaderEntity> data = null;

	/**
	 * <p>
	 * ロードバリデートフラグ.
	 * </p>
	 */
	private boolean isValidateOnLoad = false;

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.analyzer.impl.BayesFilterAnalyzerImpl#loadData(java.io.InputStream)
	 */
	public void loadData(InputStream stream) throws Exception {
		BlogHeaderParser parser = new BlogHeaderParser();
		parser.setValidate(isValidateOnLoad());
		parser.parse(stream);
		data = parser.getBlogHeaders();
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.analyzer.BayesFilterAnalyzer#analyze()
	 */
	public List<BayesFilterAnalysisEntity> analyze() throws Exception {

		List<BayesFilterAnalysisEntity> list = new ArrayList<BayesFilterAnalysisEntity>();
		if (data == null) {
			return list;
		}

		for (BlogHeaderEntity element: data) {
			// 分析
			String header = element.getHeader();
			SentenceAnalyzer analyzer = new SentenceAnalyzer();
			SentenceResultEntity result = analyzer.analyze(header);
			// 分析結果の生成
			BayesFilterAnalysisEntity entity = new BlogHeaderAnalysisEntity();
			entity.setData(element);
			entity.setResult(result);
			// リストに追加
			list.add(entity);
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.analyzer.impl.BayesFilterAnalyzer#getData()
	 */
	public Object getData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.analyzer.BayesFilterAnalyzer#getDataSize()
	 */
	public int getDataSize() {
		if (data == null) {
			return 0;
		} else {
			return data.size();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.analyzer.BayesFilterAnalyzer#isValidateOnLoad()
	 */
	public boolean isValidateOnLoad() {
		return isValidateOnLoad;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.analyzer.BayesFilterAnalyzer#setValidateOnLoad(boolean)
	 */
	public void setValidateOnLoad(boolean isValidateOnLoad) {
		this.isValidateOnLoad = isValidateOnLoad;
	}
}
