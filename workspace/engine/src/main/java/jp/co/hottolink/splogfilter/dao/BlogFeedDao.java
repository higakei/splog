package jp.co.hottolink.splogfilter.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.DAOException;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;

/**
 * <p>
 * ブログフィードテーブルのDAOクラス.
 * </p>
 * @author higa
 */
public class BlogFeedDao {

	/**
	 * <p>
	 * ブログフィードを取得するSQL.
	 * </p>
	 */
	private static final String SQL_SELECT_ALL = "select id, url, title" +
			", documentId, authorId, date, body, creation_date from blogfeed";

	/**
	 * <p>
	 * ブログフィードを登録するSQL.
	 * </p>
	 */
	private static final String SQL_INSERT = "insert into blogfeed" +
			" (authorId, documentId, url, title, body, date)" +
			" values (?, ?, ?, ?, ?, ?)";

	/**
	 * <p>
	 * ドキュメント存在チェックのSQL.
	 * </p>
	 */
	private static final String SQL_IS_DOCUMENT_EXIST = "select id from blogfeed where documentId = ?";

	/**
	 * <p>
	 * ブログフィードIDを取得するSQL.
	 * </p>
	 */
	private static final String SQL_SELECT_ID = "select id from blogfeed";

	/**
	 * <p>
	 * ブログフィードの件数を取得する.
	 * </p>
	 */
	private static final String SQL_COUNT = "select count(id) from blogfeed";

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
	public BlogFeedDao(SQLExecutor executor) {
		this.executor = executor;
	}

	/**
	 * <p>
	 * 指定したブログフィードIDの範囲のブログフィードを取得する.
	 * </p>
	 * @param range ブログフィードIDの範囲
	 * @return ブログフィード
	 */
	public List<BlogFeedEntity> selectByRange(int[] range) {

		try {
			List<BlogFeedEntity> list = new ArrayList<BlogFeedEntity>(0);
			if (range == null) {
				return list;
			}
			
			// SQLの作成
			String sql = SQL_SELECT_ALL;
			sql += " where id between ? and ?";
			
			// SQLの実行
			executor.preparedStatement(sql);
			executor.setInt(1, range[0]);
			executor.setInt(2, range[1]);
			executor.executeQuery();

			// データの取得
			while (executor.next()) {
				BlogFeedEntity entity = create(executor);
				list.add(entity);
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
	 * 指定したブログフィードIDのブログフィードを取得する.
	 * </p>
	 * @param id ブログフィードID
	 * @return ブログフィード
	 */
	public BlogFeedEntity selectById(int id) {

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

			return create(executor);

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * ブログフィードを取得する.
	 * </p>
	 * @param limit 取得件数
	 * @return ブログフィード
	 */
	public List<BlogFeedEntity> selectAll(Integer limit) {

		try {
			// SQLの作成
			String sql = SQL_SELECT_ALL;
			if (limit != null) 	{
				sql += " limit " + limit;
			}
			
			// SQLの実行
			executor.executeQuery(sql);

			// データの取得
			List<BlogFeedEntity> list = new ArrayList<BlogFeedEntity>(0);
			while (executor.next()) {
				BlogFeedEntity entity = create(executor);
				list.add(entity);
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
	 * ブログフィードを取得する.
	 * </p>
	 * @return ブログフィード
	 */
	public List<BlogFeedEntity> selectAll() {
		return selectAll(null);
	}

	public List<BlogFeedEntity> select(Integer limit, Integer offset) {

		try {
			// SQLの作成
			String sql = SQL_SELECT_ALL;
			if (limit != null) 	{
				sql += " order by id";
				sql += " limit ";
				if (offset != null) sql += offset + ",";
				sql += limit;
			}
			
			// SQLの実行
			executor.executeQuery(sql);

			// データの取得
			List<BlogFeedEntity> list = new ArrayList<BlogFeedEntity>(0);
			while (executor.next()) {
				BlogFeedEntity entity = create(executor);
				list.add(entity);
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
	 * ブログフィードを登録する.
	 * </p>
	 * @param entity ブログフィード
	 */
	public void insert(BlogFeedEntity entity) {
		try {
			executor.preparedStatement(SQL_INSERT);
			int index = 0;
			executor.setString(++index, entity.getAuthorId());
			executor.setString(++index, entity.getDocumentId());
			executor.setString(++index, entity.getUrl());
			executor.setString(++index, entity.getTitle());
			executor.setString(++index, entity.getBody());
			executor.setTimestamp(++index, entity.getDate());
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * ドキュメントの存在チェックを行う.
	 * </p>
	 * @param documentId ドキュメントID
	 * @return true:存在する, false:存在しない
	 */
	public boolean isDocumentExist(String documentId) {
		try {
			executor.preparedStatement(SQL_IS_DOCUMENT_EXIST);
			executor.setString(1, documentId);
			executor.executeQuery();
			return executor.next();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * ブログフィードの件数を取得する.
	 * </p>
	 * @return
	 */
	public int count() {
		try {
			executor.executeQuery(SQL_COUNT);
			executor.next();
			return executor.getInt(1);
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * ブログフィードIDを取得する.
	 * </p>
	 * @param limit 取得件数
	 * @return ブログフィードID
	 */
	public List<Integer> getIdList(Integer limit) {
		try {
			// SQLの作成
			String sql = SQL_SELECT_ID;
			sql += " order by id";
			if (limit != null) 	sql += " limit " + limit;

			// SQLの実行
			executor.executeQuery(sql);
			
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
	 * SQLの実行結果からブログフィードのEntityを作成する.
	 * </p><pre>
	 * SQL_SELECT_ALLに対応
	 * </pre>
	 * @param executor SQLの実行結果
	 * @return ブログフィード
	 * @throws SQLException
	 */
	private BlogFeedEntity create(SQLExecutor executor) throws SQLException {
		BlogFeedEntity entity = new BlogFeedEntity();
		entity.setId(executor.getInt("id"));
		entity.setUrl(executor.getString("url"));
		entity.setTitle(executor.getString("title"));
		entity.setDocumentId(executor.getString("documentId"));
		entity.setAuthorId(executor.getString("authorId"));
		entity.setDate(executor.getTimestamp("date"));
		entity.setBody(executor.getString("body"));
		entity.setCreationDate(executor.getTimestamp("creation_date"));
		return entity;
	}
}
