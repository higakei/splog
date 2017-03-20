package jp.co.hottolink.splogfilter.api.servlet.takeda;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.hottolink.splogfilter.api.analyzer.DocumentAnalyzer;
import jp.co.hottolink.splogfilter.api.servlet.abst.AbstractAnalyzeServlet;
import jp.co.hottolink.splogfilter.api.xml.parser.BlogFeedParser;
import jp.co.hottolink.splogfilter.common.exception.ParseException;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

/**
 * <p>
 * 文書の分析を行うサーブレット.
 * </p>
 * @author higa
 */
public class DocumentAnalyzeServlet extends AbstractAnalyzeServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 2643188163539070961L;

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(DocumentAnalyzeServlet.class);

	/* (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.api.servlet.abst.AbstractAnalyzeServlet#getLogger()
	 */
	@Override
	protected Logger getLogger() {
		return logger;
	}

	/*
	 * (非 Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/*
	 * (非 Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// パラメータの取得
			String blogFeed = request.getParameter("blogFeed");
			if ((blogFeed == null) || blogFeed.isEmpty()) {
				outputErrorXML(response, "no data");
				return;
			}

			// XMLの構文解析
			StringReader reader = new StringReader(blogFeed);
			InputSource source = new InputSource(reader);
			BlogFeedParser parser = new BlogFeedParser();
			parser.setValidate(true);
			parser.parse(source);

			// ブログフィードの取得
			List<Map<String, String>> documents = parser.getDocuments();
			if ((documents == null) || documents.isEmpty()) {
				outputErrorXML(response, "no data");
				return;
			}

			// 分析
			DocumentAnalyzer analyzer = new DocumentAnalyzer(documents);
			analyzer.analyze();

			// 結果の出力
			outputResultXML(response, analyzer);

		} catch (ParseException e) {
			outputErrorXML(response, e.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			outputErrorXML(response, "analyze fialed");
		}
	}
}
