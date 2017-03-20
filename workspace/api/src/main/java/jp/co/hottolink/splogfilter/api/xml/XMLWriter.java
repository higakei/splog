package jp.co.hottolink.splogfilter.api.xml;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.BlogResultEntity;

/**
 * <p>
 * XML作成クラス.
 * </p>
 * @author higa
 */
public class XMLWriter {

	/**
	 * <p>
	 * XMLバージョン.
	 * </p>
	 */
	private static final String XML_VERSION = "1.0";

	/**
	 * <p>
	 * タグ名.
	 * </p>
	 */
	private static final String TAG_NAME = "localName";

	/**
	 * <p>
	 * エレメントの属性.
	 * </p>
	 */
	private static final String ELEMENT_ATTRIBUTES = "attributes";

	/**
	 * <p>
	 * エレメントの値.
	 * </p>
	 */
	private static final String ELEMENT_VALUE = "value";

	/**
	 * <p>
	 * 子エレメント.
	 * </p>
	 */
	private static final String CHILD_ELEMENTS = "child";

	/**
	 * <p>
	 * 投稿者の分析結果のXMLを作成する.
	 * </p>
	 * @param writer XMLライター
	 * @param encoding エンコーディング
	 * @param results 分析結果
	 * @throws XMLStreamException
	 */
	public static void writeAuthorResult(XMLStreamWriter writer, String encoding, List<AuthorResultEntity> results)
			throws XMLStreamException {

		if (writer == null) {
			return;
		}

		writer.writeStartDocument(encoding, XML_VERSION);
		writer.writeStartElement("result");

		for (AuthorResultEntity author: results) {
			// 投稿者のスコア
			writer.writeStartElement("author");
			writer.writeAttribute("id", author.getAuthorId());
			writer.writeAttribute("score", String.valueOf(author.getScore()));
			writer.writeAttribute("interval", author.getIntervals().toString());
			writer.writeAttribute("title", author.getTitle().toString());
			writer.writeAttribute("content", author.getContent().toString());

			// ブログのスコア
			List<BlogResultEntity> documents = author.getBlogResults();
			for (BlogResultEntity document: documents) {
				writer.writeStartElement("document");
				writer.writeAttribute("id", document.getDocumentId());
				writer.writeAttribute("score", String.valueOf(document.getScore()));
				writer.writeEndElement();
			}

			writer.writeEndElement();
		}

		writer.writeEndElement();
		writer.writeEndDocument();
	}

	/**
	 * <p>
	 * 文書の分析結果のXMLを作成する.
	 * </p>
	 * @param writer XMLライター
	 * @param encoding エンコーディング
	 * @param results 分析結果
	 * @throws XMLStreamException
	 */
	public static void writeDocumentResult(XMLStreamWriter writer, String encoding, List<BlogResultEntity> results)
			throws XMLStreamException {

		if (writer == null) {
			return;
		}

		writer.writeStartDocument(encoding, XML_VERSION);
		writer.writeStartElement("result");

		for (BlogResultEntity document: results) {
			writer.writeStartElement("document");
			writer.writeAttribute("id", document.getDocumentId());
			writer.writeAttribute("score", String.valueOf(document.getScore()));
			writer.writeEndElement();
		}

		writer.writeEndElement();
		writer.writeEndDocument();
	}

	/**
	 * <p>
	 * 分析エラー時のXMLを作成する.
	 * </p>
	 * @param writer XMLライター
	 * @param encoding エンコーディング
	 * @param message エラーメッセージ
	 * @throws XMLStreamException
	 */
	public static void writeError(XMLStreamWriter writer, String encoding, String message)
			throws XMLStreamException {

		if (writer == null) {
			return;
		}

		writer.writeStartDocument(encoding, XML_VERSION);
		writer.writeStartElement("error");
		writer.writeStartElement("message");
		writer.writeCharacters(message);
		writer.writeEndElement();
		writer.writeEndElement();
		writer.writeEndDocument();
	}

	/**
	 * <p>
	 * マップのXMLを作成する.
	 * </p>
	 * @param writer XMLライター
	 * @param String エンコーディング
	 * @param element エレメント
	 * @throws XMLStreamException
	 */
	public static void outputXML(OutputStream out, String encoding, Map<String, Object> element)
			throws XMLStreamException {

		XMLStreamWriter writer = null;

		try {
			// XMLライターの作成
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			writer = factory.createXMLStreamWriter(out, encoding);

			// XML出力
			writer.writeStartDocument(encoding, XMLWriter.XML_VERSION);
			XMLWriter.writeElement(writer, element);
			writer.writeEndDocument();
			writer.flush();

		} finally {
			if (writer != null) try { writer.close(); } catch (XMLStreamException e) {}
		}
	}

	/**
	 * <p>
	 * エレメントのXMLを作成する.
	 * </p>
	 * @param writer XMLライター
	 * @param element エレメント
	 * @throws XMLStreamException
	 */
	@SuppressWarnings("unchecked")
	public static void writeElement(XMLStreamWriter writer, Map<String, Object> element) throws XMLStreamException {

		if ((writer == null) || (element == null)) {
			return;
		}

		String localName = (String)element.get(TAG_NAME);
		Map<String, Object> attributes = (Map<String, Object>)element.get(ELEMENT_ATTRIBUTES);
		Object value = element.get(ELEMENT_VALUE);
		List<Map<String, Object>> child = (List<Map<String, Object>>)element.get(CHILD_ELEMENTS);

		writer.writeStartElement(localName);

		// 属性
		if (attributes != null) {
			for (String key: attributes.keySet()) {
				Object attribute = attributes.get(key);
				if (attribute != null) writer.writeAttribute(key, attribute.toString());
			}
		}

		// 値
		if (value != null) {
			writer.writeCharacters(value.toString());
		}

		// 子エレメント
		if (child != null) {
			for (Map<String, Object> map: child) {
				writeElement(writer, map);
			}
		}

		writer.writeEndElement();
	}

	/**
	 * <p>
	 * XML出力用のエレメントを作成する.
	 * </p>
	 * @param tagName タグ名
	 * @param attributes エレメントの属性
	 * @param value エレメントの値
	 * @param child 子エレメント
	 * @return エレメント
	 */
	public static Map<String, Object> createElement(String tagName, Map<String, Object> attributes
			, Object value, List<Map<String, Object>> child) {
		Map<String, Object> element = new HashMap<String, Object>();
		element.put(TAG_NAME, tagName);
		element.put(ELEMENT_ATTRIBUTES, attributes);
		element.put(ELEMENT_VALUE, value);
		element.put(CHILD_ELEMENTS, child);
		return element;
	}
}
