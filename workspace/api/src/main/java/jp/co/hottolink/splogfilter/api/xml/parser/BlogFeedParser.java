package jp.co.hottolink.splogfilter.api.xml.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jp.co.hottolink.splogfilter.common.util.DateUtil;
import jp.co.hottolink.splogfilter.common.exception.ParseException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * ブログフィードのParserクラス.
 * </p>
 * @author higa
 *
 */
public class BlogFeedParser extends DefaultHandler {

	/**
	 * <p>
	 * ドキュメントリスト.
	 * </p>
	 */
	private List<Map<String,String>> documents;

	/**
	 * <p>
	 * ドキュメント.
	 * </p>
	 */
	private Map<String,String> document;

	/**
	 * <p>
	 * データ.
	 * </p>
	 */
	private StringBuffer data;

	/**
	 * <p>
	 * バリデートフラグ.
	 * </p>
	 */
	private boolean isValidate = false;

	/**
	 * <p>
	 * 投稿者に対するバリデートフラグ.
	 * </p>
	 */
	private boolean isValidateForAuthor = false;

	/**
	 * <p>
	 * 入力ストリームをXMLとして構文解析します.
	 * </p>
	 * @param stream 入力ストリーム
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void parse(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
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
	public void parse(InputSource source) throws ParserConfigurationException, SAXException, IOException {
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

	/**
	 * <p>
	 * 投稿者に対するバリデートフラグを取得する.
	 * </p>
	 * @return 投稿者に対するバリデートフラグ
	 */
	public boolean isValidateForAuthor() {
		return isValidateForAuthor;
	}

	/**
	 * <p>
	 * 投稿者に対するバリデートフラグを設定する.
	 * </p>
	 * @param isValidateForAuthor 投稿者に対するバリデートフラグ
	 */
	public void setValidateForAuthor(boolean isValidateForAuthor) {
		this.isValidateForAuthor = isValidateForAuthor;
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
		if ("document".equals(qName)) {
			document = new HashMap<String, String>();
		} else {
			data = new StringBuffer();
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (!"document".equals(qName)) {
			if (document != null) document.put(qName, data.toString());
			return;
		}

		// バリデーション
		if (isValidateForAuthor) {
			String message = validateForAuthor(document);
			if (message != null) {
				throw new ParseException(message);
			}
		} else if (isValidate) {
			String message = validate(document);
			if (message != null) {
				throw new ParseException(message);
			}
		}

		documents.add(document);
		document = null;
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
	 * ドキュメントのバリデートを行う.
	 * </p>
	 * @param document ドキュメント
	 * @return エラーメッセージ
	 */
	private String validate(Map<String,String> document) {

		if (document == null) {
			return null;
		}

		// 文書ID
		String documentId = document.get("documentId");
		if ((documentId == null) || documentId.isEmpty()) {
			return "documentId is required";
		}

		// 投稿日
		String date = document.get("date");
		if ((date != null) && !date.isEmpty()) {
			if (!DateUtil.validate(date)) {
				return "date is invalid";
			}
		}

		return null;
	}

	/**
	 * <p>
	 * 投稿者に対するドキュメントのバリデートを行う.
	 * </p>
	 * @param document ドキュメント
	 * @return エラーメッセージ
	 */
	private String validateForAuthor(Map<String,String> document) {

		if (document == null) {
			return null;
		}

		// 投稿者ID
		String authorId = document.get("authorId");
		if ((authorId == null) || authorId.isEmpty()) {
			return "authorId is required";
		}

		return validate(document);
	}
}
