package jp.co.hottolink.splogfilter.takeda.constants;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * <p>
 * コピーフィルタの設定クラス.
 * </p>
 * @author higa
 */
public class CopyFilterConfig {

	/**
	 * <p>
	 * プロパティファイル名.
	 * </p>
	 */
	private static final String PROPERTIE_NAME = "copyfilter";

	/**
	 * <p>
	 * プロパティ名:SELECT文のブログフィードの最大取得件数.
	 * </p>
	 */
	private static final String KEY_MAX_BLOG_FEED_SELECT_SIZE = "MAX_BLOG_FEED_SELECT_SIZE";

	/**
	 * <p>
	 * プロパティ名:分析パーティションの最大ブログフィード数.
	 * </p>
	 */
	private static final String KEY_MAX_PARTITION_SIZE = "MAX_PARTITION_SIZE";

	/**
	 * <p>
	 * プロパティ名:分析プロセス数.
	 * </p>
	 */
	private static final String KEY_NUMBER_OF_ANALYZE_PROCESS = "NUMBER_OF_ANALYZE_PROCESS";

	/**
	 * <p>
	 * プロパティ名:分析プロセスの実行コマンド.
	 * </p>
	 */
	private static final String KEY_ANALYZE_PROCESS_COMMAND = "ANALYZE_PROCESS_COMMAND";

	/**
	 * <p>
	 * プロパティ名:分析2の接尾辞インデックス数の制限値.
	 * </p>
	 */
	private static final String KEY_ANALYZE2_LIMIT_INDEX_SIZE = "ANALYZE2_LIMIT_INDEX_SIZE";

	/**
	 * <p>
	 * プロパティ名:分析3の接尾辞インデックス数の制限値.
	 * </p>
	 */
	private static final String KEY_ANALYZE3_LIMIT_INDEX_SIZE = "ANALYZE3_LIMIT_INDEX_SIZE";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(CopyFilterConfig.class);

	/**
	 * <p>
	 * SELECT文のブログフィードの最大取得件数.
	 * </p>
	 */
	public static int MAX_BLOG_FEED_SELECT_SIZE = 140000;

	/**
	 * <p>
	 * 分析パーティションの最大ブログフィード数.
	 * </p>
	 */
	public static int MAX_PARTITION_SIZE = 70000;

	/**
	 * <p>
	 * 分析プロセス数.
	 * </p>
	 */
	public static int NUMBER_OF_ANALYZE_PROCESS = 1;

	/**
	 * <p>
	 * 分析プロセスの実行コマンド.
	 * </p>
	 */
	public static String ANALYZE_PROCESS_COMMAND = "./analyze_copy_content.sh";

	/**
	 * <p>
	 * 分析2の接尾辞インデックス数の制限値.
	 * </p>
	 */
	public static int ANALYZE2_LIMIT_INDEX_SIZE = 10000000;

	/**
	 * <p>
	 * 分析3の接尾辞インデックス数の制限値.
	 * </p>
	 */
	public static int ANALYZE3_LIMIT_INDEX_SIZE = 5000000;

	/**
	 * <p>
	 * 設定ファイルをロードする.
	 * </p>
	 */
	public static void load() {

		ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTIE_NAME);

		// SELECT文のブログフィードの最大取得件数
		try {
			String value = resourceBundle.getString(KEY_MAX_BLOG_FEED_SELECT_SIZE);
			MAX_BLOG_FEED_SELECT_SIZE = Integer.parseInt(value);
		} catch (Exception e) {
			logger.warn(KEY_MAX_BLOG_FEED_SELECT_SIZE, e);
		}

		// 分析パーティションの最大ブログフィード数
		try {
			String value = resourceBundle.getString(KEY_MAX_PARTITION_SIZE);
			MAX_PARTITION_SIZE = Integer.parseInt(value);
		} catch (Exception e) {
			logger.warn(KEY_MAX_PARTITION_SIZE, e);
		}

		// 分析プロセス数
		try {
			String value = resourceBundle.getString(KEY_NUMBER_OF_ANALYZE_PROCESS);
			NUMBER_OF_ANALYZE_PROCESS = Integer.parseInt(value);
		} catch (Exception e) {
			logger.warn(KEY_NUMBER_OF_ANALYZE_PROCESS, e);
		}

		// 分析プロセスの実行コマンド
		try {
			ANALYZE_PROCESS_COMMAND = resourceBundle.getString(KEY_ANALYZE_PROCESS_COMMAND);
		} catch (Exception e) {
			logger.warn(KEY_ANALYZE_PROCESS_COMMAND, e);
		}

		// 分析2の接尾辞インデックス数の制限値
		try {
			String value = resourceBundle.getString(KEY_ANALYZE2_LIMIT_INDEX_SIZE);
			ANALYZE2_LIMIT_INDEX_SIZE = Integer.parseInt(value);
		} catch (Exception e) {
			logger.warn(KEY_ANALYZE2_LIMIT_INDEX_SIZE, e);
		}

		// 分析3の接尾辞インデックス数の制限値
		try {
			String value = resourceBundle.getString(KEY_ANALYZE3_LIMIT_INDEX_SIZE);
			ANALYZE3_LIMIT_INDEX_SIZE = Integer.parseInt(value);
		} catch (Exception e) {
			logger.warn(KEY_ANALYZE3_LIMIT_INDEX_SIZE, e);
		}
	}
}
