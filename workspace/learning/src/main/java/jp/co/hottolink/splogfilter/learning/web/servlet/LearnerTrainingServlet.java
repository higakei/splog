package jp.co.hottolink.splogfilter.learning.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.loader.DBLoader;
import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;
import jp.co.hottolink.splogfilter.learning.logic.training.LearnerTrainer;
import jp.co.hottolink.splogfilter.learning.logic.training.TrainingEntity;
import jp.co.hottolink.splogfilter.learning.logic.trial.TrialEntity;
import jp.co.hottolink.splogfilter.learning.master.MasterDataCache;
import jp.co.hottolink.splogfilter.learning.web.db.JDBCFactory;
import jp.co.hottolink.splogfilter.learning.web.util.CookieUtil;
import jp.co.hottolink.splogfilter.learning.writer.DBRecordWriter;

/**
 * <p>
 * 学習機械の訓練を行うサーブレット.
 * </p>
 * @author higa
 */
public class LearnerTrainingServlet extends BaseServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -5935150416741606901L;

	/**
	 * <p>
	 * 遷移先.
	 * </p>
	 */
	private static final String REDIRECT = "/jsp/training_completed.jsp";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private Logger logger = Logger.getLogger(LearnerTrainingServlet.class);

	/* (非 Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/* (非 Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		SQLExecutor executor = null;
		Cookie cookie = null;
		HttpSession session = null;
		boolean canRemoveCookie = false;

		try {
			// クッキーの取得
			cookie = CookieUtil.getCookie(request, "training");
			if (cookie != null) {
				canRemoveCookie = true;
				session = request.getSession();
				session.removeAttribute(cookie.getValue());
			}

			// リクエストの取得
			String[] pClassifiers = request.getParameterValues("classifier");

			// バリデーション
			if ((pClassifiers == null) || (pClassifiers.length == 0)) {
				String message = getMessage("E0002");
				String cause = getMessage("C0005", "判別器");
				error(request, response, message, cause);
				return;
			}

			// トライアル情報の取得
			TrialEntity trial = new TrialEntity();
			trial.setResultName("trial_result");
			trial.setDataType(MasterDataCache.getDataType("blog"));
			trial.setClassifiedType(MasterDataCache.getClassifiedType("spam"));

			// 判定器リストの取得
			List<ClassifierEntity> classifiers = new ArrayList<ClassifierEntity>();
			for (String pClassifier: pClassifiers) {
				classifiers.add(MasterDataCache.getClassifier(pClassifier));
			}

			// 訓練情報
			TrainingEntity training = new TrainingEntity();
			training.setTrial(trial);
			training.setClassifiers(classifiers);

			// DBに接続
			executor = JDBCFactory.getExecutor();
			//executor.setAutoCommit(false);

			// 学習機械の訓練
			DBLoader loader = new DBLoader(executor, trial.getResultName());
			LearnerTrainer tainer = new LearnerTrainer(training);
			tainer.train(loader);

			// 学習機械の出力
			DBRecordWriter writer = new DBRecordWriter(executor, "learner");
			tainer.output(writer);

			// コミット
			//executor.commit();

			// 訓練結果をセッションに設定
			if (cookie != null) {
				canRemoveCookie = true;
				session.setAttribute(cookie.getValue(), training);
			}

			// 完了画面にリダイレクト
			canRemoveCookie = false;
			response.sendRedirect(request.getContextPath() + REDIRECT);

		} catch (Throwable t) {
			//if (executor != null) try { executor.rollback(); } catch (Exception re) {}
			logger.error("", t);
			String message = getMessage("E0001", "訓練");
			error(request, response, message, t);
		} finally {
			if (executor != null) executor.finalize();
			if (canRemoveCookie) {
				if (cookie != null) {
					try { cookie.setMaxAge(0); response.addCookie(cookie); System.out.println("cookie removed"); } catch (Exception e) {}
					try { session.removeAttribute(cookie.getValue()); System.out.println("session removed"); } catch (Exception e) {}
				}
			}
		}
	}
}
