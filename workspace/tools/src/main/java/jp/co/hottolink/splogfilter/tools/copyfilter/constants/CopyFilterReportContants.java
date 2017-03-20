package jp.co.hottolink.splogfilter.tools.copyfilter.constants;

/**
 * <p>
 * コピーフィルターの検証の定数インターフェイス.
 * </p>
 * @author higa
 */
public interface CopyFilterReportContants {

	/**
	 * <p>
	 * 日付フォーマット.
	 * </p>
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";

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
	 * URLのエンコーディング.
	 * </p>
	 */
	public static final String URL_ENCODING = "UTF-8";

	/**
	 * <p>
	 * APIのユーザーID.
	 * </p>
	 */
	public static final String API_UID = "splogdev";

}
