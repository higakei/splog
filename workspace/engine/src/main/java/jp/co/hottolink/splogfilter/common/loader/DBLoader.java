package jp.co.hottolink.splogfilter.common.loader;

import java.util.LinkedHashMap;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;

/**
 * <p>
 * DBのロードクラス.
 * </p>
 * @author higa
 */
public class DBLoader implements DataLoaderImpl {

	/**
	 * <p>
	 * SQL実行クラス.
	 * </p>
	 */
	private SQLExecutor executor = null;

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
	 * @param table テーブル名
	 */
	public DBLoader(SQLExecutor executor, String table) {
		this.executor = executor;
		this.table = table;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl#open()
	 */
	@Override
	public void open() throws Exception {
		String sql = "select * from " + table;
		executor.executeQuery(sql);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl#close()
	 */
	@Override
	public void close() throws Exception {
		if (executor != null) executor.closeQuery();
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl#fetch()
	 */
	@Override
	public LinkedHashMap<String, Object> fetch() throws Exception {

		if (!executor.next()) {
			return null;
		}

		LinkedHashMap<String, Object> record = new LinkedHashMap<String, Object>();
		for (int i = 1; i <= executor.getColumnCount(); i++) {
			String key = executor.getColumnLabel(i);
			Object value = executor.getObject(i);
			record.put(key, value);
		}

		return record;
	}
}
