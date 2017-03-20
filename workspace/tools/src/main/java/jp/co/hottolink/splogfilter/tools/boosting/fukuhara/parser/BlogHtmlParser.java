package jp.co.hottolink.splogfilter.tools.boosting.fukuhara.parser;

import java.net.URL;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.NotFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.RegexFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * <p>
 * ブログのHTMLパーサークラス.
 * </p>
 * @author higa
 *
 */
public class BlogHtmlParser {

	/**
	 * <p>
	 * デフォルトエンコーディング.
	 * </p>
	 */
	private static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * <p>
	 * HTMLパーサー
	 * </p>
	 */
	private Parser parser = null;

	
	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param html HTML
	 * @throws ParserException
	 */
	public BlogHtmlParser(String html) {
		parser = Parser.createParser(html, DEFAULT_ENCODING);
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param html HTML
	 * @param encoding エンコーディング
	 * @throws ParserException
	 */
	public BlogHtmlParser(String html, String encoding) {
		parser = Parser.createParser(html, encoding);
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param url URL
	 * @throws ParserException 
	 */
	public BlogHtmlParser(URL url) throws ParserException {
		parser = new Parser(url.toString());
	}

	/**
	 * <p>
	 * 本文を取得する.
	 * </p>
	 * @param url URL
	 * @return 本文
	 * @throws ParserException
	 */
	public String getBody(String url) throws ParserException {

		if (url == null) {
			return null;
		}

		// jugem.jp
		int index = url.indexOf("jugem.jp");
		if (index > -1) {
			return getBodyForJugem();
		}

		// ameblo.jp
		index = url.indexOf("ameblo.jp");
		if (index > -1) {
			return getBodyForAmeblo();
		}

		// seesaa.net
		index = url.indexOf("seesaa.net");
		if (index > -1) {
			return getBodyForSeesaa();
		}

		// livedoor.jp
		index = url.indexOf("livedoor.jp");
		if (index > -1) {
			return getBodyForLivedoorJp();
		}

		// livedoor.biz
		index = url.indexOf("livedoor.biz");
		if (index > -1) {
			return getBodyForLivedoorBiz();
		}

		// so-net.ne.jp
		index = url.indexOf("so-net.ne.jp");
		if (index > -1) {
			return getBodyForSonet();
		}

		// fc2.com
		index = url.indexOf("fc2.com");
		if (index > -1) {
			return getBodyForFc2();
		}

		// cocolog-nifty.com
		index = url.indexOf("nifty.com");
		if (index > -1) {
			return getBodyForCocolog();
		}

		// fanblogs.jp
		index = url.indexOf("fanblogs.jp");
		if (index > -1) {
			return getBodyForFanblogs();
		}

		// dokyun.jp
		index = url.indexOf("dokyun.jp");
		if (index > -1) {
			return getBodyForDokyun();
		}

		// hamoblo.com
		index = url.indexOf("hamoblo.com");
		if (index > -1) {
			return getBodyForHamoblo();
		}

		// tumblr.com
		index = url.indexOf("tumblr.com");
		if (index > -1) {
			return getBodyForTumblr();
		}

		return getBodyForOthers();
		//return null;
	}

	/**
	 * <p>
	 * ジュゲムの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForJugem() throws ParserException {

		NodeFilter filter = new HasAttributeFilter("class", "jgm_entry_desc_mark");
		NodeList nodes = parser.parse(filter);
		if (nodes.size() == 0) {
			return null;
		}

		filter = new NotFilter(new HasAttributeFilter("id", "grokEntryAdContainer"));
		NodeList body = nodes.elementAt(0).getChildren();
		body.keepAllNodesThatMatch(filter);
		return body.asString();
	}

	/**
	 * <p>
	 * アメーバーブログの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForAmeblo() throws ParserException {

		NodeFilter filter = new HasAttributeFilter("class", "subContents");
		NodeList nodes = parser.parse(filter);
		if (nodes.size() == 0) {
			return null;
		}

		filter = new NotFilter(new HasAttributeFilter("id", "advertising2"));
		NodeList body = nodes.elementAt(0).getChildren();
		body.keepAllNodesThatMatch(filter);
		return body.asString();
	}

	/**
	 * <p>
	 * Seesaaブログの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForSeesaa() throws ParserException {

		NodeFilter filter = new HasAttributeFilter("class", "text");
		NodeList nodes = parser.parse(filter);
		if (nodes.size() == 0) {
			return null;
		}

		filter = new NotFilter(new OrFilter(new HasAttributeFilter("class", "tag-word")
				,new HasAttributeFilter("class", "listCategoryArticle")));
		NodeList body = nodes.elementAt(0).getChildren();
		body.keepAllNodesThatMatch(filter);
		return body.asString();
	}

	/**
	 * <p>
	 * livedoor Blogの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForLivedoorJp() throws ParserException {

		NodeFilter filter = new HasAttributeFilter("class", "main");
		NodeList nodes = parser.parse(filter);
		if (nodes.size() == 0) {
			return null;
		}

		return nodes.elementAt(0).toPlainTextString();
	}

	/**
	 * <p>
	 * livedoor Blogの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForLivedoorBiz() throws ParserException {

		NodeFilter filter = new HasAttributeFilter("class", "entry-content");
		NodeList nodes = parser.parse(filter);
		if (nodes.size() == 0) {
			return null;
		}

		filter = new NotFilter(new HasAttributeFilter("class", "common-theme"));
		NodeList body = nodes.elementAt(0).getChildren();
		body.keepAllNodesThatMatch(filter);
		return body.asString();
	}

	/**
	 * <p>
	 * So-netブログの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForSonet() throws ParserException {

		NodeFilter filter = new HasAttributeFilter("class", "articles-body");
		NodeList nodes = parser.parse(filter);
		if (nodes.size() == 0) {
			return null;
		}

		filter = new NotFilter(new HasAttributeFilter("class", "tag-word"));
		NodeList body = nodes.elementAt(0).getChildren();
		body.keepAllNodesThatMatch(filter);
		return body.asString();
	}

	/**
	 * <p>
	 * FC2ブログの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForFc2() throws ParserException {

		NodeList nodes = parser.parse(null);

		// main_text
		NodeFilter filter = new HasAttributeFilter("class", "main_text");
		NodeList body = nodes.extractAllNodesThatMatch(filter, true);
		if (body.size() > 0) {
			return body.elementAt(0).toPlainTextString();
		}

		// entry-body
		filter = new HasAttributeFilter("class", "entry-body");
		body = nodes.extractAllNodesThatMatch(filter, true);
		if (body.size() > 0) {
			return body.elementAt(0).toPlainTextString();
		}

		// mainEntryBase
		filter = new HasAttributeFilter("class", "mainEntryBase");
		body = nodes.extractAllNodesThatMatch(filter, true);
		if (body.size() > 0) {
			filter = new OrFilter(new HasAttributeFilter("class", "mainEntryBody")
					,new HasAttributeFilter("class", "mainEntryMore"));
			body = body.elementAt(0).getChildren().extractAllNodesThatMatch(filter);
			filter = new NotFilter(new OrFilter(new HasAttributeFilter("class", "fc2_footer"),
					new NodeClassFilter(ScriptTag.class)));
			body.keepAllNodesThatMatch(filter, true);
			return body.asString();
		}

		// main_body
		filter = new HasAttributeFilter("class", "main_body");
		body = nodes.extractAllNodesThatMatch(filter, true);
		if (body.size() > 0) {
			return body.elementAt(0).toPlainTextString();
		}

		// center_contents
		filter = new HasAttributeFilter("id", "center_contents");
		body = nodes.extractAllNodesThatMatch(filter, true);
		if (body.size() > 0) {
			return body.elementAt(0).toPlainTextString();
		}

		return null;
	}

	/**
	 * <p>
	 * ココログの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForCocolog() throws ParserException {

		NodeFilter filter = new HasAttributeFilter("class", "entry-body");
		NodeList nodes = parser.parse(filter);
		if (nodes.size() == 0) {
			return null;
		}

		return nodes.elementAt(0).toPlainTextString();
	}

	/**
	 * <p>
	 * ファンブログの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForFanblogs() throws ParserException {

		NodeFilter filter = new HasAttributeFilter("class", "entry");
		NodeList nodes = parser.parse(filter);
		if (nodes.size() == 0) {
			return null;
		}

		filter = new HasChildFilter(new HasAttributeFilter("class", "title"));
		nodes.keepAllNodesThatMatch(filter);

		filter = new HasAttributeFilter("class", "text");
		nodes = nodes.extractAllNodesThatMatch(filter, true);
		
		return nodes.asString();
	}

	/**
	 * <p>
	 * ドキュンブログの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForDokyun() throws ParserException {

		NodeFilter filter = new HasAttributeFilter("id", "kiziBody");
		NodeList nodes = parser.parse(filter);
		if (nodes.size() == 0) {
			return null;
		}

		return nodes.elementAt(0).toPlainTextString();
	}

	/**
	 * <p>
	 * ハモブロの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForHamoblo() throws ParserException {

		NodeFilter filter = new HasAttributeFilter("class", "entry");
		NodeList nodes = parser.parse(filter);
		if (nodes.size() == 0) {
			return null;
		}

		filter = new HasChildFilter(new HasAttributeFilter("id", "BlogEntryExtend"), true);
		nodes = nodes.extractAllNodesThatMatch(filter);

		filter = new HasAttributeFilter("class", "entry_text");
		nodes = nodes.extractAllNodesThatMatch(filter, true);

		filter = new NotFilter(new HasChildFilter(new HasAttributeFilter("id", "BlogEntryExtend")));
		nodes = nodes.extractAllNodesThatMatch(filter);

		return nodes.asString();
	}

	/**
	 * <p>
	 * タンブルログの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForTumblr() throws ParserException {

		NodeFilter filter = new OrFilter(new HasAttributeFilter("class", "post")
				, new HasAttributeFilter("class", "post hentry"));
		NodeList nodes = parser.parse(filter);
		if (nodes.size() == 0) {
			return null;
		}

		// quote
		filter = new HasAttributeFilter("class", "quote");
		NodeList body = nodes.elementAt(0).getChildren().extractAllNodesThatMatch(filter);
		if (body.size() > 0) {
			filter = new NotFilter(new NodeClassFilter(LinkTag.class));
			body.keepAllNodesThatMatch(filter, true);
			filter = new NotFilter(new HasAttributeFilter("class", "source"));
			body.keepAllNodesThatMatch(filter, true);
			filter = new NotFilter(new HasAttributeFilter("class", "ptime"));
			body.keepAllNodesThatMatch(filter, true);
			filter = new NotFilter(new RegexFilter("&#[0-9]+;"));
			body.keepAllNodesThatMatch(filter, true);
			return body.asString();
		}

		// link
		filter = new HasAttributeFilter("class", "link");
		body = nodes.elementAt(0).getChildren().extractAllNodesThatMatch(filter);
		if (body.size() > 0) {
			return body.asString();
		}

		// quote 
		filter = new HasAttributeFilter("class", "quote ");
		body = nodes.elementAt(0).getChildren().extractAllNodesThatMatch(filter);
		if (body.size() > 0) {
			filter = new HasAttributeFilter("class", "quote entry-content");
			body = body.elementAt(0).getChildren().extractAllNodesThatMatch(filter);
			filter = new NotFilter(new RegexFilter("&#[0-9]+;"));
			body.keepAllNodesThatMatch(filter, true);
			return body.asString();
		}

		return nodes.toHtml();
	}

	/**
	 * <p>
	 * 上記以外のサイトの本文を取得する.
	 * </p>
	 * @return 本文
	 * @throws ParserException 
	 */
	public String getBodyForOthers() throws ParserException {

		NodeList nodes = parser.parse(null);

		// entry-content
		NodeFilter filter = new HasAttributeFilter("class", "entry-content");
		NodeList body = nodes.extractAllNodesThatMatch(filter, true);
		if (body.size() > 0) {
			return body.elementAt(0).toPlainTextString();
		}

		// entry-box
		filter = new HasAttributeFilter("class", "entry-box");
		body = nodes.extractAllNodesThatMatch(filter, true);
		if (body.size() > 0) {
			filter = new HasAttributeFilter("class", "entry-body");
			body = body.elementAt(0).getChildren().extractAllNodesThatMatch(filter);
			return body.asString();
		}

		// asset-content
		filter = new HasAttributeFilter("class", "asset-body");
		body = nodes.extractAllNodesThatMatch(filter, true);
		if (body.size() > 0) {
			filter = new NotFilter(new HasAttributeFilter("class", "ms-entry-bottom-adsense"));
			body.keepAllNodesThatMatch(filter, true);
			return body.elementAt(0).toPlainTextString();
		}

		// box_entry
		filter = new HasAttributeFilter("class", "box_entry");
		body = nodes.extractAllNodesThatMatch(filter, true);
		if (body.size() > 0) {
			return body.elementAt(0).toPlainTextString();
		}

		// entry_body
		filter = new HasAttributeFilter("class", "entry_body");
		body = nodes.extractAllNodesThatMatch(filter, true);
		if (body.size() > 0) {
			return body.elementAt(0).toPlainTextString();
		}

		// postarea
		filter = new HasAttributeFilter("class", "postarea");
		body = nodes.extractAllNodesThatMatch(filter, true);
		if (body.size() > 0) {
			filter = new HasAttributeFilter("class", "txt");
			body = body.elementAt(0).getChildren().extractAllNodesThatMatch(filter);
			return body.asString();
		}

		// main_column_body
		filter = new HasAttributeFilter("id", "main_column_body");
		body = nodes.extractAllNodesThatMatch(filter, true);
		if (body.size() > 0) {
			filter = new NotFilter(new HasAttributeFilter("class", "entry-tags"));
			body.keepAllNodesThatMatch(filter, true);
			return body.elementAt(0).toPlainTextString();
		}

		return null;
	}
}
