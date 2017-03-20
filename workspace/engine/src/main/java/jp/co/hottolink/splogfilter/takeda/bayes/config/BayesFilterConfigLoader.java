package jp.co.hottolink.splogfilter.takeda.bayes.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * ベイズフィルター設定ファイルをロードするクラス.
 * </p>
 * @author higa
 *
 */
public class BayesFilterConfigLoader extends DefaultHandler {

	/**
	 * <p>
	 * デフォルトの設定ファイルパス.
	 * </p>
	 */
	private static final String DEFAULT_FILE_PATH = "/bayes.xml";

	/**
	 * <p>
	 * 設定ファイルパス.
	 * </p>
	 */
	private String filePath = null;

	/**
	 * <p>
	 * 親ノード.
	 * </p>
	 */
	private String parent = null;

	/**
	 * <p>
	 * データ.
	 * </p>
	 */
	private StringBuffer data = null;

	/**
	 * <p>
	 * 条件付き確率.
	 * </p>
	 */
	private Map<String, Double> likelihoods = null;

	/**
	 * <p>
	 * 単語.
	 * </p>
	 */
	private String word = null;

	/**
	 * <p>
	 * 確率.
	 * </p>
	 */
	private Double probability = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p><pre>
	 * デフォルトの設定ファイルパスからロードする場合
	 * </pre>
	 */
	public BayesFilterConfigLoader() {
		filePath = DEFAULT_FILE_PATH;
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p><pre>
	 * 特定の設定ファイルパスからロードする場合
	 * ※ファイルパスをクラスパスからの相対パスとする
	 * </pre>
	 * @param filePath 設定ファイルパス.
	 */
	public BayesFilterConfigLoader(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * <p>
	 * 設定ファイルをロードする.
	 * </p>
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void load() throws FileNotFoundException, IOException, ParserConfigurationException, SAXException {

		// ファイルストリームの取得
		InputStream is = getClass().getResourceAsStream(filePath);
		if (is == null) {
			throw new FileNotFoundException(filePath + "(指定されたファイルが見つかりません。)");
		}

		// サックスパーサーによる解析
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(is, this);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		data = new StringBuffer();
		if ("splog".equals(qName)) {
			parent = "splog";
		} else if ("blog".equals(qName)) {
			parent = "blog";
		} else if ("likelihoods".equals(qName)) {
			likelihoods = new HashMap<String, Double>();
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		data.append(new String(ch, start, length));
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("prior".equals(qName)) {
			// 事前確率
			if ("splog".equals(parent)) {
				BayesFilterConfig.splogPrior = new Double(data.toString());
			} else if ("blog".equals(parent)) {
				BayesFilterConfig.blogPrior = new Double(data.toString());
			}
		} else if ("word".equals(qName)) {
			// 単語
			word = data.toString();
		} else if ("probability".equals(qName)) {
			// 確率
			probability = new Double(data.toString());
		} else if ("likelihood".equals(qName)) {
			// 条件付き確率
			addProbability(word, probability, likelihoods);
		} else if ("likelihoods".equals(qName)) {
			// 条件付き確率マップ
			if ("splog".equals(parent)) {
				BayesFilterConfig.splogLikelihoods = likelihoods;
			} else if ("blog".equals(parent)) {
				BayesFilterConfig.blogLikelihoods = likelihoods;
			}
		} else if ("default".equals(qName)) {
			// 条件付き確率のデフォルト
			if ("splog".equals(parent)) {
				BayesFilterConfig.splogDefaultLikelihood = new Double(data.toString());
			} else if ("blog".equals(parent)) {
				BayesFilterConfig.blogDefaultLikelihood = new Double(data.toString());
			}
		}
	}

	/**
	 * <p>
	 * 単語の確率を加算する.
	 * </p>
	 * @param word 単語
	 * @param probability 確率
	 * @param likelihoods
	 */
	private void addProbability(String word, Double probability, Map<String, Double> likelihoods) {

		if (likelihoods == null) {
			return;
		}

		// 条件付き確率に単語が存在すれば、確率を加算
		Double value = likelihoods.get(word);
		if (value != null) {
			probability = Double.valueOf(value.doubleValue() + probability.doubleValue());
		}

		likelihoods.put(word, probability);
	}
}
