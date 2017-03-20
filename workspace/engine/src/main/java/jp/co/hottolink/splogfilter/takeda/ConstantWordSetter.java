package jp.co.hottolink.splogfilter.takeda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.dao.SpecialLettersDao;
import jp.co.hottolink.splogfilter.takeda.constants.ConstantWords;
import jp.co.hottolink.splogfilter.takeda.constants.SpecialLettersTypeConstants;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;

/**
 * <p>
 * スパム判定語設定クラス.
 * </p>
 * @author higa
 */
public class ConstantWordSetter {

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(ConstantWordSetter.class);

	/**
	 * <p>
	 * カンマ.
	 * </p>
	 */
	private static final String COMMA = ",";

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param executor SQL実行クラス
	 */
	public ConstantWordSetter(SQLExecutor executor) {
		this.executor = executor;
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p><pre>
	 * ファイルパスはクラスパスからの相対パス
	 * </pre>
	 * @param filePath ファイルパス
	 * @throws FileNotFoundException
	 */
	public ConstantWordSetter(String filePath) throws FileNotFoundException {
		in = getClass().getResourceAsStream(filePath);
		if (in == null) {
			throw new FileNotFoundException(filePath + "(指定されたファイルが見つかりません。)");
		}
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param file ファイル
	 * @throws FileNotFoundException
	 */
	public ConstantWordSetter(File file) throws FileNotFoundException {
		in = new FileInputStream(file);
	}

	/**
	 * <p>
	 * SQL実行クラス.
	 * </p>
	 */
	private SQLExecutor executor = null;

	/**
	 * <p>
	 * 入力ストリーム.
	 * </p>
	 */
	private InputStream in = null;

	/**
	 * <p>
	 * DBからスパム判定語を設定する.
	 * </p>
	 * @param executor SQL実行クラス
	 */
	public static void setFromDB(SQLExecutor executor) {
		ConstantWordSetter setter = new ConstantWordSetter(executor);
		setter.setFromDB();
	}

	/**
	 * <p>
	 * CSVファイルからスパム判定語を設定する.
	 * </p><pre>
	 * ファイルパスはクラスパスからの相対パス
	 * </pre>
	 * @param filePath ファイルパス
	 */
	public static void setFromCSV(String filePath) throws IOException {
		ConstantWordSetter setter = new ConstantWordSetter(filePath);
		setter.setFromCSV();
	}

	/**
	 * <p>
	 * CSVファイルからスパム判定語を設定する.
	 * </p>
	 * @param file ファイル
	 */
	public static void setFromCSV(File file) throws IOException {
		ConstantWordSetter setter = new ConstantWordSetter(file);
		setter.setFromCSV();
	}

	/**
	 * <p>
	 * DBからスパム判定語を設定する.
	 * </p>
	 */
	public void setFromDB() {
	
		SpecialLettersDao dao = new SpecialLettersDao(executor);
	
		// ブログ終了語
		ConstantWords.blogStopWords = dao.select(SpecialLettersTypeConstants.BLOG_STOP_WORD);
		for (String constant: ConstantWords.blogStopWords) {
			logger.debug(constant);
		}
	
		// リンクスパムワード
		ConstantWords.linkSpamWords = dao.select(SpecialLettersTypeConstants.LINK_SPAM_WORD);
		for (String constant: ConstantWords.linkSpamWords) {
			logger.debug(constant);
		}
	
		// 全角スペース
		List<String> space = dao.select(SpecialLettersTypeConstants.SPACE);
		if (space.size() > 0) ConstantWords.space = space.get(0);
		logger.debug(ConstantWords.space);
	
		// 句点
		List<String> period = dao.select(SpecialLettersTypeConstants.PERIOD);
		if (period.size() > 0) ConstantWords.period = period.get(0);
		logger.debug(ConstantWords.period);
	
		// 検索系ブログのスパムワード
		ConstantWords.searchBlogSpamWords = dao.select(SpecialLettersTypeConstants.SEARCH_BLOG_SPAM_WORD);
		for (String constant: ConstantWords.searchBlogSpamWords) {
			logger.debug(constant);
		}
	
		// タイトルスパムワード
		ConstantWords.titleSpamWords = dao.select(SpecialLettersTypeConstants.TITLE_SPAM_WORD);
		for (String constant: ConstantWords.titleSpamWords) {
			logger.debug(constant);
		}
	
		// タイトル除外記号パターン
		List<String> titleExcludeSignPattern = dao.select(SpecialLettersTypeConstants.TITLE_EXCLUDE_SIGN_PATTERN);
		if (titleExcludeSignPattern.size() > 0) ConstantWords.titleExcludeSignPattern = titleExcludeSignPattern.get(0);
		logger.debug(ConstantWords.titleExcludeSignPattern);
	
		// ひらがな
		List<String> hirakana = dao.select(SpecialLettersTypeConstants.HIRAKANA);
		if (hirakana.size() > 0) ConstantWords.hirakana = hirakana.get(0);
		logger.debug(ConstantWords.hirakana);
	
		// 漢字
		List<String> kanji = dao.select(SpecialLettersTypeConstants.KANJI);
		if (kanji.size() > 0) ConstantWords.kanji = kanji.get(0);
		logger.debug(ConstantWords.kanji);
	
		// 記号
		List<String> sign = dao.select(SpecialLettersTypeConstants.SIGN);
		if (sign.size() > 0) ConstantWords.sign = sign.get(0);
		logger.debug(ConstantWords.sign);
	
		// リンクスパムURL
		ConstantWords.linkSpamUrls = dao.select(SpecialLettersTypeConstants.LINK_SPAM_URL);
		for (String constant: ConstantWords.linkSpamUrls) {
			logger.debug(constant);
		}
	
		// 全角数字
		List<String> numeral = dao.select(SpecialLettersTypeConstants.NUMERAL);
		if (numeral.size() > 0) ConstantWords.numeral = numeral.get(0);
		logger.debug(ConstantWords.numeral);
	
		// タイトル除外ワード
		ConstantWords.titleExcludeWords = dao.select(SpecialLettersTypeConstants.TITLE_EXCLUDE_WORD);
		for (String constant: ConstantWords.titleExcludeWords) {
			logger.debug(constant);
		}
	
		// リンクのブログ終了語
		ConstantWords.linkBlogStopWords = dao.select(SpecialLettersTypeConstants.LINK_BLOG_STOP_WORD);
		for (String constant: ConstantWords.linkBlogStopWords) {
			logger.debug(constant);
		}
	
		// タイトルスパム記号
		ConstantWords.titleSpamSigns = dao.select(SpecialLettersTypeConstants.TITLE_SPAM_SIGN);
		for (String constant: ConstantWords.titleSpamSigns) {
			logger.debug(constant);
		}
	
		// 全角カタカナ
		List<String> katakana = dao.select(SpecialLettersTypeConstants.KATAKANA);
		if (katakana.size() > 0) ConstantWords.katakana = katakana.get(0);
		logger.debug(ConstantWords.katakana);
	
		// 本文スパムフレーズ
		ConstantWords.contentSpamPhrases = dao.select(SpecialLettersTypeConstants.CONTENT_SPAM_PHRASE);
		for (String constant: ConstantWords.contentSpamPhrases) {
			logger.debug(constant);
		}
	
		// カッコ開始
		List<String> parentheses = dao.select(SpecialLettersTypeConstants.PARENTHESES);
		if (parentheses.size() > 0) ConstantWords.parentheses = parentheses.get(0);
		logger.debug(ConstantWords.parentheses);
	
		// カッコ正規表現つき
		List<String> parenthesesWithPattern = dao.select(SpecialLettersTypeConstants.PARENTHESES_WITH_PATTERN);
		if (parenthesesWithPattern.size() > 0) ConstantWords.parenthesesWithPattern = parenthesesWithPattern.get(0);
		logger.debug(ConstantWords.parenthesesWithPattern);
	}

	/**
	 * <p>
	 * CSVファイルからスパム判定語を設定する.
	 * </p>
	 * @throws IOException
	 */
	public void setFromCSV() throws IOException {

		// ファイルリーダーの作成
		InputStreamReader reader = new InputStreamReader(in, SplogFilterConstants.FILE_ENCODING);
		BufferedReader bReader = new BufferedReader(reader);

		// ファイルの読み込み
		while (true) {
			String line = bReader.readLine();
			if (line == null) break;
			setFromLine(line);
		}

		ConstantWords.dubug();
	}

	/**
	 * <p>
	 * 行からスパム判定語を設定する.
	 * </p>
	 * @param line 行
	 */
	private void setFromLine(String line) {
		
		if (line == null) return;
		String[] split = line.split(COMMA);
		if (split.length != 2) return;
		String type = split[0];
		String word = split[1];

		// ブログ終了語
		if (String.valueOf(SpecialLettersTypeConstants.BLOG_STOP_WORD).equals(type)) {
			addWord(word, ConstantWords.blogStopWords);

		// リンクスパムワード
		} else if (String.valueOf(SpecialLettersTypeConstants.LINK_SPAM_WORD).equals(type)) {
			addWord(word, ConstantWords.linkSpamWords);

		// 全角スペース
		} else if (String.valueOf(SpecialLettersTypeConstants.SPACE).equals(type)) {
			ConstantWords.space = word;

		// 句点
		} else if (String.valueOf(SpecialLettersTypeConstants.PERIOD).equals(type)) {
			ConstantWords.period = word;

		// 検索系ブログのスパムワード
		} else if (String.valueOf(SpecialLettersTypeConstants.SEARCH_BLOG_SPAM_WORD).equals(type)) {
			addWord(word, ConstantWords.searchBlogSpamWords);

		// タイトルスパムワード
		} else if (String.valueOf(SpecialLettersTypeConstants.TITLE_SPAM_WORD).equals(type)) {
			addWord(word, ConstantWords.titleSpamWords);

		// タイトル除外記号パターン
		} else if (String.valueOf(SpecialLettersTypeConstants.TITLE_EXCLUDE_SIGN_PATTERN).equals(type)) {
			ConstantWords.titleExcludeSignPattern = word;
			
		// ひらがな
		} else if (String.valueOf(SpecialLettersTypeConstants.HIRAKANA).equals(type)) {
			ConstantWords.hirakana = word;

		// 漢字
		} else if (String.valueOf(SpecialLettersTypeConstants.KANJI).equals(type)) {
			ConstantWords.kanji = word;

		// 記号
		} else if (String.valueOf(SpecialLettersTypeConstants.SIGN).equals(type)) {
			ConstantWords.sign = word;

		// リンクスパムURL
		} else if (String.valueOf(SpecialLettersTypeConstants.LINK_SPAM_URL).equals(type)) {
			addWord(word, ConstantWords.linkSpamUrls);

		// 全角数字
		} else if (String.valueOf(SpecialLettersTypeConstants.NUMERAL).equals(type)) {
			ConstantWords.numeral = word;

		// タイトル除外ワード
		} else if (String.valueOf(SpecialLettersTypeConstants.TITLE_EXCLUDE_WORD).equals(type)) {
			addWord(word, ConstantWords.titleExcludeWords);

		// リンクのブログ終了語
		} else if (String.valueOf(SpecialLettersTypeConstants.LINK_BLOG_STOP_WORD).equals(type)) {
			addWord(word, ConstantWords.linkBlogStopWords);

		// タイトルスパム記号
		} else if (String.valueOf(SpecialLettersTypeConstants.TITLE_SPAM_SIGN).equals(type)) {
			addWord(word, ConstantWords.titleSpamSigns);

		// 全角カタカナ
		} else if (String.valueOf(SpecialLettersTypeConstants.KATAKANA).equals(type)) {
			ConstantWords.katakana = word;

		// 本文スパムフレーズ
		} else if (String.valueOf(SpecialLettersTypeConstants.CONTENT_SPAM_PHRASE).equals(type)) {
			addWord(word, ConstantWords.contentSpamPhrases);

		// カッコ開始
		} else if (String.valueOf(SpecialLettersTypeConstants.PARENTHESES).equals(type)) {
			ConstantWords.parentheses = word;

		// カッコ正規表現つき
		} else if (String.valueOf(SpecialLettersTypeConstants.PARENTHESES_WITH_PATTERN).equals(type)) {
			ConstantWords.parenthesesWithPattern = word;
		}
	}

	/**
	 * <p>
	 * リストに単語を追加する.
	 * </p>
	 * @param word 単語
	 * @param list リスト
	 */
	private void addWord(String word, List<String> list) {
		if (list == null) list = new ArrayList<String>();
		list.add(word);
	}

	public static void main(String[] args) {
		SQLExecutor executor = null;
		try {
			//executor = new SQLExecutor(SplogFilterConstants.DB_BLOG_FEEDS);
			//ConstantWordSetter.setFromDB(executor);
			ConstantWordSetter.setFromCSV(SplogFilterConstants.SPECIAL_LETTERS_CSV_FILE_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (executor != null) executor.finalize();
		}
	}
}
