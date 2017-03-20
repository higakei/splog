package jp.co.hottolink.splogfilter.tools.boosting.fukuhara.writer;

import java.util.Map;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.tools.boosting.fukuhara.dao.TrainingDataDao;

/**
 * <p>
 * 訓練データテーブルのレコードライター.
 * </p>
 * @author higa
 *
 */
public class TrainingDataDBRecordWriter {

	/**
	 * <p>
	 * DAO.
	 * </p>
	 */
	private TrainingDataDao dao = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param executor SQL実行クラス
	 */
	public TrainingDataDBRecordWriter(SQLExecutor executor) {
		dao = new TrainingDataDao(executor);
	}

	/**
	 * <p>
	 * テーブル名.
	 * </p>
	 */
	private String table = null;

	@SuppressWarnings("unchecked")
	public void println(Object record) throws Exception {
		Map<String, Object> map = (Map<String, Object>)record;
		if (table == null) table = dao.createTable(map);
		dao.insert(table, map);
	}

	/**
	 * <p>
	 * テーブル名を取得する.
	 * </p>
	 * @return テーブル名
	 */
	public String getTable() {
		return table;
	}
}
