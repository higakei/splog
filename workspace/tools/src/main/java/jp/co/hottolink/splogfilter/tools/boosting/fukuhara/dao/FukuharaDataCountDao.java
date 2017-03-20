package jp.co.hottolink.splogfilter.tools.boosting.fukuhara.dao;

import java.sql.SQLException;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.DAOException;

/**
 * <p>
 * 福原さんの学習データの人間判定を集計するのDAOクラス.
 * </p>
 * @author higa
 */
public class FukuharaDataCountDao {

	/**
	 * <p>
	 * judgeテーブルを集計するSQL.
	 * </p>
	 */
	private static final String SQL_COUNT_JUDGE = "select aid, spam from judge";

	/**
	 * <p>
	 * judge2テーブルを集計するSQL.
	 * </p>
	 */
	private static final String SQL_COUNT_JUDGE2 = "select aid, spam from judge2";

	/**
	 * <p>
	 * スパム数をカウントアップする.
	 * </p>
	 */
	private static final String SQL_COUNT_UP_SPAM = "update fukuhara_data set spam_count = (spam_count + 1) where aid = ?";

	/**
	 * <p>
	 * ブログ数をカウントアップする.
	 * </p>
	 */
	private static final String SQL_COUNT_UP_BLOG = "update fukuhara_data set blog_count = (blog_count + 1) where aid = ?";

	/**
	 * <p>
	 * 読み込みのSQL実行クラス.
	 * </p>
	 */
	private SQLExecutor read = null;

	/**
	 * <p>
	 * 書き込みのSQL実行クラス.
	 * </p>
	 */
	private SQLExecutor write = null;


	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param read 読み込みのSQL実行クラス
	 * @param write 書き込みのSQL実行クラス
	 */
	public FukuharaDataCountDao(SQLExecutor read, SQLExecutor write) {
		this.read = read;
		this.write = write;
	}


	/**
	 * <p>
	 * judgeテーブルを集計する.
	 * </p>
	 */
	public void countJudge() {

		try {
			// judgeテーブルの参照
			read.executeQuery(SQL_COUNT_JUDGE);

			// レコードの取得
			while (read.next()) {
				// レコードデータの取得
				int aid = read.getInt("aid");
				int spam = read.getInt("spam");

				// 集計
				if ((spam == 4) || (spam == 5)) {
					write.preparedStatement(SQL_COUNT_UP_SPAM);
					write.setInt(1, aid);
					write.executeUpdate();
				} else if ((spam == 1) || (spam == 2)) {
					write.preparedStatement(SQL_COUNT_UP_BLOG);
					write.setInt(1, aid);
					write.executeUpdate();
				}
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			read.closeQuery();
			write.closeQuery();
		}
	}

	/**
	 * <p>
	 * judge2テーブルを集計する.
	 * </p>
	 */
	public void countJudge2() {

		try {
			// judgeテーブルの参照
			read.executeQuery(SQL_COUNT_JUDGE2);

			// レコードの取得
			while (read.next()) {
				// レコードデータの取得
				int aid = read.getInt("aid");
				int spam = read.getInt("spam");

				// 集計
				if ((spam == 4) || (spam == 5)) {
					write.preparedStatement(SQL_COUNT_UP_SPAM);
					write.setInt(1, aid);
					write.executeUpdate();
				} else if ((spam == 1) || (spam == 2)) {
					write.preparedStatement(SQL_COUNT_UP_BLOG);
					write.setInt(1, aid);
					write.executeUpdate();
				}
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			read.closeQuery();
			write.closeQuery();
		}
	}

	public static void main(String[] args) {

		SQLExecutor read = null;
		SQLExecutor write = null;

		try {
			read = new SQLExecutor("fukuhara");
			write = new SQLExecutor("fukuhara");
			FukuharaDataCountDao dao = new FukuharaDataCountDao(read, write);
			dao.countJudge();
			dao.countJudge2();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (read != null) read.finalize();
			if (write != null) write.finalize();
		}
	}
}
