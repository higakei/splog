package jp.co.hottolink.splogfilter.common.loader;

import java.util.LinkedHashMap;

/**
 * <p>
 *データローダのインターフェイス.
 * </p>
 * @author higa
 */
public interface DataLoaderImpl {

	/**
	 * <p>
	 * データローダをオープンする.
	 * </p>
	 * @throws Exception
	 */
	public void open() throws Exception;

	/**
	 * <p>
	 * レコードデータを取得する.
	 * </p>
	 * @return レコードデータ
	 * @throws Exception
	 */
	public LinkedHashMap<String, Object> fetch() throws Exception;

	/**
	 * <p>
	 * データローダをクローズする.
	 * </p>
	 * @throws Exception
	 */
	public void close() throws Exception;
}
