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
 * 文書詳細情報取得のAPIクラス.
 * </p>
 * @author higa
 */
public class DocumentAPI {

	/**
	 * <p>
	 * APIのパス.
	 * </p>
	 */
	private static final String API_PATH = "/document";

	/**
	 * <p>
	 * 本文の長さ.
	 * </p>
	 */
	private Integer snippetLength = null;

	/**
	 * <p>
	 * 取得件数.
	 * </p>
	 */
	private Integer limit = null;

	/**
	 * <p>
	 * 属性AND指定.
	 * </p>
	 */
	private AttributeParameter include = null;

	/**
	 * <p>
	 * 媒体.
	 * </p>
	 */
	private String domain = null;

	/**
	 * <p>
	 * APIに問い合わせる.
	 * </p>
	 * @param searchWord 検索語
	 * @param from 開始日
	 * @param to 終了日
	 * @return 文書XML
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
	 * @return 文書XML
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

			// AND属性
			if ((include != null) && !include.isEmpty()) {
				String parameter = include.toParameter();
				url.append("&include=");
				url.append(URLEncoder.encode(parameter, BuzzAPIConstants.URL_ENCODING));
			}

			// 本文の長さ
			if (snippetLength != null) {
				url.append("&snippetLength=");
				url.append(snippetLength);
			}

			// 取得件数
			if (limit != null) {
				url.append("&limit=");
				url.append(limit);
			}

			// 取得件数
			if (domain != null) {
				url.append("&domain=");
				url.append(domain);
			}

			// APIに問い合わせ
			InternetServiceClient client = new InternetServiceClient();
			//client.addHeader("Content-Encoding", "gzip");
			return client.httpGet(url.toString());

		} catch (Exception e) {
			throw new APIException(e);
		}
	}

	/**
	 * <p>
	 * 本文の長さを取得する.
	 * </p>
	 * @return 本文の長さ
	 */
	public Integer getSnippetLength() {
		return snippetLength;
	}

	/**
	 * <p>
	 * 本文の長さを設定する.
	 * </p>
	 * @param snippetLength 本文の長さ
	 */
	public void setSnippetLength(Integer snippetLength) {
		this.snippetLength = snippetLength;
	}

	/**
	 * <p>
	 * 取得件数を取得する.
	 * </p>
	 * @return 取得件数
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * <p>
	 * 取得件数を設定する.
	 * </p>
	 * @param limit 取得件数
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
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
	public void setInclude(String key, String value) {
		if (include == null) include = new AttributeParameter();
		include.set(key, value);
	}
}
