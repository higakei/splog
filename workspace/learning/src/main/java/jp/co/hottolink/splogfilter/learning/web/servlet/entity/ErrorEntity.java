package jp.co.hottolink.splogfilter.learning.web.servlet.entity;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

/**
 * <p>
 * エラー情報のEntityクラス.
 * </p>
 * @author higa
 */
public class ErrorEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -1714105772549932856L;

	/**
	 * <p>
	 * メッセージ.
	 * </p>
	 */
	private String message = null;

	/**
	 * <p>
	 * エラーまたは例外.
	 * </p>
	 */
	private Throwable throwable = null;

	/**
	 * <p>
	 * 原因.
	 * </p>
	 */
	private String cause = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p><pre>
	 * JavaBeans用
	 * </pre>
	 */
	public ErrorEntity() {}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param message メッセージ
	 * @param throwable エラーまたは例外
	 */
	public ErrorEntity(String message, Throwable throwable) {
		this.message = message;
		this.throwable = throwable;
		cause = throwable.getClass().getName();
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param message メッセージ
	 * @param cause 原因
	 */
	public ErrorEntity(String message, String cause) {
		this.message = message;
		this.throwable = new Throwable(cause);
		this.cause = cause;
	}

	/**
	 * <p>
	 * メッセージを取得する.
	 * </p>
	 * @return メッセージ
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * <p>
	 * メッセージを設定する.
	 * </p>
	 * @param message メッセージ
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * <p>
	 * エラーまたは例外を取得する.
	 * </p>
	 * @return エラーまたは例外
	 */
	public Throwable getThrowable() {
		return throwable;
	}

	/**
	 * <p>
	 * エラーまたは例外を設定する.
	 * </p>
	 * @param throwable エラーまたは例外
	 */
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	/**
	 * <p>
	 * 原因を取得する.
	 * </p>
	 * @return 原因
	 */
	public String getCause() {
		return cause;
	}

	/**
	 * <p>
	 * 原因を設定する.
	 * </p>
	 * @param cause 原因
	 */
	public void setCause(String cause) {
		this.cause = cause;
	}

	/**
	 * <p>
	 * スタックトレースを取得する.
	 * </p>
	 * @return スタックトレース
	 */
	public String getStackTrace() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		return sw.toString();
	}
}
