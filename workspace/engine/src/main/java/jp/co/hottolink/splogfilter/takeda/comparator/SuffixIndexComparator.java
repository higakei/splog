package jp.co.hottolink.splogfilter.takeda.comparator;

import java.util.Comparator;

import jp.co.hottolink.splogfilter.takeda.util.SuffixArrayUtil;

/**
 * <p>
 * 接尾辞インデックスのコンパレータ.
 * </p>
 * @author higa
 */
public class SuffixIndexComparator implements Comparator<int[]> {

	/**
	 * <p>
	 * コンテンツ.
	 * </p>
	 */
	private String[] contents = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param contents コンテンツ
	 */
	public SuffixIndexComparator(String[] contents) {
		this.contents = contents;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(int[] index1, int[] index2) {
		String suffix1 = SuffixArrayUtil.getSuffix(contents, index1);
		String suffix2 = SuffixArrayUtil.getSuffix(contents, index2);
		return suffix1.compareTo(suffix2);
	}
}
