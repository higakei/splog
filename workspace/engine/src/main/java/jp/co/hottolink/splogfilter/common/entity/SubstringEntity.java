package jp.co.hottolink.splogfilter.common.entity;

import java.io.Serializable;

/**
 * <p>
 * 部分文字列のEntityクラス.
 * </p>
 * @author higa
 */
public class SubstringEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * インデックス.
	 * </p>
	 */
	private int index = 0;

	/**
	 * <p>
	 * 長さ.
	 * </p>
	 */
	private int length = 0;

	/**
	 * <p>
	 * インデックスを取得する.
	 * </p>
	 * @return インデックス
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * <p>
	 * インデックスを設定する.
	 * </p>
	 * @param index インデックス
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * <p>
	 * 長さを取得する.
	 * </p>
	 * @return 長さ
	 */
	public int getLength() {
		return length;
	}

	/**
	 * <p>
	 * 長さを設定する.
	 * </p>
	 * @param length 長さ
	 */
	public void setLength(int length) {
		this.length = length;
	}
}
