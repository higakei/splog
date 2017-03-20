package jp.co.hottolink.splogfilter.xml;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.extension.fusion.core.util.net.XmlResponse;
import jp.co.hottolink.splogfilter.util.XMLUtil;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <p>
 * ブログフィードのReaderクラス.
 * </p><pre>
 * ブログフィードのXMLを読み込むクラス
 * </pre>
 * @author higa
 */
public class BlogFeedReader {

	
	/**
	 * <p>
	 * ブログフィードのXPATH.
	 * </p>
	 */
	private static final String XPATH_DOCUMENT = "documents/document";

	/**
	 * <p>
	 * 文書ID
	 * </p>
	 */
	private static final String DOCUMENT_DOCUMENT_ID = "documentId";

	/**
	 * <p>
	 * 投稿者ID.
	 * </p>
	 */
	private static final String DOCUMENT_AUTHOR_ID = "authorId";

	/**
	 * <p>
	 * 投稿日.
	 * </p>
	 */
	private static final String DOCUMENT_DATE = "date";

	/**
	 * <p>
	 * タイトル.
	 * </p>
	 */
	private static final String DOCUMENT_TITLE = "title";

	/**
	 * <p>
	 * URL.
	 * </p>
	 */
	private static final String DOCUMENT_URL = "url";

	/**
	 * <p>
	 * 本文.
	 * </p>
	 */
	private static final String DOCUMENT_BODY = "body";

	/**
	 * <p>
	 * 日付パターン.
	 * </p>
	 */
	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param xml XML
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public BlogFeedReader(String xml) throws IOException, ParserConfigurationException, SAXException {
		if (xml != null) this.xml = new XmlResponse(xml);
	}

	/**
	 * <p>
	 * XML.
	 * </p>
	 */
	private XmlResponse xml = null;

	/**
	 * <p>
	 * XMLからブログフィードを取得する.
	 * </p>
	 * @return ブログフィード
	 * @throws XPathExpressionException
	 * @throws ParseException
	 */
	public List<BlogFeedEntity> read() throws XPathExpressionException, ParseException {

		List<BlogFeedEntity> list = new ArrayList<BlogFeedEntity>(0);
		if (xml == null) {
			return list;
		}
		
		NodeList nodeList = xml.getNodeList(XPATH_DOCUMENT);
		for (int i = 0; i < nodeList.getLength(); i++) {
			// XMLデータの取得
			Node node = nodeList.item(i);
			String documentId = XMLUtil.getChildNodeValue(DOCUMENT_DOCUMENT_ID, node);
			String authorId = XMLUtil.getChildNodeValue(DOCUMENT_AUTHOR_ID, node);
			String date = XMLUtil.getChildNodeValue(DOCUMENT_DATE, node);
			String title = XMLUtil.getChildNodeValue(DOCUMENT_TITLE, node);
			String url = XMLUtil.getChildNodeValue(DOCUMENT_URL, node);
			String body = XMLUtil.getChildNodeValue(DOCUMENT_BODY, node);

			// ブログフィードの作成
			BlogFeedEntity element = new BlogFeedEntity();
			element.setDocumentId(documentId);
			element.setAuthorId(authorId);
			element.setDate(toTimestamp(date, DATE_PATTERN));
			element.setTitle(title);
			element.setUrl(url);
			element.setBody(body);
			list.add(element);
		}

		return list;
	}

	/**
	 * <p>
	 * 文字列からタイムスタンプに変換する.
	 * </p>
	 * @param source 日付文字列
	 * @param pattern 日付パターン
	 * @return タイムスタンプ
	 * @throws ParseException
	 */
	public static Timestamp toTimestamp(String source, String pattern) throws ParseException {
		if (source == null) return null;
		Date date = toDate(source, pattern);
		return new Timestamp(date.getTime());
	}

	/**
	 * <p>
	 * 文字列から日付に変換する.
	 * </p>
	 * @param source 日付文字列
	 * @param pattern 日付パターン
	 * @return 日付
	 * @throws ParseException
	 */
	public static Date toDate(String source, String pattern) throws ParseException {
		if (source == null) return null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.parse(source);
	}
}
