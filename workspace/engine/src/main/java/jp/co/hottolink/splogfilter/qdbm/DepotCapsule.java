package jp.co.hottolink.splogfilter.qdbm;

import jp.co.hottolink.splogfilter.qdbm.exception.QDBMException;
import qdbm.Depot;
import qdbm.DepotException;
import qdbm.Util;

/**
 * <p>
 * Depotのカプセルクラス.
 * </p>
 * @author higa
 */
public class DepotCapsule {

	
	/**
	 * <p>
	 * データーベースファイル名.
	 * </p>
	 */
	private String name = null;

	/**
	 * <p>
	 * DBM.
	 * </p>
	 */
	private Depot dbm = null;
	
	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param name データーベースファイル名
	 */
	public DepotCapsule(String name) {
		this.name = name;
	}

	/**
	 * <p>
	 * DBMをオープンする.
	 * </p>
	 * @throws QDBMException
	 */
	public void open() throws QDBMException {
		try {
			dbm = new Depot(name);
			dbm.silent = true;
		} catch (DepotException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * DBMをオープンする.
	 * </p>
	 * @param omode 接続モード
	 * @param bnum バケット配列の要素数
	 * @throws QDBMException
	 */
	public void open(int omode, int bnum) throws QDBMException {
		try {
			dbm = new Depot(name, omode, bnum);
			dbm.silent = true;
		} catch (DepotException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * DBMをクローズする.
	 * </p>
	 * @throws QDBMException
	 * @see qdbm.Depot#close()
	 */
	public void close() throws QDBMException {
		try {
			if (dbm != null) 	{
				dbm.close();
				dbm = null;
			}
		} catch (DepotException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * DBMを削除する.
	 * </p>
	 * @throws QDBMException
	 * @see qdbm.Depot#remove(String)
	 */
	public void remove() throws QDBMException {
		try {
			try { close(); } catch (QDBMException e) {}
			if (name != null) Depot.remove(name);
		} catch (DepotException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * レコードを追加する.
	 * </p>
	 * @param key キー
	 * @param value 値
	 * @return always true. However, if the silent flag is true and replace is cancelled, false is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Depot#put(byte[], byte[])
	 */
	public boolean put(byte[] key, byte[] value) throws QDBMException {
		try {
			return dbm.put(key, value);
		} catch (DepotException e) {
			throw new QDBMException(e);
		} finally {
		}
	}

	/**
	 * <p>
	 * レコードを追加する.
	 * </p>
	 * @param key キー
	 * @param value 値
	 * @param dmode 書き込みモード
	 * @return always true. However, if the silent flag is true and replace is cancelled, false is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Depot#put(byte[], byte[], int)
	 */
	public boolean put(byte[] key, byte[] value, int dmode) throws QDBMException {
		try {
			return dbm.put(key, value, dmode);
		} catch (DepotException e) {
			throw new QDBMException(e);
		}		
	}

	/**
	 * <p>
	 * レコードを追加する.
	 * </p>
	 * @param key キー
	 * @param value 値
	 * @return always true. However, if the silent flag is true and replace is cancelled, false is returned instead of exception.
	 * @throws QDBMException
	 */
	public boolean put(long key, Object value) throws QDBMException {
		return put(key, value, Depot.DOVER);
	}

	/**
	 * <p>
	 * レコードを追加する.
	 * </p>
	 * @param key キー
	 * @param value 値
	 * @param dmode 書き込みモード
	 * @return always true. However, if the silent flag is true and replace is cancelled, false is returned instead of exception.
	 * @throws QDBMException
	 */
	public boolean put(long key, Object value, int dmode) throws QDBMException {
		byte[] bKey = Util.serializeLong(key);
		byte[] bValue = Util.serialize(value);
		return put(bKey, bValue, dmode);
	}

	/**
	 * <p>
	 * レコードを追加する.
	 * </p>
	 * @param key キー
	 * @param value 値
	 * @return always true. However, if the silent flag is true and replace is cancelled, false is returned instead of exception.
	 * @throws QDBMException
	 */
	public boolean put(long key, String value) throws QDBMException {
		return put(key, value, Depot.DOVER);
	}

	/**
	 * <p>
	 * レコードを追加する.
	 * </p>
	 * @param key キー
	 * @param value 値
	 * @param dmode 書き込みモード
	 * @return always true. However, if the silent flag is true and replace is cancelled, false is returned instead of exception.
	 * @throws QDBMException
	 */
	public boolean put(long key, String value, int dmode) throws QDBMException {
		byte[] bKey = Util.serializeLong(key);
		byte[] bValue = value.getBytes();
		return put(bKey, bValue, dmode);
	}

	/**
	 * <p>
	 * レコードを追加する.
	 * </p>
	 * @param key キー
	 * @param value 値
	 * @return always true. However, if the silent flag is true and replace is cancelled, false is returned instead of exception.
	 * @throws QDBMException
	 */
	public boolean put(Object key, Object value) throws QDBMException {
		return put(key, value, Depot.DOVER);
	}

	/**
	 * <p>
	 * レコードを追加する.
	 * </p>
	 * @param key キー
	 * @param value 値
	 * @return always true. However, if the silent flag is true and replace is cancelled, false is returned instead of exception.
	 * @param dmode 書き込みモード
	 * @throws QDBMException
	 */
	public boolean put(Object key, Object value, int dmode) throws QDBMException {
		byte[] bKey = Util.serialize(key);
		byte[] bValue = Util.serialize(value);
		return put(bKey, bValue, dmode);
	}

	/**
	 * <p>
	 * イテレータを初期化する.
	 * </p>
	 * @throws QDBMException
	 * @see qdbm.Depot#iterinit()
	 */
	public void iterinit() throws QDBMException {
		try {
			dbm.iterinit();
		} catch (DepotException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * イテレータから次のレコードのキーを取り出す.
	 * </p>
	 * @return a byte array of the key of the next record. If the silent flag is true and no record corresponds, null is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Depot#iternext()
	 */
	public byte[] iternext() throws QDBMException {
		try {
			return dbm.iternext();
		} catch (DepotException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * レコードを取得する.
	 * </p>
	 * @param key キー
	 * @return a byte array of the value of the corresponding record. If the silent flag is true and no record corresponds, null is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Depot#fetch(byte[])
	 */
	public byte[] fetch(byte[] key) throws QDBMException {
		try {
			return dbm.fetch(key);
		} catch (DepotException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * レコードを取得する.
	 * </p>
	 * @param key キー
	 * @return a serializable object of the value of the corresponding record. If the silent flag is true and no record corresponds, null is returned instead of exception.
	 * @throws QDBMException
	 */
	public String fetchString(byte[] key) throws QDBMException {
		byte[] value = fetch(key);
		if (value == null) {
			return null;
		} else {
			return new String(value);
		}
	}
	/**
	 * <p>
	 * レコードを取得する.
	 * </p>
	 * @param key キー
	 * @return a serializable object of the value of the corresponding record. If the silent flag is true and no record corresponds, null is returned instead of exception.
	 * @throws QDBMException
	 */
	public Object fetchObject(byte[] key) throws QDBMException {
		byte[] value = fetch(key);
		if (value == null) {
			return null;
		} else {
			return Util.deserialize(value);
		}
	}
	
	/**
	 * <p>
	 * レコードを取得する.
	 * </p>
	 * @param key キー
	 * @return a byte array of the value of the corresponding record. If the silent flag is true and no record corresponds, null is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Depot#get(byte[])
	 */
	public byte[] get(byte[] key) throws QDBMException {
		try {
			return dbm.get(key);
		} catch (DepotException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * レコードを取得する.
	 * </p>
	 * @param key キー
	 * @return a byte array of the value of the corresponding record. If the silent flag is true and no record corresponds, null is returned instead of exception.
	 * @throws QDBMException
	 */
	public byte[] get(long key) throws QDBMException {
		byte[] bKey = Util.serializeLong(key);
		return get(bKey);
	}

	/**
	 * <p>
	 * レコードを取得する.
	 * </p>
	 * @param key キー
	 * @return a serializable object of the value of the corresponding record. If the silent flag is true and no record corresponds, null is returned instead of exception.
	 * @throws QDBMException
	 */
	public Object getObject(byte[] key) throws QDBMException {
		byte[] value = get(key);
		if (value == null) {
			return null;
		} else {
			return Util.deserialize(value);
		}
	}

	/**
	 * <p>
	 * レコードを取得する.
	 * </p>
	 * @param key キー
	 * @return a serializable object of the value of the corresponding record. If the silent flag is true and no record corresponds, null is returned instead of exception.
	 * @throws QDBMException
	 */
	public Object getObject(long key) throws QDBMException {
		byte[] bKey = Util.serializeLong(key);
		return getObject(bKey);
	}

	/**
	 * <p>
	 * レコードを取得する.
	 * </p>
	 * @param key キー
	 * @return a serializable object of the value of the corresponding record. If the silent flag is true and no record corresponds, null is returned instead of exception.
	 * @throws QDBMException
	 */
	public Object getObject(Object key) throws QDBMException {
		byte[] bKey = Util.serialize(key);
		return getObject(bKey);
	}

	/**
	 * <p>
	 * サイレントモードを取得する.
	 * </p>
	 * @return サイレントモード
	 */
	public boolean getSilent() {
		return dbm.silent;
	}

	/**
	 * <p>
	 * サイレントモードを設定する.
	 * </p>
	 * @param silent サイレントモード
	 */
	public void setSilent(boolean silent) {
		dbm.silent = silent;
	}
	
	/**
	 * <p>
	 * データベースを更新した内容をファイルとデバイスに同期させる.
	 * </p>
	 * @throws QDBMException
	 * @see qdbm.Depot#sync()
	 */
	public void sync() throws QDBMException {
		try {
			dbm.sync();
		} catch (DepotException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * データベースのレコード数を得る.
	 * </p>
	 * @return レコード数
	 * @throws QDBMException
	 * @see qdbm.Depot#rnum()
	 */
	public int rnum() throws QDBMException {
		try {
			return dbm.rnum();
		} catch (DepotException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * レコードの値のサイズを取得する.
	 * </p>
	 * @param key キー
	 * @return the size of the value of the corresponding record. If the silent flag is true and no record corresponds, -1 is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Depot#vsiz(byte[])
	 */
	public int vsiz(byte[] key) throws QDBMException {
		try {
			return dbm.vsiz(key);
		} catch (DepotException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * レコードの値のサイズを取得する.
	 * </p>
	 * @param key キー
	 * @return the size of the value of the corresponding record. If the silent flag is true and no record corresponds, -1 is returned instead of exception.
	 * @throws QDBMException
	 */
	public int vsiz(long key) throws QDBMException {
		byte[] bKey = Util.serializeLong(key);
		return vsiz(bKey);
	}

	/**
	 * <p>
	 * レコードを削除する.
	 * </p>
	 * @param key キー
	 * @return always true. However, if the silent flag is true and no record corresponds, false is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Depot#out(byte[])
	 */
	public boolean out(byte[] key) throws QDBMException {
		try {
			return dbm.out(key);
		} catch (DepotException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * レコードを削除する.
	 * </p>
	 * @param key キー
	 * @return always true. However, if the silent flag is true and no record corresponds, false is returned instead of exception.
	 * @throws QDBMException
	 */
	public boolean out(long key) throws QDBMException {
		byte[] bKey = Util.serializeLong(key);
		return out(bKey);
	}
}
