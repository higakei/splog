package jp.co.hottolink.splogfilter.takeda.comparator;

import java.util.Comparator;

import jp.co.hottolink.splogfilter.takeda.entity.SuffixArrayEntity;

/**
 * <p>
 * 接尾辞配列のComparatorクラス.
 * </p>
 * @author higa
 */
public class SuffixArrayComparator implements Comparator<SuffixArrayEntity> {

	/**
	 * <p>
	 * 出現頻度.
	 * </p>
	 */
	public static final int KEY_FREQUENCY = 1;

	/**
	 * <p>
	 * コンストタクター.
	 * </p>
	 */
	public SuffixArrayComparator() {}

	/**
	 * <p>
	 * コンストタクター.
	 * </p>
	 * @param key 比較キー
	 */
	public SuffixArrayComparator(int key) {
		this.key = key;
	}

	/**
	 * <p>
	 * 比較キー.
	 * </p>
	 */
	private int key = 0;

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(SuffixArrayEntity object1, SuffixArrayEntity object2) {
		// 出現頻度
		if (key == KEY_FREQUENCY) {
			return object1.compareToFrequency(object2);
		// デフォルト
		} else {
			return object1.compareTo(object2);
		}
	}
}
