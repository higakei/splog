package jp.co.hottolink.splogfilter.api.analyzer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.hottolink.fusion.core.util.net.feed.FeedChannel;
import jp.co.hottolink.fusion.core.util.net.feed.FeedItem;
import jp.co.hottolink.splogfilter.api.analyzer.impl.RSSAnalyzer;
import jp.co.hottolink.splogfilter.api.xml.XMLWriter;
import jp.co.hottolink.splogfilter.common.extension.jp.co.hottolink.fusion.core.util.string.HtmlAnarizer;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.entity.AuthorResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.BlogResultEntity;
import jp.co.hottolink.splogfilter.takeda.entity.UserContentEntity;
import jp.co.hottolink.splogfilter.takeda.filter.UserAllFilter;

/**
 * <p>
 * RSSを投稿者分析するクラス.
 * </p>
 * @author higa
 */
public class RSSAuthorAnalyzer implements RSSAnalyzer {

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
	private AuthorResultEntity result = null;

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
	public String getMessage() {
		return message;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.api.analyzer.impl.RSSAnalyzer#analyze(jp.co.hottolink.fusion.core.util.net.feed.FeedChannel)
	 */
	public Map<String, Object> analyze(FeedChannel channel) {

		if (channel == null) {
			return null;
		}

		List<FeedItem> items = channel.getItems();
		if (items == null) {
			return null;
		}

		UserContentEntity userContent = new UserContentEntity();
		for (FeedItem item: items) {
			BlogFeedEntity blogFeed = new BlogFeedEntity();
			blogFeed.setDocumentId(item.getLink());
			blogFeed.setTitle(item.getTitle());
			String description = item.getDescription();
			blogFeed.setBody(HtmlAnarizer.toPlaneText(description));

			// 投稿日
			Date pubDate = item.getPubDate();
			if (pubDate == null) {
				blogFeed.setDate(null);
			} else {
				blogFeed.setDate(new Timestamp(pubDate.getTime()));
			}

			userContent.addBlogFeeds(blogFeed);
		}

		// 分析
		UserAllFilter filter = new UserAllFilter();
		result = filter.doAuthorFilter(null, userContent);

		// 原因（詳細表示のみ）
		List<Map<String, Object>> child = null;
		if (showDetail) {
			child = new ArrayList<Map<String, Object>>();

			// 投稿者
			List<String> causes = result.getCauses();
			if (causes != null) {
				for (String cause: causes) {
					Map<String, Object> element = XMLWriter.createElement("cause", null, cause, null);
					child.add(element);
				}
			}

			// 文書
			List<BlogResultEntity> documents = result.getBlogResults();
			for (BlogResultEntity document: documents) {
				causes = document.getCauses();
				if (causes == null) {
					break;
				}

				for (String cause: causes) {
					Map<String, Object> attributes = new LinkedHashMap<String, Object>();
					attributes.put("url", document.getDocumentId());
					Map<String, Object> element = XMLWriter.createElement("cause", attributes, cause, null);
					child.add(element);
				}
			}
		}

		// 属性
		Map<String, Object> attributes = new LinkedHashMap<String, Object>();
		attributes.put("splog", getSplogLevel(result.getAuthorScore()));
		if (showDetail) attributes.put("interval", result.getIntervals());
		if (showDetail) attributes.put("title", result.getTitle());
		if (showDetail) attributes.put("content", result.getContent());

		return XMLWriter.createElement("result", attributes, null, child);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.api.analyzer.impl.RSSAnalyzer#isShowDetail()
	 */
	public boolean isShowDetail() {
		return showDetail;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.api.analyzer.impl.RSSAnalyzer#setShowDetail(boolean)
	 */
	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	/**
	 * <p>
	 * 分析結果を取得する.
	 * </p>
	 * @return 分析結果
	 */
	public AuthorResultEntity getResult() {
		return result;
	}
}
