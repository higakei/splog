package jp.co.hottolink.splogfilter.common.loader;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * CSVのリーダークラス.
 * </p>
 * @author higa
 */
public class CSVReader {

	/**
	 * <p>
	 * 区切り文字.
	 * </p>
	 */
	private static final char DELIMITOR = ',';

	/**
	 * <p>
	 * デフォルトリードサイズ.
	 * </p>
	 */
	private static final int DEFAULT_READ_SIZE = 1024;

	/**
	 * <p>
	 * ダブルクォートのクリアパターン.
	 * </p>
	 */
	private static final Pattern CLEAR_OUBLE_QUOTE = Pattern.compile("^\".*", Pattern.DOTALL);

	/**
	 * <p>
	 * リーダー.
	 * </p>
	 */
	private Reader reader;

	/**
	 * <p>
	 * 改行前のデータ.
	 * </p>
	 */
	private String buffer = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param reader リーダー
	 */
	public CSVReader(Reader reader) {
		this.reader = reader;
	}

	/**
	 * <p>
	 * リーダをクローズする.
	 * </p>
	 * @throws IOException
	 * @see java.io.Reader#close()
	 */
	public void close() throws IOException {
		if (reader != null) reader.close();
	}

	/**
	 * <p>
	 * 行データを取得する.
	 * </p>
	 * @return 行データ
	 * @throws IOException
	 */
	public List<String> readLine() throws IOException {

		List<String> line = new ArrayList<String>();
		boolean isOpened = false;
		boolean isCR = false;
		StringBuffer column = new StringBuffer();

		// 改行前のデータがなければリードする
		String data = buffer;
		if ((data == null) || data.isEmpty()) {
			data = read();
			if (data == null) {
				return null;
			}
		}

		// 改行するまで最後までリード
		do {
			for (int i = 0; i < data.length(); i++) {
				char c = data.charAt(i);

				// 復帰コードから
				if (isCR) {
					// 改行
					line.add(clearDobleQuote(column.toString()));
					if (c == '\n') {
						buffer = data.substring(i + 1);
					} else {
						buffer = data.substring(i);
					}
					return line;
				}

				// ダブルコート
				if (c == '"') {
					isOpened = !isOpened;
				}

				// 区切り文字
				if (c == DELIMITOR) {
					if (!isOpened) {
						// 区切る
						line.add(clearDobleQuote(column.toString()));
						column = new StringBuffer();
						continue;
					}
				}

				// 改行文字
				if (c == '\n') {
					if (!isOpened) {
						// 改行
						line.add(clearDobleQuote(column.toString()));
						buffer = data.substring(i + 1);
						return line;
					}
				}

				// 復帰文字
				if (c == '\r') {
					if (!isOpened) isCR = true;
					else isCR = false;
					continue;
				} else {
					isCR = false;
				}

				column.append(c);
			}

		} while ((data = read()) != null);

		// 残りのデータ
		line.add(clearDobleQuote(column.toString()));
		buffer = null;

		return line;
	}

	/**
	 * <p>
	 * リードする.
	 * </p>
	 * @return リードしたデータ
	 * @throws IOException
	 */
	public String read() throws IOException {

		char[] cbuf = new char[DEFAULT_READ_SIZE];
		int length = reader.read(cbuf);
		if (length == -1) {
			return null;
		}

		return new String(cbuf, 0, length);
	}

	/**
	 * <p>
	 * CSVデータのダブルクォートをクリアする.
	 * </p>
	 * @param csv CSVデータ
	 * @return クリアしたCSVデータ
	 */
	public static String clearDobleQuote(String csv) {
	
		if (csv == null) {
			return null;
		}
	
		Matcher matcher = CLEAR_OUBLE_QUOTE.matcher(csv);
		boolean isMatched = matcher.matches();
		if (!isMatched) {
			return csv;
		}
	
		StringBuffer buffer = new StringBuffer();
		boolean isSingle = false;
		boolean isClosed = false;
	
		for (int i = 1; i < csv.length(); i++) {
			char c = csv.charAt(i);
	
			// ダブルコートが閉じている場合
			if (isClosed) {
				buffer.append(c);
				continue;
			}
	
			// 最初のダブルコートは飛ばす
			if (!isSingle && (c == '"')) {
				isSingle = true;
				continue;
			}
	
			// ""の場合
			if (c == '"') {
				buffer.append('"');
				isSingle = false;
				continue;
			}
	
			// ダブルコート以外
			buffer.append(c);
			if (isSingle) {
				// ダブルコートが閉じた場合
				isClosed = true;
			}
		}
	
		return buffer.toString();
	}
}
