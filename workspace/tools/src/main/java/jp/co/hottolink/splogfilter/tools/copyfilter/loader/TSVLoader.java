package jp.co.hottolink.splogfilter.tools.copyfilter.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import jp.co.hottolink.splogfilter.common.exception.ParseException;
import jp.co.hottolink.splogfilter.tools.copyfilter.entity.CopyFilterReportEntity;

/**
 * <p>
 * TSVのロードクラス.
 * </p>
 * @author higa
 */
public class TSVLoader {

	/**
	 * <p>
	 * 区切り文字.
	 * </p>
	 */
	private static final String DELIMITOR = "\t";

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
	public TSVLoader(InputStream in) {
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
	public TSVLoader(InputStream in, String encoding) throws UnsupportedEncodingException {
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
	public CopyFilterReportEntity fetch() throws IOException, ParseException {

		String line = reader.readLine();
		if (line == null){
			return null;
		}

		String[] splits = line.split(DELIMITOR);
		if (splits.length != 2) {
			throw new ParseException("レコードの解析に失敗しました。");
		}

		CopyFilterReportEntity report = new CopyFilterReportEntity();
		report.setDocumentId(splits[0]);
		report.setCopyRate(Double.valueOf(splits[1]));

		return report;
	}
}
