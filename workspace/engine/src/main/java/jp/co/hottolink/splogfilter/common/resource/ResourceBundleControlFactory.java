package jp.co.hottolink.splogfilter.common.resource;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p>
 * リソースバンドルの制御クラスを生成するクラス.
 * </p>
 * @author higa
 */
public class ResourceBundleControlFactory {

	/**
	 * <p>
	 * リソースバンドルのキャッシュ有効期間のデフォルト値.
	 * </p>
	 */
	private static final long DEFAULT_TIME_TO_LIVE = 60 * 1000;

	/**
	 * <p>
	 * システム共通のインスタンス.
	 * </p>
	 */
	private static ResourceBundle.Control singleton = null;

	/**
	 * <p>
	 * リソースバンドルのキャッシュ有効期間.
	 * </p>
	 */
	private long timeToLive = DEFAULT_TIME_TO_LIVE;

	/**
	 * <p>
	 * リソースバンドルの制御クラスのインスタンスを作成する.
	 * </p><pre>
	 * リソースバンドルのキャッシュ有効期間はデフォルト値
	 * リソースバンドルのキャッシュ有効期間を指定する場合、{@link #newInstance(long)}を使用する
	 * </pre>
	 * @return リソースバンドルの制御クラスのインスタンス
	 */
	public static ResourceBundle.Control newInstance() {
		ResourceBundleControlFactory factory = new ResourceBundleControlFactory();
		return factory.createInstance();
	}

	/**
	 * <p>
	 * リソースバンドルの制御クラスのインスタンスを作成する.
	 * </p>
	 * @param timeToLive リソースバンドルのキャッシュ有効期間
	 * @return リソースバンドルの制御クラスのインスタンス
	 */
	public static ResourceBundle.Control newInstance(long timeToLive) {
		ResourceBundleControlFactory factory = new ResourceBundleControlFactory();
		factory.setTimeToLive(timeToLive);
		return factory.createInstance();
	}

	/**
	 * <p>
	 * システム共通のリソースバンドルの制御クラスのインスタンスを取得する.
	 * </p><pre>
	 * システム共通のインスタンスが設定されてない場合、
	 * デフォルトのリソースバンドルのキャッシュ有効期間のインスタンスを作成し、
	 * システム共通のインスタンスとする
	 * </pre>
	 * @return リソースバンドルの制御クラスのインスタンス
	 */
	public static ResourceBundle.Control getInstance() {
		if (singleton == null) singleton = newInstance();
		return singleton;
	}

	/**
	 * <p>
	 * システム共通のリソースバンドルの制御クラスのインスタンスを作成する.
	 * </p><pre>
	 * 個別のインスタンスの場合、{@link #newInstance(long)}を使用する
	 * </pre>
	 * @param timeToLive リソースバンドルのキャッシュ有効期間
	 * @return リソースバンドルの制御クラスのインスタンス
	 */
	public static ResourceBundle.Control initialize(long timeToLive) {
		singleton = newInstance(timeToLive);
		return singleton;
	}

	/**
	 * <p>
	 * リソースバンドルのキャッシュ有効期間を取得する.
	 * </p>
	 * @return リソースバンドルのキャッシュ有効期間
	 */
	public long getTimeToLive() {
		return timeToLive;
	}

	/**
	 * <p>
	 * リソースバンドルのキャッシュ有効期間を設定する.
	 * </p>
	 * @param timeToLive リソースバンドルのキャッシュ有効期間
	 */
	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	/**
	 * <p>
	 * リソースバンドルの制御クラスのインスタンスを作成する.
	 * </p>
	 * @return リソースバンドルの制御クラスのインスタンス
	 */
	public ResourceBundle.Control createInstance() {
		return new ResourceBundle.Control() {
			public long getTimeToLive(String baseName, Locale locale) {
				return timeToLive;
			}
		};
	}
}
