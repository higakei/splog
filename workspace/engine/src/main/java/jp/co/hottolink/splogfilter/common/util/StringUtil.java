package jp.co.hottolink.splogfilter.common.util;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.common.entity.SubstringEntity;

/**
 * <p>
 * 文字列のUtilクラス.
 * </p>
 * @author higa
 */
public class StringUtil {

	/**
	 * <p>
	 * 単語の出現数を取得する.
	 * </p>
	 * @param content 文字列
	 * @param word 単語
	 * @return 出現数
	 */
	public static int getWordCount(String content, String word) {
		return getWordCount(content, word, 0);
	}

	/**
	 * <p>
	 * 単語の出現数を取得する.
	 * </p>
	 * @param content 文字列
	 * @param word 単語
	 * @param skipper スキップ数
	 * @return 出現数
	 */
	public static int getWordCount(String content, String word, int skipper) {

		int appear = 0;
		if (content == null) {
			return appear;
		}

		int index = 0;
		while ((index = content.indexOf(word)) >= 0) {
			appear++;
			if (content.length() < index + word.length() + skipper) {
				break;
			}
			content = content.substring(index + word.length() + skipper);
		}

		return appear;
	}

	/**
	 * <p>
	 * 文字列を単語で分割する.
	 * </p>
	 * @param sentence 文字列
	 * @return 分割した単語
	 */
	public static List<String> divide(String sentence) {

		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(sentence);
		List<String> list = new ArrayList<String>();
		int start = boundary.first();

		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
			list.add(new String(sentence.substring(start, end)));
		}

		return list;
	}

	/**
	 * <p>
	 * 指定した単語リストから、指定した文字列に含まれる単語リストを取得する.
	 * </p>
	 * @param string 文字列
	 * @param words 単語リスト
	 * @return 文字列に含まれる単語リスト
	 */
	public static List<String> getWordList(String string, String[] words) {

		List<String> list = new ArrayList<String>();
		if ((string == null) || string.isEmpty() || (words == null)) {
			return list;
		}

		for (String word: words) {
			if ((word != null) && !word.isEmpty()) {
				if (string.indexOf(word) >= 0) {
					list.add(word);
				}
			}
		}

		return list;
	}
	
	/**
	 * <p>
	 * 指定した単語リストから、指定した文字列に含まれる単語リストを取得する.
	 * </p>
	 * @param string 文字列
	 * @param words 単語リスト
	 * @return 文字列に含まれる単語リスト
	 */
	public static List<String> getWordList(String string, List<String> words) {

		List<String> list = new ArrayList<String>();
		if ((string == null) || string.isEmpty() || (words == null)) {
			return list;
		}

		for (String word: words) {
			if ((word != null) && !word.isEmpty()) {
				if (string.indexOf(word) >= 0) {
					list.add(word);
				}
			}
		}

		return list;
	}

	/**
	 * <p>
	 * 文字列を指定した文字で区切り、指定した文字数以上の接尾辞を取得する.
	 * </p>
	 * @param content 文字列
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @return 接尾辞
	 */
	public static List<String> getSuffixList(String content, int length, String delimitor) {

		List<String> list = new ArrayList<String>();
		if (content == null) return list;

		// 区切り文字で区切る
		String[] splits = content.split(delimitor);

		// 接尾辞配列の生成
		for (String split: splits) {
			for (int i = 0; i < split.length(); i++) {
				// 接尾辞の生成
				String subString = split.substring(i);
				// 指定した文字数より小さい場合、接尾辞配列に追加しない
				if (subString.length() < length) break;
				// 接尾辞配列に追加
				list.add(subString);
			}
		}
		
		return list;
	}

	/**
	 * <p>
	 * ２つの文字列の接頭辞の文字数を取得する.
	 * </p><pre>
	 * 先頭から一致する最長の文字列を取得する
	 * </pre>
	 * @param string1 文字列1
	 * @param sting2 文字列2
	 * @return 文字数
	 */
	public static int getPrefixLength(String string1, String sting2) {
		
		int length = 0;
		if ((string1 == null) || string1.isEmpty()
				|| (sting2 == null) || sting2.isEmpty()) {
			return length;
		}

		int limit = Math.min(string1.length(), sting2.length());
		for (int i = 0; i < limit; i++, length++) {
			if (string1.charAt(i) != sting2.charAt(i)) {
				break; 
			}
		}
	
		return length;
	}

	/**
	 * <p>
	 * 指定した文字列を、指定した文字で区切る.
	 * </p>
	 * @param string 文字列
	 * @param delimitor 区切り文字
	 * @return 区切られた文字列リスト
	 */
	public static List<SubstringEntity> split(String string, String delimitor) {
	
		List<SubstringEntity> list = new ArrayList<SubstringEntity>();

		if ((string == null) || string.isEmpty()) {
			return list;
		}

		if ((delimitor == null) || delimitor.isEmpty()) {
			SubstringEntity entity = new SubstringEntity();
			entity.setIndex(0);
			entity.setLength(string.length());
			list.add(entity);
			return list;
		}

		for (int beginIndex = 0
				, endIndex = 0
				; beginIndex < string.length()
				; beginIndex = (endIndex + delimitor.length())
		) {
			endIndex = string.indexOf(delimitor, beginIndex);
			if (endIndex == -1) endIndex = string.length();
			int length = endIndex - beginIndex;
			if (length == 0) continue;

			SubstringEntity entity = new SubstringEntity();
			entity.setIndex(beginIndex);
			entity.setLength(length);
			list.add(entity);
		}

		return list;
	}

	/**
	 * <p>
	 * XML文字列をエスケープする.
	 * </p>
	 * @param xml XML文字列
	 * @return エスケープした文字列
	 */
	public static String escapeXml(String xml) {

		if (xml == null) {
			return null;
		}

		xml = xml.replaceAll("&(?!#\\p{Digit}{0,6};)", "&amp;");
		xml = xml.replaceAll("<", "&lt;");
		xml = xml.replaceAll(">", "&gt;");
		xml = xml.replaceAll("\"", "&quot;");
		xml = xml.replaceAll("'", "&apos;");

		return xml;
	}
}
