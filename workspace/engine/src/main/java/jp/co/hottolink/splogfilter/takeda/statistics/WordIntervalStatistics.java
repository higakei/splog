package jp.co.hottolink.splogfilter.takeda.statistics;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import jp.co.hottolink.splogfilter.util.ListUtil;

/**
 * <p>
 * 単語の出現間隔の統計データクラス.
 * </p>
 * @author higa
 */
public class WordIntervalStatistics implements Serializable {

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
	 * @param word 単語
	 * @param intervals 出現間隔
	 */
	public WordIntervalStatistics(String word, List<Integer> intervals) {
		this.word = word;
		if ((intervals != null) && !intervals.isEmpty()) {
			count = intervals.size();
			min = Collections.min(intervals);
			max = Collections.max(intervals);
			average = ListUtil.getAverage(intervals);
		}
	}

	/**
	 * <p>
	 * 平均出現間隔.
	 * </p>
	 */
	private int average = 0;

	/**
	 * <p>
	 * 最大出現間隔.
	 * </p>
	 */
	private int max = 0;

	/**
	 * <p>
	 * 最小出現間隔.
	 * </p>
	 */
	private int min = 0;

	/**
	 * <p>
	 * 出現数.
	 * </p>
	 */
	private int count = 0;

	/**
	 * <p>
	 * 単語.
	 * </p>
	 */
	private String word = null;

	/**
	 * <p>
	 * 平均出現間隔を取得する.
	 * </p>
	 * @return 平均出現間隔
	 */
	public int getAverage() {
		return average;
	}

	/**
	 * <p>
	 * 平均出現間隔を設定する.
	 * </p>
	 * @param average 平均出現間隔
	 */
	public void setAverage(int average) {
		this.average = average;
	}

	/**
	 * <p>
	 * 最大出現間隔を取得する.
	 * </p>
	 * @return 最大出現間隔
	 */
	public int getMax() {
		return max;
	}

	/**
	 * <p>
	 * 最大出現間隔を設定する.
	 * </p>
	 * @param max 最大出現間隔
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * <p>
	 * 最小出現間隔を取得する.
	 * </p>
	 * @return 最小出現間隔
	 */
	public int getMin() {
		return min;
	}

	/**
	 * <p>
	 * 最小出現間隔を設定する.
	 * </p>
	 * @param min 最小出現間隔
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * <p>
	 * 出現数を取得する.
	 * </p>
	 * @return 出現数
	 */
	public int getCount() {
		return count;
	}

	/**
	 * <p>
	 * 出現数を設定する.
	 * </p>
	 * @param count 出現数
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * <p>
	 * 単語を取得する.
	 * </p>
	 * @return 単語
	 */
	public String getWord() {
		return word;
	}

	/**
	 * <p>
	 * 単語を設定する.
	 * </p>
	 * @param word 単語
	 */
	public void setWord(String word) {
		this.word = word;
	}
}
