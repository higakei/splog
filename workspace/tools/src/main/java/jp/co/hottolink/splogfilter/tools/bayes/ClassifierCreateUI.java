package jp.co.hottolink.splogfilter.tools.bayes;

import java.util.List;

import com.enigmastation.classifier.Trainer;
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
 * ベイズフィルターの確率モデルを作成するクラス.
 * </p>
 * @author higa
 */
public class ClassifierCreateUI {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		SQLExecutor executor = null;

		try {
			// 確率的分類器の作成
			Trainer titleClassifier = new FisherClassifierImpl(new TitleSenWordLister());
			Trainer contentClassifier = new FisherClassifierImpl(new ContentSenWordLister());

			// DBに接続
			executor = new SQLExecutor("splog_tools_db");
			SplogSampleLoader loader = new SplogSampleLoader(executor);
			List list = loader.getPkeyList();

			// 学習
			for (Object pkey: list) {
				SplogSampleEntity entity = loader.fecth(pkey);
				String category = BayesFilterUtil.getCategory(entity.isSplog());
				//if ((entity.getId() % 2) == 0) {
				//	continue;
				//}

				// タイトル
				String title = HtmlAnarizer.toPlaneText(entity.getTitle());
				if ((title != null) && !title.isEmpty()) {
					titleClassifier.train(title, category);
				}

				// 本文
				String content = HtmlAnarizer.toPlaneText(entity.getBody());
				if ((content != null)  && !content.isEmpty()){
					contentClassifier.train(content, category);
				}
			}

			// DBから切断
			executor.finalize();

			// ファイルに保存
			Serializer serializer = new Serializer();
			serializer.setFilename("title_classifier.ser");
			serializer.save(titleClassifier);
			serializer.setFilename("content_classifier.ser");
			serializer.save(contentClassifier);
			System.out.println("finish");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (executor != null) executor.finalize();
		}
	}
}
