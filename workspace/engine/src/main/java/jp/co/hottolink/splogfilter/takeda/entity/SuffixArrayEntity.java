package jp.co.hottolink.splogfilter.takeda.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.util.ListUtil;

/**
 * <p>
 * 接尾辞配列のEntityクラス.
 * </p>
 * @author higa
 */
public class SuffixArrayEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param suffix 接尾辞
	 */
	public SuffixArrayEntity(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * <p>
	 * 接尾辞.
	 * </p>
	 */
	private String suffix = null;

	/**
	 * <p>
	 * インデックス.
	 * </p>
	 */
	private List<Integer> index = new ArrayList<Integer>();
	
	/**
	 * <p>
	 * インデックスを取得する.
	 * </p>
	 * @return インデックス
	 */
	public List<Integer> getIndex() {
		return index;
	}

	/**
	 * <p>
	 * インデックスを取得する.
	 * </p>
	 * @param index インデックスのインデックス
	 * @return インデックス
	 */
	public Integer getIndex(int index) {
		if (this.index == null) {
			return null;
		} else {
			return this.index.get(index);
		}
	}

	/**
	 * <p>
	 * インデックスを設定する.
	 * </p>
	 * @param index インデックス
	 */
	public void setIndex(List<Integer> index) {
		this.index = index;
	}

	/**
	 * <p>
	 * インデックスを追加する.
	 * </p>
	 * @param index インデックス
	 */
	public void addIndex(int index) {
		if (this.index == null) this.index = new ArrayList<Integer>();
		ListUtil.addElement(index, this.index);
	}

	/**
	 * <p>
	 * インデックスを追加する.
	 * </p>
	 * @param index インデックス
	 */
	public void addIndex(List<Integer> index) {
		if (index == null) return;
		if (this.index == null) this.index = new ArrayList<Integer>();
		for (Integer integer: index) {
			ListUtil.addElement(integer, this.index);
		}
	}

	/**
	 * <p>
	 * 接尾辞を取得する.
	 * </p>
	 * @return 接尾辞
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * <p>
	 * 接尾辞を設定する.
	 * </p>
	 * @param suffix 接尾辞
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * <p>
	 * インデックス数を取得する.
	 * </p>
	 * @return インデックス数
	 */
	public int getIndexCount() {
		if (index == null) {
			return 0;
		} else {
			return index.size();
		}
	}

	/**
	 * <p>
	 * 接尾辞でオブジェクトの順序を比較します.
	 * </p>
	 * @param object 比較対象のオブジェクト
	 * @return このオブジェクトが指定されたオブジェクトより小さい場合は負の整数、等しい場合はゼロ、大きい場合は正の整数
	 * @see Comparable#compareTo(Object)
	 */
	public int compareToSuffix(SuffixArrayEntity object) {
		return suffix.compareTo(object.getSuffix());
	}

	/**
	 * <p>
	 * 接尾辞の文字数（降順）でオブジェクトの順序を比較します.
	 * </p>
	 * @param object 比較対象のオブジェクト
	 * @return このオブジェクトが指定されたオブジェクトより小さい場合は負の整数、等しい場合はゼロ、大きい場合は正の整数
	 * @see Comparable#compareTo(Object)
	 */
	public int compareToSuffixLength(SuffixArrayEntity object) {
		return Integer.valueOf(suffix.length()).compareTo(object.getSuffix().length()) * (-1);
	}

	/**
	 * <p>
	 * インデックス数（降順）でオブジェクトの順序を比較します.
	 * </p>
	 * @param object 比較対象のオブジェクト
	 * @return このオブジェクトが指定されたオブジェクトより小さい場合は負の整数、等しい場合はゼロ、大きい場合は正の整数
	 * @see Comparable#compareTo(Object)
	 */
	public int compareToIndexCount(SuffixArrayEntity object) {
		return Integer.valueOf(getIndexCount()).compareTo(object.getIndexCount()) * (-1);
	}

	/**
	 * <p>
	 * 出現頻度でオブジェクトの順序を比較します.
	 * </p><pre>
	 * 以下の優先順位で比較する
	 * 1. インデックス数（降順）
	 * 2. 接尾辞の文字数（降順）
	 * </pre>
	 * @param object 比較対象のオブジェクト
	 * @return このオブジェクトが指定されたオブジェクトより小さい場合は負の整数、等しい場合はゼロ、大きい場合は正の整数
	 * @see Comparable#compareTo(Object)
	 */
	public int compareToFrequency(SuffixArrayEntity object) {

		int value = compareToIndexCount(object);
		if (value != 0) {
			return value;
		}

		return compareToSuffixLength(object);
	}

	/**
	 * <p>
	 * オブジェクトの順序を比較します.
	 * </p><pre>
	 * デフォルトのComparatorは接尾辞 
	 * </pre>
	 * @param object 比較対象のオブジェクト
	 * @return このオブジェクトが指定されたオブジェクトより小さい場合は負の整数、等しい場合はゼロ、大きい場合は正の整数
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(SuffixArrayEntity object) {
		return compareToSuffix(object);
	}
}
