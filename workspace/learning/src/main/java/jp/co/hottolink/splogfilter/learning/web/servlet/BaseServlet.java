package jp.co.hottolink.splogfilter.learning.web.servlet;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;

import jp.co.hottolink.splogfilter.common.resource.ResourceBundleUtil;
import jp.co.hottolink.splogfilter.common.velocity.tools.EscapeTool;
import jp.co.hottolink.splogfilter.learning.web.servlet.entity.ErrorEntity;

/**
 * <p>
 * 基底サーブレットクラス.
 * </p>
 * @author higa
 */
public abstract class BaseServlet extends HttpServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -5868950084603522145L;

	/**
	 * <p>
	 * DBリソース名.
	 * </p>
	 */
	protected static final String DB_RESOURCE = "splog_learning_db";

	/**
	 * <p>
	 * デフォルトエンコーディング.
	 * </p>
	 */
	protected static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * <p>
	 * XMLのコンテントタイプ.
	 * </p>
	 */
	protected static final String CONTENT_TYPE_XML = "text/xml";

	/**
	 * <p>
	 * エラー画面.
	 * </p>
	 */
	private static final String JSP_ERROR = "/WEB-INF/jsp/error.jsp";

	/**
	 * <p>
	 * メッセージリソース名.
	 * </p>
	 */
	private static final String MESSAGE_RESOURCE = "message";

	/**
	 * <p>
	 * エンコーディング.
	 * </p>
	 */
	protected String encoding = DEFAULT_ENCODING;

	/**
	 * <p>
	 * VelocityTools.
	 * </p>
	 */
	protected static VelocityContext tools = null;

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
	 * エラー画面に遷移する.
	 * </p>
	 * @param request リクエスト
	 * @param response レスポンス
	 * @param message エラーメッセージ
	 * @param cause エラー原因
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void error(HttpServletRequest request,
			HttpServletResponse response, String message, String cause)
			throws ServletException, IOException {
		// エラー情報を生成
		ErrorEntity error = new ErrorEntity(message, cause);
		// リクエストにエラー情報を設定
		request.setAttribute("error", error);
		// エラー画面に遷移
		request.getRequestDispatcher(JSP_ERROR).forward(request, response);
	}

	/**
	 * <p>
	 * エラー画面に遷移する.
	 * </p>
	 * @param request リクエスト
	 * @param response レスポンス
	 * @param message エラーメッセージ
	 * @param cause エラー原因
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void error(HttpServletRequest request,
			HttpServletResponse response, String message, Throwable cause)
			throws ServletException, IOException {
		// エラー情報を生成
		ErrorEntity error = new ErrorEntity(message, cause);
		// リクエストにエラー情報を設定
		request.setAttribute("error", error);
		// エラー画面に遷移
		request.getRequestDispatcher(JSP_ERROR).forward(request, response);
	}

	/**
	 * <p>
	 * メッセージリソースからメッセージを取得する.
	 * </p>
	 * @param key メッセージキー
	 * @return メッセージ
	 */
	protected String getMessage(String key) {
		return ResourceBundleUtil.getMessage(MESSAGE_RESOURCE, key);
	}

	/**
	 * <p>
	 * メッセージリソースからフォーマットメッセージを取得する.
	 * </p>
	 * @param key メッセージキー
	 * @param argument フォーマット要素
	 * @return メッセージ
	 */
	protected String getMessage(String key, Object argument) {
		String pattern = ResourceBundleUtil.getMessage(MESSAGE_RESOURCE, key);
		Object[] arguments = new Object[1];
		arguments[0] = argument;
		return MessageFormat.format(pattern, arguments);
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
		VelocityContext context = new VelocityContext(tools);
		return context;
	}
}
