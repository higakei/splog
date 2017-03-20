package jp.co.hottolink.splogfilter.qdbm.exception;

/**
 * <p>
 * QDBMの例外クラス.
 * </p>
 * @author higa
 */
public class QDBMException extends RuntimeException {

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
	public QDBMException() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(String)
	 */
	public QDBMException(String message) {
		super(message);
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public QDBMException(Throwable cause) {
		super(cause);
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public QDBMException(String message, Throwable cause) {
		super(message, cause);
	}
}
