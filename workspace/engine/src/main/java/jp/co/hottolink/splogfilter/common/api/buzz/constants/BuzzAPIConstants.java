package jp.co.hottolink.splogfilter.common.api.buzz.constants;

/**
 * <p>
 * buzzapiの定数インターフェイス.
 * </p>
 * @author higa
 */
public interface BuzzAPIConstants {

	/**
	 * <p>
	 * APIのリソース名.
	 * </p>
	 */
	public static final String API_RESOURCE = "buzz_api";

	/**
	 * <p>
	 * 日付フォーマット.
	 * </p>
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * <p>
	 * URLのエンコーディング.
	 * </p>
	 */
	public static final String URL_ENCODING = "UTF-8";

	/**
	 * <p>
	 * 媒体：ブログ.
	 * </p>
	 */
	public static final String DOMAIN_BLOG = "blog";

	/**
	 * <p>
	 * 評判：ポジティブ.
	 * </p>
	 */
	public static final int REPUTATION_POSITIVE = 1;

	/**
	 * <p>
	 * 評判：ネガティブ.
	 * </p>
	 */
	public static final int REPUTATION_NEGATIVE = 2;

	/**
	 * <p>
	 * 評判：中立.
	 * </p>
	 */
	public static final int REPUTATION_FLAT = 3;

	/**
	 * <p>
	 * splogフィルタ(強).
	 * </p>
	 */
	public static final int SPLOG_HARD = 0x01;

	/**
	 * <p>
	 * splogフィルタ(中).
	 * </p>
	 */
	public static final int SPLOG_MEDIUM = 0x02;

	/**
	 * <p>
	 * splogフィルタ(弱).
	 * </p>
	 */
	public static final int SPLOG_SOFT = 0x03;

	/**
	 * <p>
	 * 本文重複フィルタ.
	 * </p>
	 */
	public static final int SPAM_BODY_DEPLICATED = 0x10;

	/**
	 * <p>
	 * resultStatus:PERFECT_RESULT.
	 * </p>
	 */
	public static final String RESULT_STATUS_PERFECT_RESULT = "PERFECT_RESULT";

	/**
	 * <p>
	 * resultStatus:HALFWAY_RESULT.
	 * </p>
	 */
	public static final String RESULT_STATUS_HALFWAY_RESULT = "HALFWAY_RESULT";

	/**
	 * <p>
	 * resultStatus:FAILED.
	 * </p>
	 */
	public static final String RESULT_STATUS_FAILED = "FAILED";
}
