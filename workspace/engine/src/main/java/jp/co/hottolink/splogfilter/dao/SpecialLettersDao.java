package jp.co.hottolink.splogfilter.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.DAOException;

/**
 * <p>
 * speciallettersテーブルのDAOクラス.
 * </p>
 * @author higa
 */
public class SpecialLettersDao {

	/**
	 * <p>
	 * speciallettersを取得するSQL.
	 * </p>
	 */
	private static final String SQL_SELECT_BY_TYPE = "select str from specialletters where type = ?";

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
	public SpecialLettersDao(SQLExecutor executor) {
		this.executor = executor;
	}

	/**
	 * <p>
	 * speciallettersを取得する.
	 * </p>
	 * @param type type
	 * @return specialletters
	 */
	public List<String> select(int type) {

		try {
			// SQLの実行
			executor.preparedStatement(SQL_SELECT_BY_TYPE);
			executor.setInt(1, type);
			ResultSet rs = executor.executeQuery();

			// speciallettersの取得
			List<String> list = new ArrayList<String>();
			while(rs.next()) {
				list.add(rs.getString("str"));
			}

			return list;

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}
}
