package jp.co.hottolink.splogfilter.takeda.output;

import java.io.OutputStream;
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

import jp.co.hottolink.splogfilter.takeda.copycontent.entity.CopyContentEntity;
import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.BlogResultEntity;
import jp.co.hottolink.splogfilter.takeda.xml.CopyContentResultCreator;

/**
 * <p>
 * コピーコンテンツの結果出力クラス.
 * </p>
 * @author higa
 */
public class CopyContentOutputResult implements OutputResult {

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
	public CopyContentOutputResult(List<CopyContentEntity> results) {
		this.results = results;
	}

	/* (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.output.OutputResult#getAuthorResults()
	 */
	@Deprecated
	public List<AuthorResultEntity> getAuthorResults() {
		return null;
	}

	/* (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.output.OutputResult#getBlogResults()
	 */
	@Deprecated
	public List<BlogResultEntity> getBlogResults() {
		return null;
	}


	/* (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.output.OutputResult#setShowDetail(boolean)
	 */
	@Deprecated
	public void setShowDetail(boolean showDetail) {}

	/* (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.output.OutputResult#outputtoXML(java.io.OutputStream)
	 */
	public void outputtoXML(OutputStream out) throws ParserConfigurationException, TransformerException {
		
		// 出力先を設定
		StreamResult result = new StreamResult(out);

		// XMLの作成
		CopyContentResultCreator creator = new CopyContentResultCreator(results);
		Document xml = creator.create();
		DOMSource source = new DOMSource(xml);

		// 出力フォーマットの設定
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "4");

		// 出力
		transformer.transform(source, result);		
	}
}
