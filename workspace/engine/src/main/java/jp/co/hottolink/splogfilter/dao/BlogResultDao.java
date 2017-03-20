package jp.co.hottolink.splogfilter.dao;

import java.sql.SQLException;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.DAOException;
import jp.co.hottolink.splogfilter.takeda.entity.BlogResultEntity;

/**
 * <p>
 * blog_resultテーブルのDAOクラス.
 * </p>
 * @author higa
 */
public class BlogResultDao {

	/**
	 * <p>
	 * スコアを取得するSQL.
	 * </p>
	 */
	public static final String SQL_SELECT = "select documentId, score from blog_result where documentId = ?";

	/**
	 * <p>
	 * スコアを更新するSQL.
	 * </p>
	 */
	public static final String SQL_UPDATE = "update blog_result set score = ? where documentId = ?";

	/**
	 * <p>
	 * スコアを登録するSQL.
	 * </p>
	 */
	public static final String SQL_INSERT = "insert into blog_result (documentId, score) values (?, ?)";

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
	public BlogResultDao(SQLExecutor executor) {
		this.executor = executor;
	}

	/**
	 * <p>
	 * 指定したブログIDのスコアを設定する.
	 * </p>
	 * @param documentId ブログID
	 * @param score スコア
	 */
	public void setScore(String documentId, int score) {
		BlogResultEntity entity = select(documentId);
		if (entity == null) {
			// 登録
			insert(documentId, score);
		} else {
			// 更新
			update(documentId, (score));
		}
	}

	/**
	 * <p>
	 * 指定したブログIDのスコアを設定する.
	 * </p>
	 * @param documentId ブログID
	 * @param score スコア
	 */
	public void setScore(String documentId, double score) {
		setScore(documentId, (int)score);
	}

	/**
	 * <p>
	 * 指定したブログIDのスコアを取得する.
	 * </p>
	 * @param documentId ブログID
	 * @return スコア
	 */
	public BlogResultEntity select(String documentId) {
		
		BlogResultEntity entity = null;
		
		try {
			executor.preparedStatement(SQL_SELECT);
			executor.setString(1, documentId);
			executor.executeQuery();

			if (executor.next()) {
				entity = new BlogResultEntity(documentId);
				entity.setScore(executor.getInt("score"));
			}

			return entity;

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 指定したブログIDのスコアを更新する.
	 * </p>
	 * @param documentId ブログID
	 * @param score スコア
	 */
	public void update(String documentId, int score) {
		try {
			executor.preparedStatement(SQL_UPDATE);
			executor.setInt(1, score);
			executor.setString(2, documentId);
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 指定したブログIDのスコアを登録する.
	 * </p>
	 * @param documentId ブログID
	 * @param score スコア
	 */
	public void insert(String documentId, int score) {
		try {
			executor.preparedStatement(SQL_INSERT);
			executor.setString(1, documentId);
			executor.setInt(2, score);
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}
}
