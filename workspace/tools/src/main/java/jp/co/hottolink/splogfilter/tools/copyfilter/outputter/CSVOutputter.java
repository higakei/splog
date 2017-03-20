package jp.co.hottolink.splogfilter.tools.copyfilter.outputter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * CSV出力クラス.
 * </p>
 * @author higa
 */
public class CSVOutputter {

	/**
	 * <p>
	 * 区切り文字.
	 * </p>
	 */
	private static final String DELIMITOR = ",";

	/**
	 * <p>
	 * ライター.
	 * </p>
	 */
	private BufferedWriter writer = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param out 出力先
	 * @throws UnsupportedEncodingException
	 */
	public CSVOutputter(OutputStream out) {
		OutputStreamWriter writer = new OutputStreamWriter(out);
		this.writer = new BufferedWriter(writer);
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param out 出力先
	 * @param encoding エンコーディング
	 * @throws UnsupportedEncodingException
	 */
	public CSVOutputter(OutputStream out, String encoding) throws UnsupportedEncodingException {
		if (encoding == null) {
			OutputStreamWriter writer = new OutputStreamWriter(out);
			this.writer = new BufferedWriter(writer);
		} else {
			OutputStreamWriter writer = new OutputStreamWriter(out, encoding);
			this.writer = new BufferedWriter(writer);
		}
	}

	/*
	 * (非 Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	public void finalize() throws IOException {
		if (writer != null) writer.close();
	}

	/**
	 * <p>
	 * 一行出力する.
	 * </p>
	 * @param items 出力項目
	 * @throws IOException
	 */
	public void outputLine(List<String> items) throws IOException {

		if ((items == null) || items.isEmpty()){
			return;
		}

		boolean isWrote = false;
		for (String itesm: items) {
			if (isWrote) {
				writer.write(DELIMITOR);
				writer.write(itesm);
			} else {
				writer.write(itesm);
				isWrote = true;
			}
		}

		writer.newLine();
	}

	/**
	 * <p>
	 * 一行出力する.
	 * </p>
	 * @param items 出力項目
	 * @throws IOException
	 */
	public void outputLine(String[] items) throws IOException {
		if (items != null) outputLine(Arrays.asList(items));
	}

	/**
	 * <p>
	 * ストリームをフラッシュする.
	 * </p>
	 * @throws IOException
	 */
	public void flush() throws IOException {
		writer.flush();
	}
}
