package jp.co.hottolink.splogfilter.common.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * CSVのロードクラス.
 * </p>
 * @author higa
 */
public class CSVLoader implements DataLoaderImpl {

	/**
	 * <p>
	 * 入力ストリーム.
	 * </p>
	 */
	private InputStream stream = null;

	/**
	 * <p>
	 * エンコーディング.
	 * </p>
	 */
	private String encoding = null;

	/**
	 * <p>
	 * ヘッダー行の有無.
	 * </p>
	 */
	private boolean hasHeader = false;

	/**
	 * <p>
	 * ヘッダー.
	 * </p>
	 */
	private List<String> header = null;

	/**
	 * <p>
	 * リーダー.
	 * </p>
	 */
	private CSVReader reader = null;

	/**
	 * <p>
	 * 読込中フラグ.
	 * </p>
	 */
	private boolean isRead = false;

	/**
	 * <p>
	 * コンストラクター.
	 * </p><pre>
	 * fetchMapのキーは以下の優先順位で設定されます
	 * 1. ファイルのヘッダー行
	 * 2. 自動生成
	 * </pre>
	 * @param stream 入力ストリーム(必須)
	 * @param encoding エンコーディング(任意)
	 * @param hasHeader  ヘッダー行の有無(必須)
	 */
	public CSVLoader(InputStream stream, String encoding, boolean hasHeader) {
		this.stream = stream;
		this.encoding = encoding;
		this.hasHeader = hasHeader;
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p><pre>
	 * fetchMapのキーは以下の優先順位で設定されます
	 * 1. headerパラメータ
	 * 2. ファイルのヘッダー行
	 * 3. 自動生成
	 * </pre>
	 * @param stream 入力ストリーム(必須)
	 * @param encoding エンコーディング(任意)
	 * @param hasHeader  ヘッダー行の有無(必須)
	 * @param header ヘッダー(任意)
	 */
	public CSVLoader(InputStream stream, String encoding, boolean hasHeader,
			List<String> header) {
		this.stream = stream;
		this.encoding = encoding;
		this.hasHeader = hasHeader;
		this.header = header;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl#open()
	 */
	@Override
	public void open() throws Exception {
		if (encoding == null) {
			InputStreamReader reader = new InputStreamReader(stream);
			this.reader = new CSVReader(reader);
		} else {
			InputStreamReader reader = new InputStreamReader(stream, encoding);
			this.reader = new CSVReader(reader);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl#close()
	 */
	public void close() throws Exception {
		if (reader != null) reader.close();
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl#fetch()
	 */
	public LinkedHashMap<String, Object> fetch() throws Exception {

		List<String> line = fetchList();
		if (line == null) {
			return null;
		}

		LinkedHashMap<String, Object> record = new LinkedHashMap<String, Object>();
		for (int i = 0; i < line.size(); i++) {
			String key = "column" + (i + 1);
			if (header != null) key = header.get(i);
			String value = line.get(i);
			record.put(key, value);
		}

		return record;
	}

	/**
	 * <p>
	 * ヘッダーを取得する.
	 * </p>
	 * @return ヘッダー
	 */
	public List<String> getHeader() {
		return header;
	}

	/**
	 * <p>
	 * １行読み込む.
	 * </p>
	 * @return 行データ
	 * @throws IOException
	 */
	public List<String> readLine() throws IOException {
		List<String> line = reader.readLine();
		isRead = true;
		return line;
	}

	/**
	 * <p>
	 * レコードデータを取得する.
	 * </p>
	 * @return レコードデータ
	 * @throws IOException
	 */
	public List<String> fetchList() throws IOException {

		boolean isHeader = false;
		if (!isRead) {
			isHeader = true;
		}

		List<String> line = readLine();
		if (line == null){
			return null;
		}

		// ヘッダー行は飛ばす
		if (hasHeader && isHeader) {
			header = line;
			line = readLine();
		}

		return line;
	}
}
