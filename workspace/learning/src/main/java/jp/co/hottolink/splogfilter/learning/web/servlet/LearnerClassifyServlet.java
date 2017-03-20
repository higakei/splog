package jp.co.hottolink.splogfilter.learning.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.loader.CSVLoader;
import jp.co.hottolink.splogfilter.common.loader.DBLoader;
import jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl;
import jp.co.hottolink.splogfilter.learning.logic.classify.DataLearnerClassifyResultEntity;
import jp.co.hottolink.splogfilter.learning.logic.classify.LearnerClassifyRequestParser;
import jp.co.hottolink.splogfilter.learning.logic.classify.LearnerClassifyResultEnity;
import jp.co.hottolink.splogfilter.learning.logic.classify.LearnerClassifier;
import jp.co.hottolink.splogfilter.learning.logic.classify.LearnerClassifierParameter;
import jp.co.hottolink.splogfilter.learning.web.db.JDBCFactory;
import jp.co.hottolink.splogfilter.learning.web.servlet.fileupload.MultipartRequestParser;
import jp.co.hottolink.splogfilter.learning.web.util.CookieUtil;

/**
 * <p>
 * 学習機械で判定するサーブレット.
 * </p>
 * @author higa
 */
public class LearnerClassifyServlet extends BaseServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -6630183810298932623L;

	/**
	 * <p>
	 * テンプレートパス.
	 * </p>
	 */
	private static final String TEMPLATE_PATH = "classify.vm";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private Logger logger = Logger.getLogger(LearnerTrainingServlet.class);

	/**
	 * <p>
	 * セッション.
	 * </p>
	 */
	private HttpSession session = null;

	/**
	 * <p>
	 * 進捗ID.
	 * </p>
	 */
	private Cookie progressId = null;

	/* (非 Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	/* (非 Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		SQLExecutor executor = null;
		DataLoaderImpl appLoader = null;	// アプリケーションのデータローダ
		DataLoaderImpl inLoader = null;		// 入力ファイルのデータローダ

		try {
			// コンテントタイプのチェック
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String message = getMessage("E0001", "学習機械判定");
				String cause = getMessage("C0001");
				error(request, response, message, cause);
				return;
			}

			// リクエストの解析
			MultipartRequestParser parser = new MultipartRequestParser();
			parser.parse(request);

			// アップロードファイルの取得
			FileItem item = parser.getFileItem("data");
			if (item == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String message = getMessage("E0001", "学習機械判定");
				String cause = getMessage("C0002", "data");
				error(request, response, message, cause);
				return;
			}

			// type属性のチェック
			if (item.isFormField()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String message = getMessage("E0001", "学習機械判定");
				String cause = getMessage("C0003", "data");
				error(request, response, message, cause);
				return;
			}

			// アップロードファイルのバリデーション
			String fileName = item.getName();
			if ((fileName == null) || fileName.isEmpty()) {
				String message = getMessage("E0002");
				String cause = getMessage("C0004", "データ");
				error(request, response, message, cause);
				return;
			}

			// バリデーション
			LearnerClassifyRequestParser cParser = validate(parser);

			// 判定パラメータの作成
			executor = JDBCFactory.getExecutor();
			appLoader = new DBLoader(executor, "learner");
			LearnerClassifierParameter parameter = cParser.parse(appLoader);

			// 進捗状況を設定
			progressId = CookieUtil.getCookie(request, "progress");
			if (progressId != null) {
				session = request.getSession();
				session.removeAttribute(progressId.getValue() + "_total");
				session.removeAttribute(progressId.getValue() + "_count");
				session.setAttribute(progressId.getValue() + "_total", count(parser));
			}

			// 学習機械で判定"
			inLoader = getCSVLoader(parser);
			//LearnerClassifier classifier = new LearnerClassifier(parameter);
			//LearnerClassifyResultEnity result = classifier.classify(inLoader);
			LearnerClassifyResultEnity result = classify(inLoader, parameter);

			// Velocityコンテキストに値を設定
			VelocityContext context = createVelocityContext();
			//context.put("showDetail", showDetail);
			context.put("result", result);

			// XML出力
			response.setContentType(CONTENT_TYPE_XML);
			response.setHeader("charset", encoding);
			response.setCharacterEncoding(encoding);
			PrintWriter writer = response.getWriter();
			Velocity.mergeTemplate(TEMPLATE_PATH, encoding, context, writer);

		} catch (Throwable t) {
			logger.error("", t);
			String message = getMessage("E0001", "学習機械判定");
			error(request, response, message, t);
		} finally {
			if (executor != null) executor.finalize();
			if (progressId != null) {
				try { session.removeAttribute(progressId.getValue() + "_total"); } catch (Exception e) {}
				try { session.removeAttribute(progressId.getValue() + "_count"); } catch (Exception e) {}
				try { progressId.setMaxAge(0); response.addCookie(progressId); } catch (Exception e) {}
			}
		}
	}

	/**
	 * <p>
	 * バリデーションを行う.
	 * </p>
	 * @param parser リクエスト
	 * @return リクエスト解析クラス
	 * @throws UnsupportedEncodingException
	 */
	private LearnerClassifyRequestParser validate(MultipartRequestParser parser) throws UnsupportedEncodingException {

		String dataType = parser.getParameter("data_type");
		String classifiedType = parser.getParameter("classified_type");
		String identification = parser.getParameter("identification");
		String title = parser.getParameter("title");
		String body = parser.getParameter("body");
		String correct = parser.getParameter("correct");

		// TODO バリデーション

		// TODO データ種別と判定種別から動的にする
		Map<String, String> itemNames = new HashMap<String, String>();
		itemNames.put("identification", identification);
		itemNames.put("title", title);
		itemNames.put("body", body);
		if ((correct != null) && !correct.isEmpty()) itemNames.put("correct", correct);

		// パーサーに格納
		LearnerClassifyRequestParser cParser = new LearnerClassifyRequestParser();
		cParser.setDataType(dataType);
		cParser.setClassifiedType(classifiedType);
		cParser.setItemNames(itemNames);

		return cParser;
	}

	/**
	 * <p>
	 * CSVファイルのデータローダを取得する.
	 * </p>
	 * @param parser リクエスト
	 * @return データローダ
	 * @throws IOException 
	 */
	private CSVLoader getCSVLoader(MultipartRequestParser parser) throws IOException {

		FileItem item = parser.getFileItem("data");
		String header = parser.getParameter("header");

		boolean hasHeader = false;
		if ((header != null) && "included".equals(header)) {
			hasHeader = true;
		}
		hasHeader = true;

		return new CSVLoader(item.getInputStream(), encoding, hasHeader);
	}

	/**
	 * <p>
	 * 学習機械で判定する.
	 * </p>
	 * @param loader データローダ
	 * @param parameter 判定パラメータ
	 * @return 判定結果
	 * @throws Exception
	 */
	public LearnerClassifyResultEnity classify(DataLoaderImpl loader,
			LearnerClassifierParameter parameter) throws Exception {

		try {
			// データローダのオープン
			loader.open();

			// データがなくなるまで判定
			List<DataLearnerClassifyResultEntity> classifieds = new ArrayList<DataLearnerClassifyResultEntity>();
			for (int i = 1;;i++) {
				// データの取得
				Map<String, Object> data = loader.fetch();
				if (data == null) {
					break;
				}

				// 判定
				LearnerClassifier classifier = new LearnerClassifier(parameter);
				DataLearnerClassifyResultEntity classified = classifier.classify(data);
				classifieds.add(classified);
				setProgress(i);
				//System.out.println(i);
			}

			LearnerClassifyResultEnity result = new LearnerClassifyResultEnity();
			result.setLearner(parameter.getLearner());
			result.setClassifieds(classifieds);

			return result;

		} finally {
			if (loader != null) try { loader.close(); } catch (Exception e) {}
		}
	}

	/**
	 * <p>
	 * 進捗状況を設定する.
	 * </p>
	 * @param count 処理件数
	 */
	private void setProgress(int count) {
		if (progressId != null) {
			session.setAttribute(progressId.getValue() + "_count", count);
		}
	}

	/**
	 * <p>
	 * データ数を取得する.
	 * </p>
	 * @param parser リクエスト
	 * @return データ数
	 * @throws Exception
	 */
	private int count(MultipartRequestParser parser) throws Exception {

		CSVLoader loader = null;

		try {
			// データローダの取得
			loader = getCSVLoader(parser);
			// データローダのオープン
			loader.open();
	
			// データがなくなるまでカウント
			int count = 0;
			for (;;) {
				// データの取得
				Map<String, Object> data = loader.fetch();
				if (data == null) {
					break;
				}

				count += 1;
			}

			return count;

		} finally {
			if (loader != null) try { loader.close(); } catch (Exception e) {}
		}
	}
}
