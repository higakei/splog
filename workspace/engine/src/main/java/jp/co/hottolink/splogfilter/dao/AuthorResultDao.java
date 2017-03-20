package jp.co.hottolink.splogfilter.dao;

import java.sql.SQLException;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.DAOException;
import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;

/**
 * <p>
 * author_resultテーブルのDAOクラス.
 * </p>
 * @author higa
 */
public class AuthorResultDao {

	/**
	 * <p>
	 * レコードを取得するのSQL.
	 * </p>
	 */
	private static final String SQL_SELECT = "select authorId, score, title, content, intervals from author_result where authorId = ?";

	/**
	 * <p>
	 * スコアを更新するSQL.
	 * </p>
	 */
	private static final String SQL_UPDATE_SCORE = "update author_result set score = ? where authorId = ?";

	/**
	 * <p>
	 * タイトルフィルタのスコアを更新するSQL.
	 * </p>
	 */
	private static final String SQL_UPDATE_TITLE = "update author_result set score = ?, title = ? where authorId = ?";

	/**
	 * <p>
	 * コンテンツフィルタのスコアを更新するSQL.
	 * </p>
	 */
	private static final String SQL_UPDATE_CONTENT = "update author_result set score = ?, content = ? where authorId = ?";

	/**
	 * <p>
	 * 投稿間隔フィルタのスコアを更新するSQL.
	 * </p>
	 */
	private static final String SQL_UPDATE_INTERVALS = "update author_result set score = ?, intervals = ? where authorId = ?";

	/**
	 * <p>
	 * レコードを更新するSQL.
	 * </p>
	 */
	private static final String SQL_UPDATE = "update author_result set score = ?, title = ?, content = ?, intervals = ? where authorId = ?";

	/**
	 * <p>
	 * スコアを登録するSQL.
	 * </p>
	 */
	private static final String SQL_INSERT_SCORE = "insert into author_result (authorId, score) values (?, ?)";

	/**
	 * <p>
	 * タイトルフィルタのスコアを登録するSQL.
	 * </p>
	 */
	private static final String SQL_INSERT_TITLE = "insert into author_result (authorId, score, title) values (?, ?, ?)";

	/**
	 * <p>
	 * コンテンツフィルタのスコアを登録するSQL.
	 * </p>
	 */
	private static final String SQL_INSERT_CONTENT = "insert into author_result (authorId, score, content) values (?, ?, ?)";

	/**
	 * <p>
	 * 投稿間隔フィルタのスコアを登録するSQL.
	 * </p>
	 */
	private static final String SQL_INSERT_INTERVALS = "insert into author_result (authorId, score, intervals) values (?, ?, ?)";

	/**
	 * <p>
	 * レコードを登録するSQL.
	 * </p>
	 */
	private static final String SQL_INSERT = "insert into author_result (authorId, score, title, content, intervals) values (?, ?, ?, ?, ?)";

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
	public AuthorResultDao(SQLExecutor executor) {
		this.executor = executor;
	}

	/**
	 * <p>
	 * 投稿者のスコアを設定する.
	 * </p>
	 * @param score 投稿者のスコア
	 */
	public void setScore(AuthorResultEntity score) {
		AuthorResultEntity entity = select(score.getAuthorId());
		if (entity == null) {
			// 登録
			insert(score);
		} else {
			// 更新
			update(score);
		}
	}

	/**
	 * <p>
	 * 指定した投稿者IDのタイトルフィルタのスコアを設定する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param title タイトルフィルタのスコア
	 * @return 投稿者のスコア
	 */
	public AuthorResultEntity setTitle(String authorId, int title) {

		AuthorResultEntity entity = select(authorId);
		if (entity == null) {
			// 登録
			entity = new AuthorResultEntity(authorId);
			entity.setTitleScore(title);
			insert(entity);
		} else {
			// 更新
			entity.setTitleScore(title);
			update(entity);
		}

		return entity;
	}

	/**
	 * <p>
	 * 指定した投稿者IDのコンテンツフィルタのスコアを設定する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param content コンテンツフィルタのスコア
	 * @return 投稿者のスコア
	 */
	public AuthorResultEntity setContent(String authorId, int content) {

		AuthorResultEntity entity = select(authorId);
		if (entity == null) {
			// 登録
			entity = new AuthorResultEntity(authorId);
			entity.setContentScore(content);
			insert(entity);
		} else {
			// 更新
			entity.setContentScore(content);
			update(entity);
		}

		return entity;
	}

	/**
	 * <p>
	 * 指定した投稿者IDの投稿間隔フィルタのスコアを設定する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param intervals 投稿間隔フィルタのスコア
	 * @return 投稿者のスコア
	 */
	public AuthorResultEntity setIntervals(String authorId, int intervals) {

		AuthorResultEntity entity = select(authorId);
		if (entity == null) {
			// 登録
			entity = new AuthorResultEntity(authorId);
			entity.setIntervalsScore(intervals);
			insert(entity);
		} else {
			// 更新
			entity.setIntervalsScore(intervals);
			update(entity);
		}

		return entity;
	}

	/**
	 * <p>
	 * 指定した投稿者IDのスコアを設定する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param score スコア
	 */
	public void setScore(String authorId, double score) {
		setScore(authorId, (int)score);
	}

	/**
	 * <p>
	 * 指定した投稿者IDのタイトルフィルタのスコアを設定する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param title タイトルフィルタのスコア
	 * @return 投稿者のスコア
	 */
	public AuthorResultEntity setTitle(String authorId, double title) {
		return setTitle(authorId, (int)title);
	}

	/**
	 * <p>
	 * 指定した投稿者IDのコンテンツフィルタのスコアを設定する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param content コンテンツフィルタのスコア
	 * @return 投稿者のスコア
	 */
	public AuthorResultEntity setContent(String authorId, double content) {
		return setContent(authorId, (int)content);
	}

	/**
	 * <p>
	 * 指定した投稿者IDの投稿間隔フィルタのスコアを設定する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param intervals 投稿間隔フィルタのスコア
	 * @return 投稿者のスコア
	 */
	public AuthorResultEntity setIntervals(String authorId, double intervals) {
		return setIntervals(authorId, (int)intervals);
	}

	/**
	 * <p>
	 * 指定した投稿者IDのスコアを取得する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @return スコア
	 */
	public AuthorResultEntity select(String authorId) {

		AuthorResultEntity entity = null;

		try {
			executor.preparedStatement(SQL_SELECT);
			executor.setString(1, authorId);
			executor.executeQuery();
			if (executor.next()) {
				entity = new AuthorResultEntity(authorId);
				entity.setScore(executor.getInt("score"));
				entity.setTitle(executor.getInteger("title"));
				entity.setContent(executor.getInteger("content"));
				entity.setIntervals(executor.getInteger("intervals"));
			}

			return 	entity;	

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 指定した投稿者IDのスコアを更新する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param score スコア
	 */
	public void updateScore(String authorId, int score) {
		try {
			executor.preparedStatement(SQL_UPDATE_SCORE);
			executor.setInt(1, score);
			executor.setString(2, authorId);
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 指定した投稿者IDのタイトルフィルタのスコアを更新する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param score スコア
	 * @param title タイトルフィルタのスコア
	 */
	public void updateTitle(String authorId, int score, int title) {
		try {
			executor.preparedStatement(SQL_UPDATE_TITLE);
			executor.setInt(1, score);
			executor.setInt(2, title);
			executor.setString(3, authorId);
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 指定した投稿者IDのコンテンツフィルタのスコアを更新する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param score スコア
	 * @param content コンテンツフィルタのスコア
	 */
	public void updateContent(String authorId, int score, int content) {
		try {
			executor.preparedStatement(SQL_UPDATE_CONTENT);
			executor.setInt(1, score);
			executor.setInt(2, content);
			executor.setString(3, authorId);
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 指定した投稿者IDの投稿間隔フィルタのスコアを更新する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param score スコア
	 * @param intervals 投稿間隔フィルタのスコア
	 */
	public void updateIntervals(String authorId, int score, int intervals) {
		try {
			executor.preparedStatement(SQL_UPDATE_INTERVALS);
			executor.setInt(1, score);
			executor.setInt(2, intervals);
			executor.setString(3, authorId);
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 指定した投稿者のスコアを更新する.
	 * </p>
	 * @param entity スコア
	 */
	public void update(AuthorResultEntity entity) {
		try {
			executor.preparedStatement(SQL_UPDATE);
			executor.setInt(1, entity.getScore());
			executor.setInteger(2, entity.getTitle());
			executor.setInteger(3, entity.getContent());
			executor.setInteger(4, entity.getIntervals());
			executor.setString(5, entity.getAuthorId());
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 指定した投稿者IDのスコアを登録する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param score スコア
	 */
	public void insertScore(String authorId, int score) {
		try {
			executor.preparedStatement(SQL_INSERT_SCORE);
			executor.setString(1, authorId);
			executor.setInt(2, score);
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 指定した投稿者IDのタイトルフィルタのスコアを登録する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param score スコア
	 */
	public void insertTitle(String authorId, int score) {
		try {
			executor.preparedStatement(SQL_INSERT_TITLE);
			executor.setString(1, authorId);
			executor.setInt(2, score);
			executor.setInt(3, score);
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 指定した投稿者IDのコンテンツフィルタのスコアを登録する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param score スコア
	 */
	public void insertContent(String authorId, int score) {
		try {
			executor.preparedStatement(SQL_INSERT_CONTENT);
			executor.setString(1, authorId);
			executor.setInt(2, score);
			executor.setInt(3, score);
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 指定した投稿者IDの投稿間隔フィルタのスコアを登録する.
	 * </p>
	 * @param authorId 投稿者ID
	 * @param score スコア
	 */
	public void insertIntervals(String authorId, int score) {
		try {
			executor.preparedStatement(SQL_INSERT_INTERVALS);
			executor.setString(1, authorId);
			executor.setInt(2, score);
			executor.setInt(3, score);
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 指定した投稿者のスコアを登録する.
	 * </p>
	 * @param entity スコア
	 */
	public void insert(AuthorResultEntity entity) {
		try {
			executor.preparedStatement(SQL_INSERT);
			executor.setString(1, entity.getAuthorId());
			executor.setInt(2, entity.getScore());
			executor.setInteger(3, entity.getTitle());
			executor.setInteger(4, entity.getContent());
			executor.setInteger(5, entity.getIntervals());
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}
}
