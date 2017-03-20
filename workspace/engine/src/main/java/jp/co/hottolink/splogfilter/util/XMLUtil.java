package jp.co.hottolink.splogfilter.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 * XMLのUtilクラス
 * </p>
 * @author higa
 *
 */
public class XMLUtil {

	/**
	 * <p>
	 * デフォルトのXMLバージョン.
	 * </p>
	 */
	public static final String DEFAULT_XML_VERSION = "1.0";

	/**
	 * <p>
	 * 親ノードから指定したノード名のノード値を取得する.
	 * </p>
	 * @param nodeName ノード名
	 * @param parent 親ノード
	 * @return ノード値
	 */
	public static String getChildNodeValue(String nodeName, Node parent) {
	
		if (parent == null) {
			return null;
		}
	
		NodeList childNodes = parent.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals(nodeName)) {
					Node firstChild = node.getFirstChild();
					if ((firstChild != null) && (firstChild.getNodeType() == Node.TEXT_NODE)) {
						return firstChild.getNodeValue();
					} else {
						return null;
					}
				}
			}
		}
	
		return null;
	}

}
