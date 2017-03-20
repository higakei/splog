package jp.co.hottolink.splogfilter.api.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import jp.co.hottolink.fusion.core.util.string.HtmlAnarizer;
import jp.co.hottolink.splogfilter.common.util.DateUtil;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;

/**
 * <p>
 * ブログフィードのUtilクラス.
 * </p>
 * @author higa
 *
 */
public class BlogFeedUtil {

	/**
	 * <p>
	 * 投稿日の日付パターン.
	 * </p>
	 */
	private static final String BLOG_DATE_PATTERN = "yyyy-M-D H:m:d";

	/**
	 * <p>
	 * ブログフィードのマップリストからオブジェクトリストを作成する.
	 * </p>
	 * @param mapList マップリスト
	 * @return オブジェクトリスト
	 */
	public static List<BlogFeedEntity> createList(List<Map<String, String>> mapList) {

		List<BlogFeedEntity> list = new ArrayList<BlogFeedEntity>(0);
		if (mapList == null) {
			return list;
		}

		for (Map<String, String> document: mapList) {
			BlogFeedEntity entity = create(document);
			list.add(entity);
		}

		return list;
	}

	/**
	 * <p>
	 * ブログフィードのマップからオブジェクトを作成する.
	 * </p>
	 * @param map マップ
	 * @return オブジェクト
	 */
	public static BlogFeedEntity create(Map<String, String> map) {

		if (map == null) {
			return null;
		}
		BlogFeedEntity entity = new BlogFeedEntity();
		entity.setDocumentId(map.get("documentId"));
		entity.setAuthorId(map.get("authorId"));
		entity.setUrl(map.get("url"));
		entity.setTitle(map.get("title"));
		//entity.setBody(HtmlAnarizer.toPlaneText(map.get("body")));
		entity.setDate(getBlogDate(map.get("date")));

		return entity;
	}

	/**
	 * <p>
	 * 投稿日を取得する.
	 * </p>
	 * @param date 日付文字列
	 * @return 投稿日
	 */
	public static Timestamp getBlogDate(String date) {
		try {
			return DateUtil.parseToTimestamp(date, BLOG_DATE_PATTERN);
		} catch (ParseException e) {
			return null;
		}
	}
}
