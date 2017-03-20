package jp.co.hottolink.splogfilter.api.servlet.takeda;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.hottolink.splogfilter.api.analyzer.AuthorAnalyzer;
import jp.co.hottolink.splogfilter.api.constants.SplogAPIConstants;
import jp.co.hottolink.splogfilter.api.servlet.abst.AbstractAnalyzeServlet;
import jp.co.hottolink.splogfilter.api.servlet.fileupload.MultipartRequestParser;
import jp.co.hottolink.splogfilter.api.xml.parser.BlogFeedParser;
import jp.co.hottolink.splogfilter.common.exception.ParseException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/**
 * <p>
 * XMLファイルから投稿者の分析を行うサーブレット.
 * </p>
 * @author higa
 */
public class AuthorMultipartAnalyzeServlet extends AbstractAnalyzeServlet {

	/**
	 * <p>
	 * シリアルバージョンID.
	 * </p>
	 */
	private static final long serialVersionUID = -2314931418141622581L;

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(AuthorMultipartAnalyzeServlet.class);

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
		outputErrorXML(response, "form method is not post");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// コンテントタイプの取得
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) {
				outputErrorXML(response, "form enctype is not multipart");
				return;
			}

			// リクエストの解析
			MultipartRequestParser parser = new MultipartRequestParser();
			parser.parse(request);

			// アップロードファイルの取得
			FileItem file = parser.getFileItem("blogFeed");
			if ((file == null) || file.isFormField()) {
				outputErrorXML(response, "file is not uploaded");
				return;
			}

			// コンテンツタイプの取得
			String contentType = file.getContentType();
			if (!SplogAPIConstants.CONTENT_TYPE_XML.equals(contentType)) {
				outputErrorXML(response, "uploaded file is not xml");
				return;
			}

			// XMLファイルの構文解析
			BlogFeedParser feedParser = new BlogFeedParser();
			feedParser.setValidateForAuthor(true);
			feedParser.parse(file.getInputStream());

			// ブログフィードの取得
			List<Map<String, String>> documents = feedParser.getDocuments();
			if ((documents == null) || documents.isEmpty()) {
				outputErrorXML(response, "no data");
				return;
			}

			// 分析
			AuthorAnalyzer analyzer = new AuthorAnalyzer(documents);
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
