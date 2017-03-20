package jp.co.hottolink.splogfilter.learning.writer;

import java.util.Map;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.learning.dao.TrainingDataDao;

/**
 * <p>
 * 訓練データテーブルのレコードライター.
 * </p>
 * @author higa
 *
 */
public class TrainingDataDBRecordWriter implements RecordWriterImpl {

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

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.writer.RecordWriterImpl#open()
	 */
	@Override
	public void open() throws Exception {}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.writer.RecordWriterImpl#close()
	 */
	@Override
	public void close() {}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.writer.RecordWriterImpl#println(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
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
