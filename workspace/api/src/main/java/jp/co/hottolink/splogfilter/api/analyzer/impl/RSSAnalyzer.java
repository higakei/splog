package jp.co.hottolink.splogfilter.api.analyzer.impl;

import java.util.Map;

import jp.co.hottolink.fusion.core.util.net.feed.FeedChannel;

/**
 * <p>
 * RSSを分析するインターフェイス.
 * </p>
 * @author higa
 */
public interface RSSAnalyzer {

	/**
	 * <p>
	 * RSSチャネルをバリデートする.
	 * </p>
	 * @param channel RSSチャネル
	 * @return true:有効, false:無効
	 */
	public boolean validate(FeedChannel channel);

	/**
	 * <p>
	 * メッセージを取得する.
	 * </p>
	 * @return メッセージ
	 */
	public String getMessage();

	/**
	 * <p>
	 * RSSチャネルを分析する.
	 * </p>
	 * @param channel RSSチャネル
	 * @return 分析結果
	 */
	public Map<String, Object> analyze(FeedChannel channel);

	/**
	 * <p>
	 * 詳細表示フラグを取得する.
	 * </p>
	 * @return 詳細表示フラグ
	 */
	public boolean isShowDetail();

	/**
	 * <p>
	 * 詳細表示フラグを設定する.
	 * </p>
	 * @param showDetail 詳細表示フラグ
	 */
	public void setShowDetail(boolean showDetail);

}
