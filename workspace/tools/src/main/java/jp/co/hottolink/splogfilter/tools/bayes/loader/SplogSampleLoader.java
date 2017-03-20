package jp.co.hottolink.splogfilter.tools.bayes.loader;

import java.util.List;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.tools.bayes.dao.SplogSampleDao;
import jp.co.hottolink.splogfilter.tools.bayes.entity.SplogSampleEntity;

/**
 * <p>
 * スプログサンプルのロードクラス.
 * </p>
 * @author higa
 */
public class SplogSampleLoader {

	/**
	 * <p>
	 * SQL実行クラス.
	 * </p>
	 */
	private SQLExecutor executor = null;

	
	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param executor SQL実行クラス
	 */
	public SplogSampleLoader(SQLExecutor executor) {
		this.executor = executor;
	}

	/**
	 * <p>
	 * テーブルのプライマリキーリストを取得する.
	 * </p>
	 * @return プライマリキーリスト
	 */
	@SuppressWarnings("unchecked")
	public List getPkeyList() {
		SplogSampleDao dao = new SplogSampleDao(executor);
		return dao.getIdList();
	}

	/**
	 * <p>
	 * レコードをフェッチする.
	 * </p>
	 * @param pkey プライマリキー
	 * @return レコード
	 */
	public SplogSampleEntity fecth(Object pkey) {
		SplogSampleDao dao = new SplogSampleDao(executor);
		return dao.selectById((Integer)pkey);
	}
}
