package jp.co.hottolink.splogfilter.takeda.bayes.blogheader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jp.co.hottolink.splogfilter.common.exception.ParseException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * ブログヘッダーのParserクラス.
 * </p>
 * @author higa
 */
public class BlogHeaderParser extends DefaultHandler {

	/**
	 * <p>
	 * ドキュメントリスト.
	 * </p>
	 */
	private List<Map<String,String>> documents = null;

	/**
	 * <p>
	 * ドキュメント.
	 * </p>
	 */
	private Map<String,String> document = null;

	/**
	 * <p>
	 * データ.
	 * </p>
	 */
	private StringBuffer data = null;

	/**
	 * <p>
	 * バリデートフラグ.
	 * </p>
	 */
	private boolean isValidate = false;

	/**
	 * <p>
	 * 入力ストリームをXMLとして構文解析します.
	 * </p>
	 * @param stream 入力ストリーム
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void parse(InputStream stream) throws ParserConfigurationException,
			SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(stream, this);
	}

	/**
	 * <p>
	 * 入力ソースをXMLとして構文解析します.
	 * </p>
	 * @param source 入力ソース
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void parse(InputSource source) throws ParserConfigurationException,
			SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(source, this);
	}

	/**
	 * <p>
	 * ドキュメントリストを取得する.
	 * </p>
	 * @return ドキュメントリスト
	 */
	public List<Map<String, String>> getDocuments() {
		return documents;
	}

	/**
	 * <p>
	 * ブログヘッダーを取得する.
	 * </p>
	 * @return ブログヘッダー
	 */
	public List<BlogHeaderEntity> getBlogHeaders() {

		List<BlogHeaderEntity> list = new ArrayList<BlogHeaderEntity>();
		if (documents == null) {
			return list;
		}

		for (Map<String, String> document: documents) {
			list.add(toBlogHeaderEntity(document));
		}

		return list;
	}

	/**
	 * <p>
	 * バリデートフラグを取得する.
	 * </p>
	 * @return バリデートフラグ
	 */
	public boolean isValidate() {
		return isValidate;
	}

	/**
	 * <p>
	 * バリデートフラグを設定する.
	 * </p>
	 * @param isValidate バリデートフラグ
	 */
	public void setValidate(boolean isValidate) {
		this.isValidate = isValidate;
	}

	/* (非 Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		documents = new ArrayList<Map<String,String>>();
	}

	/* (非 Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		data = new StringBuffer();
		if ("document".equals(qName)) {
			document = new HashMap<String, String>();
		}
	}

	/* (非 Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		data.append(new String(ch, start, length));
	}

	/* (非 Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException, ParseException {

		if (!"document".equals(qName)) {
			if (document != null) document.put(qName, data.toString());
			return;
		}

		// バリデーション
		if (isValidate) {
			String message = validate(document);
			if (message != null) {
				throw new ParseException(message);
			}
		}

		documents.add(document);
		document = null;
	}

	/**
	 * <p>
	 * MapからBlogHeaderEntityに変換する.
	 * </p>
	 * @param map Map
	 * @return BlogHeaderEntity
	 */
	private BlogHeaderEntity toBlogHeaderEntity(Map<String, String> map) {

		if (map == null) {
			return null;
		}

		BlogHeaderEntity header = new BlogHeaderEntity();
		header.setUrl(map.get("url"));
		header.setHeader(map.get("header"));

		return header;
	}

	/**
	 * <p>
	 * ドキュメントのバリデートを行う.
	 * </p>
	 * @param document ドキュメント
	 * @return エラーメッセージ
	 */
	private String validate(Map<String,String> document) {

		if (document == null) {
			return null;
		}

		// URL
		String url = document.get("url");
		if ((url == null) || url.isEmpty()) {
			return "url is required";
		}

		// ブログヘッダー
		String header = document.get("header");
		if ((header == null) || header.isEmpty()) {
			return "header is required";
		}
		
		return null;
	}
}
