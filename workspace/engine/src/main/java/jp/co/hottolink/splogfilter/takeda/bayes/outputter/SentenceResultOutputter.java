package jp.co.hottolink.splogfilter.takeda.bayes.outputter;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import net.java.sen.Token;

import jp.co.hottolink.splogfilter.takeda.bayes.entity.MorphemeResultEntity;
import jp.co.hottolink.splogfilter.takeda.bayes.entity.SentenceResultEntity;
import jp.co.hottolink.splogfilter.util.MathUtil;
import jp.co.hottolink.splogfilter.util.XMLUtil;

/**
 * <p>
 * 文章の分析結果の出力クラス.
 * </p>
 * @author higa
 */
public class SentenceResultOutputter implements BayesFilterResultOutputter {

	/**
	 * <p>
	 * 分析結果.
	 * </p>
	 */
	private List<SentenceResultEntity> results = null;

	/**
	 * <p>
	 * 詳細表示フラグ.
	 * </p>
	 */
	private boolean isShowDetail = false;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param results 分析結果
	 */
	public SentenceResultOutputter(List<SentenceResultEntity> results) {
		this.results = results;
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param result 分析結果
	 */
	public SentenceResultOutputter(SentenceResultEntity result) {
		results = new ArrayList<SentenceResultEntity>();
		results.add(result);
	}

	/**
	 * <p>
	 * 分析結果を取得する.
	 * </p>
	 * @return 分析結果
	 */
	public List<SentenceResultEntity> getResults() {
		return results;
	}

	/**
	 * <p>
	 * 分析結果を設定する.
	 * </p>
	 * @param results 分析結果
	 */
	public void setResults(List<SentenceResultEntity> results) {
		this.results = results;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.outputter.BayesFilterResultOutputter#isShowDetail()
	 */
	@Override
	public boolean isShowDetail() {
		return isShowDetail;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.outputter.BayesFilterResultOutputter#setShowDetail(boolean)
	 */
	@Override
	public void setShowDetail(boolean isShowDetail) {
		this.isShowDetail = isShowDetail;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.bayes.outputter.BayesFilterResultOutputter#outputXML(java.io.OutputStream, java.lang.String)
	 */
	@Override
	public void outputXML(OutputStream out, String encoding) throws XMLStreamException {

		XMLStreamWriter writer = null;

		try {
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			writer = factory.createXMLStreamWriter(out, encoding);
			writer.writeStartDocument(encoding, XMLUtil.DEFAULT_XML_VERSION);
			writer.writeStartElement("results");

			for (SentenceResultEntity result: results) {
				String sentence = result.getSentence();
				boolean isSplog = result.isSplog();

				writer.writeStartElement("result");
				writer.writeStartElement("judgement");
				if (isSplog) {
					writer.writeCharacters("splog");
				} else {
					writer.writeCharacters("blog");
				}
				writer.writeEndElement();

				writer.writeStartElement("sentence");
				writer.writeCharacters(sentence);
				writer.writeEndElement();

				// 詳細表示
				if (isShowDetail) {
					writeDetail(writer, result);
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

	/**
	 * <p>
	 * 分析結果の詳細を出力する.
	 * </p>
	 * @param writer XMLライター
	 * @param result 分析結果
	 * @throws XMLStreamException
	 */
	public static void writeDetail(XMLStreamWriter writer, SentenceResultEntity result) throws XMLStreamException {

		if ((writer == null) || (result == null)) {
			return;
		}

		List<MorphemeResultEntity> likelihoods = result.getMorphemes();
		Double splogClassify = result.getSplogClassify();
		Double blogClassify = result.getBlogClassify();

		writer.writeStartElement("detail");
		writer.writeStartElement("classify");
		writer.writeAttribute("splog", MathUtil.toExponent(splogClassify));
		writer.writeAttribute("blog", MathUtil.toExponent(blogClassify));
		writer.writeEndElement();

		writer.writeStartElement("likelihoods");
		for (MorphemeResultEntity likelihood: likelihoods) {
			Token morpheme = likelihood.getMorpheme();
			Double splogLikelihood = likelihood.getSplogLikelihood();
			Double blogLikelihood = likelihood.getBlogLikelihood();

			writer.writeStartElement("likelihood");
			writer.writeAttribute("splog", splogLikelihood.toString());
			writer.writeAttribute("blog", blogLikelihood.toString());
			writer.writeCharacters(morpheme.getSurface());
			writer.writeEndElement();
		}

		writer.writeEndElement();
		writer.writeEndElement();
	}
}
