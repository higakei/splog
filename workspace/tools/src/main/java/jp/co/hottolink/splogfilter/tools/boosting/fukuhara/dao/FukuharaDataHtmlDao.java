package jp.co.hottolink.splogfilter.tools.boosting.fukuhara.dao;

import java.sql.SQLException;

import org.htmlparser.util.ParserException;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.DAOException;
import jp.co.hottolink.splogfilter.tools.boosting.fukuhara.parser.BlogHtmlParser;

/**
 * <p>
 * HTMLから福原さんの学習データの本文を取得するのDAOクラス.
 * </p>
 * @author higa
 */
public class FukuharaDataHtmlDao {

	/**
	 * <p>
	 * HTMソースを取得するSQL.
	 * </p>
	 */
	private static final String SQL_SELECT_SOURCE = "select aid, source from fukuhara_data join html using(aid) where url = ?";

	/**
	 * <p>
	 * 本文を設定するSQL.
	 * </p>
	 */
	private static final String SQL_UPDATE_BODY = "update fukuhara_data set body = ? where aid = ?";

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
	public FukuharaDataHtmlDao(SQLExecutor executor) {
		this.executor = executor;
	}

	/**
	 * <p>
	 * URLの本文を取得する.
	 * </p>
	 * @param url URL
	 * @throws DAOException
	 * @throws ParserException
	 */
	public void getBody(String url) throws DAOException, ParserException {

		try {
			// SQLの実行
			executor.preparedStatement(SQL_SELECT_SOURCE);
			executor.setString(1, url);
			executor.executeQuery();
			if (!executor.next()) {
				return;
			}

			// レコードデータの取得
			int aid = executor.getInt("aid");
			String source = executor.getString("source");
			//System.out.println(source);

			// HTMLから本文を取得
			BlogHtmlParser parser = new BlogHtmlParser(source);
			//BlogHtmlParser parser = new BlogHtmlParser(new URL(url));
			String body = parser.getBody(url);
			//System.out.println(body);

			// 本文の設定
			executor.preparedStatement(SQL_UPDATE_BODY);
			executor.setString(1, body);
			executor.setInt(2, aid);
			executor.executeUpdate();

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	public static void main(String[] args) {

		SQLExecutor executor = null;

		try {
			executor = new SQLExecutor("fukuhara");
			FukuharaDataHtmlDao dao = new FukuharaDataHtmlDao(executor);
			dao.getBody(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (executor != null) executor.finalize();
		}
	}
}
