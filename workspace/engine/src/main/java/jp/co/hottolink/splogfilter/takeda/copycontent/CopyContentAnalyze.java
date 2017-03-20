package jp.co.hottolink.splogfilter.takeda.copycontent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import jp.co.hottolink.splogfilter.common.db.SQLExecutor;
import jp.co.hottolink.splogfilter.common.util.StringUtil;
import jp.co.hottolink.splogfilter.dao.BlogFeedDao;
import jp.co.hottolink.splogfilter.entity.BlogFeedEntity;
import jp.co.hottolink.splogfilter.takeda.ConstantWordSetter;
import jp.co.hottolink.splogfilter.takeda.comparator.SuffixIndexComparator;
import jp.co.hottolink.splogfilter.takeda.constants.ConstantWords;
import jp.co.hottolink.splogfilter.takeda.constants.CopyFilterConfig;
import jp.co.hottolink.splogfilter.takeda.constants.SplogFilterConstants;
import jp.co.hottolink.splogfilter.takeda.copycontent.entity.CharacterCodeKeyEntity;
import jp.co.hottolink.splogfilter.takeda.copycontent.entity.CopyContentEntity;
import jp.co.hottolink.splogfilter.takeda.copycontent.util.SuffixArrayUtil;

/**
 * <p>
 * コピーコンテンツの分析クラス.
 * </p>
 * @author higa
 */
public class CopyContentAnalyze {

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(CopyContentAnalyze.class);

	/**
	 * <p>
	 * コンテンツ.
	 * </p>
	 */
	private String[] contents = null;

	/**
	 * <p>
	 * コピー領域.
	 * </p>
	 */
	private boolean[][] fills = null;

	/**
	 * <p>
	 * コンパレータ.
	 * </p>
	 */
	private SuffixIndexComparator comparator = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param documents コピーコンテンツ
	 */
	public CopyContentAnalyze(List<CopyContentEntity> documents) {
		if (documents != null) {
			contents = new String[documents.size()];
			fills = new boolean[documents.size()][];
			for (int i = 0; i < documents.size(); i++) {
				CopyContentEntity document = documents.get(i);
				contents[i] = document.getContent();
				fills[i] = document.getFill();
			}
			comparator = new SuffixIndexComparator(contents);
		}
	}

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @param documents1 コピーコンテンツ1
	 * @param documents2 コピーコンテンツ2
	 */
	public CopyContentAnalyze(List<CopyContentEntity> documents1,
			List<CopyContentEntity> documents2) {

		// サイズの取得
		int size1 = 0;
		if (documents1 != null) size1 = documents1.size();
		int size2 = 0;
		if (documents2 != null) size2 = documents2.size();
		int size = size1 + size2;

		// 初期化
		contents = new String[size];
		fills = new boolean[size][];
		int index = 0;

		// コピーコンテンツ1
		for (int i = 0; i < size1; i++, index++) {
			CopyContentEntity content = documents1.get(i);
			contents[index] = content.getContent();
			fills[index] = content.getFill();
		}

		// コピーコンテンツ2
		for (int i = 0; i < size2; i++, index++) {
			CopyContentEntity content = documents2.get(i);
			contents[index] = content.getContent();
			fills[index] = content.getFill();
		}

		// コンパレータの作成
		comparator = new SuffixIndexComparator(contents);
	}

	/**
	 * <p>
	 * 本文を分析する.
	 * </p>
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 */
	public void analyze(int length, String delimitor) {
		analyze(length, delimitor, null);
	}

	/**
	 * <p>
	 * 本文を分析する.
	 * </p>
	 * @param length 文字数
	 * @param delimitor 区切り文字
 	 * @param key 文字コードキー 
	 */
	public void analyze(int length, String delimitor, List<CharacterCodeKeyEntity> keys) {
		long time = System.currentTimeMillis();
		// 接尾辞インデックス数の取得
		int indexSize = 0;
		for (int i = 0; i < contents.length; i++) {
			indexSize += SuffixArrayUtil.getSuffixIndexSize(contents[i], length, delimitor, keys);
		}
		if (keys == null) logger.info("get suffix index size:" + getTime(time) + "sec");
		if (keys == null) logger.info("suffix index size:" + indexSize);
		// 分析
		if (indexSize < CopyFilterConfig.ANALYZE2_LIMIT_INDEX_SIZE) {
			analyze2(length, delimitor, keys);
		} else {
			analyze3(length, delimitor, keys);
		}
	}

	/**
	 * <p>
	 * 本文を分析する.
	 * </p>
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 */
	public void analyze1(int length, String delimitor) {
		analyze1(length, delimitor, null);
	}

	/**
	 * <p>
	 * 本文を分析する.
	 * </p>
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @param keys 文字コードキー 
	 */
	public void analyze1(int length, String delimitor, List<CharacterCodeKeyEntity> keys) {
		
		// 文字コードキーサイズが文字数を超えた場合
		int maxSize = length - 1;
		if ((keys != null) && (maxSize < keys.size())) {
			keys = keys.subList(0, maxSize); 
		}
		long time = System.currentTimeMillis();
		// 接尾辞インデックスの取得
		List<int[]> suffixIndexes = new ArrayList<int[]>(0);
		for (int i = 0; i < contents.length; i++) {
			List<int[]> document = SuffixArrayUtil.createSuffixIndex(contents[i], length, delimitor, i, keys);
			suffixIndexes.addAll(document);
		}
		if (keys == null) logger.info("create suffix index:" + getTime(time) + "sec");
		if (keys == null) time = System.currentTimeMillis();
		// 接尾辞インデックスのソート
		Collections.sort(suffixIndexes, comparator);
		if (keys == null) logger.info("sort suffix index:" + getTime(time) + "sec");
		if (keys == null) time = System.currentTimeMillis();
		// コピー領域のマーク
		fill(suffixIndexes, length);
		if (keys == null) logger.info("fill:" + getTime(time) + "sec");
		if (keys != null) logger.info("code" + CharacterCodePartition.getPartitionLabel(keys) + " " + suffixIndexes.size() + ":" + getTime(time) + "sec");
	}

	/**
	 * <p>
	 * 本文を分析する.
	 * </p>
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 */
	public void analyze2(int length, String delimitor) {
		analyze2(length, delimitor, null);
	}

	/**
	 * <p>
	 * 本文を分析する.
	 * </p>
	 * @param length 文字数
	 * @param delimitor 区切り文字
 	 * @param keys 文字コードキー
	 */
	public void analyze2(int length, String delimitor, List<CharacterCodeKeyEntity> keys) {

		// 文字コードキーサイズが文字数に達したら、分析1
		int maxSize = length - 1;
		if ((keys != null) && (maxSize <= keys.size())) {
			analyze1(length, delimitor, keys.subList(0, maxSize));
			return;
		}
		long time = System.currentTimeMillis();
		// 接尾辞インデックスの取得
		List<List<int[]>> suffixIndexes = createSuffixIndex2(length, delimitor, keys);
		logger.debug("create suffix index:" + getTime(time) + "sec");
		// コピー領域のマーク
		int size = 0;
		for (int i = 0; i < suffixIndexes.size(); i++) {
			List<int[]> subList = suffixIndexes.get(i);
			long loopTime = System.currentTimeMillis();
			Collections.sort(subList, comparator);
			fill(subList, length);
			if (keys == null) logger.info("code(" + (i + 1) + ") " + subList.size() + ":" + getTime(loopTime) + "sec");
			size += subList.size();
		}
		if (keys != null) logger.info("code" + CharacterCodePartition.getPartitionLabel(keys) + " " + size + ":" + getTime(time) + "sec");
	}

	/**
	 * <p>
	 * 本文を分析する.
	 * </p>
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 */
	public void analyze3(int length, String delimitor) {
		analyze3(length, delimitor, null);
	}

	/**
	 * <p>
	 * 本文を分析する.
	 * </p>
	 * @param length 文字数
	 * @param delimitor 区切り文字
 	 * @param key 文字コードキー 
	 */
	public void analyze3(int length, String delimitor, List<CharacterCodeKeyEntity> keys) {

		// 文字コードキーサイズが文字数に達したら、分析1
		int maxSize = length - 1;
		if ((keys != null) && (maxSize <= keys.size())) {
			analyze1(length, delimitor, keys.subList(0, maxSize));
			return;
		}

		for (int i = 0; i < CharacterCodePartition.PARTITION_CHARACTER_CODE3.length; i++) {
			List<CharacterCodeKeyEntity> newKey = CharacterCodePartition.addKey(i, CharacterCodePartition.PARTITION_CHARACTER_CODE3, keys);
			//analyze2(length, delimitor, newKey);
			
			// 接尾辞インデックス数の取得
			int indexSize = 0;
			for (int j = 0; j < contents.length; j++) {
				indexSize += SuffixArrayUtil.getSuffixIndexSize(contents[j], length, delimitor, newKey);
			}

			// 分析
			if (indexSize < CopyFilterConfig.ANALYZE3_LIMIT_INDEX_SIZE) {
				analyze2(length, delimitor, newKey);
			} else {
				analyze3(length, delimitor, newKey);
			}
		}
	}

	/**
	 * <p>
	 * 本文を分析する.
	 * </p>
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 */
	public void analyze4(int length, String delimitor) {
		analyze4(length, delimitor, null);
	}

	/**
	 * <p>
	 * 本文を分析する.
	 * </p>
	 * @param length 文字数
	 * @param delimitor 区切り文字
 	 * @param key 文字コードキー 
	 */
	public void analyze4(int length, String delimitor, List<CharacterCodeKeyEntity> keys) {

		// 文字コードキーサイズが文字数に達したら、分析1
		int maxSize = length - 1;
		if ((keys != null) && (maxSize <= keys.size())) {
			analyze1(length, delimitor, keys.subList(0, maxSize));
			return;
		}


		for (int i = 0; i < CharacterCodePartition.PARTITION_CHARACTER_CODE3.length; i++) {
			List<CharacterCodeKeyEntity> newKey = CharacterCodePartition.addKey(i, CharacterCodePartition.PARTITION_CHARACTER_CODE3, keys);

			// 接尾辞インデックス数の取得
			int indexSize = 0;
			for (int j = 0; j < contents.length; j++) {
				indexSize += SuffixArrayUtil.getSuffixIndexSize(contents[j], length, delimitor, newKey);
			}

			// 分析
			if (indexSize < 8000000) {
				analyze2(length, delimitor, newKey);
			} else {
				analyze3(length, delimitor, newKey);
			}
		}
	}

	/**
	 * <p>
	 * 分析2の接尾辞インデックス数を取得する.
	 * </p>
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 */
	public void countSuffixIndex2(int length, String delimitor) {
		List<List<int[]>> suffixIndexes = createSuffixIndex2(length, delimitor);
		for (int i = 0; i < suffixIndexes.size(); i++) {
			List<int[]> partition = suffixIndexes.get(i);
			logger.info("\t" + CharacterCodePartition.getCharacterCodeLabel2(i)
					+ "\t" + partition.size()
			);
		}
	}
	
	/**
	 * <p>
	 * 分析3の接尾辞インデックス数を取得する.
	 * </p>
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 */
	public void countSuffixIndex3(int length, String delimitor) {
		List<List<int[]>> suffixIndexes = createSuffixIndex3(length, delimitor);
		for (int i = 0; i < suffixIndexes.size(); i++) {
			List<int[]> partition = suffixIndexes.get(i);
			logger.info("\t" + CharacterCodePartition.getCharacterCodeLabel3(i)
					+ "\t" + partition.size()
			);
		}
	}

	/**
	 * <p>
	 * 処理時間を秒で取得する.
	 * </p>
	 * @param start 開始時間
	 * @return 処理時間
	 */
	private static double getTime(long start) {
		long end = System.currentTimeMillis();
		double time = end - start;
		return time / 1000;
	}

	/**
	 * <p>
	 * 分析2の接尾辞インデックスを作成する.
	 * </p><ul>
	 * <li>コンテンツから、指定した文字で区切り、指定した文字数以上の、接尾辞インデックスを作成する</li>
	 * <li>接尾辞インデックスを文字コードで分割したリストに格納する</li>
	 * </ul>
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @return 接尾辞インデックス
	 */
	private List<List<int[]>> createSuffixIndex2(int length, String delimitor) {
		return createSuffixIndex2(length, delimitor, null);
	}

	/**
	 * <p>
	 * 分析2の接尾辞インデックスを作成する.
	 * </p><ul>
	 * <li>コンテンツから、指定した文字で区切り、指定した文字数以上の、指定した文字コードキーで始まる、接尾辞インデックスを作成する</li>
	 * <li>接尾辞インデックスを文字コードで分割したリストに格納する</li>
	 * </ul>
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @param keys 文字コードキー
	 * @return 接尾辞インデックス
	 */
	private List<List<int[]>> createSuffixIndex2(int length, String delimitor, List<CharacterCodeKeyEntity> keys) {

		List<List<int[]>> partitions = new ArrayList<List<int[]>>(0);
		if (contents == null) {
			return partitions;
		}

		// 初期化
		for (int i = 0; i < CharacterCodePartition.PARTITION_CHARACTER_CODE2.length; i++) {
			List<int[]> partition = new ArrayList<int[]>(0);
			partitions.add(partition);
		}

		int position = 0;
		if (keys != null) {
			position = keys.size();
		}
		
		// 接尾辞インデックスの作成 
		for (int i = 0; i < contents.length; i++) {
			List<int[]> suffixIndexes = SuffixArrayUtil.createSuffixIndex(contents[i], length, delimitor, i, keys);
			for (int[] suffixIndex: suffixIndexes) {
				String suffix = SuffixArrayUtil.getSuffix(contents, suffixIndex);
				int partitionIndex = CharacterCodePartition.getCharacterCodeIndex2(suffix, position);
				partitions.get(partitionIndex).add(suffixIndex);
			}
		}

		return partitions;
	}

	/**
	 * <p>
	 * 分析3の接尾辞インデックスを作成する.
	 * </p><ul>
	 * <li>コンテンツから、指定した文字で区切り、指定した文字数以上の、接尾辞インデックスを作成する</li>
	 * <li>接尾辞インデックスを先頭の文字コードで分割したリストに格納する</li>
	 * </ul>
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @return 接尾辞インデックス
	 */
	private List<List<int[]>> createSuffixIndex3(int length, String delimitor) {

		List<List<int[]>> partitions = new ArrayList<List<int[]>>(0);
		if (contents == null) {
			return partitions;
		}

		// 初期化
		for (int i = 0; i < CharacterCodePartition.PARTITION_CHARACTER_CODE3.length; i++) {
			List<int[]> partition = new ArrayList<int[]>(0);
			partitions.add(partition);
		}

		// 接尾辞インデックスの作成 
		for (int i = 0; i < contents.length; i++) {
			List<int[]> suffixIndexes = SuffixArrayUtil.createSuffixIndex(contents[i], length, delimitor, i);
			for (int[] suffixIndex: suffixIndexes) {
				String suffix = SuffixArrayUtil.getSuffix(contents, suffixIndex);
				int partitionIndex = CharacterCodePartition.getCharacterCodeIndex3(suffix, 0);
				partitions.get(partitionIndex).add(suffixIndex);
			}
		}

		return partitions;
	}

	/**
	 * <p>
	 * コピー領域をマークする.
	 * </p>
	 * @param suffixIndexes 接尾辞インデックス
	 * @param length 文字数
	 */
	private void fill(List<int[]> suffixIndexes, int length) {

		if (suffixIndexes == null) {
			return;
		}

		for (int i = 0; i < suffixIndexes.size() - 1; i++) {
			int[] current = suffixIndexes.get(i);
			int[] next = suffixIndexes.get(i + 1);
	
			// 同じドキュメントの場合、マークしない
			int currentId = current[SuffixArrayUtil.DOCUMENT_INDEX];
			int nextId = next[SuffixArrayUtil.DOCUMENT_INDEX];
			if (currentId == nextId) {
				continue;
			}
		
			// インデックスの取得
			String currentSuffix = SuffixArrayUtil.getSuffix(contents, current);
			String nextSuffix = SuffixArrayUtil.getSuffix(contents, next);
				
			// 一致長の取得
			int range = StringUtil.getPrefixLength(currentSuffix, nextSuffix);
			if (range >= length) {
				// 両方にマークする
				fill(current[SuffixArrayUtil.CONTENT_INDEX], range, fills[currentId]);
				fill(next[SuffixArrayUtil.CONTENT_INDEX], range, fills[nextId]);
			}
		}
	}

	/**
	 * <p>
	 * マーク領域にマークする.
	 * </p>
	 * @param index インデックス
	 * @param range 長さ
	 * @param fill マーク領域
	 */
	private void fill(int index, int range, boolean[] fill) {
	
		if (fill == null) {
			return;
		}
	
		int beginIndex = index;
		int endIndex = index + range;
		if ((0 > beginIndex) || (beginIndex > endIndex) || (endIndex > fill.length)) {
			return;
		}
	
		for (int i = beginIndex; i < endIndex; i++) {
			fill[i] = true;
		}
	
		return;
	}

	public static void main(String[] args) {

		SQLExecutor executor = null;

		try {
			Integer size = null;
			try {
				size = Integer.parseInt(args[0]);
			} catch (Exception e) {	}

			// 1. スパム判定語の取得
			executor = new SQLExecutor(SplogFilterConstants.DB_BLOG_FEEDS);
			ConstantWordSetter.setFromDB(executor);

			// 2. ブログフィードの取得
			BlogFeedDao blogFeedDao = new BlogFeedDao(executor);
			List<BlogFeedEntity> blogFeeds = blogFeedDao.selectAll(size);
			logger.info("blogfeeds size:" + blogFeeds.size());

			// 3. コピーコンテンツの作成
			CopyContentCreator creator = new CopyContentCreator(blogFeeds);
			List<CopyContentEntity> documents = creator.createByClearBody(SplogFilterConstants.COPY_CONTENT_LENGTH);

			// 4. 結果の出力
			CopyContentAnalyze analyzer = new CopyContentAnalyze(documents);
			analyzer.countSuffixIndex2(SplogFilterConstants.COPY_CONTENT_LENGTH, ConstantWords.period);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (executor != null) executor.finalize();
		}
	}
}
