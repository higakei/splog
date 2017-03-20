package jp.co.hottolink.splogfilter.common.resource;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * <p>
 * リソースバンドルのUtilクラス.
 * </p>
 * @author higa
 */
public class ResourceBundleUtil {

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(ResourceBundleUtil.class);

	/**
	 * <p>
	 * メッセージリソースからメッセージを取得する.
	 * </p>
	 * @param resource メッセージリソース名
	 * @param key キー
	 * @return 値
	 */
	public static String getMessage(String resource, String key) {
		try {
			ResourceBundle.Control control = ResourceBundleControlFactory.getInstance();
			ResourceBundle properties = ResourceBundle.getBundle(resource, control);
			return properties.getString(key);
		} catch (Exception e) {
			logger.warn("resource=" + resource + "key=" + key, e);
			return key;
		}
	}

	/**
	 * <p>
	 * プロパティリソースの値を取得する.
	 * </p>
	 * @param resource プロパティリソース名
	 * @param key キー
	 * @return 値
	 */
	public static String getProperty(String resource, String key) {
		ResourceBundle.Control control = ResourceBundleControlFactory.getInstance();
		ResourceBundle properties = ResourceBundle.getBundle(resource, control);
		return properties.getString(key);
	}

	/**
	 * <p>
	 * リソースバンドルを取得する.
	 * </p>
	 * @param resource リソースバンドルのリソース名
	 * @return 値
	 */
	public static ResourceBundle getBundle(String resource) {
		ResourceBundle.Control control = ResourceBundleControlFactory.getInstance();
		return ResourceBundle.getBundle(resource, control);
	}

	/**
	 * <p>
	 * プロパティリソースの値を取得する.
	 * </p>
	 * @param resource プロパティリソース名
	 * @param key キー
	 * @return 値
	 */
	public static int getIntProperty(String resource, String key) {
		String property = getProperty(resource, key);
		return Integer.parseInt(property);
	}
}
