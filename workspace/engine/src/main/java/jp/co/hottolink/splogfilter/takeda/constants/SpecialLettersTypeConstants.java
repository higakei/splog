package jp.co.hottolink.splogfilter.takeda.constants;

/**
 * <p>
 * speciallettersのtypeの定数インターフェイス.
 * </p>
 * @author higa
 */
public interface SpecialLettersTypeConstants {

	/**
	 * <p>
	 * ブログ終了語.
	 * </p><pre>
	 * 本文に出現した時点でブログ本文が終わっている可能性が高い単語
	 * </pre>
	 */
	public static final int BLOG_STOP_WORD = 10;

	/**
	 * <p>
	 * リンクスパムワード.
	 * </p><pre>
	 * 本文のリンクタグに出現するスパムワード
	 * <pre>
	 */
	public static final int LINK_SPAM_WORD = 11;

	/**
	 * <p>
	 * 全角スペース.
	 * </p>
	 */
	public static final int SPACE = 12;

	/**
	 * <p>
	 * 句点.
	 * </p>
	 */
	public static final int PERIOD = 13;

	/**
	 * <p>
	 * 検索系ブログのスパムワード.
	 * </p><pre>
	 * 検索系ブログの本文に等間隔に近い間隔で出現する
	 * <pre>
	 */
	public static final int SEARCH_BLOG_SPAM_WORD = 14;

	/**
	 * <p>
	 * タイトルスパムワード.
	 * </p>
	 */
	public static final int TITLE_SPAM_WORD = 15;

	/**
	 * <p>
	 * タイトル除外記号パターン.
	 * </p><pre>
	 * タイトルから除外する記号の正規表現
	 * </pre>
	 */
	public static final int TITLE_EXCLUDE_SIGN_PATTERN = 16;

	/**
	 * <p>
	 * ひらがな.
	 * </p>
	 */
	public static final int HIRAKANA = 17;

	/**
	 * <p>
	 * 漢字.
	 * </p>
	 */
	public static final int KANJI = 18;

	/**
	 * <p>
	 * 記号.
	 * </p>
	 */
	public static final int SIGN = 19;

	/**
	 * <p>
	 * リンクスパムURL.
	 * </p><pre>
	 * 本文のリンクURLに出現するスパムURL
	 * </pre>
	 */
	public static final int LINK_SPAM_URL = 20;

	/**
	 * <p>
	 * 全角数字.
	 * </p>
	 */
	public static final int NUMERAL = 21;

	/**
	 * <p>
	 * タイトル除外ワード.
	 * </p><pre>
	 * タイトルのスパム判定で除外する単語
	 * </pre>
	 */
	public static final int TITLE_EXCLUDE_WORD = 22;

	/**
	 * <p>
	 * リンクのブログ終了語.
	 * </p><pre>
	 * 本文のリンクタグに出現した時点でブログ本文が終わっている可能性が高い単語
	 * </pre>
	 */
	public static final int LINK_BLOG_STOP_WORD = 23;

	/**
	 * <p>
	 * タイトルスパム記号.
	 * </p>
	 */
	public static final int TITLE_SPAM_SIGN = 25;

	/**
	 * <p>
	 * 全角カタカナ.
	 * </p>
	 */
	public static final int KATAKANA = 26;

	/**
	 * <p>
	 * 本文スパムフレーズ.
	 * </p>
	 */
	public static final int CONTENT_SPAM_PHRASE = 51;
	
	/**
	 * <p>
	 * カッコ開始.
	 * </p>
	 */
	public static final int PARENTHESES = 901;

	/**
	 * <p>
	 * カッコ正規表現つき.
	 * </p>
	 */
	public static final int PARENTHESES_WITH_PATTERN = 1001;
}
