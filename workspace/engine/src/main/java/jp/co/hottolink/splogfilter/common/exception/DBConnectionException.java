package jp.co.hottolink.splogfilter.common.exception;

/**
 * <p>
 * DB接続の例外クラス.
 * </p>
 * @author higa
 */
public class DBConnectionException extends RuntimeException {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException()
	 */
	public DBConnectionException() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(String)
	 */
	public DBConnectionException(String message) {
		super(message);
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public DBConnectionException(Throwable cause) {
		super(cause);
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public DBConnectionException(String message, Throwable cause) {
		super(message, cause);
	}
}
