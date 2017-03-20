package jp.co.hottolink.splogfilter.tools.copyfilter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import jp.co.hottolink.splogfilter.tools.copyfilter.loader.CSVLoader;

/**
 * <p>
 * コピーフィルターの検証結果を集計するクラス.
 * </p>
 * @author higa
 *
 */
public class CopyFilterReportSumUI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		CSVLoader loader = null;

		try {
			InputStream in = new FileInputStream(args[0]);
			loader = new CSVLoader(in);

			int document = 0;
			int hard = 0;
			int medium = 0;
			int soft = 0;
			int deplicated = 0;

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

				++document;
				if ("splog".equals(line.get(2))) ++hard;
				if ("splog".equals(line.get(3))) ++medium;
				if ("splog".equals(line.get(4))) ++soft;
				if ("splog".equals(line.get(5))) ++deplicated;
			}

			System.out.println("document" + "\t" + document);
			System.out.println("splog_hard" + "\t"+ hard);
			System.out.println("splog_medium" + "\t" + medium);
			System.out.println("splog_soft" + "\t" + soft);
			System.out.println("spam_body_deplicated" + "\t" + deplicated);

			System.exit(0);

		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		} finally {
			if (loader != null) try { loader.finalize(); } catch (Exception e) {}
		}
	}

}
