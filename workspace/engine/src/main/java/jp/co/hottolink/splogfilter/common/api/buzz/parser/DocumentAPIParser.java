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
 * 文書取得APIのパーサークラス.
 * </p>
 * @author higa
 */
public class DocumentAPIParser extends DefaultHandler {

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
	 * ドキュメントリストを取得する.
	 * </p>
	 * @return ドキュメントリスト
	 */
	public List<Map<String, String>> getDocuments() {
		return documents;
	}

	/**
	 * <p>
	 * ドキュメントを取得する.
	 * </p>
	 * @return ドキュメント
	 */
	public Map<String, String> getDocument() {
		if ((documents == null) || documents.isEmpty()) {
			return null;
		} else {
			return documents.get(0);
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		documents = new ArrayList<Map<String,String>>(0);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		data = new StringBuffer();
		if ("document".equals(qName)) {
			document = new HashMap<String, String>();
		} else if ("error".equals(qName)) {
			isError = true;
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("document".equals(qName)) {
			documents.add(document);
			document = null;
		} else if (isError && "message".equals(qName)) {
			message = data.toString();
		} else {
			if (document != null) document.put(qName, data.toString());
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
	 * エラーメッセージを取得する.
	 * </p>
	 * @return エラーメッセージ
	 */
	public String getMessage() {
		return message;
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
}
