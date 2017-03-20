package jp.co.hottolink.splogfilter.takeda.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jp.co.hottolink.splogfilter.takeda.entity.BlogResultEntity;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>
 * 文書の結果のXML作成クラス.
 * </p>
 * @author higa
 */
public class BlogResultCreator {

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
	 * スコア(属性名).
	 * </p>
	 */
	private static final String ATTRIBUTE_SCORE = "score";

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param blogResults 文書の結果
	 */
	public BlogResultCreator(List<BlogResultEntity> blogResults) {
		this.blogResults = blogResults;
	}

	/**
	 * <p>
	 * 文書の結果.
	 * </p>
	 */
	private List<BlogResultEntity> blogResults = new ArrayList<BlogResultEntity>();

	/**
	 * <p>
	 * 文書の結果のXMLを作成する.
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
        for (BlogResultEntity blogResult: blogResults) {
        	Element element = createElement(document, blogResult);
            root.appendChild(element);
        }
        
        return document;
    }

	/**
	 * <p>
	 * 文書の結果のエレメントを作成する.
	 * </p>
	 * @param document ドキュメント
	 * @param blogResult 文書の結果
	 * @return エレメント
	 * @throws ParserConfigurationException
	 */
	public static Element createElement(Document document, BlogResultEntity blogResult) throws ParserConfigurationException {

    	// 項目の取得
    	String documentId = blogResult.getDocumentId();
    	int score = blogResult.getScore();

        // 文書のエレメントの作成
       	Element element = document.createElement(TAG_DOCUMENT);

       	// 属性の設定
    	element.setAttribute(ATTRIBUTE_ID, documentId);					// 文書ID
    	element.setAttribute(ATTRIBUTE_SCORE, String.valueOf(score));	// スコア

    	return element;
	}
}
