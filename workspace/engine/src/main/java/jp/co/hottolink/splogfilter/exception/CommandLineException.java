package jp.co.hottolink.splogfilter.exception;

/**
 * <p>
 * コマンドラインの例外クラス.
 * </p>
 * @author higa
 */
public class CommandLineException extends RuntimeException {

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
	public CommandLineException() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(String)
	 */
	public CommandLineException(String message) {
		super(message);
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public CommandLineException(Throwable cause) {
		super(cause);
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public CommandLineException(String message, Throwable cause) {
		super(message, cause);
	}
}
