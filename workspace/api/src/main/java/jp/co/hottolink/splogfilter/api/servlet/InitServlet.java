package jp.co.hottolink.splogfilter.api.servlet;

import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.velocity.app.Velocity;

import jp.co.hottolink.splogfilter.takeda.ConstantWordSetter;
import jp.co.hottolink.splogfilter.takeda.bayes.config.BayesFilterConfigLoader;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;

/**
 * <p>
 * コンテキストの初期化を行うサーブレット.
 * </p>
 * @author higa
 */
public class InitServlet extends HttpServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -3583365979745712147L;

	/**
	 * <p>
	 * テンプレートパス.
	 * </p>
	 */
	private static final String TEMPLATE_PATH = "/WEB-INF/template";

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		try {
			// スパム判定語の取得
			ConstantWordSetter.setFromCSV(SplogFilterConstants.SPECIAL_LETTERS_CSV_FILE_PATH);

			// ベイズフィルターの設定のロード
			BayesFilterConfigLoader loader = new BayesFilterConfigLoader();
			loader.load();

			// Velocityエンジンの初期化
			Properties prop = new Properties();
			String templatePath = getServletContext().getRealPath(TEMPLATE_PATH);
			prop.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templatePath);
			prop.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, Boolean.TRUE.toString());
			Velocity.init(prop);

		} catch (Exception e) {
			throw new RuntimeException("初期化に失敗しました。", e);
		}
	}
}
