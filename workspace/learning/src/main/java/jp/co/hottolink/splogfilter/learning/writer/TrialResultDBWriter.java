package jp.co.hottolink.splogfilter.learning.writer;

import java.util.Map;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.learning.dao.TrialResultDao;

/**
 * <p>
 * トライアル結果をDBに出力するクラス.
 * </p>
 * @author higa
 */
public class TrialResultDBWriter implements RecordWriterImpl {

	/**
	 * <p>
	 * DAO.
	 * </p>
	 */
	private TrialResultDao dao = null;

	/**
	 * <p>
	 * テーブル名.
	 * </p>
	 */
	private String table = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param executor SQL実行クラス
	 */
	public TrialResultDBWriter(SQLExecutor executor) {
		dao = new TrialResultDao(executor);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.writer.RecordWriterImpl#close()
	 */
	@Override
	public void close() {}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.writer.RecordWriterImpl#open()
	 */
	@Override
	public void open() throws Exception {
		table = dao.createTable();
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.writer.RecordWriterImpl#println(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void println(Object record) throws Exception {
		Map<String, Object> map = (Map<String, Object>)record;
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
