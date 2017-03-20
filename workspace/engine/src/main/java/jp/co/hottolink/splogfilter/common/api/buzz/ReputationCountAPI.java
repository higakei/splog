package jp.co.hottolink.splogfilter.common.api.buzz;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.splogfilter.common.api.buzz.constants.BuzzAPIConstants;
import jp.co.hottolink.splogfilter.common.api.buzz.parameter.AttributeParameter;
import jp.co.hottolink.splogfilter.common.api.buzz.parser.SearchAPIParser;
import jp.co.hottolink.splogfilter.common.exception.APIException;

import org.xml.sax.InputSource;

/**
 * <p>
 * 評判の文書数を取得するAPIクラス.
 * </p>
 * @author higa
 */
public class ReputationCountAPI {

	/**
	 * <p>
	 * 文書数リスト.
	 * </p>
	 */
	private List<Map<String, String>> counts = null;

	/**
	 * <p>
	 * 結果.
	 * </p>
	 */
	private Map<String, String> results = null;

	/**
	 * <p>
	 * 媒体.
	 * </p>
	 */
	private String domain = null;

	/**
	 * <p>
	 * splogフィルタ.
	 * </p>
	 */
	private Integer splog = null;

	/**
	 * <p>
	 * APIに問い合わせる.
	 * </p>
	 * @param searchWord 検索語
	 * @param from 開始日
	 * @param to 終了日
	 * @return 問い合わせ結果
	 * @throws APIException
	 */
	public List<Map<String, String>> call(String searchWord, Date from, Date to) throws APIException {
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
	 * @return 問い合わせ結果
	 * @throws APIException
	 */
	public List<Map<String, String>> call(String searchWord, String from, String to) throws APIException {

		// APIに問い合わせ
		SearchAPIParser positive = call(searchWord, from, to, BuzzAPIConstants.REPUTATION_POSITIVE);
		SearchAPIParser negative = call(searchWord, from, to, BuzzAPIConstants.REPUTATION_NEGATIVE);
		SearchAPIParser flat = call(searchWord, from, to, BuzzAPIConstants.REPUTATION_FLAT);
		SearchAPIParser all = call(searchWord, from, to, null);

		// 結果のマージ
		results = positive.getResults();
		boolean isPerfectResult = isPerfectResult(positive.getResults())
				&& isPerfectResult(negative.getResults())
				&& isPerfectResult(flat.getResults())
				&& isPerfectResult(all.getResults());
		if (isPerfectResult) {
			results.put("resultStatus", BuzzAPIConstants.RESULT_STATUS_PERFECT_RESULT);
		} else {
			results.put("resultStatus", BuzzAPIConstants.RESULT_STATUS_HALFWAY_RESULT);
		}

		// 評判のラベルをつける
		Map<String, Map<String,String>> pCounts = label(positive.getCounts(), BuzzAPIConstants.REPUTATION_POSITIVE);
		Map<String, Map<String,String>> nCounts = label(negative.getCounts(), BuzzAPIConstants.REPUTATION_NEGATIVE);
		Map<String, Map<String,String>> fCounts = label(flat.getCounts(), BuzzAPIConstants.REPUTATION_FLAT);
		Map<String, Map<String,String>> aCounts = label(all.getCounts(), null);

		// 文書数リストのマージ
		counts = new ArrayList<Map<String,String>>();
		for (String key: pCounts.keySet()) {
			Map<String, String> count = new HashMap<String, String>();
			count.putAll(pCounts.get(key));
			count.putAll(nCounts.get(key));
			count.putAll(fCounts.get(key));
			count.putAll(aCounts.get(key));
			counts.add(count);
		}

		return counts;
	}

	/**
	 * <p>
	 * 文書数リストを取得する.
	 * </p>
	 * @return 文書数リスト
	 */
	public List<Map<String, String>> getCounts() {
		return counts;
	}

	/**
	 * <p>
	 * 結果を取得する.
	 * </p>
	 * @return 結果
	 */
	public Map<String, String> getResults() {
		return results;
	}

	/**
	 * <p>
	 * 結果ステータスを取得する.
	 * </p>
	 * @return 結果ステータス
	 */
	public String getResultStatus() {
		if (results == null) {
			return null;
		} else {
			return results.get("resultStatus");
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
	 * splogフィルタを取得する.
	 * </p>
	 * @return splogフィルタ
	 */
	public Integer getSplog() {
		return splog;
	}

	/**
	 * <p>
	 * splogフィルタを設定する.
	 * </p>
	 * @param splog splogフィルタ
	 */
	public void setSplog(Integer splog) {
		this.splog = splog;
	}

	/**
	 * <p>
	 * 指定した評判の文書数をAPIに問い合わせる.
	 * </p>
	 * @param searchWord 検索語
	 * @param from 開始日
	 * @param to 終了日
	 * @param reputation 評判
	 * @return 問い合わせ結果
	 * @throws APIException
	 */
	private SearchAPIParser call(String searchWord, String from, String to,
			Integer reputation) throws APIException {

		// APIに問い合わせ
		SearchAPI api = new SearchAPI();
		api.setDomain(domain);
		if (splog != null) api.setExclude("splog", splog.toString());
		if (reputation != null) api.setReputation(searchWord, reputation, AttributeParameter.INCLUDE);
		String xml = api.call(searchWord, from, to);

		// XMLの解析
		StringReader reader = new StringReader(xml);
		InputSource source = new InputSource(reader);
		SearchAPIParser parser = new SearchAPIParser();
		parser.parse(source);
		if (parser.isError()) {
			throw new APIException(parser.getMessage());
		}

		return parser;
	}

	/**
	 * <p>
	 * 結果が完全かどうか判定する.
	 * </p>
	 * @param results 結果
	 * @return <code>true</code>:完全, <code>false</code>不完全
	 */
	private boolean isPerfectResult(Map<String, String> results) {
		String resultStatus = results.get("resultStatus");
		return BuzzAPIConstants.RESULT_STATUS_PERFECT_RESULT.equals(resultStatus);
	}

	/**
	 * <p>
	 * 文書数に評判のラベルをつける.
	 * </p>
	 * @param counts ラベルをつける文書数リスト
	 * @param reputation 評判
	 * @return ラベルをつけた文書数リスト
	 */
	private Map<String, Map<String,String>> label(List<Map<String,String>> counts, Integer reputation) {

		Map<String, Map<String,String>> map = new HashMap<String, Map<String,String>>();

		for (Map<String,String> count: counts) {
			String fromDate = count.get("fromDate");
			String toDate = count.get("toDate");
			Map<String,String> result = new HashMap<String, String>();
			result.put("fromDate", fromDate);
			result.put("toDate", toDate);
			result.put(getReputationLabel(reputation), count.get("value"));
			map.put(fromDate + toDate, result);
		}

		return map;
	}

	/**
	 * <p>
	 * 評判のラベルを取得する.
	 * </p>
	 * @param reputation 評判
	 * @return 評判のラベル
	 */
	private static String getReputationLabel(Integer reputation) {
		if (reputation == null) {
			return "all";
		} else if (reputation == BuzzAPIConstants.REPUTATION_POSITIVE) {
			return "positive";
		} else if (reputation == BuzzAPIConstants.REPUTATION_NEGATIVE) {
			return "negative";
		} else if (reputation == BuzzAPIConstants.REPUTATION_FLAT) {
			return "flat";
		} else {
			throw new RuntimeException("(" + reputation + ") is a invalid reputation");
		}
	}
}
