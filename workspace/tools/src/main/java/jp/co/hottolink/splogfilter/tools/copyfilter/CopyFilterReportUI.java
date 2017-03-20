package jp.co.hottolink.splogfilter.tools.copyfilter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import jp.co.hottolink.splogfilter.common.exception.ParseException;
import jp.co.hottolink.splogfilter.tools.copyfilter.entity.CopyFilterReportEntity;
import jp.co.hottolink.splogfilter.tools.copyfilter.loader.TSVLoader;
import jp.co.hottolink.splogfilter.tools.copyfilter.outputter.CSVOutputter;
import jp.co.hottolink.splogfilter.tools.copyfilter.reporter.CopyFilterReporter;
import jp.co.hottolink.splogfilter.tools.copyfilter.util.FormatUtil;

/**
 * <p>
 * コピーフィルターの検証を実行するクラス.
 * </p>
 * @author higa
 *
 */
public class CopyFilterReportUI {

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(CopyFilterReportUI.class);

	/**
	 * <p>
	 * 見出し.
	 * </p>
	 */
	private static final String[] HEADER = { "documentId", "copy_rate",
			"splog_hard", "splog_medium", "splog_soft", "spam_body_deplicated",
			"url" };

	/**
	 * <p>
	 * コピー率の表示形式.
	 * </p>
	 */
	private static final String COPY_RATE_PATTERN = "0.000";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		TSVLoader loader = null;
		CSVOutputter outputter = null;

		try {
			InputStream in = System.in;
			OutputStream out = System.out;
			String inEncoding = null;
			String outEncoding = null;

			// パラメータの取得
			for (int i = 0, j = 0; i < args.length; i++) {
				String[] splits = args[i].split("=");
				if ("--input-file".equalsIgnoreCase(splits[0])) {
					if (splits.length == 2) in = new FileInputStream(splits[1]);
				} else if("--input-encoding".equalsIgnoreCase(splits[0])) {
					if (splits.length == 2) inEncoding = splits[1];
				} else if("--output-encoding".equalsIgnoreCase(splits[0])) {
					if (splits.length == 2) outEncoding = splits[1];
				} else if (j == 0) {
					out = new FileOutputStream(args[i]);
					++j;
				}
			}

			// データなし
			if (in.available() == 0) {
				System.err.println("no data");
				System.exit(1);
				return;
			}

			// 入出力の設定
			loader = new TSVLoader(in, inEncoding);
			outputter = new CSVOutputter(out, outEncoding);

			// 見出しの出力
			outputter.outputLine(HEADER);

			int document = 0;
			int splogHard = 0;
			int splogMedium = 0;
			int splogSoft = 0;
			int spamBodyDeplicated = 0;

			// 分析結果のロード
			CopyFilterReportEntity report = null;
			for(int line = 1;;++line) {
				// 分析結果の取得
				try {
					report = loader.fetch();
					if (report == null) {
						loader.finalize();
						break;
					}
				} catch (ParseException e) {
					logger.error("failed line" + line, e);
					continue;
				}

				String documentId = report.getDocumentId();
				logger.info("line" + line + " " + documentId);

				try {
					// 検証結果の作成
					CopyFilterReporter reporter = new CopyFilterReporter();
					report = reporter.report(report);

					// 検証結果の出力
					outputter.outputLine(getItems(report));
					outputter.flush();

					// 集計
					++document;
					if (report.isSplogHard()) ++splogHard;
					if (report.isSplogMedium()) ++splogMedium;
					if (report.isSplogSoft()) ++splogSoft;
					if (report.isSpamBodyDeplicated()) ++spamBodyDeplicated;

				} catch (Exception e) {
					logger.error("failed line" + line + " " + documentId, e);
				}
			}

			// 集計結果の作成
			List<String> footer = new ArrayList<String>();
			footer.add(String.valueOf(document));
			footer.add("");
			footer.add(String.valueOf(splogHard));
			footer.add(String.valueOf(splogMedium));
			footer.add(String.valueOf(splogSoft));
			footer.add(String.valueOf(spamBodyDeplicated));
			footer.add("");

			// 集計結果の出力
			outputter.outputLine(footer);
			outputter.finalize();

			logger.info("document:" + document);
			if (document > 0) {
				logger.info("splog_hard:" + FormatUtil.formatDecimal((double)splogHard / document, "#.#%"));
				logger.info("splog_medium:" + FormatUtil.formatDecimal((double)splogMedium / document, "#.#%"));
				logger.info("splog_soft:" + FormatUtil.formatDecimal((double)splogSoft / document, "#.#%"));
				logger.info("spam_body_deplicated:" + FormatUtil.formatDecimal((double)spamBodyDeplicated / document, "#.#%"));
			}

			System.exit(0);

		} catch (Exception e) {
			logger.error("検証に失敗しました。", e);
			System.exit(1);
		} finally {
			if (loader != null) try { loader.finalize(); } catch (Exception e) {}
			if (outputter != null) try { outputter.finalize(); } catch (Exception e) {}
		}
	}

	/**
	 * <p>
	 * 検証結果の表示項目データを取得する.
	 * </p>
	 * @param report 検証結果
	 * @return 表示項目データ
	 */
	private static List<String> getItems(CopyFilterReportEntity report) {

		if (report == null) {
			return null;
		}

		List<String> list = new ArrayList<String>();
		list.add(report.getDocumentId());
		list.add(FormatUtil.formatDecimal(report.getCopyRate(), COPY_RATE_PATTERN));
		list.add(FormatUtil.formatIsSplog(report.isSplogHard()));
		list.add(FormatUtil.formatIsSplog(report.isSplogMedium()));
		list.add(FormatUtil.formatIsSplog(report.isSplogSoft()));
		list.add(FormatUtil.formatIsSplog(report.isSpamBodyDeplicated()));
		list.add(report.getUrl());

		return list;
	}
}
