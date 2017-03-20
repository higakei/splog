package jp.co.hottolink.splogfilter.tools.bayes;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.tools.bayes.counter.SplogSampleCounter;

/**
 * <p>
 * スプログサンプルに出現する単語の集計を実行するクラス.
 * </p>
 * @author higa
 */
public class SplogSampleCounterUI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SQLExecutor executor = null;

		try {
			executor = new SQLExecutor("splog_tools_db");
			SplogSampleCounter counter = new SplogSampleCounter();
			counter.count(executor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (executor != null) executor.finalize();
		}
	}
}
