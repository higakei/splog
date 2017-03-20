package jp.co.hottolink.splogfilter.tools.boosting.fukuhara;

import java.util.LinkedHashMap;
import java.util.Map;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.tools.boosting.seesaa.TrainingDataDBRecordWriter;

/**
 * <p>
 * 福原さんの学習データをBoosting用データに変換するクラス.
 * </p>
 * @author higa
 */
public class FukuharaDataConverter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SQLExecutor read = null;
		SQLExecutor write = null;

		try {
			// DBに接続
			read = new SQLExecutor("fukuhara");
			write = new SQLExecutor("splog_learning_db");

			// 福原さんの学習データの取得
			String sql = "select title, url, body, spam_count, blog_count from fukuhara_data where body is not null";
			read.executeQuery(sql);

			TrainingDataDBRecordWriter writer = new TrainingDataDBRecordWriter(write);
			while (read.next()) {
				Map<String, String> record = new LinkedHashMap<String, String>();
				record.put("url", read.getString("url"));
				record.put("title", read.getString("title"));
				record.put("body", read.getString("body"));

				// 判定結果
				int spam = read.getInt("spam_count");
				int blog = read.getInt("blog_count");
				if (spam > blog) {
					record.put("result", "spam");
				} else {
					record.put("result", "blog");
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
}
