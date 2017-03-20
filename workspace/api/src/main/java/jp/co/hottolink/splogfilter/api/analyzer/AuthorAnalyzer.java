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
import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.filter.UserAllFilter;

/**
 * <p>
 * 投稿者の分析クラス.
 * </p>
 * @author higa
 */
public class AuthorAnalyzer {

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
	private List<AuthorResultEntity> results = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param documents ドキュメント
	 * @throws ParseException
	 */
	public AuthorAnalyzer(List<Map<String, String>> documents) throws ParseException {
		blogFeeds = BlogFeedUtil.createList(documents);
	}

	/**
	 * <p>
	 * 分析を行う.
	 * </p>
	 * @return 分析結果
	 */
	public List<AuthorResultEntity> analyze() {
		UserAllFilter filter = new UserAllFilter(blogFeeds);
		results = filter.doAuthorFilter();
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
			XMLWriter.writeAuthorResult(writer, encoding, results);
			writer.flush();
		} finally {
			if (writer != null) try { writer.close(); } catch (XMLStreamException e) {}
		}
	}
}
