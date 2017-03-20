package jp.co.hottolink.splogfilter.common.exception;

import java.net.SocketTimeoutException;

/**
 * <p>
 * APIの例外クラス.
 * </p>
 * @author higa
 */
public class APIException extends RuntimeException {

	/**
	 * <p>
	 * その他.
	 * </p>
	 */
	public static final int OTHERS = 0;

	/**
	 * <p>
	 * タイムアウト.
	 * </p>
	 */
	public static final int TIMEOUT = 1;

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 4600755295631484198L;

	/**
	 * <p>
	 * 種別.
	 * </p>
	 */
	private int type = OTHERS;

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException()
	 */
	public APIException() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(String)
	 */
	public APIException(String message) {
		super(message);
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public APIException(Throwable cause) {
		super(cause);
		type = getType(cause);
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public APIException(String message, Throwable cause) {
		super(message, cause);
		type = getType(cause);
	}

	/**
	 * <p>
	 * 種別を取得する.
	 * </p>
	 * @return type 種別
	 */
	public int getType() {
		return type;
	}

	/**
	 * <p>
	 * 原因から種別を取得する.
	 * </p>
	 * @param cause 原因
	 */
	private int getType(Throwable cause) {
		if (cause == null) {
			return OTHERS;
		} else if (cause instanceof SocketTimeoutException) {
			return TIMEOUT;
		} else {
			return OTHERS;
		}
	}
}
