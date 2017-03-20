package jp.co.hottolink.splogfilter.takeda.util;

import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.takeda.statistics.WordIntervalStatistics;

/**
 * <p>
 * 単語のUtilクラス.
 * </p>
 * @author higa
 */
public class WordUtil {

	/**
	 * <p>
	 * 単語の出現間隔を取得する.
	 * </p>
	 * @param content 文字列
	 * @param word 単語
	 * @param skipper スキップ数
	 * @return 出現間隔
	 */
	public static WordIntervalStatistics getWordInterval(String content, String word, int skipper) {

		String encontent = new String(content);
		int index = 0;
		List<Integer> indices = new ArrayList<Integer>();
		
		while ((index = encontent.indexOf(word)) > -1) {
			indices.add(index);
			if (encontent.length() < index + word.length() + skipper) {
				break;
			}
			encontent = encontent.substring(index + word.length() + skipper);
		}

		return new WordIntervalStatistics(word, indices);
	}

	/**
	 * <p>
	 * 単語の出現間隔を取得する.
	 * </p>
	 * @param content 文字列
	 * @param word 単語
	 * @return 出現間隔
	 */
	public static WordIntervalStatistics getWordInterval(String content, String word) {
		return getWordInterval(content, word, 0);
	}
}
