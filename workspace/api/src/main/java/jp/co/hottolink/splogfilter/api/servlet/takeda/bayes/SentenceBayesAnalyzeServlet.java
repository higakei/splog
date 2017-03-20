package jp.co.hottolink.splogfilter.api.servlet.takeda.bayes;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import jp.co.hottolink.splogfilter.api.servlet.abst.AbstractAnalyzeServlet;
import jp.co.hottolink.splogfilter.takeda.bayes.analyzer.SentenceAnalyzer;
import jp.co.hottolink.splogfilter.takeda.bayes.entity.SentenceResultEntity;
import jp.co.hottolink.splogfilter.takeda.bayes.outputter.BayesFilterResultOutputter;
import jp.co.hottolink.splogfilter.takeda.bayes.outputter.SentenceResultOutputter;

/**
 * <p>
 * 文章をベイズフィルターで分析するサーブレット.
 * </p>
 * @author higa
 */
public class SentenceBayesAnalyzeServlet extends AbstractAnalyzeServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -2336904149408629195L;

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(SentenceBayesAnalyzeServlet.class);

	/* (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.api.servlet.abst.AbstractAnalyzeServlet#getLogger()
	 */
	@Override
	protected Logger getLogger() {
		return logger;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletresponseonse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// パラメータの取得
			String sentence = request.getParameter("sentence");
			String detail = request.getParameter("detail");
			if ((sentence == null) || sentence.isEmpty()) {
				outputErrorXML(response, "no data");
				return;
			}

			// 詳細表示の設定
			boolean showDetail = false;
			if ("show".equals(detail)) {
				showDetail = true;
			}
			
			// 分析
			SentenceAnalyzer analyzer = new SentenceAnalyzer();
			SentenceResultEntity result = analyzer.analyze(sentence);

			// 結果の出力
			BayesFilterResultOutputter outputter = new SentenceResultOutputter(result);
			outputter.setShowDetail(showDetail);
			outputResultXML(response, outputter);

		} catch (Exception e) {
			logger.error("", e);
			outputErrorXML(response, "analyze fialed");
		}
	}
}
