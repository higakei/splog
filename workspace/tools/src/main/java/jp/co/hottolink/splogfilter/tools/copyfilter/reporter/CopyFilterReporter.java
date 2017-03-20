package jp.co.hottolink.splogfilter.tools.copyfilter.reporter;

import java.io.StringReader;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.InputSource;

import jp.co.hottolink.splogfilter.common.api.buzz.DocumentAPI;
import jp.co.hottolink.splogfilter.common.api.buzz.DocumentByDocumentIdAPI;
import jp.co.hottolink.splogfilter.common.api.buzz.parser.DocumentAPIParser;
import jp.co.hottolink.splogfilter.common.exception.APIException;
import jp.co.hottolink.splogfilter.common.util.DateUtil;
import jp.co.hottolink.splogfilter.tools.copyfilter.constants.CopyFilterReportContants;
import jp.co.hottolink.splogfilter.tools.copyfilter.entity.CopyFilterReportEntity;

/**
 * <p>
 * コピーフィルターの検証結果を作成するクラス.
 * </p>
 * @author higa
 */
public class CopyFilterReporter {

	/**
	 * <p>
	 * コピーフィルターの検証結果を作成する.
	 * </p>
	 * @param result 分析結果
	 * @return 検証結果
	 * @throws ParseException 
	 * @throws APIException 
	 * @throws InterruptedException 
	 */
	public CopyFilterReportEntity report(CopyFilterReportEntity result)
			throws APIException, ParseException, InterruptedException {

		List<Map<String, String>> documents = null;
		if (result == null) {
			return null;
		}

		// 文書IDから文書を取得
		String documentId = result.getDocumentId();
		Map<String, String> document = getDocumentByDocumentId(documentId);

		// URL
		String url = document.get("url");
		result.setUrl(url);

		// 開始日・終了日
		String date = document.get("date");
		Date from = DateUtil.parseToDate(date, CopyFilterReportContants.DATE_FORMAT);

		// 属性AND指定
		String authorId = document.get("authorId");
		Map<String, String> include = new HashMap<String, String>();
		include.put("authorId", authorId);

		// splogフィルタ(強)
		documents = getDocuments(from, CopyFilterReportContants.SPLOG_HARD);
		result.setSplogHard(isSplog(documentId, documents));

		// splogフィルタ(中)
		documents = getDocuments(from, CopyFilterReportContants.SPLOG_MEDIUM);
		result.setSplogMedium(isSplog(documentId, documents));

		// splogフィルタ(弱)
		documents = getDocuments(from, CopyFilterReportContants.SPLOG_SOFT);
		result.setSplogSoft(isSplog(documentId, documents));
		Thread.sleep(1000);

		// 本文重複フィルタ
		documents = getDocuments(from, CopyFilterReportContants.SPAM_BODY_DEPLICATED);
		result.setSpamBodyDeplicated(isSplog(documentId, documents));

		return result;
	}

	/**
	 * <p>
	 *  文書IDから文書を取得する.
	 * </p>
	 * @param documentId 文書ID
	 * @return 文書
	 * @throws APIException
	 * @throws ParseException
	 */
	private Map<String, String> getDocumentByDocumentId(String documentId) {

		if (documentId == null) {
			return null;
		}

		DocumentByDocumentIdAPI api = new DocumentByDocumentIdAPI();
		String xml = api.call(documentId);

		StringReader reader = new StringReader(xml);
		InputSource source = new InputSource(reader);

		DocumentAPIParser parser = new DocumentAPIParser();
		parser.parse(source);
		if (parser.isError()) {
			throw new APIException(parser.getMessage());
		}

		return parser.getDocument();
	}

	/**
	 * <p>
	 * splogフィルタから文書を取得する.
	 * </p>
	 * @param date 検索範囲（開始日・終了日）
	 * @param splog splog属性
	 * @return 文書
	 * @throws APIException
	 * @throws ParseException
	 */
	private List<Map<String, String>> getDocuments(Date date, int splog)
			throws APIException, ParseException {

		if (date == null) {
			return null;
		}

		DocumentAPI api = new DocumentAPI();
		api.setInclude("splog", String.valueOf(splog));
		String xml = api.call(null, date, date);

		StringReader reader = new StringReader(xml);
		InputSource source = new InputSource(reader);

		DocumentAPIParser parser = new DocumentAPIParser();
		parser.parse(source);
		if (parser.isError()) {
			throw new APIException(parser.getMessage());
		}

		return parser.getDocuments();
	}

	/**
	 * <p>
	 * 指定したsplogフィルタ強度で文書をsplogかどうか判定する.
	 * </p>
	 * @param documentId 文書ID
	 * @param documents splogフィルタ強度
	 * @return true:splog, false:blog
	 */
	private boolean isSplog(String documentId, List<Map<String, String>> documents) {

		if ((documents == null) || (documentId == null)) {
			return false;
		}

		for (Map<String, String> document: documents) {
			String value = document.get("documentId");
			if (documentId.equals(value)) {
				return true;
			}
		}

		return false;
	}
}
