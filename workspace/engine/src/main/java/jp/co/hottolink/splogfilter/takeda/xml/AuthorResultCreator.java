package jp.co.hottolink.splogfilter.takeda.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.BlogResultEntity;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>
 * 投稿者の結果のXML作成クラス.
 * </p>
 * @author higa
 */
public class AuthorResultCreator {

	/**
	 * <p>
	 * ルート(タグ名).
	 * </p>
	 */
	private static final String TAG_ROOT = "result";

	/**
	 * <p>
	 * 投稿者タグ(タグ名).
	 * </p>
	 */
	private static final String TAG_AUTHOR = "author";

	/**
	 * <p>
	 * 投稿者ID(属性名).
	 * </p>
	 */
	private static final String ATTRIBUTE_ID = "id";

	/**
	 * <p>
	 * タイトルフィルタ(属性名).
	 * </p>
	 */
	private static final String ATTRIBUTE_TITLE = "title";

	/**
	 * <p>
	 * コンテンツフィルタ(属性名).
	 * </p>
	 */
	private static final String ATTRIBUTE_CONTENT = "content";

	/**
	 * <p>
	 * 投稿間隔フィルタ(属性名).
	 * </p>
	 */
	private static final String ATTRIBUTE_INTERVAL = "interval";

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
	 * @param authorResults 投稿者の結果
	 */
	public AuthorResultCreator(List<AuthorResultEntity> authorResults) {
		this.authorResults = authorResults;
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param authorResults 投稿者の結果
	 * @param isDebug デバッグモード
	 */
	public AuthorResultCreator(List<AuthorResultEntity> authorResults, boolean isDebug) {
		this.authorResults = authorResults;
		this.showDetail = isDebug;
	}

	/**
	 * <p>
	 * 投稿者の結果.
	 * </p>
	 */
	private List<AuthorResultEntity> authorResults = new ArrayList<AuthorResultEntity>();

	/**
	 * <p>
	 * 詳細表示モード.
	 * </p>
	 */
	private boolean showDetail = false;

	/**
	 * <p>
	 * 詳細表示モードを設定する.
	 * </p>
	 * @param showDetail 詳細表示モード
	 */
	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	/**
	 * <p>
	 * 投稿者の結果のXMLを作成する.
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
 
        // 投稿者のノードを追加
        for (AuthorResultEntity authorResult: authorResults) {
        	Element element = createElement(document, authorResult);
            root.appendChild(element);
        }
        
        return document;
    }

	/**
	 * <p>
	 * 投稿者の結果のエレメントを作成する.
	 * </p>
	 * @param document ドキュメント
	 * @param authorResult 投稿者の結果
	 * @return エレメント
	 * @throws ParserConfigurationException
	 */
	private Element createElement(Document document, AuthorResultEntity authorResult) throws ParserConfigurationException {

    	// 項目の取得
    	String authorId = authorResult.getAuthorId();
    	Integer title = authorResult.getTitle();
    	Integer content = authorResult.getContent();
    	Integer interval = authorResult.getIntervals();
    	int score = authorResult.getScore();

        // 投稿者のエレメントの作成
       	Element element = document.createElement(TAG_AUTHOR);

       	// 属性の設定
    	element.setAttribute(ATTRIBUTE_ID, authorId);											// 投稿者ID
    	if (title != null) element.setAttribute(ATTRIBUTE_TITLE, title.toString());				// タイトルフィルタ
    	if (content != null) element.setAttribute(ATTRIBUTE_CONTENT, content.toString());		// コンテンツフィルタ
    	if (interval != null) element.setAttribute(ATTRIBUTE_INTERVAL, interval.toString());	// 投稿間隔フィルタ
    	element.setAttribute(ATTRIBUTE_SCORE, String.valueOf(score));							// スコア

    	// 文書のエレメントを設定
    	if (showDetail) {
    		List<BlogResultEntity> blogResults = authorResult.getBlogResults(); 
            for (BlogResultEntity blogResult: blogResults) {
            	Element blogElement = BlogResultCreator.createElement(document, blogResult);
            	element.appendChild(blogElement);
            }
    	}
    	
    	return element;
	}
}
