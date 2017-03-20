package jp.co.hottolink.splogfilter.tools.bayes;

import java.util.List;

import com.enigmastation.classifier.FisherClassifier;
import com.enigmastation.classifier.impl.FisherClassifierImpl;
import com.enigmastation.classifier.persistence.Serializer;

import jp.co.hottolink.fusion.core.util.string.HtmlAnarizer;
import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.tools.bayes.entity.SplogSampleEntity;
import jp.co.hottolink.splogfilter.tools.bayes.extractors.ContentSenWordLister;
import jp.co.hottolink.splogfilter.tools.bayes.extractors.TitleSenWordLister;
import jp.co.hottolink.splogfilter.tools.bayes.loader.SplogSampleLoader;
import jp.co.hottolink.splogfilter.tools.bayes.util.BayesFilterUtil;

/**
 * <p>
 * ベイズフィルターを実行するクラス.
 * </p>
 * @author higa
 */
public class BayesFilterUI {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		SQLExecutor executor = null;

		try {
			// 確率的分類器の生成
			FisherClassifier titleClassifier = new FisherClassifierImpl(new TitleSenWordLister());
			FisherClassifier contentClassifier = new FisherClassifierImpl(new ContentSenWordLister());

			// 確率モデルのロード
			Serializer serializer = new Serializer();
			serializer.setFilename("title_classifier.ser");
			serializer.load(titleClassifier);
			serializer.setFilename("content_classifier.ser");
			serializer.load(contentClassifier);

			// DBに接続
			executor = new SQLExecutor("splog_tools_db");
			SplogSampleLoader loader = new SplogSampleLoader(executor);
			List list = loader.getPkeyList();

			int counter = 0;
			int blog_blog = 0;
			int blog_splog = 0;
			int splog_blog = 0;
			int splog_splog = 0;
			int title_blog_blog = 0;
			int title_blog_splog = 0;
			int title_splog_blog = 0;
			int title_splog_splog = 0;
			int content_blog_blog = 0;
			int content_blog_splog = 0;
			int content_splog_blog = 0;
			int content_splog_splog = 0;

			//System.out.println("human" + "\t" + "title" + "\t" + "content");
			for (Object pkey: list) {
				SplogSampleEntity entity = loader.fecth(pkey);
				//String category = BayesFilterUtil.getCategory(entity.isSplog());
				//if ((entity.getId() % 2) == 1) {
				//	continue;
				//}

				// 判定
				String titleClassification = titleClassifier.getClassification(HtmlAnarizer.toPlaneText(entity.getTitle()), "blog");
				String contentClassification = contentClassifier.getClassification(HtmlAnarizer.toPlaneText(entity.getBody()), "blog");
				//System.out.println(category + "\t" + titleClassification + "\t" + contentClassification);

				// タイトルの集計
				boolean isTitleSplog = BayesFilterUtil.isSplog(titleClassification);
				if (isTitleSplog) {
					if (entity.isSplog()) {
						title_splog_splog += 1;
					} else {
						title_splog_blog += 1;
					}
				} else {
					if (entity.isSplog()) {
						title_blog_splog += 1;
					} else {
						title_blog_blog += 1;
					}
				}

				// 本文の集計
				boolean isContentSplog = BayesFilterUtil.isSplog(contentClassification);
				if (isContentSplog) {
					if (entity.isSplog()) {
						content_splog_splog += 1;
					} else {
						content_splog_blog += 1;
					}
				} else {
					if (entity.isSplog()) {
						content_blog_splog += 1;
					} else {
						content_blog_blog += 1;
					}
				}

				// 全体の集計
				boolean isSplog = isTitleSplog && isContentSplog;
				if (isSplog) {
					if (entity.isSplog()) {
						splog_splog += 1;
					} else {
						splog_blog += 1;
					}
				} else {
					if (entity.isSplog()) {
						blog_splog += 1;
					} else {
						blog_blog += 1;
					}
				}

				counter += 1;
			}

			// DBから切断
			executor.finalize();

			System.out.println("" + "\t" + "blog_recall" + "\t" + "blog_precision" + "\t" + "splog_recall" + "\t" + "splog_precision"+ "\t" + "recall");
			System.out.println("total" + "\t" + (double)blog_blog/(blog_blog + splog_blog) + "\t" + (double)blog_blog/(blog_blog + blog_splog) + "\t" + (double)splog_splog/(blog_splog + splog_splog) + "\t" + (double)splog_splog/(splog_blog + splog_splog) + "\t" + (double)(blog_blog + splog_splog)/counter);
			System.out.println("tite" + "\t" + (double)title_blog_blog/(title_blog_blog + title_splog_blog) + "\t" + (double)title_blog_blog/(title_blog_blog + title_blog_splog) + "\t" + (double)title_splog_splog/(title_blog_splog + title_splog_splog) + "\t" + (double)title_splog_splog/(title_splog_blog + title_splog_splog) + "\t" + (double)(title_blog_blog + title_splog_splog)/counter);
			System.out.println("content" + "\t" + (double)content_blog_blog/(content_blog_blog + content_splog_blog) + "\t" + (double)content_blog_blog/(content_blog_blog + content_blog_splog) + "\t" + (double)content_splog_splog/(content_blog_splog + content_splog_splog) + "\t" + (double)content_splog_splog/(content_splog_blog + content_splog_splog) + "\t" + (double)(content_blog_blog + content_splog_splog)/counter);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (executor != null) executor.finalize();
		}
	}
}
