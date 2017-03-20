package jp.co.hottolink.splogfilter.learning.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.loader.DBLoader;
import jp.co.hottolink.splogfilter.learning.logic.matching.MatchingEntity;
import jp.co.hottolink.splogfilter.learning.logic.matching.TrialResultMatcher;
import jp.co.hottolink.splogfilter.learning.logic.trial.TrialEntity;
import jp.co.hottolink.splogfilter.learning.master.MasterDataCache;
import jp.co.hottolink.splogfilter.learning.web.db.JDBCFactory;

import org.apache.log4j.Logger;

/**
 * <p>
 * トライアル結果を照合するサーブレット.
 * </p>
 * @author higa
 */
public class TrialResultMatchingServlet extends BaseServlet {


	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 2874500049478760038L;

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private Logger logger = Logger.getLogger(TrialResultMatchingServlet.class);

	/**
	 * <p>
	 * 遷移先.
	 * </p>
	 */
	private static final String FORWARD = "/WEB-INF/jsp/matching_result.jsp";

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

		SQLExecutor executor = null;

		try {
			// トライアル情報の取得
			TrialEntity trial = new TrialEntity();
			trial.setResultName("trial_result");
			trial.setDataType(MasterDataCache.getDataType("blog"));
			trial.setClassifiedType(MasterDataCache.getClassifiedType("spam"));
			trial.setClassifiers(trial.getClassifiedType().getClassifiers());

			//  照合
			executor = JDBCFactory.getExecutor();
			DBLoader loader = new DBLoader(executor, trial.getResultName());
			TrialResultMatcher matcher = new TrialResultMatcher(trial);
			MatchingEntity matching = matcher.match(loader);

			// 照合結果画面に遷移
			request.setAttribute("trial", matching.getTrial());
			request.setAttribute("matchings", matching.getMatchings());
			request.getRequestDispatcher(FORWARD).forward(request, response);

		} catch (Throwable t) {
			logger.error("", t);
			String message = getMessage("E0001", "照合");
			error(request, response, message, t);
		} finally {
			if (executor != null) executor.finalize();
		}
	}
}
