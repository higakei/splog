package jp.co.hottolink.splogfilter.takeda;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.extension.townleyenterprises.command.CommandParser;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;
import jp.co.hottolink.splogfilter.takeda.filter.BlogFeedFilter;
import jp.co.hottolink.splogfilter.takeda.filter.UserAllFilter;
import jp.co.hottolink.splogfilter.takeda.output.OutputResult;
import jp.co.hottolink.splogfilter.xml.BlogFeedReader;

import org.apache.log4j.Logger;

import com.townleyenterprises.command.CommandOption;
import com.townleyenterprises.command.DefaultCommandListener;

/**
 * <p>
 * コンソールによるSplogFilter実行クラス.
 * </p>
 * @author higa
 */
public class SplogFilterByCosole {

	/**
	 * <p>
	 * 入力終了.
	 * </p>
	 */
	private static final String END_OF_INPUT = "__END_OF_INPUT__";

	/**
	 * <p>
	 * ブログフィードの終了.
	 * </p>
	 */
	private static final String END_OF_BLOGFEED = "__END_OF_BLOGFEED__";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(SplogFilterByCosole.class);

	/**
	 * <p>
	 * コマンドラインパーサー.
	 * </p>
	 */
	private static CommandParser parser = null;

	/**
	 * <p>
	 * 詳細表示オプション.
	 * </p>
	 */
	private static CommandOption commandDetail = new CommandOption(
		"detail", 'd', false, null, "詳細表示" 
	);

	/**
	 * <p>
	 * 入力ファイルオプション.
	 * </p>
	 */
	private static CommandOption commandInputFile = new CommandOption(
		"input-file", (char)0, true, "FILE", "入力ファイル" 
	);

	/**
	 * <p>
	 * 入力エンコードオプション.
	 * </p>
	 */
	private static CommandOption commandInputEncoding = new CommandOption(
		"input-encoding", (char)0, true, "NAME", "入力エンコード" 
	);


	/**
	 * <p>
	 * コマンドラインオプション.
	 * </p>
	 */
	private static CommandOption[] commandOptions = {
		commandDetail, commandInputFile, commandInputEncoding
	};

	/**
	 * <p>
	 * SplogFilterを実行する.
	 * </p>
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			// 1.1 パラメータの取得
			parser = new CommandParser("splog_console.sh");
			parser.setExitOnMissingArg(true, 1);
			parser.addCommandListener(new DefaultCommandListener("オプション", commandOptions));
			parser.parse(args);
			parser.executeCommands();

			// 1.2 入力ファイルの取得
			String filePath = null;
			if (commandInputFile.getMatched()) {
				filePath = commandInputFile.getArg();
			}

			// 1.3 入力エンコーディングの取得
			String encoding = null;
			if (commandInputEncoding.getMatched()) {
				encoding = commandInputEncoding.getArg();
			}
			
			// 1.4 入力ストリームの設定
			InputStreamReader isReader = new InputStreamReader(System.in);
			if (filePath == null) {
				if (encoding != null) isReader = new InputStreamReader(System.in, encoding);
			} else {
				FileInputStream fStream = new FileInputStream(filePath);
				if (encoding == null) encoding = SplogFilterConstants.FILE_ENCODING;
				isReader = new InputStreamReader(fStream, encoding);
			}

			// 2. フィルタの設定
			BlogFeedFilter filter = new UserAllFilter();
			logger.info("----- start " + filter.getClass().getSimpleName() + " -----");

			// 3. スパム判定語の取得
			ConstantWordSetter.setFromCSV(SplogFilterConstants.SPECIAL_LETTERS_CSV_FILE_PATH);

			// 4. ブログフィードの取得し、フィルターを実行する
			BufferedReader bReader = new BufferedReader(isReader);
			StringWriter sWriter = new StringWriter();
			PrintWriter pWriter = new PrintWriter(sWriter);
			while (true) {
				String line = bReader.readLine();
				if ((line == null) || END_OF_INPUT.equals(line) || END_OF_BLOGFEED.equals(line)) {
					pWriter.flush();
					pWriter.close();
					doFilter(filter, sWriter.toString());
					if (END_OF_BLOGFEED.equals(line)) {
						sWriter = new StringWriter();
						pWriter = new PrintWriter(sWriter);
						continue;
					}
					break;
				}
				pWriter.println(line);
			}

			logger.info("----- end " + filter.getClass().getSimpleName() + " -----");
			System.exit(0);

		} catch (Exception e) {
			logger.error("", e);
			String reason = e.getMessage();
			if (reason == null) reason = e.toString();
			System.err.println("500 error(" + reason + ")");
			System.exit(1);
		}
	}

	/**
	 * <p>
	 * フィルタを実行する.
	 * </p>
	 * @param filter フィルタ
	 * @param input 入力情報
	 */
	private static void doFilter(BlogFeedFilter filter, String input) {
		try {
			// ブログフィードの取得
			BlogFeedReader reader = new BlogFeedReader(input);
			List<BlogFeedEntity> blogFeeds = reader.read();
			filter.setBlogFeeds(blogFeeds);

			// フィルタの実行
			OutputResult result = filter.doFilter();

			// 結果の出力
			System.out.println("200 sucess");
			result.setShowDetail(commandDetail.getMatched());
			result.outputtoXML(System.out);

		} catch (Exception e) {
			logger.error("", e);
			String reason = e.getMessage();
			if (reason == null) reason = e.toString();
			System.err.println("500 error(" + reason + ")");
		}
	}
}
