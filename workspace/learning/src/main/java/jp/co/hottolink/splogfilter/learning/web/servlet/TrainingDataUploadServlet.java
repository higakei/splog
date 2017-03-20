package jp.co.hottolink.splogfilter.learning.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.loader.CSVLoader;
import jp.co.hottolink.splogfilter.learning.web.db.JDBCFactory;
import jp.co.hottolink.splogfilter.learning.web.servlet.fileupload.MultipartRequestParser;
import jp.co.hottolink.splogfilter.learning.writer.TrainingDataDBRecordWriter;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/**
 * <p>
 * 学習データをアップロードするサーブレット.
 * </p>
 * @author higa
 */
public class TrainingDataUploadServlet extends BaseServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 6221360596798148995L;

	/**
	 * <p>
	 * 遷移先.
	 * </p>
	 */
	private static final String FORWARD = "/upload_completed.html";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private Logger logger = Logger.getLogger(TrainingDataUploadServlet.class);

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// コンテントタイプのチェック
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String message = getMessage("E0001", "アップロード");
				String cause = getMessage("C0001");
				error(request, response, message, cause);
				return;
			}

			// リクエストの解析
			MultipartRequestParser parser = new MultipartRequestParser();
			parser.parse(request);

			// パラメータの取得
			String header = parser.getParameter("header");

			// アップロードファイルの取得
			FileItem item = parser.getFileItem("data");
			if (item == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String message = getMessage("E0001", "アップロード");
				String cause = getMessage("C0002", "data");
				error(request, response, message, cause);
				return;
			}

			// type属性のチェック
			if (item.isFormField()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String message = getMessage("E0001", "アップロード");
				String cause = getMessage("C0003", "data");
				error(request, response, message, cause);
				return;
			}

			// バリデーション
			String fileName = item.getName();
			if ((fileName == null) || fileName.isEmpty()) {
				String message = getMessage("E0002");
				String cause = getMessage("C0004", "学習データ");
				error(request, response, message, cause);
				return;
			}

			// ヘッダー行の有無の設定
			boolean hasHeader = false;
			if ((header != null) && "included".equals(header)) {
				hasHeader = true;
			}
			hasHeader = true;

			// ローダーの生成
			CSVLoader loader = new CSVLoader(item.getInputStream(), encoding, hasHeader);
			// DBにアップロード
			upload(loader);

			// 完了画面にリダイレクト
			response.sendRedirect(request.getContextPath() + FORWARD);

		} catch (Throwable t) {
			logger.error("", t);
			String message = getMessage("E0001", "アップロード");
			error(request, response, message, t);
		}
	}

	/**
	 * <p>
	 * アップロードを行う.
	 * </p>
	 * @param loader データローダ
	 * @return テーブル名
	 * @throws SQLException 
	 */
	private String upload(CSVLoader loader) throws Exception {

		SQLExecutor executor = null;

		try {
			// ファイルオープン
			loader.open();

			// DBに接続
			executor = JDBCFactory.getExecutor();
			//executor.setAutoCommit(false);

			// DBに登録
			TrainingDataDBRecordWriter writer = new TrainingDataDBRecordWriter(executor);
			for (;;) {
				// データの取得
				Map<String, Object> data = loader.fetch();
				if (data == null) {
					break;
				}

				// DBに書き込み
				writer.println(data);
			}

			// コミット
			//executor.commit();

			return writer.getTable();

		} catch (Exception e) {
			//if (executor != null) try { executor.rollback(); } catch (SQLException re) {}
			throw e;
		} finally {
			if (executor != null) executor.finalize();
			if (loader != null) try { loader.close(); } catch (Exception e) {}
		}
	}
}
