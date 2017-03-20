package jp.co.hottolink.splogfilter.util;

import java.util.regex.Pattern;

import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;

/**
 * <p>
 * HTMLのUtilクラス.
 * </p>
 * @author higa
 */
public class HTMLUtil {

	/**
	 * <p>
	 * HTMLタグ.
	 * </p>
	 */
	private static final Tag[] tags = HTML.getAllTags();

	/**
	 * <p>
	 * 開始タグの正規表現.
	 * </p>
	 */
	private static final Pattern START_TAG_REGEX = Pattern.compile(createStartTagRegex(), Pattern.CASE_INSENSITIVE);

	/**
	 * <p>
	 * 終了タグの正規表現.
	 * </p>
	 */
	private static final Pattern END_TAG_REGEX = Pattern.compile(createEndTagRegex(), Pattern.CASE_INSENSITIVE);

	/**
	 * <p>
	 * HTMLタグを除去する.
	 * </p>
	 * @param html 除去前の文字列
	 * @return 除去後の文字列
	 */
	public static String removeTag(String html) {

		if (html == null) {
			return null;
		}

		html = START_TAG_REGEX.matcher(html).replaceAll("");
		html = END_TAG_REGEX.matcher(html).replaceAll("");

		return html;
	}

	/**
	 * <p>
	 * 開始タグの正規表現を作成する.
	 * </p>
	 * @param tagName タグ名
	 * @return 正規表現
	 */
	public static String createStartTagRegex(String tagName) {
		if (tagName == null) return null;
		StringBuffer buffer = new StringBuffer();
		buffer.append("<");
		buffer.append("(");
		buffer.append(tagName.toLowerCase());
		buffer.append("|");
		buffer.append(tagName.toUpperCase());
		buffer.append(")");
		buffer.append("(");
		buffer.append("[ ]*");
		buffer.append("|");
		buffer.append("[ ]*/[ ]*");
		buffer.append("|");
		buffer.append("[ ]+[^>]*");
		buffer.append(")");
		buffer.append(">");
		return buffer.toString();
	}

	/**
	 * <p>
	 * 終了タグの正規表現を作成する.
	 * </p>
	 * @param tagName タグ名
	 * @return 正規表現
	 */
	public static String createEndTagRegex(String tagName) {
		if (tagName == null) return null;
		StringBuffer buffer = new StringBuffer();
		buffer.append("<");
		buffer.append("/[ ]*");
		buffer.append("(");
		buffer.append(tagName.toLowerCase());
		buffer.append("|");
		buffer.append(tagName.toUpperCase());
		buffer.append(")");
		buffer.append("[ ]*");
		buffer.append(">");
		return buffer.toString();
	}

	/**
	 * <p>
	 * 開始タグの正規表現を作成する.
	 * </p>
	 * @return 正規表現
	 */
	public static String createStartTagRegex() {

		if ((tags == null) || (tags.length == 0)) {
			return null;
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<");
		buffer.append("(");

		for (int i = 0; i < tags.length; i++) {
			String tagName = tags[i].toString();
			if (i > 0) buffer.append("|");
			buffer.append(tagName.toLowerCase());
			buffer.append("|");
			buffer.append(tagName.toUpperCase());
		}

		buffer.append(")");
		buffer.append("(");
		buffer.append("[ ]*");
		buffer.append("|");
		buffer.append("[ ]*/[ ]*");
		buffer.append("|");
		buffer.append("[ ]+[^>]*");
		buffer.append(")");
		buffer.append(">");

		return buffer.toString();
	}

	/**
	 * <p>
	 * 終了タグの正規表現を作成する.
	 * </p>
	 * @return 正規表現
	 */
	public static String createEndTagRegex() {

		if ((tags == null) || (tags.length == 0)) {
			return null;
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("<");
		buffer.append("/[ ]*");
		buffer.append("(");

		for (int i = 0; i < tags.length; i++) {
			String tagName = tags[i].toString();
			if (i > 0) buffer.append("|");
			buffer.append(tagName.toLowerCase());
			buffer.append("|");
			buffer.append(tagName.toUpperCase());
		}

		buffer.append(")");
		buffer.append("[ ]*");
		buffer.append(">");

		return buffer.toString();
	}
}
