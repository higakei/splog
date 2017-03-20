package jp.co.hottolink.splogfilter.tools.boosting.fukuhara.dao;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.xml.sax.InputSource;

import jp.co.hottolink.splogfilter.common.api.buzz.DocumentByAuthorIdAPI;
import jp.co.hottolink.splogfilter.common.api.buzz.parser.DocumentAPIParser;
import jp.co.hottolink.splogfilter.common.api.buzz.util.BuzzAuthorIdGenerator;
import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.APIException;
import jp.co.hottolink.splogfilter.common.exception.DAOException;
import jp.co.hottolink.splogfilter.common.exception.ParseException;

/**
 * <p>
 * 著者IDから福原さんの学習データの本文を取得するDAOクラス.
 * </p>
 * @author higa
 */
public class FukuharaDataAuthorIdDao {

	/**
	 * <p>
	 * 学習データを取得するSQL.
	 * </p>
	 */
	private static final String SQL_SELECT_ALL = "select aid, url, author_id from fukuhara_data";

	/**
	 * <p>
	 * 著者IDがある学習データを取得するSQL.
	 * </p>
	 */
	private static final String SQL_SELECT_HAVING_AUTHOR_ID = "select aid, url, author_id, document_id, body from fukuhara_data where author_id is not null";

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
	public FukuharaDataAuthorIdDao(SQLExecutor executor) {
		this.executor = executor;
	}

	/**
	 * <p>
	 * 学習データのURLから本文を取得する.
	 * </p>
	 * @throws DAOException
	 * @throws ParseException 
	 * @throws APIException 
	 */
	public void getBody() throws DAOException, ParseException, APIException {
		try {
			// SQLの実行
			executor.preparedStatement(SQL_SELECT_HAVING_AUTHOR_ID, ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_UPDATABLE);
			executor.executeQuery();

			// レコードの取得
			while (executor.next()) {
				// データの取得
				String url = executor.getString("url");
				String authorId = executor.getString("author_id");
				if (authorId == null) {
					continue;
				}

				// APIから文書を取得
				Map<String, String> document = getDocument(authorId, url);
				if (document == null) {
					continue;
				}

				// レコードの更新
				executor.updateString("document_id", document.get("documentId"));
				executor.updateString("body", document.get("body"));
				executor.updateRow();
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * 学習データのURLから著者IDを設定する.
	 * </p>
	 * @throws DAOException
	 * @throws URISyntaxException 
	 */
	public void setAuthorId() throws DAOException, URISyntaxException {

		try {
			// SQLの実行
			executor.preparedStatement(SQL_SELECT_ALL, ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_UPDATABLE);
			executor.executeQuery();

			// レコードの取得
			while (executor.next()) {
				// URLの取得
				String url = executor.getString("url");

				// 著者IDの生成
				String authorId = BuzzAuthorIdGenerator.generate(url);
				if (authorId == null) {
					continue;
				}

				// レコードの更新
				executor.updateString("author_id", authorId);
				executor.updateRow();
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			executor.closeQuery();
		}
	}

	/**
	 * <p>
	 * buzzapiから文書を取得する.
	 * </p>
	 * @param authorId 著者ID
	 * @param url URL
	 * @return 本文
	 * @throws IOException
	 * @throws ParseException
	 */
	private Map<String, String> getDocument(String authorId, String url) throws APIException, ParseException {

		// buzzapiの呼び出し
		DocumentByAuthorIdAPI api = new DocumentByAuthorIdAPI();
		api.setSnippetLength(0);
		String xml = api.call(authorId);
		StringReader reader = new StringReader(xml);
		InputSource source = new InputSource(reader);

		// XMLの解析
		DocumentAPIParser parser = new DocumentAPIParser();
		parser.parse(source);
		if (parser.isError()) {
			System.out.println(parser.getMessage());
			return null;
		}

		// 著者の文書の取得
		List<Map<String, String>> documents = parser.getDocuments();
		for (Map<String, String> document: documents) {
			// URLが一致した場合
			if (url.equals(document.get("url"))) {
				System.out.println(url);
				return document;
			}
		}

		return null;
	}

	public static void main(String[] args) {

		SQLExecutor executor = null;

		try {
			executor = new SQLExecutor("fukuhara");
			FukuharaDataAuthorIdDao dao = new FukuharaDataAuthorIdDao(executor);
			//dao.setAuthorId();
			dao.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (executor != null) executor.finalize();
		}
	}
}
