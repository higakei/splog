package jp.co.hottolink.splogfilter.api.analyzer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.fusion.core.util.net.feed.FeedChannel;
import jp.co.hottolink.fusion.core.util.net.feed.FeedItem;
import jp.co.hottolink.splogfilter.api.analyzer.impl.RSSAnalyzer;
import jp.co.hottolink.splogfilter.api.xml.XMLWriter;
import jp.co.hottolink.splogfilter.common.extension.jp.co.hottolink.fusion.core.util.string.HtmlAnarizer;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.entity.BlogResultEntity;
import jp.co.hottolink.splogfilter.takeda.filter.ContentFilter;

/**
 * <p>
 * RSSを文書分析するクラス.
 * </p>
 * @author higa
 */
public class RSSDocumentAnalyzer implements RSSAnalyzer {

	/**
	 * <p>
	 * メッセージ.
	 * </p>
	 */
	private String message = null;

	/**
	 * <p>
	 * 詳細表示フラグ.
	 * </p>
	 */
	private boolean showDetail = false;

	/**
	 * <p>
	 * 分析結果.
	 * </p>
	 */
	private List<BlogResultEntity> results = null;

	/**
	 * <p>
	 * スコアからスプログレベルを取得する.
	 * </p>
	 * @param score
	 * @return スプログレベル
	 */
	private static String getSplogLevel(int score) {
		if (score < -3800) {
			return "3";
		} else if (score < -50) {
			return "2";
		} else if (score < 0) {
			return "1";
		} else {
			return "0";
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.api.analyzer.impl.RSSAnalyzer#validate(jp.co.hottolink.fusion.core.util.net.feed.FeedChannel)
	 */
	@Override
	public boolean validate(FeedChannel channel) {

		message = null;

		if (channel == null) {
			message = "no data";
			return false;
		}

		List<FeedItem> items = channel.getItems();
		if ((items == null) || items.isEmpty()) {
			message = "no data";
			return false;
		}

		for (FeedItem item: items) {
			// link
			String link = item.getLink();
			if ((link == null) || link.isEmpty()) {
				message = "link is requried";
				return false;
			}

			// description
			String description = item.getDescription();
			if ((description == null) || description.isEmpty()) {
				message = "description is requried";
				return false;
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.api.analyzer.impl.RSSAnalyzer#getMessage()
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.api.analyzer.impl.RSSAnalyzer#analyze(jp.co.hottolink.fusion.core.util.net.feed.FeedChannel)
	 */
	@Override
	public Map<String, Object> analyze(FeedChannel channel) {

		if (channel == null) {
			return null;
		}

		List<FeedItem> items = channel.getItems();
		if (items == null) {
			return null;
		}

		// 分析
		results = new ArrayList<BlogResultEntity>();
		for (FeedItem item: items) {
			BlogFeedEntity blogFeed = new BlogFeedEntity();
			blogFeed.setDocumentId(item.getLink());
			String description = item.getDescription();
			blogFeed.setBody(HtmlAnarizer.toPlaneText(description));

			// 分析
			ContentFilter filter = new ContentFilter();
			BlogResultEntity result = filter.doBlogFilter(blogFeed);
			results.add(result);
		}

		// 分析結果の作成
		List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
		for (BlogResultEntity result: results) {
			// 属性
			Map<String, Object> attributes = new LinkedHashMap<String, Object>();
			attributes.put("url", result.getDocumentId());
			attributes.put("splog", getSplogLevel(result.getScore()));
			if (showDetail) attributes.put("score", result.getScore());

			// 理由
			List<Map<String, Object>> list = null;
			List<String> causes = result.getCauses();
			if (showDetail && (causes != null) && !causes.isEmpty()) {
				list = new ArrayList<Map<String,Object>>();
				for (String cause: causes) {
					Map<String, Object> map = XMLWriter.createElement("cause", null, cause, null);
					list.add(map);
				}
			}

			Map<String, Object> document = XMLWriter.createElement("document", attributes, null, list);
			child.add(document);
		}

		return XMLWriter.createElement("result", null, null, child);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.api.analyzer.impl.RSSAnalyzer#isShowDetail()
	 */
	@Override
	public boolean isShowDetail() {
		return showDetail;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.api.analyzer.impl.RSSAnalyzer#setShowDetail(boolean)
	 */
	@Override
	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	/**
	 * <p>
	 * 分析結果を取得する.
	 * </p>
	 * @return 分析結果
	 */
	public List<BlogResultEntity> getResults() {
		return results;
	}
}
