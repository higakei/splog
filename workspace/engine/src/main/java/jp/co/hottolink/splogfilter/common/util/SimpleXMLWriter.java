package jp.co.hottolink.splogfilter.common.util;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * <p>
 * XML出力の簡易クラス.
 * </p>
 * @author higa
 */
public class SimpleXMLWriter {

	/**
	 * <p>
	 * XMLバージョン.
	 * </p>
	 */
	private static final String XML_VERSION = "1.0";

	/**
	 * <p>
	 * デフォルトのルートのタグ名.
	 * </p>
	 */
	private static final String DEFAULT_ROOT_TAG_NAME = "root";

	
	/**
	 * <p>
	 * デフォルトの要素のタグ名.
	 * </p>
	 */
	private static final String DEFAULT_ELEMENT_TAG_NAME = "element";

	/**
	 * <p>
	 * ドキュメント.
	 * </p>
	 */
	private List<Map<String, Object>> document = null;

	/**
	 * <p>
	 * ルートのタグ名.
	 * </p>
	 */
	private String rootTag = DEFAULT_ROOT_TAG_NAME;

	/**
	 * <p>
	 * 要素のタグ名.
	 * </p>
	 */
	private String elementTag = DEFAULT_ELEMENT_TAG_NAME;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param document ドキュメント
	 */
	public SimpleXMLWriter(List<Map<String, Object>> document) {
		this.document = document;
	}

	/**
	 * <p>
	 * ドキュメントを属性としてXML出力する.
	 * </p>
	 * @param out 出力先
	 * @param encoding エンコーディング
	 * @throws XMLStreamException
	 */
	public void writeAttributes(OutputStream out, String encoding) throws XMLStreamException {

		XMLStreamWriter writer = null;

		try {
			if (document == null) {
				return;
			}

			// XMLライターの作成
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			writer = factory.createXMLStreamWriter(out, encoding);

			writer.writeStartDocument(encoding, XML_VERSION);
			writer.writeStartElement(rootTag);
			writer.writeStartElement(elementTag);

			for (Map<String, Object> attributes: document) {
				for (String key: attributes.keySet()) {
					String value = null;
					Object object = attributes.get(key);
					if (object != null) {
						value = object.toString();
					}

					writer.writeAttribute(key, value);
				}
			}

			writer.writeEndElement();
			writer.writeEndElement();
			writer.writeEndDocument();

		} finally {
			if (writer != null) try { writer.close(); } catch (XMLStreamException e) {}
		}
	}

	/**
	 * <p>
	 * ドキュメントを要素としてXML出力する.
	 * </p>
	 * @param out 出力先
	 * @param encoding エンコーディング
	 * @throws XMLStreamException
	 */
	public void writeElements(OutputStream out, String encoding) throws XMLStreamException {

		XMLStreamWriter writer = null;

		try {
			if (document == null) {
				return;
			}

			// XMLライターの作成
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			writer = factory.createXMLStreamWriter(out, encoding);

			writer.writeStartDocument(encoding, XML_VERSION);
			writer.writeStartElement(rootTag);
			writer.writeStartElement(elementTag);

			for (Map<String, Object> elements: document) {
				for (String key: elements.keySet()) {
					String value = null;
					Object object = elements.get(key);
					if (object != null) {
						value = object.toString();
					}

					writer.writeStartElement(key);
					writer.writeCharacters(value);
					writer.writeEndElement();
				}
			}

			writer.writeEndElement();
			writer.writeEndElement();
			writer.writeEndDocument();

		} finally {
			if (writer != null) try { writer.close(); } catch (XMLStreamException e) {}
		}
	}

	/**
	 * <p>
	 * ルートのタグ名を取得する.
	 * </p>
	 * @return ルートのタグ名
	 */
	public String getRootTag() {
		return rootTag;
	}

	/**
	 * <p>
	 * ルートのタグ名を設定する.
	 * </p>
	 * @param rootTag ルートのタグ名
	 */
	public void setRootTag(String rootTag) {
		this.rootTag = rootTag;
	}

	/**
	 * <p>
	 * 要素のタグ名を取得する.
	 * </p>
	 * @return 要素のタグ名
	 */
	public String getElementTag() {
		return elementTag;
	}

	/**
	 * <p>
	 * 要素のタグ名を設定する.
	 * </p>
	 * @param elementTag 要素のタグ名
	 */
	public void setElementTag(String elementTag) {
		this.elementTag = elementTag;
	}
}
