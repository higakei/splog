package jp.co.hottolink.splogfilter.common.api.buzz;

import java.net.URLEncoder;
import java.util.ResourceBundle;

import jp.co.hottolink.fusion.core.util.net.InternetServiceClient;
import jp.co.hottolink.splogfilter.common.api.buzz.constants.BuzzAPIConstants;
import jp.co.hottolink.splogfilter.common.exception.APIException;
import jp.co.hottolink.splogfilter.common.resource.ResourceBundleUtil;

/**
 * <p>
 * 文書ID指定文書取得のAPIクラス.
 * </p>
 * @author higa
 */
public class DocumentByDocumentIdAPI {

	/**
	 * <p>
	 * APIのパス.
	 * </p>
	 */
	private static final String API_PATH = "/documentByDocumentId";

	/**
	 * <p>
	 * APIに問い合わせる.
	 * </p>
	 * @param documentId 文書ID
	 * @return 文書XML
	 * @throws APIException
	 */
	public String call(String documentId) throws APIException {

		try {
			// リソースの取得
			ResourceBundle resource = ResourceBundleUtil.getBundle(BuzzAPIConstants.API_RESOURCE);

			// URLの作成
			StringBuffer url = new StringBuffer();
			url.append(resource.getString("api.url"));
			url.append(API_PATH);
			url.append("?uid=");
			url.append(resource.getString("api.uid"));
			url.append("&documentId=");
			url.append(URLEncoder.encode(documentId, BuzzAPIConstants.URL_ENCODING));

			// APIに問い合わせ
			InternetServiceClient client = new InternetServiceClient();
			return client.httpGet(url.toString());

		} catch (Exception e) {
			throw new APIException(e);
		}
	}
}
