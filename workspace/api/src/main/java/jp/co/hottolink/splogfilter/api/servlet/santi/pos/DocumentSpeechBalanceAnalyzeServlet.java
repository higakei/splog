package jp.co.hottolink.splogfilter.api.servlet.santi.pos;

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
import jp.co.hottolink.splogfilter.santi.pos.SpeechBalanceAnalyzer;

/**
 * <p>
 * タイトルと本文を品詞バランスフィルタで分析するサーブレット.
 * </p>
 * @author higa
 */
public class DocumentSpeechBalanceAnalyzeServlet extends AbstractAnalyzeServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -7713839686439839617L;

	/**
	 * <p>
	 * テンプレートパス.
	 * </p>
	 */
	private static final String TEMPLATE_PATH = "speech_balance/document.vm";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(DocumentSpeechBalanceAnalyzeServlet.class);

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
			SpeechBalanceAnalyzer analyzer = new SpeechBalanceAnalyzer();
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
