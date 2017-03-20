package jp.co.hottolink.splogfilter.api.servlet.takeda;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;

import jp.co.hottolink.fusion.core.util.net.RssUtil;
import jp.co.hottolink.fusion.core.util.net.feed.FeedChannel;
import jp.co.hottolink.splogfilter.api.analyzer.RSSAuthorAnalyzer;
import jp.co.hottolink.splogfilter.api.analyzer.RSSDocumentAnalyzer;
import jp.co.hottolink.splogfilter.api.analyzer.impl.RSSAnalyzer;
import jp.co.hottolink.splogfilter.api.constants.SplogAPIConstants;
import jp.co.hottolink.splogfilter.api.xml.XMLWriter;

/**
 * <p>
 * RSSのURLから分析を行うサーブレット.
 * </p>
 * @author higa
 *
 */
public class RssUrlAnalyzeServlet extends HttpServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 5191661473048582334L;

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(RssUrlAnalyzeServlet.class);

	/**
	 * <p>
	 * エンコーディング.
	 * </p>
	 */
	private String encoding = SplogAPIConstants.DEFAULT_ENCODING;

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
			String url = request.getParameter("url");
			String type = request.getParameter("type");
			String detail = request.getParameter("detail");

			// URL
			if ((url == null) || url.isEmpty()) {
				outputErrorXML(response, null, "url is required");
				return;
			}

			// 分析タイプ
			RSSAnalyzer analyzer = null;
			if ("1".equals(type)) {
				analyzer = new RSSAuthorAnalyzer();
			} else {
				analyzer = new RSSDocumentAnalyzer();
			}

			// 詳細表示の設定
			boolean showDetail = false;
			if ("show".equals(detail)) {
				showDetail = true;
			}

			// RSSフィードの取得
			FeedChannel channel = RssUtil.getRss(url);

			// RSSフィードのバリデーション
			if (!analyzer.validate(channel)) {
				outputErrorXML(response, null, analyzer.getMessage());
				return;
			}

			// 分析
			analyzer.setShowDetail(showDetail);
			Map<String, Object>	result = analyzer.analyze(channel);

			// 結果出力
			response.setContentType(SplogAPIConstants.CONTENT_TYPE_XML);
			response.setHeader("charset", encoding); 
			ServletOutputStream out = response.getOutputStream();
			XMLWriter.outputXML(out, encoding, result);

		} catch (Exception e) {
			logger.error("分析に失敗しました。", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * <p>
	 * 分析エラー時のXMLを出力する.
	 * </p>
	 * @param response レスポンス
	 * @param code エラーコード
	 * @param message エラーメッセージ
	 * @param encoding エンコーディング
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	private void outputErrorXML(HttpServletResponse response, String code, String message)
			throws IOException, XMLStreamException {

		if ((response == null) || (response.isCommitted())) {
			return;
		}

		// エレメントの作成
		Map<String, Object> attributes = new LinkedHashMap<String, Object>();
		attributes.put("code", code);
		Map<String, Object> element = XMLWriter.createElement("error", attributes, message, null);

		// レスポンスの設定
		response.setContentType(SplogAPIConstants.CONTENT_TYPE_XML);
		response.setHeader("charset", encoding); 

		// XML出力
		ServletOutputStream out = response.getOutputStream();
		XMLWriter.outputXML(out, encoding, element);
	}
}
