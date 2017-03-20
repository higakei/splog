package jp.co.hottolink.splogfilter.takeda.filter;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import qdbm.Depot;
import qdbm.Util;

import jp.buzzengine.analyze.DocumentSetAnalyzer;
import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.util.StringUtil;
import jp.co.hottolink.splogfilter.dao.BlogFeedDao;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.qdbm.DepotCapsule;
import jp.co.hottolink.splogfilter.qdbm.exception.QDBMException;
import jp.co.hottolink.splogfilter.takeda.ConstantWordSetter;
import jp.co.hottolink.splogfilter.takeda.constants.ConstantWords;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;
import jp.co.hottolink.splogfilter.takeda.copycontent.CharacterCodePartition;
import jp.co.hottolink.splogfilter.takeda.copycontent.CopyContentCreator;
import jp.co.hottolink.splogfilter.takeda.copycontent.entity.CharacterCodeKeyEntity;
import jp.co.hottolink.splogfilter.takeda.copycontent.entity.CopyContentEntity;
import jp.co.hottolink.splogfilter.takeda.copycontent.entity.SuffixIndexEntity;
import jp.co.hottolink.splogfilter.takeda.copycontent.util.SuffixArrayUtil;

public class CopyFilterByQDBM implements DocumentSetAnalyzer {

	/**
	 * <p>
	 * 設定ファイルパス
	 * </p>
	 */
	private static final String PROPERTY_FILE_PATH = "/copyfilter.xml";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(CopyFilterByQDBM.class);

	/**
	 * <p>
	 * 分析データのDBM.
	 * </p>
	 */
	private DepotCapsule inputDbm = null;

	/**
	 * <p>
	 * 分析結果のマップ.
	 * </p>
	 */
	private Map<Integer, boolean[]> fillMap = null;

	/**
	 * <p>
	 * 最大文書数.
	 * </p>
	 */
	private Integer maxContentNumber = null;

	/**
	 * <p>
	 * 最大接尾辞インデックス数.
	 * </p>
	 */
	private Integer maxSuffixIndexNumber = null;

	/**
	 * <p>
	 * 接尾辞インデックスのハッシュコードの文字数.
	 * </p>
	 */
	private Integer suffixHashcodeLength = null;

	/**
	 * <p>
	 * システム時間を取得する.
	 * </p>
	 * @return システム時間
	 */
	public static synchronized String getSystemTime() {
		try {
			Thread.sleep(10);
			return String.valueOf(System.currentTimeMillis());
		} catch (InterruptedException e) {
			return String.valueOf(System.currentTimeMillis());
		}
	}

	/**
	 * <p>
	 * 処理時間を秒で取得する.
	 * </p>
	 * @param start 開始時間
	 * @return 処理時間
	 */
	public static double getExecuteSecond(long start) {
		long end = System.currentTimeMillis();
		double time = end - start;
		return time / 1000;
	}

	/**
	 * <p>
	 * コピー検出領域にマークする.
	 * </p>
	 * @param index インデックス
	 * @param range 長さ
	 * @param fill コピー検出領域
	 */
	public static boolean[] fill(int index, int range, boolean[] fill) {
	
		if (fill == null) {
			return fill;
		}
	
		int beginIndex = index;
		int endIndex = index + range;
		if ((0 > beginIndex) || (beginIndex > endIndex) || (endIndex > fill.length)) {
			return fill;
		}
	
		for (int i = beginIndex; i < endIndex; i++) {
			fill[i] = true;
		}
	
		return fill;
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 */
	public CopyFilterByQDBM() {}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	public void finalize() {
		if (inputDbm != null) try { inputDbm.remove(); inputDbm = null; } catch (QDBMException e) {}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.buzzengine.analyze.DocumentSetAnalyzer#endInput()
	 */
	public void endInput() {}

	/*
	 * (non-javadoc)
	 * @see jp.buzzengine.analyze.DocumentSetAnalyzer#initialize(java.util.Properties)
	 */
	public void initialize(Properties props) {
		try {
			// スパム判定語の取得
			ConstantWordSetter.setFromCSV(SplogFilterConstants.SPECIAL_LETTERS_CSV_FILE_PATH);

			// 設定ファイルの取得
			Properties self = new Properties();
			InputStream in = getClass().getResourceAsStream(PROPERTY_FILE_PATH);
			self.loadFromXML(in);

			// 設定値の取得
			maxContentNumber = Integer.valueOf(self.getProperty("MAX_DOCUMENT_NUMBER", "100000"));
			maxSuffixIndexNumber = Integer.valueOf(self.getProperty("MAX_SUFFIX_INDEX_NUMBER", "5000000"));
			suffixHashcodeLength = Integer.valueOf(self.getProperty("SUFFIX_HASHCODE_LENGTH", "2"));
			logger.debug("MAX_DOCUMENT_NUMBER:" + maxContentNumber);
			logger.debug("MAX_SUFFIX_INDEX_NUMBER:" + maxSuffixIndexNumber);
			logger.debug("SUFFIX_HASHCODE_LENGTH:" + suffixHashcodeLength);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.buzzengine.analyze.DocumentSetAnalyzer#getHint()
	 */
	public Map<String, Object> getHint() {
		Map<String,Object> hint = new HashMap<String, Object>();
		hint.put("TIMEOUT", 18000);
		hint.put("MIN_DOCUMENT_NUM", 0);
		hint.put("MAX_DOCUMENT_NUM", maxContentNumber);
		hint.put("NEED_ATTRIBUTE", "documentid\tbody\turl\tauthorid");
		return hint;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.buzzengine.analyze.DocumentSetAnalyzer#startInput()
	 */
	public void startInput() {
		String dbmName = CopyContentEntity.class.getSimpleName() + getSystemTime();
		inputDbm = new DepotCapsule(dbmName);
		inputDbm.open(Depot.OWRITER | Depot.OCREAT | Depot.OTRUNC, -1);
		inputDbm.close();
	}

	/*
	 * (non-Javadoc)
	 * @see jp.buzzengine.analyze.DocumentSetAnalyzer#analyze()
	 */
	public Map<Object, String> analyze() {
		try {
			return analyze2();
		} finally {
			finalize();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jp.buzzengine.analyze.DocumentSetAnalyzer#input(java.lang.Object, java.util.Map)
	 */
	public void input(Object key, Map<String, String> attr) {

		// 入力データの取得
		String documentId = attr.get("documentid");
		String body = attr.get("body");
		String url = attr.get("url");
		String authorId = attr.get("authorid");

		// フィードオブジェクトの作成
		BlogFeedEntity blogFeed = new BlogFeedEntity();
		blogFeed.setDocumentId(documentId);
		blogFeed.setBody(body);
		blogFeed.setUrl(url);
		blogFeed.setAuthorId(authorId);

		// 分析データの作成
		input(blogFeed);
	}

	/**
	 * <p>
	 * データを入力する.
	 * </p>
	 * @param blogFeed ブログフィード
	 */
	public void input(BlogFeedEntity blogFeed) {

		// 分析データの作成
		CopyContentEntity document = CopyContentCreator.create(blogFeed, SplogFilterConstants.COPY_CONTENT_LENGTH);

		// 分析データの格納
		inputDbm.open(Depot.OWRITER, -1);
		int blogFeedId = inputDbm.rnum() + 1;
		inputDbm.put(blogFeedId, document);
		int rnum = inputDbm.rnum();
		if ((rnum % 50000) == 0) logger.info("input size:" + rnum);
		inputDbm.close();

		// 分析結果マップの作成
		if (fillMap == null) fillMap = new HashMap<Integer, boolean[]>(0);
		fillMap.put(blogFeedId, document.getFill());
	}

	/**
	 * <p>
	 * 分析を行う.
	 * </p>
	 * @return 分析結果
	 */
	private Map<Object, String> analyze1() {

		DepotCapsule contentDbm = null;

		try {
			// コンテンツの保存
			long time = System.currentTimeMillis();
			String dbmName = "content" + getSystemTime();
			contentDbm = new DepotCapsule(dbmName);
			saveContent(inputDbm, contentDbm);
			logger.info("save content:" + getExecuteSecond(time) + "sec");

			// 分析
			time = System.currentTimeMillis();
			analyze1(contentDbm, fillMap);
			logger.info("analyze:" + getExecuteSecond(time) + "sec");

			// 分析結果の格納
			time = System.currentTimeMillis();
			Map<Object, String> results = store1(fillMap);
			logger.info("store:" + getExecuteSecond(time) + "sec");

			return results;

		} finally {
			if (contentDbm != null) try { contentDbm.remove(); } catch (QDBMException e) {}
		}
	}

	/**
	 * <p>
	 * 分析を行う.
	 * </p>
	 * @param contentDbm コンテンツのDBM
	 * @param fillMap 分析結果
	 */
	private void analyze1(DepotCapsule contentDbm, Map<Integer, boolean[]> fillMap) {
		analyze1(contentDbm, fillMap, null);
	}

	/**
	 * <p>
	 * 分析を行う.
	 * </p>
	 * @param contentDbm コンテンツのDBM
	 * @param fillMap 分析結果
	 * @param partitionKey パーティションキー
	 */
	private void analyze1(DepotCapsule contentDbm, Map<Integer, boolean[]> fillMap, List<CharacterCodeKeyEntity> partitionKey) {

		List<SuffixIndexEntity> suffixList = null;
		// 接尾辞インデックスの作成
		long time = System.currentTimeMillis();
		if (partitionKey == null) partitionKey = new ArrayList<CharacterCodeKeyEntity>(0);
		if (partitionKey.size() >= SplogFilterConstants.COPY_CONTENT_LENGTH) {
			suffixList = create1(contentDbm, partitionKey);
		} else {
			suffixList = create1(contentDbm, partitionKey, maxSuffixIndexNumber);
		}

		// 分割しない場合
		if (suffixList != null) {
			logger.info("create suffix index" + CharacterCodePartition.getPartitionLabel(partitionKey) + " " + suffixList.size() + ":" + getExecuteSecond(time) + "sec");
			time = System.currentTimeMillis();
			analyze1(suffixList, fillMap);
			logger.info("anlyze" + CharacterCodePartition.getPartitionLabel(partitionKey) + ":" + getExecuteSecond(time) + "sec");
			return;
		}
		logger.info("create suffix index" + CharacterCodePartition.getPartitionLabel(partitionKey) + ":" + getExecuteSecond(time) + "sec");
		// 文字コードで分割
		for (int i = 0; i < CharacterCodePartition.PARTITION_CHARACTER_CODE3.length; i++) {
			List<CharacterCodeKeyEntity> newKey = CharacterCodePartition.addKey(i, CharacterCodePartition.PARTITION_CHARACTER_CODE3, partitionKey);
			analyze1(contentDbm, fillMap, newKey);
		}
	}

	/**
	 * <p>
	 * 分析を行う.
	 * </p>
	 * @param suffixList 接尾辞インデックス
	 * @param fillMap 分析結果
	 */
	private void analyze1(List<SuffixIndexEntity> suffixList, Map<Integer, boolean[]> fillMap) {

		// 接尾辞インデックスのソート
		long time = System.currentTimeMillis();
		Collections.sort(suffixList);
		logger.debug("sort suffix index:" + getExecuteSecond(time) + "sec");

		// コピー検出領域のマーク
		time = System.currentTimeMillis();
		fill1(suffixList, fillMap);
		logger.debug("fill:" + getExecuteSecond(time) + "sec");
	}

	/**
	 * <p>
	 * コンテンツに保存する.
	 * </p>
	 * @param in 入力データのDBM
	 * @param out コンテンツのDBM
	 */
	private void saveContent(DepotCapsule in, DepotCapsule out) {
		
		try {
			// DBMの作成
			out.open(Depot.OWRITER | Depot.OCREAT | Depot.OTRUNC, -1);
			if (in == null) {
				out.close();
				return;
			}

			// 分析データのオープン
			in.open();
			in.iterinit();

			// コンテンツの保存
			Map<Integer, String> map = new HashMap<Integer, String>(0);
			for (int counter = 0, decimal = 10;;) {
				double percent = (double)++counter / in.rnum() * 100;
				if (percent >= decimal) {
					logger.info("save content:" + (int)percent + "%");
					decimal += 10;
				}

				// キーの取得
				byte[] key = in.iternext();
				if (key == null) {
					break;
				}

				// マップに格納
				int blogFeedId = (int)Util.deserializeLong(key);
				CopyContentEntity document = (CopyContentEntity)in.fetchObject(key);
				String content = document.getContent();
				map.put(blogFeedId, content);
				//if (map.size() < 10000) {
				//if (map.size() < 5000) {
				if (map.size() < 1000) {
					continue;
				}

				// DBMにマップを保存
				out.put(out.rnum(), map);
				map = new HashMap<Integer, String>(0);
			}

			// 残りをDBMに保存
			if ((map != null) && !map.isEmpty()) {
				out.put(out.rnum(), map);
			}
			
			out.close();
			
		} finally {
			if (out != null) try { out.close(); } catch (QDBMException e) {}
			if (in != null) try { in.close(); } catch (QDBMException e) {}
		}
	}

	/**
	 * <p>
	 * 接尾辞インデックスを作成する.
	 * </p>
	 * @param dbm コンテンツのDBM
	 * @param partitionKey パーティションキー
	 * @return 接尾辞インデックス
	 */
	private List<SuffixIndexEntity> create1(DepotCapsule dbm, List<CharacterCodeKeyEntity> partitionKey) {
		return create1(dbm, partitionKey, null);
	}

	/**
	 * <p>
	 * 接尾辞インデックスを作成する.
	 * </p>
	 * @param dbm コンテンツのDBM
	 * @param partitionKey パーティションキー
	 * @param limit 制限サイズ
	 * @return 接尾辞インデックス(制限サイズを超えた場合、nullを返す)
	 */
	@SuppressWarnings("unchecked")
	private List<SuffixIndexEntity> create1(DepotCapsule dbm, List<CharacterCodeKeyEntity> partitionKey, Integer limit) {

		List<SuffixIndexEntity> list = null;

		try {
			list = new ArrayList<SuffixIndexEntity>(0);
			if (dbm == null) {
				return list;
			}

			// DBMのオープン
			dbm.open();
			dbm.iterinit();

			// 接尾辞インデックスの作成
			for (;;) {
				// キーの取得
				byte[] key = dbm.iternext();
				if (key == null) {
					break;
				}

				// コンテンツマップの取得
				Map<Integer, String> contentMap = (Map<Integer, String>)dbm.getObject(key);

				// コンテンツごとに接尾辞インデックスを作成する
				for (Integer blogFeedId: contentMap.keySet()) {
					String content = contentMap.get(blogFeedId);
					list.addAll(
							SuffixArrayUtil.createSuffixIndexEntity(content, SplogFilterConstants.COPY_CONTENT_LENGTH
									, ConstantWords.period, blogFeedId, partitionKey));
					if ((limit != null) && (list.size() > limit)) {
						return null;
					}
				}
			}
			
			return list;

		} catch (OutOfMemoryError e) {
			logger.warn("suffix index size:" + list.size(), e);
			return null;
		} finally {
			if (dbm != null) try { dbm.close(); } catch (QDBMException e) {}
		}
	}

	/**
	 * <p>
	 * コピー検出領域にマークする.
	 * </p>
	 * @param suffixList 接尾辞インデックス
	 * @param fillMap コピー検出領域
	 */
	private void fill1(List<SuffixIndexEntity> suffixList, Map<Integer, boolean[]> fillMap) {

		if (suffixList == null) {
			return;
		}

		for (int i = 0; i < suffixList.size() - 1; i++) {
			SuffixIndexEntity current = suffixList.get(i);
			SuffixIndexEntity next = suffixList.get(i + 1);

			// 同じドキュメントの場合、マークしない
			int currentId = current.getDocumentId();
			int nextId = next.getDocumentId();
			if (currentId == nextId) {
				continue;
			}

			// インデックスの取得
			String currentSuffix = current.getSuffix();
			String nextSuffix = next.getSuffix();

			// 一致長の取得
			int range = StringUtil.getPrefixLength(currentSuffix, nextSuffix);
			if (range < SplogFilterConstants.COPY_CONTENT_LENGTH) {
				continue;
			}

			// 分析結果の取得
			boolean[] currentFill = fillMap.get(currentId);
			boolean[] nextFill = fillMap.get(nextId);
				
			// 両方にマークする
			fill(current.getPosition(), range, currentFill);
			fill(next.getPosition(), range, nextFill);
			
			// 分析結果の更新
			fillMap.put(currentId, currentFill);
			fillMap.put(nextId, nextFill);
		}
	}

	/**
	 * <p>
	 * 分析結果を格納する.
	 * </p>
	 * @param fillMap コピー検出領域
	 * @return 分析結果
	 */
	private Map<Object, String> store1(Map<Integer, boolean[]> fillMap) {

		try {
			// QDBMのオープン
			inputDbm.open();

			// 初期化
			Map<Object, String> results = new HashMap<Object, String>(0); 
			inputDbm.iterinit();

			int spamCount = 0;
			for (;;) {
				// キーの取得
				byte[] key = inputDbm.iternext();
				if (key == null) {
					break;
				}

				// 分析結果の取得
				int blogFeedId = (int)Util.deserializeLong(key);
				boolean[] fill = fillMap.get(blogFeedId);
				
				// コピー率の算出
				CopyContentEntity result = (CopyContentEntity)inputDbm.fetchObject(key);
				result.setFill(fill);
				result.setCopyRate();
				if (result.getCopyRate() > 0.75) {
					++spamCount;
				}
				
				// 分析結果の格納
				BigDecimal bd = new BigDecimal(result.getCopyRate());
		    	BigDecimal rate = bd.setScale(3, BigDecimal.ROUND_DOWN);
				results.put(result.getDocumentId(), rate.toString());
			}

			logger.info("spam count:" + spamCount);
			logger.info("analyze count:" + results.size());
			if (results.isEmpty()) {
				logger.info("rate:0.0%");
			} else {
				logger.info("rate:" + (((double)spamCount/results.size()) * 100) + "%");
			}

			return results;

		} finally {
			if (inputDbm != null) try { inputDbm.close(); } catch (QDBMException e) {}
		}
	}

	private Map<Object, String> analyze2() {

		DepotCapsule contentDbm = null;

		try {
			// コンテンツの保存
			long time = System.currentTimeMillis();
			String dbmName = "content" + getSystemTime();
			contentDbm = new DepotCapsule(dbmName);
			saveContent(inputDbm, contentDbm);
			logger.info("save content:" + getExecuteSecond(time) + "sec");

			// 接尾辞インデックスのカウント
			time = System.currentTimeMillis();
			Map<Integer, Integer> countMap = count2(contentDbm);
			logger.info("count suffix index:" + getExecuteSecond(time) + "sec");
	
			logger.info("hashcode:" + countMap.size());
			int max = 0;
			for (Integer count: countMap.values()) {
				if (count > max) max = count;
			}
			logger.info("max count:" + max);
	
			// 分析を分散
			time = System.currentTimeMillis();
			List<Set<Integer>> partitionList = distribute(countMap);
			countMap = null;
			logger.info("distribute:" + getExecuteSecond(time) + "sec");
			logger.info("partition:" + partitionList.size());

			// 分析
			time = System.currentTimeMillis();
			compare2(contentDbm, partitionList, fillMap);
			logger.info("compare:" + getExecuteSecond(time) + "sec");

			// 分析結果の格納
			time = System.currentTimeMillis();
			Map<Object, String> results = store1(fillMap);
			logger.info("store:" + getExecuteSecond(time) + "sec");

			return results;
		
		} finally {
			if (contentDbm != null) try { contentDbm.remove(); } catch (QDBMException e) {}
		}
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, Integer> count2(DepotCapsule contentDbm) {

		try {
			Map<Integer, Integer> map = new HashMap<Integer, Integer>(0);
			if (contentDbm == null) {
				return map;
			}

			// DBMのオープン
			contentDbm.open();
			contentDbm.iterinit();

			// DBMの読み込み
			for (int counter = 0, decimal = 10;;) {
				// キーの取得
				byte[] key = contentDbm.iternext();
				if (key == null) {
					break;
				}

				// レコードの取得
				Map<Integer, String> contentMap = (Map<Integer, String>)contentDbm.getObject(key);

				// コンテンツごとに接尾辞インデックスを作成する
				for (Integer blogFeedId: contentMap.keySet()) {
					String content = contentMap.get(blogFeedId);
					
					List<SuffixIndexEntity> suffixList = SuffixArrayUtil.createSuffixIndexEntity(
							content, SplogFilterConstants.COPY_CONTENT_LENGTH, ConstantWords.period, blogFeedId);

					// ハッシュコードごとにカウント
					for (int i = 0; i < suffixList.size(); i++) {
						SuffixIndexEntity entity = suffixList.get(i);
						String suffix = entity.getSuffix();
						int hashcode = suffix.substring(0, suffixHashcodeLength).hashCode();

						Integer count = map.get(hashcode);
						if (count == null) {
							map.put(hashcode, 1);
						} else {
							map.put(hashcode, ++count);
						}
					}
				}

				double percent = (double)++counter / contentDbm.rnum() * 100;
				if (percent >= decimal) {
					logger.info("count suffix index:" + (int)percent + "%");
					decimal += 10;
				}
			}
			
			return map;

		} finally {
			if (contentDbm != null) try { contentDbm.close(); } catch (QDBMException e) {}
		}
	}

	private List<Set<Integer>> distribute(Map<Integer, Integer> countMap) {

		List<Set<Integer>> list = new ArrayList<Set<Integer>>(0);
		if (countMap == null) {
			return list;
		}

		int total = 0;
		Set<Integer> set = new HashSet<Integer>(0);

		for (Integer hashcode: countMap.keySet()) {
			Integer count = countMap.get(hashcode);
			if (count < 2) {
				continue;
			}

			if ((total + count) <= maxSuffixIndexNumber) {
				set.add(hashcode);
				total += count;
				continue;
			}

			list.add(set);
			//logger.info("partition(" + (list.size()) + "):" + total);
			set = new HashSet<Integer>(0);
			set.add(hashcode);
			total = count;
		}

		if ((set != null) && !set.isEmpty()) {
			list.add(set);
			//logger.info("partition(" + (list.size()) + "):" + total);
		}

		return list;
	}

	private void compare2(DepotCapsule contentDbm, List<Set<Integer>> partitionList, Map<Integer, boolean[]> fillMap) {

		if (partitionList == null) {
			return;
		}

		for (int i = 0; i < partitionList.size(); i++) {
			long total = System.currentTimeMillis();
			logger.info("partition(" + (i + 1) + ") start");
			Set<Integer> hashcodeSet = partitionList.get(i);

			// 接尾辞インデックスの作成
			long time = System.currentTimeMillis();
			List<SuffixIndexEntity> suffixList = create2(contentDbm, hashcodeSet);
			logger.info("create suffix index(" + suffixList.size() + "):" + getExecuteSecond(time) + "sec");

			// 接尾辞インデックスのソート
			try {
				time = System.currentTimeMillis();
				Collections.sort(suffixList);
				logger.info("sort:" + getExecuteSecond(time) + "sec");
			} catch (OutOfMemoryError e) {
				String message = "sort suufix index=" + suffixList.size();
				throw new RuntimeException(message, e);
			}

			// コピー検出領域のマーク
			time = System.currentTimeMillis();
			fill1(suffixList, fillMap);
			logger.info("fill:" + getExecuteSecond(time) + "sec");
			logger.info("partition(" + (i + 1) + "):" + getExecuteSecond(total) + "sec");
		}
	}

	@SuppressWarnings("unchecked")
	private List<SuffixIndexEntity> create2(DepotCapsule contentDbm, Set<Integer> hashcodeSet) {

		List<SuffixIndexEntity> list = null;

		try {
			list = new ArrayList<SuffixIndexEntity>(0);
			if (contentDbm == null) {
				return list;
			}

			// 分析データのオープン
			contentDbm.open();
			contentDbm.iterinit();
	
			// 分析データの読み込み
			for (int counter = 0, decimal = 10;;) {
				// キーの取得
				byte[] key = contentDbm.iternext();
				if (key == null) {
					break;
				}
	
				// レコードの取得
				Map<Integer, String> contentMap = (Map<Integer, String>)contentDbm.getObject(key);

				// 接尾辞インデックスの作成
				for (Integer blogFeedId: contentMap.keySet()) {
					String content = contentMap.get(blogFeedId);
					list.addAll(
							SuffixArrayUtil.createSuffixIndexEntity(content, SplogFilterConstants.COPY_CONTENT_LENGTH
									, ConstantWords.period, blogFeedId, suffixHashcodeLength, hashcodeSet));
				}

				double percent = (double)++counter / contentDbm.rnum() * 100;
				if (percent >= decimal) {
					logger.info("create suffix index:" + (int)percent + "%");
					decimal += 10;
				}
			}

			return list;

		} catch (OutOfMemoryError e) {
			String message = "create suufix index=" + list.size();
			throw new RuntimeException(message, e);
		} finally {
			if (contentDbm != null) try { contentDbm.close(); } catch (QDBMException e) {}
		}
	}

	public static void main(String[] args) {

		SQLExecutor executor = null;
		CopyFilterByQDBM filter = null;

		try {
			// パラメータの取得
			Integer limit = null;
			try {
				limit = Integer.parseInt(args[0]);
			} catch (Exception e) {	}

			// 初期処理
			executor = new SQLExecutor(SplogFilterConstants.DB_BLOG_FEEDS);
			filter = new CopyFilterByQDBM();
			filter.initialize(null);

			// ブログフィードの取得
			BlogFeedDao blogFeedDao = new BlogFeedDao(executor);
			List<Integer> blogFeeds = blogFeedDao.getIdList(limit);
			
			long total = System.currentTimeMillis();
			logger.info("----- start -----");

			// 入力開始
			long time = System.currentTimeMillis();
			filter.startInput();
			
			// 入力
			for (int blogFeedId: blogFeeds) {
				BlogFeedEntity blogFeed = blogFeedDao.selectById(blogFeedId);
				filter.input(blogFeed);
			}

			// 入力終了
			filter.endInput();
			logger.info("input(" + blogFeeds.size() + "):" + getExecuteSecond(time) + "sec");

			// 分析
			Map<Object, String> results = filter.analyze();

			logger.info("----- end -----");
			logger.info("total:" + getExecuteSecond(total) + "sec");
			//logger.debug("\t" + "documentId" + "\t" + "rate");
			//for (Object key: results.keySet()) {
			//	logger.debug("\t" + key
			//			+ "\t" + results.get(key)
			//	);
			//}

		} catch (Exception e) {
			logger.error("", e);
		} catch (OutOfMemoryError e) {
			logger.error("", e);
		} finally {
			if (filter != null) filter.finalize();
			if (executor != null) executor.finalize();
		}
	}
}
