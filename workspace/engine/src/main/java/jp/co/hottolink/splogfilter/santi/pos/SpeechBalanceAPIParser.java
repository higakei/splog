package jp.co.hottolink.splogfilter.santi.pos;

import java.io.StringReader;
import java.util.HashMap;
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
 * 品詞バランスフィルタAPIのパーサークラス.
 * </p>
 * @author higa
 */
public class SpeechBalanceAPIParser extends DefaultHandler {

	/**
	 * <p>
	 * 分析結果.
	 * </p>
	 */
	private Map<String, Object> result = null;

	/**
	 * <p>
	 * データ.
	 * </p>
	 */
	private StringBuffer data = null;

	/**
	 * <p>
	 * タグカウンター.
	 * </p>
	 */
	private int counter = 0;

	/**
	 * <p>
	 * 文字列をXMLとして構文解析します.
	 * </p>
	 * @param xml XML文字列
	 * @throws ParseException
	 */
	public void parse(String xml) throws ParseException {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			StringReader reader = new StringReader(xml);
			InputSource source = new InputSource(reader);
			parser.parse(source, this);
		} catch (Exception e) {
			throw new ParseException(e);
		}
	}

	/**
	 * <p>
	 * 分析結果を取得する.
	 * </p>
	 * @return 分析結果
	 */
	public Map<String, Object> getResult() {
		return result;
	}

	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		data = null;
		counter += 1;
		if ("result".equals(qName) && (counter == 1)) {
			Map<String, Object> map = getAttributes(attributes);
			result = new HashMap<String, Object>(map);
		} else if ("judge".equals(qName) && (counter == 2)) {
			data = new StringBuffer();
		} else if ("error".equals(qName) && (counter == 1)) {
			Map<String, Object> map = getAttributes(attributes);
			result = new HashMap<String, Object>(map);
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		counter -= 1;
		if ((data != null) && (result != null)) {
			result.put(qName, data.toString());
			data = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (data != null) {
			data.append(new String(ch, start, length));
		}
	}

	/**
	 * <p>
	 * 要素に付加された属性を取得する.
	 * </p>
	 * @param attributes 要素に付加された属性
	 * @return 属性マップ
	 */
	public Map<String, Object> getAttributes(Attributes attributes) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (attributes == null) {
			return map;
		}

		int length = attributes.getLength();
		for (int i = 0; i < length; i++) {
			String key = attributes.getQName(i);
			String value = attributes.getValue(i);
			map.put(key, value);
		}

		return map;
	}
}
