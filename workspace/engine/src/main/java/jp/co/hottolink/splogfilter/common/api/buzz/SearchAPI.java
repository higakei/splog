package jp.co.hottolink.splogfilter.common.api.buzz;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import jp.co.hottolink.fusion.core.util.net.InternetServiceClient;
import jp.co.hottolink.splogfilter.common.api.buzz.constants.BuzzAPIConstants;
import jp.co.hottolink.splogfilter.common.api.buzz.parameter.AttributeParameter;
import jp.co.hottolink.splogfilter.common.exception.APIException;
import jp.co.hottolink.splogfilter.common.resource.ResourceBundleUtil;

/**
 * <p>
 * 文書数検索のAPIクラス.
 * </p>
 * @author higa
 */
public class SearchAPI {

	/**
	 * <p>
	 * APIのパス.
	 * </p>
	 */
	private static final String API_PATH = "/search";

	/**
	 * <p>
	 * 媒体.
	 * </p>
	 */
	private String domain = null;

	/**
	 * <p>
	 * 属性AND指定.
	 * </p>
	 */
	private AttributeParameter include = null;

	/**
	 * <p>
	 * 属性NOT指定.
	 * </p>
	 */
	private AttributeParameter exclude = null;

	/**
	 * <p>
	 * APIに問い合わせる.
	 * </p>
	 * @param searchWord 検索語
	 * @param from 開始日
	 * @param to 終了日
	 * @return 問い合わせ結果のXML
	 * @throws APIException
	 */
	public String call(String searchWord, Date from, Date to) throws APIException {
		SimpleDateFormat formatter = new SimpleDateFormat(BuzzAPIConstants.DATE_FORMAT);
		return call(searchWord, formatter.format(from), formatter.format(to));
	}

	/**
	 * <p>
	 * APIに問い合わせる.
	 * </p>
	 * @param searchWord 検索語
	 * @param from 開始日
	 * @param to 終了日
	 * @return 問い合わせ結果のXML
	 * @throws APIException
	 */
	public String call(String searchWord, String from, String to) throws APIException {

		try {
			// リソースの取得
			ResourceBundle resource = ResourceBundleUtil.getBundle(BuzzAPIConstants.API_RESOURCE);

			// URLの作成
			StringBuffer url = new StringBuffer();
			url.append(resource.getString("api.url"));
			url.append(API_PATH);
			url.append("?uid=");
			url.append(resource.getString("api.uid"));
			url.append("&search_word=");
			url.append(URLEncoder.encode(searchWord, BuzzAPIConstants.URL_ENCODING));
			url.append("&from=");
			url.append(from);
			url.append("&to=");
			url.append(to);

			// 媒体
			if (domain != null) {
				url.append("&domain=");
				url.append(domain);
			}

			// 属性AND指定
			if ((include != null) && !include.isEmpty()) {
				String parameter = include.toParameter();
				url.append("&include=");
				url.append(URLEncoder.encode(parameter, BuzzAPIConstants.URL_ENCODING));
			}

			// 属性NOT指定
			if ((exclude != null) && !exclude.isEmpty()) {
				String parameter = exclude.toParameter();
				url.append("&exclude=");
				url.append(URLEncoder.encode(parameter, BuzzAPIConstants.URL_ENCODING));
			}

			// APIに問い合わせ
			InternetServiceClient client = new InternetServiceClient();
			return client.httpGet(url.toString());

		} catch (Exception e) {
			throw new APIException(e);
		}
	}

	/**
	 * <p>
	 * 媒体を取得する.
	 * </p>
	 * @return 媒体
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * <p>
	 * 媒体を設定する.
	 * </p>
	 * @param domain 媒体
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * <p>
	 * 属性AND指定を取得する.
	 * </p>
	 * @return include
	 */
	public AttributeParameter getInclude() {
		return include;
	}

	/**
	 * <p>
	 * 属性AND指定を設定する.
	 * </p>
	 * @param include 属性AND指定
	 */
	public void setInclude(AttributeParameter include) {
		this.include = include;
	}

	/**
	 * <p>
	 * 属性AND指定を設定する.
	 * </p>
	 * @param key 属性名
	 * @param value 値
	 */
	public void setInclude(String key, Object value) {
		if (include == null) include = new AttributeParameter();
		include.set(key, value);
	}

	/**
	 * <p>
	 * 属性NOT指定を取得する.
	 * </p>
	 * @return 属性NOT指定
	 */
	public AttributeParameter getExclude() {
		return exclude;
	}

	/**
	 * <p>
	 * 属性NOT指定を設定する.
	 * </p>
	 * @param exclude 属性NOT指定
	 */
	public void setExclude(AttributeParameter exclude) {
		this.exclude = exclude;
	}

	/**
	 * <p>
	 * 属性NOT指定を設定する.
	 * </p>
	 * @param key 属性名
	 * @param value 値
	 */
	public void setExclude(String key, Object value) {
		if (exclude == null) exclude = new AttributeParameter();
		exclude.set(key, value);
	}

	/**
	 * <p>
	 * 評判を設定する.
	 * </p>
	 * @param word 対象語
	 * @param reputation 評判
	 * @param parameter パラメータ(include or exclude)
	 */
	public void setReputation(String word, int reputation, int parameter) {
		if (parameter == AttributeParameter.INCLUDE) {
			if (include == null) include = new AttributeParameter();
			include.setReputation(word, reputation);
		} else if (parameter == AttributeParameter.EXCLUDE) {
			if (exclude == null) exclude = new AttributeParameter();
			exclude.setReputation(word, reputation);
		}
	}
}
