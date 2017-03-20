package jp.co.hottolink.splogfilter.common.api.buzz;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import jp.co.hottolink.fusion.core.util.net.InternetServiceClient;
import jp.co.hottolink.splogfilter.common.api.buzz.constants.BuzzAPIConstants;
import jp.co.hottolink.splogfilter.common.api.buzz.entity.BlogWordEntity;
import jp.co.hottolink.splogfilter.common.api.buzz.parser.BlogWordAPIParser;
import jp.co.hottolink.splogfilter.common.exception.APIException;
import jp.co.hottolink.splogfilter.common.exception.ParseException;
import jp.co.hottolink.splogfilter.common.resource.ResourceBundleUtil;

import org.xml.sax.InputSource;

/**
 * <p>
 * 話題語を取得するAPIクラス.
 * </p>
 * @author higa
 */
public class BlogWordAPI {

	/**
	 * <p>
	 * APIのパス.
	 * </p>
	 */
	private static final String API_PATH = "/blogWord";

	/**
	 * <p>
	 * APIに問い合わせる.
	 * </p>
	 * @param from 開始日
	 * @param to 終了日
	 * @param num 取得数
	 * @return 話題語XML
	 * @throws APIException
	 */
	public String call(Date from, Date to, Integer num) throws APIException {

		try {
			// 初期処理
			ResourceBundle resource = ResourceBundleUtil.getBundle(BuzzAPIConstants.API_RESOURCE);
			SimpleDateFormat formatter = new SimpleDateFormat(BuzzAPIConstants.DATE_FORMAT);

			// URLの作成
			StringBuffer url = new StringBuffer();
			url.append(resource.getString("api.url"));
			url.append(API_PATH);
			url.append("?uid=");
			url.append(resource.getString("api.uid"));
			url.append("&from=");
			url.append(formatter.format(from));
			url.append("&to=");
			url.append(formatter.format(to));
			if (num != null) {
				url.append("&num=");
				url.append(num);
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
	 * 話題語を取得する.
	 * </p>
	 * @param from 開始日
	 * @param to 終了日
	 * @param num 取得数
	 * @return 話題語
	 * @throws APIException
	 * @throws ParseException
	 */
	public static List<BlogWordEntity> getBlogWords(Date from, Date to, Integer num)
			throws APIException, ParseException {

		// APIに問い合わせ
		BlogWordAPI api = new BlogWordAPI();
		String xml = api.call(from, to, num);
		StringReader reader = new StringReader(xml);
		InputSource source = new InputSource(reader);

		// XMLの解析
		BlogWordAPIParser parser = new BlogWordAPIParser();
		parser.parse(source);
		if (parser.isError()) {
			throw new APIException(parser.getMessage());
		}

		return parser.getBlogWords();
	}
	
	/**
	 * <p>
	 * 話題語を取得する.
	 * </p>
	 * @param from 開始日
	 * @param to 終了日
	 * @throws APIException
	 * @throws ParseException
	 */
	public static List<BlogWordEntity> getBlogWords(Date from, Date to)
			throws APIException, ParseException {
		return getBlogWords(from, to, null);
	}
}
