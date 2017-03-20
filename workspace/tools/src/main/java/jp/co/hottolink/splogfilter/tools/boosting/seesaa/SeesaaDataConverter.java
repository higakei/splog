package jp.co.hottolink.splogfilter.tools.boosting.seesaa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;

/**
 * <p>
 * SeesaaのブログデータをBoosting用データに変換するクラス.
 * </p>
 * @author higa
 */
public class SeesaaDataConverter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SQLExecutor read = null;
		SQLExecutor write = null;

		try {
			// DBに接続
			read = new SQLExecutor("seesaa_feed");
			write = new SQLExecutor("splog_learning_db");

			// Seesaaのブログデータの取得
			String sql = "select doc_url, doc_title, human_result, doc_body from document_info where human_result in (0,1,3,4)";
			read.executeQuery(sql);

			TrainingDataDBRecordWriter writer = new TrainingDataDBRecordWriter(write);
			while (read.next()) {
				Map<String, String> record = new LinkedHashMap<String, String>();
				record.put("url", read.getString("doc_url"));
				record.put("title", read.getString("doc_title"));

				// 本文
				byte[] blob = read.getBytes("doc_body");
				String body = getBody(blob);
				record.put("body", body);

				// 人間判定
				Integer result = read.getInteger("human_result");
				if ((result == 0) || (result == 1)) {
					record.put("result", "blog");
				} else if ((result == 3) || (result == 4)) {
					record.put("result", "spam");
				}

				writer.println(record);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (read != null) read.finalize();
			if (write != null) write.finalize();
		}
	}

	/**
	 * <p>
	 * バイトデータから本文を取得する
	 * </p>
	 * @param zipBytes バイトデータ
	 * @return 本文
	 * @throws IOException
	 */
	private static String getBody(byte[] zipBytes) throws IOException {

		if (zipBytes == null) {
			return null;
		}

		ByteArrayOutputStream bout = new  ByteArrayOutputStream();
		ZipInputStream in = new ZipInputStream (new ByteArrayInputStream(zipBytes));
		int data = 0;
		while(in.getNextEntry() != null) {
			//1バイトずつ、エントリから読み込んで、展開先ファイルに出力する
		    while( (data = in.read()) != -1 ) {
		    	bout.write(data);
		    }
			    
		    //現在のZIPエントリを閉じる
		    in.closeEntry();
		}

		return bout.toString("utf-8");
	}
}
