package jp.co.hottolink.splogfilter.api.analyzer;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import jp.co.hottolink.splogfilter.api.util.BlogFeedUtil;
import jp.co.hottolink.splogfilter.api.xml.XMLWriter;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.entity.BlogResultEntity;
import jp.co.hottolink.splogfilter.takeda.filter.ContentFilter;

/**
 * <p>
 * 文書の分析クラス.
 * </p>
 * @author higa
 */
public class DocumentAnalyzer {

	/**
	 * <p>
	 * 分析データ.
	 * </p>
	 */
	private List<BlogFeedEntity> blogFeeds = null;

	/**
	 * <p>
	 * 分析結果.
	 * </p>
	 */
	private List<BlogResultEntity> results = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param documents ドキュメント
	 * @throws ParseException
	 */
	public DocumentAnalyzer(List<Map<String, String>> documents) throws ParseException {
		blogFeeds = BlogFeedUtil.createList(documents);
	}

	/**
	 * <p>
	 * 分析を行う.
	 * </p>
	 * @return 分析結果
	 */
	public List<BlogResultEntity> analyze() {
		ContentFilter filter = new ContentFilter(blogFeeds);
		results = filter.doBlogFilter();
		return results;
	}

	/**
	 * <p>
	 * 分析結果をXML出力する.
	 * </p>
	 * @param out 出力先
	 * @param encoding エンコーディング
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public void outputXML(OutputStream out, String encoding) throws IOException, XMLStreamException {

		XMLStreamWriter writer = null;

		try {
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			writer = factory.createXMLStreamWriter(out, encoding);
			XMLWriter.writeDocumentResult(writer, encoding, results);
			writer.flush();
		} finally {
			if (writer != null) try { writer.close(); } catch (XMLStreamException e) {}
		}
	}
}
