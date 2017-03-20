package jp.co.hottolink.splogfilter.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * リストのUtilクラス.
 * </p>
 * @author higa
 */
public class ListUtil {

	/**
	 * <p>
	 * リストから平均を取得する.
	 * </p>
	 * @param list リスト
	 * @return 平均
	 */
	public static int getAverage(List<Integer> list) {
	
		if ((list == null) || list.isEmpty()) {
			return 0;
		}
	
		int total = 0;
		for (int interval: list) {
			total += interval;
		}
		
		return (total / list.size());
	}

	/**
	 * <p>
	 * リストにエレメントを追加する.
	 * </p><pre>
	 * リストにエレメントが存在したら、エレメントを追加しない
	 * </pre>
	 * @param element エレメント
	 * @param list リスト
	 */
	public static void addElement(String element, List<String> list) {
		if ((element == null) || (list == null)) return;
		if (list.indexOf(element) == -1) list.add(element);
	}

	/**
	 * <p>
	 * リストにエレメントを追加する.
	 * </p><pre>
	 * リストにエレメントが存在したら、エレメントを追加しない
	 * </pre>
	 * @param element エレメント
	 * @param list リスト
	 */
	public static void addElement(Integer element, List<Integer> list) {
		if ((element == null) || (list == null)) return;
		if (list.indexOf(element) == -1) list.add(element);
	}

	/**
	 * <p>
	 * 指定した２つのリストを結合する
	 * </p><pre>
	 * ２つのリストに存在するエレメントをマージする
	 * </pre>
	 * @param list1 結合するリスト1
	 * @param list2 結合するリスト2
	 * @return 結合したリスト
	 */
	public static List<Integer> concatenate(List<Integer> list1, List<Integer> list2) {

		List<Integer> list;
		if (list1 == null) {
			list = new ArrayList<Integer>();
		} else {
			list = new ArrayList<Integer>(list1);
		}

		if (list2 == null) {
			return list;
		}

		for (Integer integer: list2) {
			ListUtil.addElement(integer, list);
		}

		return list;
	}
}
