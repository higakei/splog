package jp.co.hottolink.splogfilter.takeda.bayes.blogheader;

import java.io.OutputStream;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import jp.co.hottolink.splogfilter.takeda.bayes.entity.BayesFilterAnalysisEntity;
import jp.co.hottolink.splogfilter.takeda.bayes.entity.SentenceResultEntity;
import jp.co.hottolink.splogfilter.takeda.bayes.outputter.AbstractResultOutputter;
import jp.co.hottolink.splogfilter.takeda.bayes.outputter.BayesFilterResultOutputter;
import jp.co.hottolink.splogfilter.takeda.bayes.outputter.SentenceResultOutputter;
import jp.co.hottolink.splogfilter.util.XMLUtil;

/**
 * <p>
 * ブログヘッダーの分析結果の出力クラス.
 * </p>
 * @author higa
 */
public class BlogHeaderResultOutputter extends AbstractResultOutputter
		implements BayesFilterResultOutputter {

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param analyses 分析情報
	 */
	public BlogHeaderResultOutputter(
			List<BayesFilterAnalysisEntity> analyses) {
		super(analyses);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.outputter.BayesFilterResultOutputter#outputXML(java.io.OutputStream, java.lang.String)
	 */
	public void outputXML(OutputStream out, String encoding)
			throws XMLStreamException {

		XMLStreamWriter writer = null;

		try {
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			writer = factory.createXMLStreamWriter(out, encoding);
			writer.writeStartDocument(encoding, XMLUtil.DEFAULT_XML_VERSION);
			writer.writeStartElement("results");

			for (BayesFilterAnalysisEntity analysis: analyses) {
				BlogHeaderEntity data = (BlogHeaderEntity)analysis.getData();
				String url = data.getUrl();
				SentenceResultEntity result = (SentenceResultEntity)analysis.getRsult();
				boolean isSplog = result.isSplog();

				writer.writeStartElement("document");
				writer.writeAttribute("url", url);
				if (isSplog) {
					writer.writeAttribute("result", "splog");
				} else {
					writer.writeAttribute("result", "blog");
				}

				// 詳細表示
				if (isShowDetail) {
					SentenceResultOutputter.writeDetail(writer, result);
				}
				writer.writeEndElement();
			}

			writer.writeEndElement();
			writer.writeEndDocument();
			writer.flush();

		} finally {
			writer.close();
		}
	}
}
