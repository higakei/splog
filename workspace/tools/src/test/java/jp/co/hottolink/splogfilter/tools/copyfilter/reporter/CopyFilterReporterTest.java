package jp.co.hottolink.splogfilter.tools.copyfilter.reporter;

import jp.co.hottolink.splogfilter.tools.copyfilter.entity.CopyFilterReportEntity;

/**
 * <p>
 * CopyFilterReporterのテストクラス.
 * </p>
 * @author higa
 *
 */
public class CopyFilterReporterTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CopyFilterReportEntity report = new CopyFilterReportEntity();
			report.setDocumentId(args[0]);

			CopyFilterReporter reporter = new CopyFilterReporter();
			report = reporter.report(report);
			System.out.println(report);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
