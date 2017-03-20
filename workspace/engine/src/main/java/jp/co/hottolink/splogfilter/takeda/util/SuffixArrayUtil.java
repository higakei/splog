package jp.co.hottolink.splogfilter.takeda.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.co.hottolink.splogfilter.common.util.StringUtil;
import jp.co.hottolink.splogfilter.takeda.comparator.SuffixArrayComparator;
import jp.co.hottolink.splogfilter.takeda.entity.SuffixArrayEntity;
import jp.co.hottolink.splogfilter.util.ListUtil;

/**
 * <p>
 * 接尾辞配列のUtilクラス.
 * </p>
 * @author higa
 */
public class SuffixArrayUtil {

	/**
	 * <p>
	 * 接尾辞インデックスのフィールド数.
	 * </p>
	 */
	public static final int SUFFIX_INDEX_SIZE = 3;

	/**
	 * <p>
	 * ドキュメントのインデックス.
	 * </p><pre>
	 * 接尾辞インデックスのフィールドインデックス
	 * </pre>
	 */
	public static final int DOCUMENT_INDEX = 0;

	/**
	 * <p>
	 * コンテンツのインデックス.
	 * </p><pre>
	 * 接尾辞インデックスのフィールドインデックス
	 * </pre>
	 */
	public static final int CONTENT_INDEX = 1;

	/**
	 * <p>
	 * 接尾辞の文字数.
	 * </p><pre>
	 * 接尾辞インデックスのフィールドインデックス
	 * </pre>
	 */
	public static final int SUFFIX_LENGTH = 2;

	/**
	 * <p>
	 * リストの文字列から指定した文字数以上の接尾辞配列を取得する.
	 * </p><pre>
	 * 接尾辞にリストのインデックスを付加する
	 * </pre>
	 * @param contents 文字列リスト
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @return 接尾辞配列
	 */
	public static List<SuffixArrayEntity> getSuffixArrayList(List<String> contents, int length, String delimitor) {
	
		Map<String, SuffixArrayEntity> map = new TreeMap<String, SuffixArrayEntity>();
		if (contents == null) return new ArrayList<SuffixArrayEntity>();
	
		for (int i = 0; i < contents.size(); i++) {
			String content = contents.get(i);
			if (content == null) continue;
	
			// 接尾辞の取得
			List<String> suffixArray = StringUtil.getSuffixList(content, length, delimitor);
			// インデックスの付加
			for (String suffix: suffixArray) {
				SuffixArrayEntity entity = map.get(suffix);
				if (entity == null) {
					entity = new SuffixArrayEntity(suffix);
					entity.addIndex(i);
					map.put(suffix, entity);
				} else {
					entity.addIndex(i);
				}
			}
		}

		return new ArrayList<SuffixArrayEntity>(map.values());
	}

	/**
	 * <p>
	 * リストに指定した回数以上を出現する接尾辞配列を取得する.
	 * </p><pre>
	 * リストの文字列を指定した文字で区切り、指定した文字数以上の接尾辞
	 * </pre>
	 * @param contents 文字列リスト
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @param count 出現数
	 * @return 接尾辞配列
	 */
	public static List<SuffixArrayEntity> getSuffixArrayList(List<String> contents, int length, String delimitor, int count) {

		Map<String, SuffixArrayEntity> map = new HashMap<String, SuffixArrayEntity>();

		// 接尾辞配列の取得
		List<SuffixArrayEntity> list = getSuffixArrayList(contents, length, delimitor);
	
		int size = list.size();
		for (int i = 0; i < size; i++) {
			SuffixArrayEntity target = list.get(i);

			// 出現数の算出
			for (int j = (i + 1); j < size; j++) {
				String suffix = target.getSuffix();
				SuffixArrayEntity next = list.get(j);
				String nextSuffix = next.getSuffix();

				// 接頭辞の取得
				int prefixLength = StringUtil.getPrefixLength(suffix, nextSuffix);
				String prefix = suffix.substring(0, prefixLength);

				// 接頭辞が指定した文字数より小さい、または、すでに集計済みの場合は終了
				if ((prefixLength < length) || map.containsKey(prefix)) {
					break;
				}

				// 接頭辞が接尾辞の場合、インデックスを追加
				if (prefix.equals(suffix)) {
					target.addIndex(next.getIndex());
					continue;
				}

				// 接頭辞の出現数の取得
				List<Integer> newIndex = ListUtil.concatenate(target.getIndex(), next.getIndex());

				// 接頭辞の出現数が多い場合
				if (newIndex.size() > target.getIndexCount()) {
					// 指定した出現数以上の場合、結果に追加する
					if (target.getIndexCount() >= count) {
						map.put(target.getSuffix(), target);
					}

					// 接頭辞を集計する
					target = new SuffixArrayEntity(prefix);
					target.setIndex(newIndex);
				}
			}

			// 指定した出現数以上の場合、結果に追加する
			if (target.getIndexCount() >= count) {
				map.put(target.getSuffix(), target);
			}

		}

		// 出現数でソート
		List<SuffixArrayEntity> result = new ArrayList<SuffixArrayEntity>(map.values());
		Collections.sort(result, new SuffixArrayComparator(SuffixArrayComparator.KEY_FREQUENCY));
	
		return result;
	}

	/**
	 * <p>
	 * 接尾辞を取得する.
	 * </p>
	 * @param contents コンテンツ
	 * @param suffixIndex 接尾辞インデックス
	 * @return 接尾辞
	 */
	public static String getSuffix(String[] contents, int[] suffixIndex) {

		// パラメータチェック
		if ((contents == null) || (suffixIndex == null)
				|| (suffixIndex.length != SUFFIX_INDEX_SIZE)) {
			return null;
		}

		// ドキュメントインデックスのチェック
		int contentIndex = suffixIndex[DOCUMENT_INDEX];
		if ((0 > contentIndex) || (contentIndex >= contents.length)) {
			return null;
		}

		// コンテンツの取得
		String content = contents[contentIndex];
		if (content == null) {
			return null;
		}

		// コンテンツインデックスのチェック
		int beginIndex = suffixIndex[CONTENT_INDEX];
		int endIndex = beginIndex + suffixIndex[SUFFIX_LENGTH];
		if ((0 > beginIndex) || (beginIndex >= endIndex)
				|| (endIndex > content.length())) {
			return null;
		}
		
		return content.substring(beginIndex, endIndex);
	}
}
