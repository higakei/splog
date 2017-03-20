package jp.co.hottolink.splogfilter.learning.dao;

import java.sql.SQLException;
import java.util.Map;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.DAOException;
import jp.co.hottolink.splogfilter.common.util.UniqueNameGenerator;

/**
 * <p>
 * 学習データテーブルのDAOクラス.
 * </p>
 * @author higa
 *
 */
public class TrainingDataDao {

	/**
	 * <p>
	 * デフォルトテーブル名.
	 * </p>
	 */
	private static final String DEFAULT_TABLE_NAME = "training_data";

	/**
	 * <p>
	 * テーブルを作成するSQL.
	 * </p>
	 */
	private static final String SQL_CREATE_TABLE = "CREATE TABLE `%_TABLE_NAME_%` ("
			+ "  `id` int(11) NOT NULL AUTO_INCREMENT,"
			+ "%_DATA_ITEM_%"
			+ "  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
			+ "  PRIMARY KEY (`id`)"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

	/**
	 * <p>
	 * 学習データの項目のDDL.
	 * </p>
	 */
	private static final String DDL_DATA_ITEM = "  `%_COLUMN_NAME_%` mediumtext,";

	/**
	 * <p>
	 * SQL実行クラス.
	 * </p>
	 */
	private SQLExecutor executor = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param executor SQL実行クラス
	 */
	public TrainingDataDao(SQLExecutor executor) {
		this.executor = executor;
	}

	/**
	 * <p>
	 * テーブルを作成する.
	 * </p>
	 * @param data 学習データ
	 * @return テーブル名
	 * @throws DAOException
	 */
	public String createTable(Map<String, Object> data) throws DAOException {

		try {
			// テーブル名の取得
			executor.executeUpdate("drop table if exists " + DEFAULT_TABLE_NAME);
			String tableName = DEFAULT_TABLE_NAME;
			//tableName = UniqueNameGenerator.generate(tableName);

			// 判別器カラム定義の作成
			StringBuffer ddl = new StringBuffer();
			for (String coulmnName: data.keySet()) {
				ddl.append(DDL_DATA_ITEM.replaceAll("%_COLUMN_NAME_%", coulmnName));
			}

			// SQLの作成
			String sql = SQL_CREATE_TABLE;
			sql = sql.replaceAll("%_TABLE_NAME_%", tableName);
			sql = sql.replaceAll("%_DATA_ITEM_%", ddl.toString());

			// テーブルの作成
			executor.executeUpdate(sql);

			return tableName;

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * レコードを登録する.
	 * </p>
	 * @param tableName テーブル名
	 * @param record レコード
	 * @throws DAOException
	 */
	public void insert(String tableName, Map<String, Object> record) throws DAOException {

		try {
			StringBuffer nBuffer = new StringBuffer();
			StringBuffer vBuffer = new StringBuffer();
			for (String key: record.keySet()) {
				nBuffer.append("," + key);
				vBuffer.append(",?");
			}

			// SQLの作成
			StringBuffer sql = new StringBuffer();
			sql.append("insert into ");
			sql.append(tableName);
			sql.append(" (");
			sql.append(nBuffer.substring(1));
			sql.append(") values (");
			sql.append(vBuffer.substring(1));
			sql.append(")");

			// 値の設定
			executor.preparedStatement(sql.toString());
			int index = 0;
			for (String key: record.keySet()) {
				executor.setObject(++index, record.get(key));
			}

			// SQLの実行
			executor.executeUpdate();

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * レコード数を取得する.
	 * </p>
	 * @param tableName テーブル名
	 * @return レコード数
	 * @throws DAOException
	 */
	public int count(String tableName) throws DAOException {
		try {
			if (tableName == null) tableName = DEFAULT_TABLE_NAME;
			String sql = "select count(*) from " + tableName;
			executor.executeQuery(sql);
			executor.next();
			return executor.getInt(1);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}
}
