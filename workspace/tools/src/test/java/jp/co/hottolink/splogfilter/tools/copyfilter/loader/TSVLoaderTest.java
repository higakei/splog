package jp.co.hottolink.splogfilter.tools.copyfilter.loader;

import java.io.FileInputStream;

/**
 * <p>
 * TSVLoaderのテストクラス.
 * </p>
 * @author higa
 */
public class TSVLoaderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		TSVLoader loader = null;
		
		try {
			FileInputStream in = new FileInputStream(args[0]);
			loader = new TSVLoader(in);
			loader.fetch();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if (loader != null) loader.finalize(); } catch (Exception e) {}
		}
	}

}
