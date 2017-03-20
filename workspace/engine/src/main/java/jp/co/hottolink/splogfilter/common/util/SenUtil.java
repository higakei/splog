package jp.co.hottolink.splogfilter.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.java.sen.StringTagger;
import net.java.sen.Token;

/**
 * <p>
 * senのUtilクラス.
 * </p>
 * @author higa
 *
 */
public class SenUtil {

	/**
	 * <p>
	 * 形態素解析を行う.
	 * </p>
	 * @param sentence 文章
	 * @return 形態素リスト
	 * @throws IOException
	 */
	public static List<Token> getMorphemes(String sentence) throws IOException {

		List<Token> list = new ArrayList<Token>();
		if (sentence == null) {
			return list;
		}

		StringTagger tagger = StringTagger.getInstance();
		Token[] token = tagger.analyze(sentence);
		if (token == null) {
			return list;
		}

		return Arrays.asList(token);
	}
}
