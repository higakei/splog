package jp.co.hottolink.splogfilter.learning.dao;

import java.sql.SQLException;
import java.util.Map;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.DAOException;
import jp.co.hottolink.splogfilter.common.util.UniqueNameGenerator;

/**
 * <p>
 * トライアル結果テーブルのDAOクラス.
 * </p>
 * @author higa
 */
public class TrialResultDao {

	/**
	 * <p>
	 * デフォルトテーブル名.
	 * </p>
	 */
	private static final String DEFAULT_TABLE_NAME = "trial_result";

	/**
	 * <p>
	 * テーブルを作成するSQL.
	 * </p>
	 */
	private static final String SQL_CREATE_TABLE = "CREATE TABLE `%_TABLE_NAME_%` ("
			+ "  `id` int(11) NOT NULL AUTO_INCREMENT,"
			+ "  `trial_id` int(11) DEFAULT NULL,"
			+ "  `classifier` varchar(255) NOT NULL,"
			+ "  `data_id` int(11) NOT NULL,"
			+ "  `identification` text,"
			+ "  `correct` varchar(255) DEFAULT NULL,"
			+ "  `answer` varchar(255) NOT NULL,"
			+ "  `matching` enum('true','false') DEFAULT NULL,"
			+ "  `creation_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,"
			+ "  PRIMARY KEY (`id`),"
			+ "  UNIQUE KEY `unique` (`data_id`,`classifier`),"
			+ "  KEY `classifier` (`classifier`),"
			+ "  KEY `data` (`data_id`)"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

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
	public TrialResultDao(SQLExecutor executor) {
		this.executor = executor;
	}

	/**
	 * <p>
	 * テーブルを作成する.
	 * </p>
	 * @return テーブル名
	 */
	public String createTable() {

		try {
			// テーブル名の取得
			executor.executeUpdate("drop table if exists " + DEFAULT_TABLE_NAME);
			String tableName = DEFAULT_TABLE_NAME;
			//tableName = UniqueNameGenerator.generate(tableName);

			// SQLの作成
			String sql = SQL_CREATE_TABLE;
			sql = sql.replaceAll("%_TABLE_NAME_%", tableName);

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
	 */
	public void insert(String tableName, Map<String, Object> record) {

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
}
