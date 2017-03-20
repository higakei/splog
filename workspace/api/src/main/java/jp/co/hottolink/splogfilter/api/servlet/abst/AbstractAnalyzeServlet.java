package jp.co.hottolink.splogfilter.api.servlet.abst;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.EscapeTool;

import jp.co.hottolink.splogfilter.api.analyzer.AuthorAnalyzer;
import jp.co.hottolink.splogfilter.api.analyzer.DocumentAnalyzer;
import jp.co.hottolink.splogfilter.api.constants.SplogAPIConstants;
import jp.co.hottolink.splogfilter.takeda.bayes.outputter.BayesFilterResultOutputter;

/**
 * <p>
 * 分析サーブレットの抽象クラス.
 * </p>
 * @author higa
 */
public abstract class AbstractAnalyzeServlet extends HttpServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 2049172143414492765L;

	/**
	 * <p>
	 * エラーテンプレート.
	 * </p>
	 */
	private static final String ERROR_TEMPLATE = "error.vm";

	/**
	 * <p>
	 * エンコーディング.
	 * </p>
	 */
	protected String encoding = SplogAPIConstants.DEFAULT_ENCODING;

	/**
	 * <p>
	 * VelocityTools.
	 * </p>
	 */
	protected static VelocityContext tools = null;

	/**
	 * <p>
	 * ロガーを取得する.
	 * </p>
	 * @return ロガー
	 */
	protected abstract Logger getLogger();

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		tools = new VelocityContext();
		tools.put("escape", new EscapeTool());
	}

	/**
	 * <p>
	 * 分析エラー時のXMLを出力する.
	 * </p>
	 * @param response レスポンス
	 * @param message エラーメッセージ
	 * @param encoding エンコーディング
	 * @throws IOException
	 */
	protected void outputErrorXML(HttpServletResponse response, String message) throws IOException {

		try {
			if ((response == null) || (response.isCommitted())) {
				return;
			}

			// レスポンスの設定
			response.setContentType(SplogAPIConstants.CONTENT_TYPE_XML);
			response.setHeader("charset", encoding);
			response.setCharacterEncoding(encoding);

			// Velocityコンテキストに値を設定
			VelocityContext context = createVelocityContext();
			context.put("message", message);

			// XML出力
			PrintWriter writer = response.getWriter();
			Velocity.mergeTemplate(ERROR_TEMPLATE, encoding, context, writer);

		} catch (Exception e) {
			getLogger().error("XML出力に失敗しました。", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * <p>
	 * 分析結果をXML出力する.
	 * </p>
	 * @param response レスポンス
	 * @param analyzer 分析クラス
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	protected void outputResultXML(HttpServletResponse response, AuthorAnalyzer analyzer)
			throws IOException, XMLStreamException {
		response.setContentType(SplogAPIConstants.CONTENT_TYPE_XML);
		response.setHeader("charset", encoding);
		ServletOutputStream out = response.getOutputStream();
		analyzer.outputXML(out, encoding);
	}

	/**
	 * <p>
	 * 分析結果をXML出力する.
	 * </p>
	 * @param response レスポンス
	 * @param analyzer 分析クラス
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	protected void outputResultXML(HttpServletResponse response, DocumentAnalyzer analyzer)
			throws IOException, XMLStreamException {
		response.setContentType(SplogAPIConstants.CONTENT_TYPE_XML);
		response.setHeader("charset", encoding);
		ServletOutputStream out = response.getOutputStream();
		analyzer.outputXML(out, encoding);
	}

	/**
	 * <p>
	 * 分析結果をXML出力する.
	 * </p>
	 * @param response レスポンス
	 * @param outputter 分析結果出力クラス
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	protected void outputResultXML(HttpServletResponse response, BayesFilterResultOutputter outputter)
			throws IOException, XMLStreamException {
		response.setContentType(SplogAPIConstants.CONTENT_TYPE_XML);
		response.setHeader("charset", encoding);
		ServletOutputStream out = response.getOutputStream();
		outputter.outputXML(out, encoding);
	}

	/**
	 * <p>
	 * Velocityコンテキストを作成する.
	 * </p><pre>
	 * GenericToolsをラッピングする
	 * </pre>
	 * @return Velocityコンテキスト
	 */
	protected VelocityContext createVelocityContext() {
		VelocityContext context = new VelocityContext(AbstractAnalyzeServlet.tools);
		return context;
	}

	/**
	 * <p>
	 * Velocityコンテキストを作成する.
	 * </p><pre>
	 * ストレージとGenericToolsをラッピングする
	 * </pre>
	 * @param storage ストレージ
	 * @return Velocityコンテキスト
	 */
	@SuppressWarnings("unchecked")
	protected VelocityContext createVelocityContext(Map storage) {
		VelocityContext context = new VelocityContext(storage, AbstractAnalyzeServlet.tools);
		return context;
	}
}
