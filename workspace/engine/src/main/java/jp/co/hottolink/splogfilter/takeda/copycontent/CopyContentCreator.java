package jp.co.hottolink.splogfilter.takeda.copycontent;

import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.constants.ConstantWords;
import jp.co.hottolink.splogfilter.takeda.copycontent.entity.CopyContentEntity;

/**
 * <p>
 * コピーコンテンツ作成クラス.
 * </p>
 * @author higa
 */
public class CopyContentCreator {

	/**
	 * <p>
	 * ブログの終了部分の長さ.
	 * </p>
	 */
	private static final int BLOG_TAIL_LENGTH = 200;

	/**
	 * <p>
	 * ブログの終了部分のインデックスの係数.
	 * </p>
	 */
	private static final int COEFFICIENT_BLOG_TAIL_INDEX = 10 / 13;

	/**
	 * <p>
	 * コンテンツ長の係数.
	 * </p>
	 */
	private static final int COEFFICIENT_CONTENT_LENGTH = 3;

	/**
	 * <p>
	 * コンテンツのインデックスの係数.
	 * </p>
	 */
	private static final int COEFFICIENT_CONTENT_INDEX = 2;

	/**
	 * <p>
	 * コンテンツのインデックスの定数.
	 * </p>
	 */
	private static final int CONTENT_INDEX = 4;

	/**
	 * <p>
	 * ブログフィード.
	 * </p>
	 */
	private List<BlogFeedEntity> blogFeeds = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param blogFeeds ブログフィード
	 */
	public CopyContentCreator(List<BlogFeedEntity> blogFeeds) {
		this.blogFeeds = blogFeeds;
	}

	/**
	 * <p>
	 * コピーコンテンツを作成する.
	 * </p>
	 * @param blogFeed ブログフィード
	 * @param length 文字数
	 * @param isClearBody true:本文をクリアする, false:本文をクリアしない
	 * @return コピーコンテンツ
	 */
	public static CopyContentEntity create(BlogFeedEntity blogFeed, int length, boolean isClearBody) {

		if (blogFeed == null) {
			return null;
		}

		// データの取得
		String documentId = blogFeed.getDocumentId();
		String content = blogFeed.getBody();
		String authorId = blogFeed.getAuthorId();
		String url = blogFeed.getUrl();
		if (isClearBody) blogFeed.setBody(null);

		// 本文のサニタイズ
		content = sanitize(content, length);

		// コピーコンテンツの作成
		CopyContentEntity document = new CopyContentEntity(documentId);
		document.setContentAndCreateFill(content);
		document.setAuthorId(authorId);
		document.setUrl(url);

		return document;
	}

	/**
	 * <p>
	 * コピーコンテンツを作成する.
	 * </p>
	 * @param blogFeed ブログフィード
	 * @param length 文字数
	 * @return コピーコンテンツ
	 */
	public static CopyContentEntity create(BlogFeedEntity blogFeed, int length) {
		return create(blogFeed, length, false);
	}

	/**
	 * <p>
	 * コピーコンテンツを作成する.
	 * </p><pre>
	 * ブログフィードの本文をクリアする
	 * </pre>
	 * @param blogFeed ブログフィード
	 * @param length 文字数
	 * @return コピーコンテンツ
	 */
	public static CopyContentEntity createByClearBody(BlogFeedEntity blogFeed, int length) {
		return create(blogFeed, length, true);
	}

	/**
	 * <p>
	 * コピーコンテンツを作成する.
	 * </p>
	 * @param length 文字数
	 * @param isClearBody true:本文をクリアする, false:本文をクリアしない
	 * @return コピーコンテンツ
	 */
	public List<CopyContentEntity> create(int length, boolean isClearBody) {

		List<CopyContentEntity> list = new ArrayList<CopyContentEntity>(0);
		if (blogFeeds == null) {
			return list;
		}

		for (BlogFeedEntity blogFeed: blogFeeds) {
			CopyContentEntity document = create(blogFeed, length, isClearBody);
			list.add(document);
		}

		return list;
	}

	/**
	 * <p>
	 * コピーコンテンツを作成する.
	 * </p>
	 * @param length 文字数
	 * @return コピーコンテンツ
	 */
	public List<CopyContentEntity> create(int length) {
		return create(length, false);
	}

	/**
	 * <p>
	 * コピーコンテンツを作成する.
	 * </p><pre>
	 * ブログフィードの本文をクリアする
	 * </pre>
	 * @param length 文字数
	 * @return コピーコンテンツ
	 */
	public List<CopyContentEntity> createByClearBody(int length) {
		return create(length, true);
	}

	/**
	 * <p>
	 * 本文をサニタイズする.
	 * </p>
	 * @param content サニタイズする本文
	 * @param limit 文字数
	 * @return サニタイズした本文
	 */
	private static String sanitize(String content, int limit) {
	
		if (content == null) {
			return "";
		}
	
		// 改行タグを改行に置換
		content = content.replaceAll("<br>", "\n");
		content = content.replaceAll("<BR>", "\n");
		content = content.replaceAll("<br />", "\n");
		content = content.replaceAll("<BR />", "\n");

		// コード値をスペースに置換
		content = content.replaceAll("&#\\p{Digit}{0,6};", " ");
		content = content.replaceAll("&\\p{Alpha}{0,5};", " ");

		// 空白文字をスペースに置換
		content = content.replaceAll("[ \\t\\x0B\\f\\r]+", " ");

		// 改行をまとめる
		content = content.replaceAll(" \\n", "\n");
		content = content.replaceAll("\\n{2,}", "\n\n");
	
		// ブログの終了部分を除外 
		content = exclude(content);

		// 空白符号をスペースに置換
		content = content.replaceAll("&nbsp;", " ");

		// 指定した文字数の 3倍に満たない場合
		if (content.length() < limit * COEFFICIENT_CONTENT_LENGTH) {
			return "";
		}
	
		// 指定した文字数を前後に除外する
		int start = limit / COEFFICIENT_CONTENT_INDEX;
		int end = content.length() - start;
		content = content.substring(start, end);

		// 半角記号による線や、URLなどの文字列をスペースに置換
		content = content.substring(CONTENT_INDEX);
		content = content.replaceAll("(\\p{Punct}|\\p{Alpha}){20,}", " ");
	
		return content;
	}

	/**
	 * <p>
	 * ブログの終了部分を除外する.
	 * </p>
	 * @param content 本文
	 * @return 除外された本文
	 */
	private static String exclude(String content) {
	
		if (content == null) {
			return null;
		}
	
		// ブログの終了部分の位置を指定する
		int length = content.length();
		int tailIndex = length - BLOG_TAIL_LENGTH;
		if (tailIndex < 0) {
			tailIndex = 0;
		} else {
			tailIndex = Math.min(tailIndex, length * COEFFICIENT_BLOG_TAIL_INDEX);
		}
	
		// 除外位置を設定する
		String contentTail = content.substring(tailIndex);
		contentTail = contentTail.toLowerCase();
		int blogStopIndex = length;
		for (String blogStopWord: ConstantWords.blogStopWords) {
			// ブログ終了語を検索する
			int index = contentTail.indexOf(blogStopWord);
			if (index == -1) continue;
	
			// 除外位置より前にブログ終了語が出現すれば、除外位置を設定する
			index += tailIndex;
			if (index < blogStopIndex) {
				blogStopIndex = index;
			}
		}
	
		return content.substring(0, blogStopIndex);
	}
}
