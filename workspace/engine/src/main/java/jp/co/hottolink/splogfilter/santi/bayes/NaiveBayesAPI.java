package jp.co.hottolink.splogfilter.santi.bayes;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import jp.co.hottolink.fusion.core.util.net.InternetServiceClient;
import jp.co.hottolink.splogfilter.common.api.BaseAPI;
import jp.co.hottolink.splogfilter.common.exception.APIException;
import jp.co.hottolink.splogfilter.common.exception.ParseException;
import jp.co.hottolink.splogfilter.common.resource.ResourceBundleUtil;

/**
 * <p>
 * NaiveBayesフィルタのAPIクラス.
 * </p>
 * @author higa
 */
public class NaiveBayesAPI extends BaseAPI {

	/**
	 * <p>
	 * APIのホスト.
	 * </p>
	 */
	private String host = null;

	/**
	 * <p>
	 * APIのパス.
	 * </p>
	 */
	private String path = null;

	/**
	 * <p>
	 * タイムアウト.
	 * </p>
	 */
	public static String timeout = null;


	/**
	 * <p>
	 * 問い合わせ結果のXML.
	 * </p>
	 */
	private String xml = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 */
	public NaiveBayesAPI() {
		ResourceBundle properties = ResourceBundleUtil.getBundle("santi_bayes");
		host = properties.getString("API_HOST");
		path = properties.getString("API_PATH");
		timeout = properties.getString("API_TIMEOUT");
	}

	/**
	 * <p>
	 * APIに問い合わせる.
	 * </p>
	 * @param title タイトル
	 * @param body 本文
	 * @return 問い合わせ結果
	 * @throws APIException
	 */
	public String call(String title, String body) throws APIException {

		try {
			// パラメータの取得
			Map<String, String> params = new HashMap<String, String>();
			params.put("timeout", timeout);
			params.put("title", title);
			params.put("body", body);

			// URLの作成
			String url = createURI(protocol, host, path).toString();

			// APIに問い合わせ
			InternetServiceClient client = new InternetServiceClient();
			xml = client.httpPost(url, params);

			return xml;

		} catch (Exception e) {
			throw new APIException(e);
		}
	}

	/**
	 * <p>
	 * APIに問い合わせる.
	 * </p>
	 * @param content コンテンツ
	 * @param body 本文
	 * @return 問い合わせ結果
	 * @throws APIException
	 */
	public String call(String content) throws APIException {

		try {
			// パラメータの取得
			Map<String, String> params = new HashMap<String, String>();
			params.put("timeout", timeout);
			params.put("body", content);

			// URLの作成
			String url = createURI(protocol, host, path).toString();

			// APIに問い合わせ
			InternetServiceClient client = new InternetServiceClient();
			xml = client.httpPost(url, params);

			return xml;

		} catch (Exception e) {
			throw new APIException(e);
		}
	}

	/**
	 * <p>
	 * 問い合わせ結果のXMLを解析する.
	 * </p>
	 * @return 問い合わせ結果
	 * @throws ParseException
	 */
	public Map<String, Object> parse() throws ParseException {
		NaiveBayesAPIParser parser = new NaiveBayesAPIParser();
		parser.parse(xml);
		return parser.getResult();
	}
}
