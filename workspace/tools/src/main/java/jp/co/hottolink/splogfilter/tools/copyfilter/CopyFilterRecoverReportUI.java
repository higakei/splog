package jp.co.hottolink.splogfilter.tools.copyfilter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.tools.copyfilter.loader.CSVLoader;
import jp.co.hottolink.splogfilter.tools.copyfilter.outputter.CSVOutputter;

/**
 * <p>
 * コピーフィルターの作成するクラス.
 * </p>
 * @author higa
 *
 */
public class CopyFilterRecoverReportUI {

	/**
	 * <p>
	 * 見出し.
	 * </p>
	 */
	private static final String[] HEADER = { "documentId", "copy_rate", "url" };

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		CSVLoader loader = null;
		CSVOutputter outputter = null;

		try {
			// 入出力の設定
			InputStream in = new FileInputStream(args[0]);
			loader = new CSVLoader(in);
			outputter = new CSVOutputter(System.out);

			// 見出しの出力
			outputter.outputLine(HEADER);

			for (int i = 0;;i++) {
				List<String> line = loader.fetch();
				if (line == null) {
					loader.finalize();
					break;
				}

				// ヘッダー、フッターは集計しない
				if (!"splog".equals(line.get(2)) && !"blog".equals(line.get(2))) {
					continue;
				}

				if ("blog".equals(line.get(2))) {
					List<String> list = new ArrayList<String>();
					list.add(line.get(0));
					list.add(line.get(1));
					list.add(line.get(6));
					outputter.outputLine(list);
					outputter.flush();
				}
			}

			System.exit(0);

		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		} finally {
			if (loader != null) try { loader.finalize(); } catch (Exception e) {}
			if (outputter != null) try { outputter.finalize(); } catch (Exception e) {}
		}
	}

}
