package jp.co.hottolink.splogfilter.api.servlet.santi.bayes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import jp.co.hottolink.splogfilter.api.constants.SplogAPIConstants;
import jp.co.hottolink.splogfilter.api.servlet.abst.AbstractAnalyzeServlet;
import jp.co.hottolink.splogfilter.santi.bayes.NaiveBayesAnalyzer;

/**
 * <p>
 * タイトルと本文をNaiveBayesフィルタで分析するサーブレット.
 * </p>
 * @author higa
 */
public class DocumentNaiveBayesAnalyzeServlet extends AbstractAnalyzeServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -2986516557687763426L;

	/**
	 * <p>
	 * テンプレートパス.
	 * </p>
	 */
	private static final String TEMPLATE_PATH = "santi_bayes/document.vm";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(DocumentNaiveBayesAnalyzeServlet.class);

	/*
	 * (non-Javadoc)
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
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// パラメータの取得
			String title = request.getParameter("title");
			String body = request.getParameter("body");

			// 分析
			NaiveBayesAnalyzer analyzer = new NaiveBayesAnalyzer();
			Map<String, Object> result = analyzer.analyze(title, body);
			if (result.containsKey("cause")) {
				outputErrorXML(response, (String)result.get("cause"));
				return;
			}

			// Velocityコンテキストに値を設定
			VelocityContext context = createVelocityContext(result);

			// XML出力
			response.setContentType(SplogAPIConstants.CONTENT_TYPE_XML);
			response.setHeader("charset", encoding);
			response.setCharacterEncoding(encoding);
			PrintWriter writer = response.getWriter();
			Velocity.mergeTemplate(TEMPLATE_PATH, encoding, context, writer);

		} catch (Exception e) {
			logger.error("", e);
			outputErrorXML(response, "analyze fialed");
		}
	}
}
