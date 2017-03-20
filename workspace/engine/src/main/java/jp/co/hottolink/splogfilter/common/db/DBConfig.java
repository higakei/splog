package jp.co.hottolink.splogfilter.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

import jp.co.hottolink.splogfilter.common.exception.DBConnectionException;
import jp.co.hottolink.splogfilter.common.resource.ResourceBundleUtil;

/**
 * <p>
 * データベースの設定クラス.
 * </p>
 * @author higa
 */
public class DBConfig {

	/**
	 * <p>
	 * プロパティ名：サーバ名.
	 * </p>
	 */
	private static final String KEY_SERVER_NAME = "SERVER_NAME";

	/**
	 * <p>
	 * プロパティ名：ポート番号.
	 * </p>
	 */
	private static final String KEY_PORT = "PORT";

	/**
	 * <p>
	 * プロパティ名：データベース名.
	 * </p>
	 */
	private static final String KEY_DB_NAME = "DB_NAME";

	/**
	 * <p>
	 * プロパティ名：ユーザー名.
	 * </p>
	 */
	private static final String KEY_USER_NAME = "USER_NAME";

	/**
	 * <p>
	 * プロパティ名：パスワード.
	 * </p>
	 */
	private static final String KEY_PASSWORD = "PASSWORD";

	/**
	 * <p>
	 * プロパティ名：文字コード.
	 * </p>
	 */
	private static final String KEY_CHARACTER_ENCODING = "CHARACTER_ENCODING";

	/**
	 * <p>
	 * JDBCドライバ.
	 * </p>
	 */
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	
	/**
	 * <p>
	 * サーバ名.
	 * </p>
	 */
	private String server = null;

	/**
	 * <p>
	 * ポート番号.
	 * </p>
	 */
	private String port = null;

	/**
	 * <p>
	 * データベース名.
	 * </p>
	 */
	private String db = null;

	/**
	 * <p>
	 * ユーザー名.
	 * </p>
	 */
	private String user = null;
	
	/**
	 * <p>
	 * パスワード.
	 * </p>
	 */
	private String password = null;

	/**
	 * <p>
	 * JDBCドライバ.
	 * </p>
	 */
	private String characterEncoding = null;
	
	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param resource DBリソース
	 */
	public DBConfig(String resource) {
		ResourceBundle properties = ResourceBundleUtil.getBundle(resource);
		server = properties.getString(KEY_SERVER_NAME);
		port = properties.getString(KEY_PORT);
		db = properties.getString(KEY_DB_NAME);
		user = properties.getString(KEY_USER_NAME);
		password = properties.getString(KEY_PASSWORD);
		characterEncoding = properties.getString(KEY_CHARACTER_ENCODING);
	}

	/**
	 * <p>
	 * Connectionを取得する.
	 * </p>
	 * @param resource DBリソース
	 * @return Connection
	 */
	public static Connection getConnection(String resource) {
		DBConfig config = new DBConfig(resource);
		return config.getConnection();
	}

	/**
	 * <p>
	 * JDBCのURLを取得する.
	 * </p>
	 * @return URL
	 */
	public String getJdbcUrl() {
		StringBuffer url = new StringBuffer("jdbc:mysql://");
		url.append(server);
		url.append(":");
		url.append(port);
		url.append("/");
		url.append(db);
		url.append("?user=");
		url.append(user);
		url.append("&password=");
		url.append(password);
		url.append("&useUnicode=true");
		url.append("&characterEncoding=");
		url.append(characterEncoding);
		url.append("&zeroDateTimeBehavior=convertToNull");
		return url.toString();
	}

	/**
	 * <p>
	 * Connectionを取得する.
	 * </p>
	 * @return Connection
	 */
	public Connection getConnection() {
		try {
			Class.forName(JDBC_DRIVER);
			String url = getJdbcUrl();
			return DriverManager.getConnection(url);
		} catch (Exception e) {
			throw new DBConnectionException(e);
		}
	}
}
