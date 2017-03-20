package jp.co.hottolink.splogfilter.api.servlet.santi.waisetsu;

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
import jp.co.hottolink.splogfilter.santi.waisetsu.WaisetsuAnalyzer;

/**
 * <p>
 * タイトルと本文をわいせつ語フィルタで分析するサーブレット.
 * </p>
 * @author higa
 */
public class DocumentWaisetsuAnalyzeServlet extends AbstractAnalyzeServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 7168628899103420070L;

	/**
	 * <p>
	 * テンプレートパス.
	 * </p>
	 */
	private static final String TEMPLATE_PATH = "waisetsu/document.vm";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(DocumentWaisetsuAnalyzeServlet.class);

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
			String detail = request.getParameter("detail");

			// 詳細表示の設定
			boolean showDetail = false;
			if ("show".equals(detail)) {
				showDetail = true;
			}

			// 分析
			WaisetsuAnalyzer analyzer = new WaisetsuAnalyzer();
			Map<String, Object> result = analyzer.analyze(title, body);
			if (result.containsKey("cause")) {
				outputErrorXML(response, (String)result.get("cause"));
				return;
			}

			// Velocityコンテキストに値を設定
			VelocityContext context = createVelocityContext(result);
			context.put("showDetail", showDetail);

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
