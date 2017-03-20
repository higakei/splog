package jp.co.hottolink.splogfilter.tools.bayes.counter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.sen.Token;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.util.SenUtil;
import jp.co.hottolink.splogfilter.tools.bayes.dao.MorphemeCountDao;
import jp.co.hottolink.splogfilter.tools.bayes.entity.MorphemeCountEntity;
import jp.co.hottolink.splogfilter.tools.bayes.entity.SplogSampleEntity;
import jp.co.hottolink.splogfilter.tools.bayes.loader.SplogSampleLoader;

/**
 * <p>
 * スプログサンプルに出現する単語を集計するクラス.
 * </p>
 * @author higa
 */
public class SplogSampleCounter {

	/**
	 * <p>
	 * 集計する.
	 * </p>
	 * @param executor SQL実行クラス.
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void count(SQLExecutor executor) throws IOException {

		SplogSampleLoader loader = new SplogSampleLoader(executor);
		List list = loader.getPkeyList();

		for (Object pkey: list) {
			// スプログサンプルの取得
			SplogSampleEntity entity = loader.fecth(pkey);
			boolean isSplog = entity.isSplog();

			// タイトルの集計
			Map<String, MorphemeCountEntity> titeCount = countTitle(entity.getTitle(), isSplog);
			write(executor, titeCount);

			// 本文の集計
			Map<String, MorphemeCountEntity> contentCount = countContent(entity.getBody(), isSplog);
			write(executor, contentCount);
		}
	}

	/**
	 * <p>
	 * タイトルの形態素頻度を集計する.
	 * </p>
	 * @param title タイトル
	 * @param isSplog スプログフラグ
	 * @return 形態素頻度
	 * @throws IOException
	 */
	private Map<String, MorphemeCountEntity> countTitle(String title, boolean isSplog) throws IOException {

		Map<String, MorphemeCountEntity> map = new HashMap<String, MorphemeCountEntity>();
		if (title == null) {
			return map;
		}

		// 形態素解析
		List<Token> morphemes = SenUtil.getMorphemes(title);

		// 集計
		for (Token morpheme: morphemes) {
			String key = morpheme.getSurface();
			MorphemeCountEntity value = map.get(key);

			if (value == null) {
				value = new MorphemeCountEntity();
				value.setSurface(morpheme.getSurface());
				value.setPos(morpheme.getPos());

				if (isSplog) {
					value.setSplogTitleTF(1);
					value.setSplogTitleDF(1);
				} else {
					value.setBlogTitleTF(1);
					value.setBlogTitleDF(1);
				}
			} else {
				if (isSplog) {
					value.addSplogTitleTF(1);
				} else {
					value.addBlogTitleTF(1);
				}
			}
			map.put(key, value);
		}

		return map;
	}

	/**
	 * <p>
	 * 本文の形態素頻度を集計する.
	 * </p>
	 * @param content 本文
	 * @param isSplog スプログフラグ
	 * @return 形態素頻度
	 * @throws IOException
	 */
	private Map<String, MorphemeCountEntity> countContent(String content, boolean isSplog) throws IOException {

		Map<String, MorphemeCountEntity> map = new HashMap<String, MorphemeCountEntity>();
		if (content == null) {
			return map;
		}

		// 形態素解析
		List<Token> morphemes = SenUtil.getMorphemes(content);

		// 集計
		for (Token morpheme: morphemes) {
			String key = morpheme.getSurface();
			MorphemeCountEntity value = map.get(key);

			if (value == null) {
				value = new MorphemeCountEntity();
				value.setSurface(morpheme.getSurface());
				value.setPos(morpheme.getPos());

				if (isSplog) {
					value.setSplogContentTF(1);
					value.setSplogContentDF(1);
				} else {
					value.setBlogContentTF(1);
					value.setBlogContentDF(1);
				}
			} else {
				if (isSplog) {
					value.addSplogContentTF(1);
				} else {
					value.addBlogContentTF(1);
				}
			}
			map.put(key, value);
		}

		return map;
	}

	/**
	 * <p>
	 * ドキュメントの頻度をDBに書き込む.
	 * </p>
	 * @param executor SQL実行クラス.
	 * @param document ドキュメントの頻度
	 */
	private void write(SQLExecutor executor, Map<String, MorphemeCountEntity> document) {

		if (document == null) {
			return;
		}

		for (String surface: document.keySet()) {
			// レコードの取得
			MorphemeCountEntity entity = document.get(surface);
			MorphemeCountDao dao = new MorphemeCountDao(executor);
			MorphemeCountEntity record = dao.select(surface);

			// 登録
			if (record == null) {
				dao.insert(entity);
				continue;
			}

			// 更新
			record.addSplogTitleTF(entity.getSplogTitleTF());
			record.addSplogTitleDF(entity.getSplogTitleDF());
			record.addSplogContentTF(entity.getSplogContentTF());
			record.addSplogContentDF(entity.getSplogContentDF());
			record.addBlogTitleTF(entity.getBlogTitleTF());
			record.addBlogTitleDF(entity.getBlogTitleDF());
			record.addBlogContentTF(entity.getBlogContentTF());
			record.addBlogContentDF(entity.getBlogContentDF());
			dao.update(record);
		}
	}
}
