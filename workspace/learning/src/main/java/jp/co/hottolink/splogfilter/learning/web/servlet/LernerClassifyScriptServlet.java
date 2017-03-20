package jp.co.hottolink.splogfilter.learning.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 学習機械判定のJavaScriptを実行するサーブレット.
 * </p>
 * @author higa
 */
public class LernerClassifyScriptServlet extends HttpServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -3305638715204185438L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// キャッシュを無効にする
		response.setHeader("Pragma","no-cache");
		response.setHeader("Cache-Control","no-cache");
		response.setDateHeader("Expires", 0);

		// セッションの作成
		request.getSession();

		// レスポンスの設定
		response.setContentType("text/plain");
		response.setHeader("charset", "UTF-8");
		response.setCharacterEncoding("UTF-8");

		// script出力
		PrintWriter writer = response.getWriter();
		writer.println("setProgress();");
		writer.println("setTimeout(showWaiting, 1000);");
		writer.println("document.classifyForm.submit();");
		writer.flush();
		writer.close();
	}
}
