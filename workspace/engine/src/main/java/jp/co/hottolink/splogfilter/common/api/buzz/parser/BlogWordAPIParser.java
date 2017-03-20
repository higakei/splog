package jp.co.hottolink.splogfilter.common.api.buzz.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jp.co.hottolink.splogfilter.common.api.buzz.entity.BlogWordEntity;
import jp.co.hottolink.splogfilter.common.exception.ParseException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * 話題語取得APIのパーサークラス.
 * </p>
 * @author higa
 */
public class BlogWordAPIParser extends DefaultHandler {

	/**
	 * <p>
	 * 話題語リスト.
	 * </p>
	 */
	private List<BlogWordEntity> blogWords = null;

	/**
	 * <p>
	 * 話題語.
	 * </p>
	 */
	private BlogWordEntity word = null;

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
	 * 話題語リストを取得する.
	 * </p>
	 * @return 話題語リスト
	 */
	public List<BlogWordEntity> getBlogWords() {
		return blogWords;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		blogWords = new ArrayList<BlogWordEntity>();
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		data = new StringBuffer();
		if ("word".equals(qName)) {
			word = new BlogWordEntity();
			word.setRank(Integer.parseInt(attributes.getValue("rank")));
			word.setCount(Integer.parseInt(attributes.getValue("count")));
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
		if ("word".equals(qName)) {
			word.setWord(data.toString());
			blogWords.add(word);
			word = null;
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
