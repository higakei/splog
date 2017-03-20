package jp.co.hottolink.splogfilter.common.api.buzz;

import java.net.URLEncoder;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import jp.co.hottolink.fusion.core.util.net.InternetServiceClient;
import jp.co.hottolink.splogfilter.common.api.buzz.constants.BuzzAPIConstants;
import jp.co.hottolink.splogfilter.common.exception.APIException;
import jp.co.hottolink.splogfilter.common.resource.ResourceBundleUtil;

/**
 * <p>
 * 著者ID指定文書取得のAPIクラス.
 * </p>
 * @author higa
 */
public class DocumentByAuthorIdAPI {

	/**
	 * <p>
	 * APIのパス.
	 * </p>
	 */
	private static final String API_PATH = "/documentByAuthorId";

	/**
	 * <p>
	 * 取得する本文の長さ.
	 * </p>
	 */
	private Integer snippetLength = null;

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private Logger logger = Logger.getLogger(DocumentByAuthorIdAPI.class);

	/**
	 * <p>
	 * APIに問い合わせる.
	 * </p>
	 * @param searchWord 検索語
	 * @param from 開始日
	 * @param to 終了日
	 * @param include 属性AND指定
	 * @return 文書XML
	 * @throws APIException
	 */
	public String call(String authorId) throws APIException {

		try {
			// リソースの取得
			ResourceBundle resource = ResourceBundleUtil.getBundle(BuzzAPIConstants.API_RESOURCE);

			// URLの作成
			StringBuffer url = new StringBuffer();
			url.append(resource.getString("api.url"));
			url.append(API_PATH);
			url.append("?uid=");
			url.append(resource.getString("api.uid"));
			url.append("&authorId=");
			url.append(URLEncoder.encode(authorId, BuzzAPIConstants.URL_ENCODING));

			// 取得する本文の長さ
			if (snippetLength != null) {
				url.append("&snippetLength=");
				url.append(snippetLength);
			}

			// APIに問い合わせ
			InternetServiceClient client = new InternetServiceClient();
			logger.info(url);
			return client.httpGet(url.toString());

		} catch (Exception e) {
			throw new APIException(e);
		}
	}

	/**
	 * <p>
	 * 取得する本文の長さを取得する.
	 * </p>
	 * @return 取得する本文の長さ
	 */
	public Integer getSnippetLength() {
		return snippetLength;
	}

	/**
	 * <p>
	 * 取得する本文の長さを設定する.
	 * </p>
	 * @param snippetLength 取得する本文の長さ
	 */
	public void setSnippetLength(Integer snippetLength) {
		this.snippetLength = snippetLength;
	}
}
