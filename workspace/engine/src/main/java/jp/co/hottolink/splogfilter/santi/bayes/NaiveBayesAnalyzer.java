package jp.co.hottolink.splogfilter.santi.bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.fusion.core.util.net.feed.FeedItem;
import jp.co.hottolink.splogfilter.common.exception.APIException;
import jp.co.hottolink.splogfilter.common.exception.ParseException;

/**
 * <p>
 * NaiveBayesフィルタの分析クラス.
 * </p>
 * @author higa
 */
public class NaiveBayesAnalyzer {

	/**
	 * <p>
	 * 分析結果.
	 * </p>
	 */
	private List<Map<String, Object>> results = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 */
	public NaiveBayesAnalyzer() {
		results = new ArrayList<Map<String,Object>>();
	}

	/**
	 * <p>
	 * 分析を行う.
	 * </p>
	 * @param title タイトル
	 * @param body 本文
	 * @return 分析結果
	 * @throws APIException
	 */
	public Map<String, Object> analyze(String title, String body) throws APIException, ParseException {

		// APIに問い合わせ
		NaiveBayesAPI api = new NaiveBayesAPI();
		api.call(title, body);
		Map<String, Object> result = api.parse();

		// 分析結果を作成
		result.put("title", title);
		result.put("body", body);
		results.add(result);

		return result;
	}

	/**
	 * <p>
	 * 分析を行う.
	 * </p>
	 * @param content コンテンツ
	 * @return 分析結果
	 * @throws APIException
	 * @throws ParseException
	 */
	public Map<String, Object> analyze(String content) throws APIException, ParseException {

		// APIに問い合わせ
		NaiveBayesAPI api = new NaiveBayesAPI();
		api.call(content);
		Map<String, Object> result = api.parse();

		// 入力情報を追加
		result.put("content", content);
		results.add(result);

		return result;
	}

	/**
	 * <p>
	 * 分析を行う.
	 * </p>
	 * @param item RSSアイテム
	 * @return 分析結果
	 * @throws APIException
	 * @throws ParseException
	 */
	public Map<String, Object> analyze(FeedItem item) throws APIException, ParseException {

		if (item == null) {
			return null;
		}

		// APIに問い合わせ
		NaiveBayesAPI api = new NaiveBayesAPI();
		api.call(item.getTitle(), item.getDescription());
		Map<String, Object> result = api.parse();

		// 入力情報を追加
		result.put("item", item);
		results.add(result);

		return result;
	}

	/**
	 * <p>
	 * 分析結果を取得する.
	 * </p>
	 * @return 分析結果
	 */
	public List<Map<String, Object>> getResults() {
		return results;
	}

	/**
	 * <p>
	 * 分析エラー結果を追加する.
	 * </p>
	 * @param result 分析結果
	 */
	public void addErrorResult(Throwable throwable, FeedItem item) {
		Map<String, Object> result = createErrorResult(throwable, item);
		results.add(result);
	}

	/**
	 * <p>
	 * 分析エラー結果を作成する.
	 * </p>
	 * @param throwable エラーまたは例外
	 * @param item RSSアイテム
	 * @return 分析エラー結果
	 */
	private Map<String, Object> createErrorResult(Throwable throwable, FeedItem item) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cause", getCause(throwable));
		result.put("item", item);
		return result;
	}

	/**
	 * <p>
	 * エラー原因を取得する.
	 * </p>
	 * @param throwable エラーまたは例外
	 * @return エラー原因
	 */
	private String getCause(Throwable throwable) {

		if (throwable == null) {
			return null;
		}

		String cause = "analyze fialed";
		if (!(throwable instanceof APIException)) {
			return cause;
		}

		int type = ((APIException)throwable).getType();
		if (type == APIException.TIMEOUT) {
			return "timeout";
		}

		return cause;
	}
}
