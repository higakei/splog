package jp.co.hottolink.splogfilter.takeda.constants;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * <p>
 * スパム判定語クラス.
 * </p><pre>
 * specialletters(DB)のデータを格納する
 * </pre>
 * @author higa
 */
public class ConstantWords {

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(ConstantWords.class);

	/**
	 * <p>
	 * ブログ終了語(type=10).
	 * </p>
	 */
	public static List<String> blogStopWords = new ArrayList<String>(0);

	/**
	 * <p>
	 * リンクスパムワード(type=11).
	 * </p>
	 */
	public static List<String> linkSpamWords = new ArrayList<String>(0);

	/**
	 * <p>
	 * 全角スペース(type=12).
	 * </p>
	 */
	public static String space = "　";

	/**
	 * <p>
	 * 句点(type=13).
	 * </p>
	 */
	public static String period = "。";

	/**
	 * <p>
	 * 検索系ブログのスパムワード(type=14).
	 * </p>
	 */
	public static List<String> searchBlogSpamWords = new ArrayList<String>(0);

	/**
	 * <p>
	 * タイトルスパムワード(type=15).
	 * </p>
	 */
	public static List<String> titleSpamWords = new ArrayList<String>(0);

	/**
	 * <p>
	 * タイトル除外記号パターン(type=16).
	 * </p>
	 */
	public static String titleExcludeSignPattern = "";

	/**
	 * <p>
	 * ひらがな(type=17).
	 * </p>
	 */
	public static String hirakana = "";

	/**
	 * <p>
	 * 漢字(type=18).
	 * </p>
	 */
	public static String kanji = "";

	/**
	 * <p>
	 * 記号(type=19).
	 * </p>
	 */
	public static String sign = "";

	/**
	 * <p>
	 * リンクスパムURL(type=20).
	 * </p>
	 */
	public static List<String> linkSpamUrls = new ArrayList<String>(0);

	/**
	 * <p>
	 * 全角数字(type=21).
	 * </p>
	 */
	public static String numeral = "";

	/**
	 * <p>
	 * タイトル除外ワード(type=22).
	 * </p>
	 */
	public static List<String> titleExcludeWords = new ArrayList<String>(0);

	/**
	 * <p>
	 * リンクのブログ終了語(type=23).
	 * </p>
	 */
	public static List<String> linkBlogStopWords = new ArrayList<String>(0);

	/**
	 * <p>
	 * タイトルスパム記号(type=25).
	 * </p>
	 */
	public static List<String> titleSpamSigns = new ArrayList<String>(0);

	/**
	 * <p>
	 * 全角カタカナ(type=26).
	 * </p>
	 */
	public static String katakana = "";

	/**
	 * <p>
	 * 本文スパムフレーズ(type=51).
	 * </p>
	 */
	public static List<String> contentSpamPhrases = new ArrayList<String>(0);

	/**
	 * <p>
	 * カッコ開始(type=901).
	 * </p>
	 */
	public static String parentheses = "";

	/**
	 * <p>
	 * カッコ正規表現つき(type=1001).
	 * </p>
	 */
	public static String parenthesesWithPattern = "";

	/**
	 * <p>
	 * デバッグログを出力する.
	 * </p>
	 */
	public static void dubug() {

		// ブログ終了語
		for (String constant: ConstantWords.blogStopWords) {
			logger.debug(constant);
		}
	
		// リンクスパムワード
		for (String constant: ConstantWords.linkSpamWords) {
			logger.debug(constant);
		}
	
		// 全角スペース
		logger.debug(ConstantWords.space);
	
		// 句点
		logger.debug(ConstantWords.period);
	
		// 検索系ブログのスパムワード
		for (String constant: ConstantWords.searchBlogSpamWords) {
			logger.debug(constant);
		}
	
		// タイトルスパムワード
		for (String constant: ConstantWords.titleSpamWords) {
			logger.debug(constant);
		}
	
		// タイトル除外記号パターン
		logger.debug(ConstantWords.titleExcludeSignPattern);
	
		// ひらがな
		logger.debug(ConstantWords.hirakana);
	
		// 漢字
		logger.debug(ConstantWords.kanji);
	
		// 記号
		logger.debug(ConstantWords.sign);
	
		// リンクスパムURL
		for (String constant: ConstantWords.linkSpamUrls) {
			logger.debug(constant);
		}
	
		// 全角数字
		logger.debug(ConstantWords.numeral);
	
		// タイトル除外ワード
		for (String constant: ConstantWords.titleExcludeWords) {
			logger.debug(constant);
		}
	
		// リンクのブログ終了語
		for (String constant: ConstantWords.linkBlogStopWords) {
			logger.debug(constant);
		}
	
		// タイトルスパム記号
		for (String constant: ConstantWords.titleSpamSigns) {
			logger.debug(constant);
		}
	
		// 全角カタカナ
		logger.debug(ConstantWords.katakana);
	
		// 本文スパムフレーズ
		for (String constant: ConstantWords.contentSpamPhrases) {
			logger.debug(constant);
		}
	
		// カッコ開始
		logger.debug(ConstantWords.parentheses);
	
		// カッコ正規表現つき
		logger.debug(ConstantWords.parenthesesWithPattern);
	}
}
