package jp.co.hottolink.splogfilter.takeda.copycontent.entity;

import java.io.Serializable;

/**
 * <p>
 * 文字コードキーのEntityクラス.
 * </p>
 * @author higa
 *
 */
public class CharacterCodeKeyEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * 開始コード.
	 * </p><pre>
	 * 文字コードキーの開始文字コード
	 * </pre>
	 */
	private int from = 0;

	/**
	 * <p>
	 * 終了コード.
	 * </p><pre>
	 * 文字コードキーの終了文字コード
	 * </pre>
	 */
	private int to = 0;

	/**
	 * <p>
	 * インデックス.
	 * </p><pre>
	 * 文字コードパーティションのインデックス
	 * </pre>
	 */
	private int index = 0;
	
	/**
	 * <p>
	 * 開始コードを取得する.
	 * </p>
	 * @return 開始コード
	 */
	public int getFrom() {
		return from;
	}

	/**
	 * <p>
	 * 開始コードを設定する.
	 * </p>
	 * @param from 開始コード
	 */
	public void setFrom(int from) {
		this.from = from;
	}

	/**
	 * <p>
	 * 終了コードを取得する.
	 * </p>
	 * @return 終了コード
	 */
	public int getTo() {
		return to;
	}

	/**
	 * <p>
	 * 終了コードを設定する.
	 * </p>
	 * @param to 終了コード
	 */
	public void setTo(int to) {
		this.to = to;
	}

	/**
	 * <p>
	 * インデックスを取得する.
	 * </p>
	 * @return index インデックス
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
}
