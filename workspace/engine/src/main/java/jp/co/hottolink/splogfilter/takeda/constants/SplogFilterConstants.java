package jp.co.hottolink.splogfilter.takeda.constants;

/**
 * <p>
 * SplogFilterの定数インターフェイス.
 * </p>
 * @author higa
 */
public interface SplogFilterConstants {

	/**
	 * <p>
	 * データベース名(blogfeeds).
	 * </p>
	 */
	public static final String DB_BLOG_FEEDS = "blogfeeds";

	/**
	 * <p>
	 * スコアの最小値.
	 * </p>
	 */
	public static final int SCORE_MIN_VALUE = -100000;

	/**
	 * <p>
	 * コンテンツのスパム判定スコア.
	 * </p>
	 */
	public static final int SCORE_CONTENT_IS_SPAM = -9000;

	/**
	 * <p>
	 * 正常なブログのスコア.
	 * </p>
	 */
	public static final int SCORE_NOMAL_BLOG_FEED = 0;

	/**
	 * <p>
	 * スパム判定語のCSVファイルパス.
	 * </p>
	 */
	public static final String SPECIAL_LETTERS_CSV_FILE_PATH = "/specialletters.csv";

	/**
	 * <p>
	 * ファイルのエンコーディング.
	 * </p>
	 */
	public static final String FILE_ENCODING = "UTF-8";

	/**
	 * <p>
	 * コピーコンテンツ長.
	 * </p>
	 */
	public static final int COPY_CONTENT_LENGTH = 12;
}
