package jp.co.hottolink.splogfilter.tools.bayes.dao;

import java.sql.SQLException;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.DAOException;
import jp.co.hottolink.splogfilter.tools.bayes.entity.MorphemeCountEntity;

/**
 * <p>
 * 形態素頻度テーブルのDAOクラス.
 * </p>
 * @author higa
 */
public class MorphemeCountDao {

	/**
	 * <p>
	 * 形態素の頻度を取得するSQL.
	 * </p>
	 */
	private static final String SQL_SELECT = "select id, surface, pos"
			+ ", splog_title_tf, splog_title_df, splog_content_tf, splog_content_df"
			+ ", blog_title_tf, blog_title_df, blog_content_tf, blog_content_df"
			+ " from morpheme_count where surface = ?";

	/**
	 * <p>
	 * 形態素の頻度を登録するSQL.
	 * </p>
	 */
	private static final String SQL_INSERT = "insert into morpheme_count (surface, pos"
			+ ", splog_title_tf, splog_title_df, splog_content_tf, splog_content_df"
			+ ", blog_title_tf, blog_title_df, blog_content_tf, blog_content_df"
			+ ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * <p>
	 * 形態素の頻度を更新するSQL.
	 * </p>
	 */
	private static final String SQL_UPDATE = "update morpheme_count set surface = ?, pos = ?"
			+ ", splog_title_tf = ?, splog_title_df = ?, splog_content_tf = ?, splog_content_df = ?"
			+ ", blog_title_tf = ?, blog_title_df = ?, blog_content_tf = ?, blog_content_df = ?"
			+ " where id = ?";

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
	public MorphemeCountDao(SQLExecutor executor) {
		this.executor = executor;
	}

	/**
	 * <p>
	 * 形態素の頻度を取得する.
	 * </p>
	 * @param surface 表層形
	 * @return 形態素の頻度
	 * @throws DAOException
	 */
	public MorphemeCountEntity select(String surface) throws DAOException {

		try {
			// SQLの実行
			executor.preparedStatement(SQL_SELECT);
			executor.setString(1, surface);
			executor.executeQuery();
			if (!executor.next()) {
				return null;
			}

			// データの取得
			MorphemeCountEntity entity = new MorphemeCountEntity();
			entity.setId(executor.getInt("id"));
			entity.setSurface(executor.getString("surface"));
			entity.setPos(executor.getString("pos"));
			entity.setSplogTitleTF(executor.getInt("splog_title_tf"));
			entity.setSplogTitleDF(executor.getInt("splog_title_df"));
			entity.setSplogContentTF(executor.getInt("splog_content_tf"));
			entity.setSplogContentDF(executor.getInt("splog_content_df"));
			entity.setBlogTitleTF(executor.getInt("blog_title_tf"));
			entity.setBlogTitleDF(executor.getInt("blog_title_df"));
			entity.setBlogContentTF(executor.getInt("blog_content_tf"));
			entity.setBlogContentDF(executor.getInt("blog_content_df"));

			return entity;

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 形態素の頻度を登録する.
	 * </p>
	 * @param entity 形態素頻度情報
	 */
	public void insert(MorphemeCountEntity entity) {
		String surface = entity.getSurface();
		String pos = entity.getPos();
		try {
			int index = 0;
			executor.preparedStatement(SQL_INSERT);
			executor.setString(++index, entity.getSurface());
			executor.setString(++index, entity.getPos());
			executor.setInt(++index, entity.getSplogTitleTF());
			executor.setInt(++index, entity.getSplogTitleDF());
			executor.setInt(++index, entity.getSplogContentTF());
			executor.setInt(++index, entity.getSplogContentDF());
			executor.setInt(++index, entity.getBlogTitleTF());
			executor.setInt(++index, entity.getBlogTitleDF());
			executor.setInt(++index, entity.getBlogContentTF());
			executor.setInt(++index, entity.getBlogContentDF());
			executor.executeUpdate();
		} catch (SQLException e) {
			System.out.println(surface.length() + ":" + surface);
			System.out.println(pos);
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 形態素の頻度を更新する.
	 * </p>
	 * @param entity 形態素頻度情報
	 */
	public void update(MorphemeCountEntity entity) {
		try {
			int index = 0;
			executor.preparedStatement(SQL_UPDATE);
			executor.setString(++index, entity.getSurface());
			executor.setString(++index, entity.getPos());
			executor.setInt(++index, entity.getSplogTitleTF());
			executor.setInt(++index, entity.getSplogTitleDF());
			executor.setInt(++index, entity.getSplogContentTF());
			executor.setInt(++index, entity.getSplogContentDF());
			executor.setInt(++index, entity.getBlogTitleTF());
			executor.setInt(++index, entity.getBlogTitleDF());
			executor.setInt(++index, entity.getBlogContentTF());
			executor.setInt(++index, entity.getBlogContentDF());
			executor.setInt(++index, entity.getId());
			executor.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}
}
