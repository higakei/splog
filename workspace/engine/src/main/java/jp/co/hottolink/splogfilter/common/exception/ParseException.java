package jp.co.hottolink.splogfilter.common.exception;

/**
 * <p>
 * 構文解析の例外クラス.
 * </p>
 * @author higa
 */
public class ParseException extends RuntimeException {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -457586386435485088L;

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException()
	 */
	public ParseException() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(String)
	 */
	public ParseException(String message) {
		super(message);
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public ParseException(Throwable cause) {
		super(cause);
	}

	/*
	 * (non-Javadoc)
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
