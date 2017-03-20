package jp.co.hottolink.splogfilter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.townleyenterprises.command.CommandOption;
import com.townleyenterprises.command.DefaultCommandListener;

import jp.co.hottolink.splogfilter.common.api.buzz.BlogWordAPI;
import jp.co.hottolink.splogfilter.common.api.buzz.DocumentAPI;
import jp.co.hottolink.splogfilter.common.api.buzz.constants.BuzzAPIConstants;
import jp.co.hottolink.splogfilter.common.api.buzz.entity.BlogWordEntity;
import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.exception.APIException;
import jp.co.hottolink.splogfilter.dao.BlogFeedDao;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.extension.townleyenterprises.command.CommandParser;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;
import jp.co.hottolink.splogfilter.xml.BlogFeedReader;

/**
 * <p>
 * ブログフィードを登録するクラス.
 * </p>
 * @author higa
 *
 */
public class BlogFeedRegister {

	/**
	 * <p>
	 * 日付フォーマット.
	 * </p>
	 */
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * <p>
	 * 前述.
	 * </p>
	 */
	private static final String PREAMBLE = "search_word 検索語\n" +
			"  検索語未指定の場合、対象期間の話題語を検索";

	/**
	 * <p>
	 * 後述.
	 * </p>
	 */
	private static final String POSTAMBLE = "デフォルト:\n" +
			"  from                             前日\n" +
			"  to                               前日\n" +
			"  limit-word                       10件\n" +
			"  limit-document                   1,000件";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(BlogFeedRegister.class);

	/**
	 * <p>
	 * コマンドラインパーサー.
	 * </p>
	 */
	private static CommandParser parser = null;

	/**
	 * <p>
	 * 開始日オプション.
	 * </p>
	 */
	private static CommandOption commandInputFrom = new CommandOption(
		"from", (char)0, true, "date(yyyy-mm-dd)", "開始日"
	);

	/**
	 * <p>
	 * 終了日オプション.
	 * </p>
	 */
	private static CommandOption commandInputTo = new CommandOption(
		"to", (char)0, true, "date(yyyy-mm-dd)", "終了日"
	);

	/**
	 * <p>
	 * 話題語の件数オプション.
	 * </p>
	 */
	private static CommandOption commandInputLimitWord = new CommandOption(
		"limit-word", (char)0, true, "#", "話題語の件数"
	);

	/**
	 * <p>
	 * ドキュメントの件数オプション.
	 * </p>
	 */
	private static CommandOption commandInputLimitDocument = new CommandOption(
		"limit-document", (char)0, true, "#", "ドキュメントの件数"
	);

	/**
	 * <p>
	 * コマンドラインオプション.
	 * </p>
	 */
	private static CommandOption[] commandOptions = {
		commandInputFrom
		, commandInputTo
		, commandInputLimitWord
		, commandInputLimitDocument
	};

	/**
	 * <p>
	 * ブログフィードを登録する.
	 * </p>
	 * @param args
	 */
	public static void main(String[] args) {

		SQLExecutor executor = null;

		try {
			// パラメータの初期化
			Calendar calendar = Calendar.getInstance(); 
			calendar.add(Calendar.DATE, -1);
			Date from = calendar.getTime();
			Date to = calendar.getTime();
			Integer limitWord = null;
			Integer limitDocument = null;
				
			// パラメータの取得
			parser = new CommandParser("regist_blogfeed", "[search_word...]");
			parser.setExitOnMissingArg(true, 1);
			parser.addCommandListener(new DefaultCommandListener("オプション", commandOptions));
			parser.setExtraHelpText(PREAMBLE, POSTAMBLE);
			parser.parse(args);
			parser.executeCommands();
			
			// 開始日の設定
			if (commandInputFrom.getMatched()) {
				from = parse(commandInputFrom.getArg());
			}

			// 終了日の設定
			if (commandInputTo.getMatched()) {
				to = parse(commandInputTo.getArg());
			}

			// 話題語の件数の設定
			if (commandInputLimitWord.getMatched()) {
				limitWord = Integer.parseInt(commandInputLimitWord.getArg());
			}

			// ドキュメントの件数の設定
			if (commandInputLimitDocument.getMatched()) {
				limitDocument = Integer.parseInt(commandInputLimitDocument.getArg());
			}

			List<BlogWordEntity> blogWords = new ArrayList<BlogWordEntity>();
			String[] searchWords = parser.getUnhandledArguments();
			for (String searchWord: searchWords) {
				BlogWordEntity blogWord = new BlogWordEntity();
				blogWord.setWord(searchWord);
				blogWords.add(blogWord);
			}

			// 話題語の取得
			if (blogWords.isEmpty()) {
				blogWords = BlogWordAPI.getBlogWords(from, to, limitWord);
			}

			// DBに接続
			executor = new SQLExecutor(SplogFilterConstants.DB_BLOG_FEEDS);
			BlogFeedDao dao = new BlogFeedDao(executor);

			// 文書の登録
			int total = 0;
			for (BlogWordEntity blogWord: blogWords) {
				// APIの設定
				DocumentAPI api = new DocumentAPI();
				api.setDomain(BuzzAPIConstants.DOMAIN_BLOG);
				api.setSnippetLength(0);
				api.setLimit(limitDocument);

				// 文書の取得
				String xml = null;
				try {
					xml = api.call(blogWord.getWord(), from, to);
				} catch (APIException e) {
					logger.warn("文書の取得に失敗しました。", e);
					continue;
				}

				// ブログフィードの取得
				BlogFeedReader reader = new BlogFeedReader(xml);
				List<BlogFeedEntity> blogFeeds = reader.read();

				// DBに登録
				int counter = 0;
				for (BlogFeedEntity blogFeed: blogFeeds) {
					if (dao.isDocumentExist(blogFeed.getDocumentId())) continue;
					dao.insert(blogFeed);
					++counter;
				}
				logger.info(blogWord.getWord() + ":" + counter + "件");
				total += counter;
			}
			logger.info("合計:" + total + "件");
			System.exit(0);

		} catch (Exception e) {
			logger.error("", e);
			System.exit(1);
		} finally {
			if (executor != null) executor.finalize();
		}
	}

	/**
	 * <p>
	 * 文字列から日付に変換する.
	 * </p>
	 * @param string 文字列
	 * @return 日付
	 * @throws ParseException
	 */
	private static Date parse(String string) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		return formatter.parse(string);
	}
}
