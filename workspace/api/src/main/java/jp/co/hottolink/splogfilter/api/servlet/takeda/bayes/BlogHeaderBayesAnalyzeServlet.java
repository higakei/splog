package jp.co.hottolink.splogfilter.api.servlet.takeda.bayes;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import jp.co.hottolink.splogfilter.api.constants.SplogAPIConstants;
import jp.co.hottolink.splogfilter.api.servlet.abst.AbstractAnalyzeServlet;
import jp.co.hottolink.splogfilter.api.servlet.fileupload.MultipartRequestParser;
import jp.co.hottolink.splogfilter.common.exception.ParseException;
import jp.co.hottolink.splogfilter.takeda.bayes.analyzer.BayesFilterAnalyzer;
import jp.co.hottolink.splogfilter.takeda.bayes.blogheader.BlogHeaderAnalyzer;
import jp.co.hottolink.splogfilter.takeda.bayes.blogheader.BlogHeaderResultOutputter;
import jp.co.hottolink.splogfilter.takeda.bayes.entity.BayesFilterAnalysisEntity;
import jp.co.hottolink.splogfilter.takeda.bayes.outputter.BayesFilterResultOutputter;

/**
 * <p>
 * ブログヘッダーをベイズフィルターで分析するサーブレット.
 * </p>
 * @author higa
 */
public class BlogHeaderBayesAnalyzeServlet extends AbstractAnalyzeServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 4516329513534247290L;

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(BlogHeaderBayesAnalyzeServlet.class);

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
		outputErrorXML(response, "form method is not post");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletrequestuest, javax.servlet.http.HttpServletresponseonse)
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

			// パラメータの取得
			FileItem blogHeader = parser.getFileItem("blogHeader");
			String detail = parser.getParameter("detail");
			if ((blogHeader == null) || blogHeader.isFormField()) {
				outputErrorXML(response, "file is not uploaded");
				return;
			}

			// コンテンツタイプのチェック
			String contentType = blogHeader.getContentType();
			if (!SplogAPIConstants.CONTENT_TYPE_XML.equals(contentType)) {
				outputErrorXML(response, "uploaded file is not xml");
				return;
			}

			// 詳細表示の設定
			boolean showDetail = false;
			if ("show".equals(detail)) {
				showDetail = true;
			}

			// 分析データのロード
			BayesFilterAnalyzer analyzer = new BlogHeaderAnalyzer();
			analyzer.setValidateOnLoad(true);
			analyzer.loadData(blogHeader.getInputStream());
			if (analyzer.getDataSize() == 0) {
				outputErrorXML(response, "no data");
				return;
			}

			// 分析
			List<BayesFilterAnalysisEntity> results = analyzer.analyze();

			// 分析結果の出力
			BayesFilterResultOutputter outputter = new BlogHeaderResultOutputter(results);
			outputter.setShowDetail(showDetail);
			outputResultXML(response, outputter);

		} catch (ParseException e) {
			outputErrorXML(response, e.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			outputErrorXML(response, "analyze fialed");
		}
	}
}
