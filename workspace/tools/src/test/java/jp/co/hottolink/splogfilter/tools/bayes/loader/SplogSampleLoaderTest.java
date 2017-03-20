package jp.co.hottolink.splogfilter.tools.bayes.loader;

import java.util.List;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.tools.bayes.entity.SplogSampleEntity;

/**
 * <p>
 * SplogSampleLoaderのテストクラス.
 * </p>
 * @author higa
 */
public class SplogSampleLoaderTest {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		SQLExecutor executor = null;

		try {
			executor = new SQLExecutor("splog_tools_db");
			SplogSampleLoader loader = new SplogSampleLoader(executor);
			List list = loader.getPkeyList();

			for (Object pkey: list) {
				SplogSampleEntity entity = loader.fecth(pkey);
				System.out.println(entity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (executor != null) executor.finalize();
		}
	}

}
