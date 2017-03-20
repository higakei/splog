package jp.co.hottolink.splogfilter.takeda.xml;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jp.co.hottolink.splogfilter.takeda.copycontent.entity.CopyContentEntity;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>
 * コピーコンテンツの分析結果のXML作成クラス.
 * </p>
 * @author higa
 */
public class CopyContentResultCreator {

	/**
	 * <p>
	 * ルート(タグ名).
	 * </p>
	 */
	private static final String TAG_ROOT = "result";

	/**
	 * <p>
	 * 文書タグ(タグ名).
	 * </p>
	 */
	private static final String TAG_DOCUMENT = "document";

	/**
	 * <p>
	 * 文書ID(属性名).
	 * </p>
	 */
	private static final String ATTRIBUTE_ID = "id";

	/**
	 * <p>
	 * URL(属性名).
	 * </p>
	 */
	private static final String ATTRIBUTE_URL = "url";

	/**
	 * <p>
	 * コピー率(属性名).
	 * </p>
	 */
	private static final String ATTRIBUTE_RATE = "rate";

	/**
	 * <p>
	 * 分析結果.
	 * </p>
	 */
	private List<CopyContentEntity> results = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param results 分析結果
	 */
	public CopyContentResultCreator(List<CopyContentEntity> results) {
		this.results = results;
	}

	/**
	 * <p>
	 * コピーコンテンツの分析結果のXMLを作成する.
	 * </p>
	 * @return XML
	 * @throws ParserConfigurationException
	 */
	public Document create() throws ParserConfigurationException {

        // XMLドキュメントの作成
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        Document document = dom.createDocument("", TAG_ROOT, null);
        Element root = document.getDocumentElement();
 
        // 文書のノードを追加
        for (CopyContentEntity result: results) {
        	Element element = createElement(document, result);
            root.appendChild(element);
        }
        
        return document;
    }

	/**
	 * <p>
	 * コピーコンテンツの分析結果のエレメントを作成する.
	 * </p>
	 * @param document ドキュメント
	 * @param result 投稿者の結果
	 * @return エレメント
	 * @throws ParserConfigurationException
	 */
	private Element createElement(Document document, CopyContentEntity result) throws ParserConfigurationException {

    	// 項目の取得
		String documentId = result.getDocumentId();
    	String url = result.getUrl();
    	BigDecimal bd = new BigDecimal(result.getCopyRate());
    	BigDecimal rate = bd.setScale(3, BigDecimal.ROUND_DOWN);
 
        // 文書のエレメントの作成
       	Element element = document.createElement(TAG_DOCUMENT);

       	// 属性の設定
    	element.setAttribute(ATTRIBUTE_ID, documentId);			// 文書ID
    	element.setAttribute(ATTRIBUTE_RATE, rate.toString());	// コピー率
    	element.setAttribute(ATTRIBUTE_URL, url);				// URL
   	
    	return element;
	}
}
