package jp.co.hottolink.splogfilter.qdbm;

import jp.co.hottolink.splogfilter.qdbm.exception.QDBMException;
import qdbm.Villa;
import qdbm.VillaException;
import qdbm.Util;

/**
 * <p>
 * Villaのカプセルクラス.
 * </p>
 * @author higa
 */
public class VillaCapsule {

	
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
	private Villa dbm = null;
	
	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param name データーベースファイル名
	 */
	public VillaCapsule(String name) {
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
			dbm = new Villa(name);
			dbm.silent = true;
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * DBMをオープンする.
	 * </p>
	 * @param omode 接続モード
	 * @param cmode 比較モード
	 * @throws QDBMException
	 */
	public void open(int omode, int cmode) throws QDBMException {
		try {
			dbm = new Villa(name, omode, cmode);
			dbm.silent = true;
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * DBMをクローズする.
	 * </p>
	 * @throws QDBMException
	 * @see qdbm.Villa#close()
	 */
	public void close() throws QDBMException {
		try {
			if (dbm != null) 	{
				dbm.close();
				dbm = null;
			}
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * DBMを削除する.
	 * </p>
	 * @throws QDBMException
	 * @see qdbm.Villa#remove(String)
	 */
	public void remove() throws QDBMException {
		try {
			try { close(); } catch (QDBMException e) {}
			if (name != null) Villa.remove(name);
		} catch (VillaException e) {
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
	 * @see qdbm.Villa#put(byte[], byte[])
	 */
	public boolean put(byte[] key, byte[] value) throws QDBMException {
		try {
			return dbm.put(key, value);
		} catch (VillaException e) {
			throw new QDBMException(e);
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
	 * @see qdbm.Villa#put(byte[], byte[], int)
	 */
	public boolean put(byte[] key, byte[] value, int dmode) throws QDBMException {
		try {
			return dbm.put(key, value, dmode);
		} catch (VillaException e) {
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
		return put(key, value, Villa.DOVER);
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
		return put(key, value, Villa.DOVER);
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
	public boolean put(String key, Object value) throws QDBMException {
		return put(key, value, Villa.DOVER);
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
	public boolean put(String key, Object value, int dmode) throws QDBMException {
		byte[] bKey = key.getBytes();
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
	public boolean putobj(Object key, Object value) throws QDBMException {
		return putobj(key, value, Villa.DOVER);
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
	 * @see qdbm.Villa#putobj(java.lang.Object, java.lang.Object, int)
	 */
	public boolean putobj(Object key, Object value, int dmode) throws QDBMException {
		try {
			return dbm.putobj(key, value, dmode);
		} catch (VillaException e) {
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
	 * @see qdbm.Villa#fetch(byte[])
	 */
	public byte[] fetch(byte[] key) throws QDBMException {
		try {
			return dbm.fetch(key);
		} catch (VillaException e) {
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
	 * @see qdbm.Villa#get(byte[])
	 */
	public byte[] get(byte[] key) throws QDBMException {
		try {
			return dbm.get(key);
		} catch (VillaException e) {
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
	 * @see qdbm.Villa#get(byte[])
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
	public Object getobj(long key) throws QDBMException {
		byte[] bKey = Util.serializeLong(key);
		return getobj(bKey);
	}

	/**
	 * <p>
	 * レコードを取得する.
	 * </p>
	 * @param key キー
	 * @return a serializable object of the value of the corresponding record. If the silent flag is true and no record corresponds, null is returned instead of exception.
	 * @throws QDBMException
	 */
	public Object getobj(byte[] key) throws QDBMException {
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
	 * @see qdbm.Villa#getobj(java.lang.Object)
	 */
	public Object getobj(Object key) throws QDBMException {
		try {
			return dbm.getobj(key);
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * 性能を調整するパラメータを指定する.
	 * </p>
	 * @param lrecmax リーフに入れるレコードの最大数
	 * @param nidxmax 非リーフノードに入れるインデックスの最大数
	 * @param lcnum キャッシュに入れるリーフの最大数
	 * @param ncnum キャッシュに入れる非リーフノードの最大数
	 * @throws QDBMException
	 * @see qdbm.Villa#settuning(int, int, int, int)
	 */
	public void settuning(int lrecmax, int nidxmax, int lcnum, int ncnum) throws QDBMException {
		try {
			dbm.settuning(lrecmax, nidxmax, lcnum, ncnum);
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * カーソルを最初のレコードに移動させる.
	 * </p>
	 * @return always true. However, if the silent flag is true and no record corresponds, false is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Villa#curfirst()
	 */
	public boolean curfirst() throws QDBMException {
		try {
			return dbm.curfirst();
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * カーソルを次のレコードに移動させる.
	 * </p>
	 * @return always true. However, if the silent flag is true and no record corresponds, false is returned instead of exception.
	 * @throws VillaException
	 * @see qdbm.Villa#curnext()
	 */
	public boolean curnext() throws QDBMException {
		try {
			return dbm.curnext();
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * カーソルのあるレコードの値を取得する.
	 * </p>
	 * @return a serializable object of the value of the corresponding record. If the silent flag is true and no record corresponds, null is returned instead of exception. 
	 * @throws QDBMException
	 * @see qdbm.Villa#curvalobj()
	 */
	public Object curvalobj() throws QDBMException {
		try {
			return dbm.curvalobj();
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
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
	 * カーソルのあるレコードの値を取得する.
	 * </p>
	 * @return a byte array of the value of the corresponding record. If the silent flag is true and no record corresponds, `null' is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Villa#curval()
	 */
	public byte[] curval() throws QDBMException {
		try {
			return dbm.curval();
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * カーソルのあるレコードを削除する.
	 * </p>
	 * @return always true. However, if the silent flag is true and no record corresponds to the cursor, false is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Villa#curout()
	 */
	public boolean curout() throws QDBMException {
		try {
			return dbm.curout();
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * カーソルのあるレコードのキーを取得する.
	 * </p>
	 * @return a byte array of the key of the corresponding record. If the silent flag is true and no record corresponds, null is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Villa#curkey()
	 */
	public byte[] curkey() throws QDBMException {
		try {
			return dbm.curkey();
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * カーソルのあるレコードのキーを取得する.
	 * </p>
	 * @return a byte array of the key of the corresponding record. If the silent flag is true and no record corresponds, null is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Villa#curkey()
	 */
	public Long curkeyLong() throws QDBMException {
		byte[] key = curkey();
		if (key == null) {
			return null;
		} else {
			return Util.deserializeLong(key);
		}
	}

	/**
	 * <p>
	 * カーソルを最後のレコードに移動させる.
	 * </p>
	 * @return always true. However, if the silent flag is true and no record corresponds, false is returned instead of exception.
	 * @throws QDBMException
	 * @see qdbm.Villa#curlast()
	 */
	public boolean curlast() throws QDBMException {
		try {
			return dbm.curlast();
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * データベースのレコード数を得る.
	 * </p>
	 * @return レコード数
	 * @throws QDBMException
	 * @see qdbm.Villa#rnum()
	 */
	public int rnum() throws QDBMException {
		try {
			return dbm.rnum();
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * キーに一致するレコードの数を取得する.
	 * </p>
	 * @param key キー
	 * @return レコードの数
	 * @throws QDBMException
	 * @see qdbm.Villa#vnum(byte[])
	 */
	public int vnum(byte[] key) throws QDBMException {
		try {
			return dbm.vnum(key);
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * キーに一致するレコードの数を取得する.
	 * </p>
	 * @param key キー
	 * @return レコードの数
	 * @throws QDBMException
	 */
	public int vnum(long key) throws QDBMException {
		byte[] bKey = Util.serializeLong(key);
		return vnum(bKey);
	}

	/**
	 * <p>
	 * キーに一致するレコードの数を取得する.
	 * </p>
	 * @param key キー
	 * @return レコードの数
	 * @throws QDBMException
	 * @see qdbm.Villa#vnumobj(java.lang.Object)
	 */
	public int vnumobj(Object key) throws QDBMException {
		try {
			return dbm.vnumobj(key);
		} catch (VillaException e) {
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
	 * @see qdbm.Villa#vsiz(byte[])
	 */
	public int vsiz(byte[] key) throws QDBMException {
		try {
			return dbm.vsiz(key);
		} catch (VillaException e) {
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
	 * カーソルを特定のレコードの前後に移動させる.
	 * </p>
	 * @param key キー
	 * @return always true. However, if the silent flag is true and no record corresponds, false is returned instead of exception
	 * @throws QDBMException
	 * @see qdbm.Villa#curjump(byte[])
	 */
	public boolean curjump(byte[] key) throws QDBMException {
		try {
			return dbm.curjump(key);
		} catch (VillaException e) {
			throw new QDBMException(e);
		}
	}

	/**
	 * <p>
	 * カーソルを特定のレコードの前後に移動させる.
	 * </p>
	 * @param key キー
	 * @return always true. However, if the silent flag is true and no record corresponds, false is returned instead of exception
	 * @throws QDBMException
	 */
	public boolean curjump(long key) throws QDBMException {
		byte[] bKey = Util.serializeLong(key);
		return curjump(bKey);
	}
}
