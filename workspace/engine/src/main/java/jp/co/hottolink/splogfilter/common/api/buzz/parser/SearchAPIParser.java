package jp.co.hottolink.splogfilter.common.api.buzz.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jp.co.hottolink.splogfilter.common.exception.ParseException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * 文書数検索APIのパーサークラス.
 * </p>
 * @author higa
 */
public class SearchAPIParser extends DefaultHandler {

	/**
	 * <p>
	 * 文書数リスト.
	 * </p>
	 */
	private List<Map<String, String>> counts = null;

	/**
	 * <p>
	 * 文書数.
	 * </p>
	 */
	private Map<String, String> count = null;

	/**
	 * <p>
	 * データ.
	 * </p>
	 */
	private StringBuffer data;

	/**
	 * <p>
	 * エラーフラグ.
	 * </p>
	 */
	private boolean isError = false;

	/**
	 * <p>
	 * エラーメッセージ.
	 * </p>
	 */
	private String message = null;

	/**
	 * <p>
	 * 結果.
	 * </p>
	 */
	private Map<String, String> results = null;

	/**
	 * <p>
	 * 入力ストリームをXMLとして構文解析します.
	 * </p>
	 * @param stream 入力ストリーム
	 * @throws ParseException
	 */
	public void parse(InputStream stream) throws ParseException {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			parser.parse(stream, this);
		} catch (Exception e) {
			throw new ParseException(e);
		}
	}

	/**
	 * <p>
	 * 入力ソースをXMLとして構文解析します.
	 * </p>
	 * @param source 入力ソース
	 * @throws ParseException
	 */
	public void parse(InputSource source) throws ParseException {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			parser.parse(source, this);
		} catch (Exception e) {
			throw new ParseException(e);
		}
	}

	/**
	 * <p>
	 * エラーフラグを取得する.
	 * </p>
	 * @return エラーフラグ
	 */
	public boolean isError() {
		return isError;
	}

	/**
	 * <p>
	 * 文書数リストを取得する.
	 * </p>
	 * @return 文書数リスト
	 */
	public List<Map<String, String>> getCounts() {
		return counts;
	}

	/**
	 * <p>
	 * 文書数を取得する.
	 * </p>
	 * @return 文書数
	 */
	public Map<String, String> getCount() {
		if ((counts == null) || counts.isEmpty()) {
			return null;
		} else {
			return counts.get(0);
		}
	}

	/**
	 * <p>
	 * エラーメッセージを取得する.
	 * </p>
	 * @return エラーメッセージ
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * <p>
	 * 結果を取得する.
	 * </p>
	 * @return results
	 */
	public Map<String, String> getResults() {
		return results;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		counts = new ArrayList<Map<String,String>>(0);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		data = new StringBuffer();
		if ("count".equals(qName)) {
			count = getAttributes(attributes);
		} else if ("error".equals(qName)) {
			isError = true;
		} else if ("results".equals(qName)) {
			results = getAttributes(attributes);
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("count".equals(qName)) {
			counts.add(count);
		} else if (isError && "message".equals(qName)) {
			message = data.toString();
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		data.append(new String(ch, start, length));
	}

	/**
	 * <p>
	 * 属性を取得する.
	 * </p>
	 * @param attributes 属性
	 * @return 属性
	 */
	private static Map<String, String> getAttributes(Attributes attributes) {

		Map<String, String> map = new HashMap<String, String>();
		if (attributes == null) {
			return map;
		}

		for (int i = 0; i < attributes.getLength(); i++) {
			map.put(attributes.getQName(i), attributes.getValue(i));
		}

		return map;
	}
}
