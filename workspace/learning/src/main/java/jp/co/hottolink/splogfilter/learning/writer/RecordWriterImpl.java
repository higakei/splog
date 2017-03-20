package jp.co.hottolink.splogfilter.learning.writer;

/**
 * <p>
 * レコードライターのインターフェイス.
 * </p>
 * @author higa
 */
public interface RecordWriterImpl {

	/**
	 * <p>
	 * レコードライターをオープンする.
	 * </p>
	 * @throws Exception
	 */
	public void open() throws Exception;

	/**
	 * <p>
	 * レコードライターをクローズする.
	 * </p>
	 */
	public void close();

	/**
	 * <p>
	 * レコードを一行書き込む.
	 * </p>
	 * @param record レコード
	 * @throws Exception
	 */
	public void println(Object record) throws Exception;
}
