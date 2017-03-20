package jp.co.hottolink.splogfilter.tools.copyfilter.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import jp.co.hottolink.splogfilter.common.exception.ParseException;
import jp.co.hottolink.splogfilter.common.util.StringUtil;

/**
 * <p>
 * CSVのロードクラス.
 * </p>
 * @author higa
 */
public class CSVLoader {

	/**
	 * <p>
	 * 区切り文字.
	 * </p>
	 */
	private static final String DELIMITOR = ",";

	/**
	 * <p>
	 * リーダー.
	 * </p>
	 */
	private BufferedReader reader = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param in 入力元
	 */
	public CSVLoader(InputStream in) {
		InputStreamReader reader = new InputStreamReader(in);
		this.reader = new BufferedReader(reader);
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param in 入力元
	 * @param encoding エンコーディング
	 * @throws UnsupportedEncodingException
	 */
	public CSVLoader(InputStream in, String encoding) throws UnsupportedEncodingException {
		if (encoding == null) {
			InputStreamReader reader = new InputStreamReader(in);
			this.reader = new BufferedReader(reader);
		} else {
			InputStreamReader reader = new InputStreamReader(in, encoding);
			this.reader = new BufferedReader(reader);
		}
	}

	/*
	 * (非 Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	public void finalize() throws IOException {
		if (reader != null) reader.close();
	}

	/**
	 * <p>
	 * レコードを読み込む.
	 * </p>
	 * @return レコード
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<String> fetch() throws IOException {

		String line = reader.readLine();
		if (line == null){
			return null;
		}

		int count = StringUtil.getWordCount(line, DELIMITOR);
		String[] splits = line.split(DELIMITOR, count + 1);
		return Arrays.asList(splits);
	}
}
