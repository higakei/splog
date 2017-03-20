package jp.co.hottolink.splogfilter.learning.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.loader.DBLoader;
import jp.co.hottolink.splogfilter.learning.boosting.entity.ClassifierEntity;
import jp.co.hottolink.splogfilter.learning.dao.TrainingDataDao;
import jp.co.hottolink.splogfilter.learning.logic.entity.DatasetEntity;
import jp.co.hottolink.splogfilter.learning.logic.trial.TrialEntity;
import jp.co.hottolink.splogfilter.learning.master.MasterDataCache;
import jp.co.hottolink.splogfilter.learning.master.entity.ClassifiedTypeEntity;
import jp.co.hottolink.splogfilter.learning.master.entity.DataTypeEntity;
import jp.co.hottolink.splogfilter.learning.web.db.JDBCFactory;
import jp.co.hottolink.splogfilter.learning.writer.TrialResultDBWriter;

import org.apache.log4j.Logger;

/**
 * <p>
 * 学習データのトライアルを行うサーブレット.
 * </p>
 * @author higa
 */
public class TrainingDataTrialServlet extends BaseServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -6350915655520453209L;

	/**
	 * <p>
	 * 遷移先.
	 * </p>
	 */
	private static final String FORWARD = "/WEB-INF/jsp/trial.jsp";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private Logger logger = Logger.getLogger(TrainingDataTrialServlet.class);

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

		SQLExecutor read = null;
		SQLExecutor write = null;

		try {
			// リクエストの取得
			String data_type = request.getParameter("data_type");
			String classified_type = request.getParameter("classified_type");
			String identification = request.getParameter("identification");
			String title = request.getParameter("title");
			String body = request.getParameter("body");
			String correct = request.getParameter("correct");

			// データセット情報の取得
			DatasetEntity dataset = new DatasetEntity();
			dataset.setName("training_data");

			// データ種別の取得
			DataTypeEntity dataType = MasterDataCache.getDataType(data_type);
			// 判別種別の取得
			ClassifiedTypeEntity classifiedType = MasterDataCache.getClassifiedType(classified_type);
			// 判別器リストの取得
			List<ClassifierEntity> classifiers = classifiedType.getClassifiers();

			// TODO データ種別と判定種別から動的にする
			Map<String, String> itemNames = new HashMap<String, String>();
			itemNames.put("identification", identification);
			itemNames.put("title", title);
			itemNames.put("body", body);
			if ((correct != null) && !correct.isEmpty()) itemNames.put("correct", correct);

			// トライアル情報の作成
			TrialEntity trial = new TrialEntity();
			trial.setDataset(dataset);
			trial.setDataType(dataType);
			trial.setClassifiedType(classifiedType);
			trial.setClassifiers(classifiers);
			trial.setItemNames(itemNames);

			// データローダの生成
			read = JDBCFactory.getExecutor();
			String datasetName = trial.getDataset().getName();
			DBLoader loader = new DBLoader(read, datasetName);

			// レコードライターの生成
			write = JDBCFactory.getExecutor();
			TrialResultDBWriter writer = new TrialResultDBWriter(write);

			// 学習データ数の取得
			TrainingDataDao dao = new TrainingDataDao(read);
			int total = dao.count(datasetName);

			// トライアルの実施
			request.setAttribute("trial", trial);
			request.setAttribute("loader", loader);
			request.setAttribute("writer", writer);
			request.setAttribute("total", total);
			request.getRequestDispatcher(FORWARD).forward(request, response);

		} catch (Throwable t) {
			logger.error("", t);
			String message = getMessage("E0001", "トライアル");
			error(request, response, message, t);
		} finally {
			if (read != null) read.finalize();
			if (write != null) write.finalize();
		}
	}
}
