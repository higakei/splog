package jp.co.hottolink.splogfilter.tools.bayes.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.DAOException;
import jp.co.hottolink.splogfilter.tools.bayes.entity.SplogSampleEntity;

/**
 * <p>
 * スプログサンプルテーブルのDAOクラス.
 * </p>
 * @author higa
 */
public class SplogSampleDao {

	/**
	 * <p>
	 * スプログサンプルを取得するSQL.
	 * </p>
	 */
	private static final String SQL_SELECT_ALL = "select id, url, title" +
			", body, splog from splog_sample";

	/**
	 * <p>
	 * サンプルIDリストを取得するSQL.
	 * </p>
	 */
	private static final String SQL_SELECT_ID = "select id from splog_sample";

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
	public SplogSampleDao(SQLExecutor executor) {
		this.executor = executor;
	}

	/**
	 * <p>
	 * サンプルIDリストを取得する.
	 * </p>
	 * @param limit 取得件数
	 * @return サンプルID
	 * @throws DAOException
	 */
	public List<Integer> getIdList() throws DAOException {
		try {
			// SQLの実行
			executor.executeQuery(SQL_SELECT_ID);
			
			// データの取得
			List<Integer> list = new ArrayList<Integer>(0);
			while (executor.next()) {
				list.add(executor.getInt("id"));
			}

			return list;

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}		
	}

	/**
	 * <p>
	 * 指定したサンプルIDのスプログサンプルを取得する.
	 * </p>
	 * @param id サンプルID
	 * @return スプログサンプル
	 * @throws DAOException
	 */
	public SplogSampleEntity selectById(int id) throws DAOException {

		try {
			// SQLの作成
			String sql = SQL_SELECT_ALL;
			sql += " where id = ?";
			
			// SQLの実行
			executor.preparedStatement(sql);
			executor.setInt(1, id);
			executor.executeQuery();
			if (!executor.next()) {
				return null;
			}

			// データの取得
			SplogSampleEntity entity = new SplogSampleEntity();
			entity.setId(executor.getInt("id"));
			entity.setUrl(executor.getString("url"));
			entity.setTitle(executor.getString("title"));
			entity.setBody(executor.getString("body"));
			entity.setSplog(executor.getBoolean("splog"));

			return entity;

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}
}
