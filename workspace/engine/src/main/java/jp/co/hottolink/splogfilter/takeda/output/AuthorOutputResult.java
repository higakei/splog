package jp.co.hottolink.splogfilter.takeda.output;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.serializer.OutputPropertiesFactory;
import org.w3c.dom.Document;

import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.BlogResultEntity;
import jp.co.hottolink.splogfilter.takeda.xml.AuthorResultCreator;

/**
 * <p>
 * 投稿者の結果出力クラス.
 * </p>
 * @author higa
 */
public class AuthorOutputResult implements OutputResult {

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param authorResults 投稿者のスコア
	 */
	public AuthorOutputResult(List<AuthorResultEntity> authorResults) {
		this.authorResults = authorResults;
	}

	/**
	 * <p>
	 * 投稿者のスコア.
	 * </p>
	 */
	private List<AuthorResultEntity> authorResults = new ArrayList<AuthorResultEntity>(0);

	/**
	 * <p>
	 * 詳細表示モード.
	 * </p>
	 */
	private boolean showDetail = false;

	/* (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.output.OutputResult#setDebug(boolean)
	 */
	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	/* (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.output.OutputResult#outputtoXML(java.io.OutputStream)
	 */
	public void outputtoXML(OutputStream out) throws ParserConfigurationException, TransformerException {

		// 出力先を設定
		StreamResult result = new StreamResult(out);

		// XMLの作成
		AuthorResultCreator creator = new AuthorResultCreator(authorResults, showDetail);
		Document xml = creator.create();
		DOMSource source = new DOMSource(xml);

		// 出力フォーマットの設定
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "4");

		// 出力
		transformer.transform(source, result);
	}

	/* (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.output.OutputResult#getAuthorResults()
	 */
	public List<AuthorResultEntity> getAuthorResults() {
		return authorResults;
	}

	/* (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.output.OutputResult#getBlogResults()
	 */
	public List<BlogResultEntity> getBlogResults() {

		List<BlogResultEntity> blogResults = new ArrayList<BlogResultEntity>();
		if (authorResults == null) {
			return blogResults;
		}

		for (AuthorResultEntity authorResult: authorResults) {
			blogResults.addAll(authorResult.getBlogResults());
		}

		return blogResults;
	}
}
