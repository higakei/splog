package jp.co.hottolink.splogfilter.learning.writer;

import java.util.Map;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;

/**
 * <p>
 * DBのレコードライター.
 * </p>
 * @author higa
 */
public class DBRecordWriter implements RecordWriterImpl {

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
	public DBRecordWriter(SQLExecutor executor, String table) {
		this.executor = executor;
		this.table = table;
	}

	/*
	 * (非 Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.writer.RecordWriterImpl#open()
	 */
	@Override
	public void open() throws Exception {
		executor.executeQuery("truncate table " + table);
	}

	/*
	 * (非 Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.writer.RecordWriterImpl#close()
	 */
	@Override
	public void close() {}

	/*
	 * (非 Javadoc)
	 * @see jp.co.hottolink.splogfilter.learning.writer.RecordWriterImpl#println(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void println(Object record) throws Exception {

		try {
			Map<String, Object> map = (Map<String, Object>)record;

			// パラメータの作成
			StringBuffer nBuffer = new StringBuffer();
			StringBuffer vBuffer = new StringBuffer();
			for (String key: map.keySet()) {
				nBuffer.append("," + key);
				vBuffer.append(",?");
			}

			// SQLの作成
			StringBuffer sql = new StringBuffer();
			sql.append("insert into ");
			sql.append(table);
			sql.append(" (");
			sql.append(nBuffer.substring(1));
			sql.append(") values (");
			sql.append(vBuffer.substring(1));
			sql.append(")");

			// 値の設定
			executor.preparedStatement(sql.toString());
			int index = 0;
			for (String key: map.keySet()) {
				executor.setObject(++index, map.get(key));
			}

			// SQLの実行
			executor.executeUpdate();

		} finally {
			if (executor != null) executor.closeQuery();
		}
	}
}
