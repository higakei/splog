package jp.co.hottolink.splogfilter.common.exception;

/**
 * <p>
 * DBアクセスの例外クラス.
 * </p>
 * @author higa
 */
public class DAOException extends RuntimeException {

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
	public DAOException() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(String)
	 */
	public DAOException(String message) {
		super(message);
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}
}
