package jp.co.hottolink.splogfilter.takeda.output;

import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.BlogResultEntity;

/**
 * <p>
 * フィルタの結果出力クラス.
 * </p>
 * @author higa
 */
public interface OutputResult {

	/**
	 * <p>
	 * 詳細表示モードを設定する.
	 * </p>
	 * @param showDetail 詳細表示モード
	 */
	public void setShowDetail(boolean showDetail);

	/**
	 * <p>
	 * XMLに出力する.
	 * </p>
	 * @param out 出力先
	 */
	public void outputtoXML(OutputStream out) throws ParserConfigurationException, TransformerException;

	/**
	 * <p>
	 * 投稿者の結果を出力する.
	 * </p>
	 * @return 投稿者の結果
	 */
	public List<AuthorResultEntity> getAuthorResults();

	/**
	 * <p>
	 * ブログの結果を出力する.
	 * </p>
	 * @return ブログの結果
	 */
	public List<BlogResultEntity> getBlogResults();
}
