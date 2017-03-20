package jp.co.hottolink.splogfilter.takeda.bayes.outputter;

import java.io.OutputStream;

import javax.xml.stream.XMLStreamException;

/**
 * <p>
 * ベイズフィルターの分析結果出力のインターフェイス.
 * </p>
 * @author higa
 */
public interface BayesFilterResultOutputter {

	/**
	 * <p>
	 * 分析結果をXML出力する.
	 * </p>
	 * @param out 出力先
	 * @param encoding エンコーディング
	 * @throws XMLStreamException
	 */
	public void outputXML(OutputStream out, String encoding) throws XMLStreamException;

	/**
	 * <p>
	 * 詳細表示フラグを取得する.
	 * </p>
	 * @return 詳細表示フラグ
	 */
	public boolean isShowDetail();

	/**
	 * <p>
	 * 詳細表示フラグを設定する.
	 * </p>
	 * @param isShowDetail 詳細表示フラグ
	 */
	public void setShowDetail(boolean isShowDetail);
}
