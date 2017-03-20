package jp.co.hottolink.splogfilter.common.api;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p>
 * APIの基底クラス.
 * </p>
 * @author higa
 */
public abstract class BaseAPI {

	/**
	 * <p>
	 * デフォルトプロトコル.
	 * </p>
	 */
	private static final String DEFAULT_PROTOCOL = "http";

	/**
	 * <p>
	 * プロトコル.
	 * </p>
	 */
	protected static String protocol = DEFAULT_PROTOCOL;

	/**
	 * <p>
	 * URLを作成する.
	 * </p>
	 * @param protocol プロトコル
	 * @param host ホスト
	 * @param path パス
	 * @return URL
	 * @throws URISyntaxException
	 */
	protected URI createURI(String protocol, String host, String path) throws URISyntaxException {
		return new URI(protocol, host, path, null);
	}
}
