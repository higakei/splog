package jp.co.hottolink.splogfilter.common.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

/**
 * <p>
 * SQL実行クラス.
 * </p>
 * @author higa
 */
public class SQLExecutor {

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(SQLExecutor.class);

	/**
	 * <p>
	 * Connection.
	 * </p>
	 */
	private Connection conn = null;

	/**
	 * <p>
	 * Statement.
	 * </p>
	 */
	private Statement stmt = null;

	/**
	 * <p>
	 * ResultSet.
	 * </p>
	 */
	private ResultSet rs = null;

	/**
	 * <p>
	 * コネクション作成フラグ.
	 * </p>
	 */
	private boolean isCreate = false;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param resource DBリソース
	 */
	public SQLExecutor(String resource) {
		conn = DBConfig.getConnection(resource);
		isCreate = true;
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param conn Connection
	 */
	public SQLExecutor(Connection conn) {
		this.conn = conn;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	public void finalize() {
		closeQuery();
		if (isCreate) close(conn);
	}

	/**
	 * <p>
	 * コネクション作成フラグを設定する.
	 * </p>
	 * @param isCreate コネクション作成フラグ
	 */
	public void setCreate(boolean isCreate) {
		this.isCreate = isCreate;
	}

	/**
	 * <p>
	 * PreparedStatementを取得する.
	 * </p>
	 * @param sql SQL
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement preparedStatement(String sql) throws SQLException {
		close(stmt);
		stmt = conn.prepareStatement(sql);
		return (PreparedStatement)stmt;
	}

	/**
	 * <p>
	 * PreparedStatementを取得する.
	 * </p>
	 * @param sql SQL
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement preparedStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		close(stmt);
		stmt = conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
		return (PreparedStatement)stmt;
	}

	/**
	 * <p>
	 * PreparedStatementに値を設定する.
	 * </p>
	 * @param index インデックス
	 * @param value 値
	 * @throws SQLException
	 */
	public void setInt(int index, int value) throws SQLException {
		((PreparedStatement)stmt).setInt(index, value);
	}

	/**
	 * <p>
	 * PreparedStatementに値を設定する.
	 * </p>
	 * @param index インデックス
	 * @param value 値
	 * @throws SQLException
	 */
	public void setLong(int index, long value) throws SQLException {
		((PreparedStatement)stmt).setLong(index, value);
	}

	/**
	 * <p>
	 * PreparedStatementに値を設定する.
	 * </p>
	 * @param index インデックス
	 * @param value 値
	 * @throws SQLException
	 */
	public void setString(int index, String value) throws SQLException {
		((PreparedStatement)stmt).setString(index, value);
	}

	/**
	 * <p>
	 * PreparedStatementに値を設定する.
	 * </p>
	 * @param index インデックス
	 * @param value 値
	 * @throws SQLException
	 */
	public void setObject(int index, Object value) throws SQLException {
		((PreparedStatement)stmt).setObject(index, value);
	}

	/**
	 * <p>
	 * PreparedStatementに値を設定する.
	 * </p>
	 * @param index インデックス
	 * @param value 値
	 * @throws SQLException
	 */
	public void setTimestamp(int index, Timestamp value) throws SQLException {
		((PreparedStatement)stmt).setTimestamp(index, value);
	}

	/**
	 * <p>
	 * PreparedStatementに値を設定する.
	 * </p>
	 * @param index インデックス
	 * @param value 値
	 * @throws SQLException
	 */
	public void setDouble(int index, Double value) throws SQLException {
		((PreparedStatement)stmt).setDouble(index, value);
	}

	/**
	 * <p>
	 * PreparedStatementに値を設定する.
	 * </p>
	 * @param index インデックス
	 * @param value 値
	 * @throws SQLException
	 */
	public void setDate(int index, Date value) throws SQLException {
		((PreparedStatement)stmt).setDate(index, value);
	}

	/**
	 * <p>
	 * PreparedStatementに値を設定する.
	 * </p>
	 * @param index インデックス
	 * @param value 値
	 * @throws SQLException
	 */
	public void setDate(int index, java.util.Date value) throws SQLException {
		java.sql.Date date = new java.sql.Date(value.getTime());
		setDate(index, date);
	}

	/**
	 * <p>
	 * PreparedStatementに値を設定する.
	 * </p>
	 * @param index インデックス
	 * @param value 値
	 * @throws SQLException
	 */
	public void setInteger(int index, Integer value) throws SQLException {
		if (value == null) {
			setString(index, null);
		} else {
			setInt(index, value);
		}
	}

	/**
	 * <p>
	 * PreparedStatementに値を設定する.
	 * </p>
	 * @param index インデックス
	 * @param value 値
	 * @throws SQLException
	 * @throws IOException
	 */
	public void setBinaryStream(int index, Object value) throws SQLException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(value);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		((PreparedStatement)stmt).setBinaryStream(index, bais);
	}
	
	/**
	 * <p>
	 * SQLを実行する(PreparedStatement).
	 * </p>
	 * @throws SQLException
	 */
	public ResultSet executeQuery() throws SQLException {
		close(rs);
		rs = ((PreparedStatement)stmt).executeQuery();
		return rs;
	}

	/**
	 * <p>
	 * SQLを実行する(PreparedStatement).
	 * </p>
	 * @return (1) SQL データ操作言語 (DML) 文の場合は行数、(2) 何も返さない SQL 文の場合は 0
	 * @throws SQLException
	 */
	public int executeUpdate() throws SQLException {
		return ((PreparedStatement)stmt).executeUpdate();
	}

	/**
	 * <p>
	 * SQLを実行する(Statement).
	 * </p>
	 * @param sql SQL
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		closeQuery();
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);
		return rs;
	}

	/**
	 * <p>
	 * SQLを実行する(Statement).
	 * </p>
	 * @param sql SQL
	 * @return (1) SQL データ操作言語 (DML) 文の場合は行数、(2) 何も返さない SQL 文の場合は 0
	 * @throws SQLException
	 */
	public int executeUpdate(String sql) throws SQLException {
		closeQuery();
		stmt = conn.createStatement();
		return stmt.executeUpdate(sql);
	}

	/**
	 * <p>
	 * クエリを閉じる.
	 * </p>
	 */
	public void closeQuery() {
		close(rs);
		close(stmt);
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnLabel カラム名
	 * @return 値
	 * @throws SQLException
	 */
	public int getInt(String columnLabel) throws SQLException {
		return rs.getInt(columnLabel);
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnIndex カラムインデックス
	 * @return 値
	 * @throws SQLException
	 */
	public int getInt(int columnIndex) throws SQLException {
		return rs.getInt(columnIndex);
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnLabel カラム名
	 * @return 値
	 * @throws SQLException
	 */
	public long getLong(String columnLabel) throws SQLException {
		return rs.getLong(columnLabel);
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnLabel カラム名
	 * @return 値
	 * @throws SQLException
	 */
	public String getString(String columnLabel) throws SQLException {
		return rs.getString(columnLabel);
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnIndex カラムインデックス
	 * @return 値
	 * @throws SQLException
	 */
	public String getString(int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnLabel カラム名
	 * @return 値
	 * @throws SQLException
	 */
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return rs.getTimestamp(columnLabel);
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnLabel カラム名
	 * @return 値
	 * @throws SQLException
	 */
	public Boolean getBoolean(String columnLabel) throws SQLException {
		return rs.getBoolean(columnLabel);
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnIndex カラムインデックス
	 * @return 値
	 * @throws SQLException
	 */
	public Object getObject(int columnIndex) throws SQLException {
		return rs.getObject(columnIndex);
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnLabel カラム名
	 * @return 値
	 * @throws SQLException
	 */
	public byte[] getBytes(String columnLabel) throws SQLException {
		return rs.getBytes(columnLabel);
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnLabel カラム名
	 * @return 値
	 * @throws SQLException
	 */
	public Double getDouble(String columnLabel) throws SQLException {
		return rs.getDouble(columnLabel);
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnLabel カラム名
	 * @return 値
	 * @throws SQLException
	 */
	public Date getDate(String columnLabel) throws SQLException {
		return rs.getDate(columnLabel);
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnLabel カラム名
	 * @return 値
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object getBinaryStream(String columnLabel) throws SQLException, IOException, ClassNotFoundException {

		InputStream is = rs.getBinaryStream(columnLabel);
		if (is == null) {
			return null;
		}

		ObjectInputStream ois = new ObjectInputStream(is);
		return ois.readObject();
	}

	/**
	 * <p>
	 * ResultSetからカラムの値を取得する.
	 * </p>
	 * @param columnLabel カラム名
	 * @return 値
	 * @throws SQLException
	 */
	public Integer getInteger(String columnLabel) throws SQLException {
		int integer = rs.getInt(columnLabel);
		if (rs.wasNull()) {
			return null;
		} else {
			return integer;
		}
	}

	/**
	 * <p>
	 * ResultSetのカーソルを次の行に移動する.
	 * </p>
	 * @return true:新しい現在の行が有効な場合, false:それ以上行がない場合
	 * @throws SQLException
	 */
	public boolean next() throws SQLException {
		return rs.next();
	}

	/**
	 * <p>
	 *  ResultSetのカラムの値を更新する.
	 * </p>
	 * @param columnLabel カラム名
	 * @param value 値
	 * @throws SQLException
	 */
	public void updateInt(String columnLabel, int value) throws SQLException {
		rs.updateInt(columnLabel, value);
	}

	/**
	 * <p>
	 *  ResultSetのカラムの値を更新する.
	 * </p>
	 * @param columnLabel カラム名
	 * @param value 値
	 * @throws SQLException
	 */
	public void updateLong(String columnLabel, long value) throws SQLException {
		rs.updateLong(columnLabel, value);
	}

	/**
	 * <p>
	 *  ResultSetのカラムの値を更新する.
	 * </p>
	 * @param columnLabel カラム名
	 * @param value 値
	 * @throws SQLException
	 */
	public void updateString(String columnLabel, String value) throws SQLException {
		rs.updateString(columnLabel, value);
	}

	/**
	 * <p>
	 * ResultSetの現在の行を更新する.
	 * </p>
	 * @throws SQLException
	 */
	public void updateRow() throws SQLException {
		rs.updateRow();
	}

	/**
	 * <p>
	 * 自動コミットモードを設定する.
	 * </p>
	 * @param autoCommit true:有効, false:無効
	 * @throws SQLException
	 */
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		conn.setAutoCommit(autoCommit);
	}

	/**
	 * <p>
	 * 自動コミットモードを取得する.
	 * </p>
	 * @return 自動コミットモード
	 * @throws SQLException
	 */
	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
	}

	/**
	 * <p>
	 * コミットする.
	 * </p>
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		conn.commit();
	}

	/**
	 * <p>
	 * ロールバックする.
	 * </p>
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		conn.rollback();
	}

	/**
	 * <p>
	 * ResultSetのカラム数を取得する.
	 * </p>
	 * @return カラム数
	 * @throws SQLException
	 */
	public int getColumnCount() throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		return rsmd.getColumnCount();
	}

	/**
	 * <p>
	 * ResultSetからカラム名を取得します。
	 * </p>
	 * @param columnIndex カラムインデックス
	 * @return カラム名
	 * @throws SQLException
	 */
	public String getColumnLabel(int columnIndex) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		return rsmd.getColumnLabel(columnIndex);
	}

	/**
	 * <p>
	 * Connectionをクローズする.
	 * </p>
	 * @param conn Connection
	 */
	private void close(Connection conn) {
		try {
			if (!isClosed(conn)) conn.close();
			conn = null;
		} catch (Exception e) {
			logger.warn("Connectionのクローズに失敗しました。", e);
		}
	}

	/**
	 * <p>
	 * Statementをクローズする.
	 * </p>
	 * @param stmt Statement
	 */
	private void close(Statement stmt) {
		try {
			if (!isClosed(stmt)) stmt.close();
			stmt = null;
		} catch (Exception e) {
			logger.warn("Statementのクローズに失敗しました。", e);
		}
	}

	/**
	 * <p>
	 * ResultSetをクローズする.
	 * </p>
	 * @param stmt ResultSet
	 * @throws SQLException
	 */
	private void close(ResultSet rs) {
		try {
			if (!isClosed(rs)) rs.close();
			rs = null;
		} catch (Exception e) {
			logger.warn("ResultSeのクローズに失敗しました。", e);
		}
	}

	/**
	 * <p>
	 * Connectionのクローズ状態を判定する.
	 * </p>
	 * @param conn Connection
	 * @return true:クローズ済、false:未クローズ
	 * @throws SQLException
	 */
	private boolean isClosed(Connection conn) throws SQLException {
		return ((conn == null) || conn.isClosed());
	}

	/**
	 * <p>
	 * Statementのクローズ状態を判定する.
	 * </p>
	 * @param stmt Statement
	 * @return true:クローズ済、false:未クローズ
	 * @throws SQLException
	 */
	private boolean isClosed(Statement stmt) throws SQLException {
		return (stmt == null);
	}

	/**
	 * <p>
	 * ResultSetのクローズ状態を判定する.
	 * </p>
	 * @param rs ResultSet
	 * @return true:クローズ済、false:未クローズ
	 * @throws SQLException
	 */
	private boolean isClosed(ResultSet rs) throws SQLException {
		return (rs == null);
	}
}
