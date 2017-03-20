package jp.co.hottolink.splogfilter.takeda.copycontent.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jp.co.hottolink.splogfilter.common.entity.SubstringEntity;
import jp.co.hottolink.splogfilter.common.util.StringUtil;
import jp.co.hottolink.splogfilter.takeda.copycontent.entity.CharacterCodeKeyEntity;
import jp.co.hottolink.splogfilter.takeda.copycontent.entity.SuffixIndexEntity;

/**
 * <p>
 * 接尾辞配列のUtilクラス.
 * </p>
 * @author higa
 */
public class SuffixArrayUtil {

	/**
	 * <p>
	 * 接尾辞インデックスのフィールド数.
	 * </p>
	 */
	public static final int SUFFIX_INDEX_SIZE = 3;

	/**
	 * <p>
	 * ドキュメントのインデックス.
	 * </p><pre>
	 * 接尾辞インデックスのフィールドインデックス
	 * </pre>
	 */
	public static final int DOCUMENT_INDEX = 0;

	/**
	 * <p>
	 * コンテンツのインデックス.
	 * </p><pre>
	 * 接尾辞インデックスのフィールドインデックス
	 * </pre>
	 */
	public static final int CONTENT_INDEX = 1;

	/**
	 * <p>
	 * 接尾辞の文字数.
	 * </p><pre>
	 * 接尾辞インデックスのフィールドインデックス
	 * </pre>
	 */
	public static final int SUFFIX_LENGTH = 2;

	/**
	 * <p>
	 * コンテンツから、指定した文字で区切り、指定した文字数以上の、接尾辞インデックスを作成する.
	 * </p>
	 * @param content コンテンツ
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @param documentId ドキュメントID
	 * @return 接尾辞インデックス
	 */
	public static List<int[]> createSuffixIndex(String content, int length, String delimitor, int documentId) {
		return createSuffixIndex(content, length, delimitor, documentId, null);
	}

	/**
	 * <p>
	 * コンテンツから、指定した文字で区切り、指定した文字数以上、指定した文字コードキーで始まる、接尾辞インデックスを作成する.
	 * </p>
	 * @param content コンテンツ
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @param documentId ドキュメントID
	 * @param keys 文字コードキー
	 * @return 接尾辞インデックス
	 */
	public static List<int[]> createSuffixIndex(String content, int length,
			String delimitor, int documentId, List<CharacterCodeKeyEntity> keys) {
	
		List<int[]> list = new ArrayList<int[]>(0);
		if ((content == null) || (content.length() < length)) {
			return list;
		}
	
		// 区切り文字で区切る
		List<SubstringEntity> splits = StringUtil.split(content, delimitor);
	
		// 接尾辞インデックスリストの作成
		for (SubstringEntity split: splits) {
			int beginIndex = split.getIndex();
			int endIndex = beginIndex + split.getLength();
			String substring = content.substring(beginIndex, endIndex);

			for (int i = 0; i < split.getLength(); i++) {
				// 接尾辞の作成
				String suffix = substring.substring(i);
				if (suffix.length() < length) {
					// 指定した文字数より小さい場合、リストに追加しない
					break;
				}

				// 接頭辞で始まらない場合、リストに追加しない
				if (!startsWith(suffix, keys)) {
					continue;
				}

				// インデックスの作成
				int[] index = new int[SUFFIX_INDEX_SIZE];
				index[DOCUMENT_INDEX] = documentId;
				index[CONTENT_INDEX] = split.getIndex() + i;
				index[SUFFIX_LENGTH] = suffix.length();
	
				// リストに追加
				list.add(index);
			}
		}
	
		return list;
	}

	/**
	 * <p>
	 * コンテンツから、指定した文字で区切り、指定した文字数以上、指定した文字コードキーで始まる、接尾辞インデックスを作成する.
	 * </p>
	 * @param content コンテンツ
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @param documentId ドキュメントID
	 * @return 接尾辞インデックス
	 */
	public static List<SuffixIndexEntity> createSuffixIndexEntity(
			String content, int length, String delimitor, int documentId) {
		return createSuffixIndexEntity(content, length, delimitor, documentId, null);
	}

	/**
	 * <p>
	 * コンテンツから、指定した文字で区切り、指定した文字数以上、指定した文字コードキーで始まる、接尾辞インデックスを作成する.
	 * </p>
	 * @param content コンテンツ
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @param documentId ドキュメントID
	 * @param keys 文字コードキー
	 * @return 接尾辞インデックス
	 */
	public static List<SuffixIndexEntity> createSuffixIndexEntity(
			String content, int length, String delimitor, int documentId,
			List<CharacterCodeKeyEntity> keys) {
		
		List<SuffixIndexEntity> list = new ArrayList<SuffixIndexEntity>(0);
		if ((content == null) || (content.length() < length)) {
			return list;
		}
	
		// 区切り文字で区切る
		List<SubstringEntity> splits = StringUtil.split(content, delimitor);
	
		// 接尾辞インデックスリストの作成
		for (SubstringEntity split: splits) {
			int beginIndex = split.getIndex();
			int endIndex = beginIndex + split.getLength();
			String substring = content.substring(beginIndex, endIndex);

			for (int i = 0; i < split.getLength(); i++) {
				// 接尾辞の作成
				String suffix = substring.substring(i);
				if (suffix.length() < length) {
					// 指定した文字数より小さい場合、リストに追加しない
					break;
				}

				// 接頭辞で始まらない場合、リストに追加しない
				if (!startsWith(suffix, keys)) {
					continue;
				}

				// 接尾辞インデックスの作成
				SuffixIndexEntity entity = new SuffixIndexEntity(suffix);
				entity.setDocumentId(documentId);
				entity.setPosition(split.getIndex() + i);
	
				// リストに追加
				list.add(entity);
			}
		}
	
		return list;
	}

	/**
	 * <p>
	 * コンテンツから、指定した文字で区切り、指定した文字数以上、指定した文字コードキーで始まる、接尾辞インデックスを作成する.
	 * </p>
	 * @param content コンテンツ
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @param documentId ドキュメントID
	 * @param hashcodeLength ハッシュコードの文字数
	 * @param hashcodeSet ハッシュコードセット
	 * @return 接尾辞インデックス
	 */
	public static List<SuffixIndexEntity> createSuffixIndexEntity(
			String content, int length, String delimitor, int documentId,
			int hashcodeLength, Set<Integer> hashcodeSet) {
		
		List<SuffixIndexEntity> list = new ArrayList<SuffixIndexEntity>(0);
		if ((content == null) || (content.length() < length)) {
			return list;
		}
	
		// 区切り文字で区切る
		List<SubstringEntity> splits = StringUtil.split(content, delimitor);
	
		// 接尾辞インデックスリストの作成
		for (SubstringEntity split: splits) {
			int beginIndex = split.getIndex();
			int endIndex = beginIndex + split.getLength();
			String substring = content.substring(beginIndex, endIndex);

			for (int i = 0; i < split.getLength(); i++) {
				// 接尾辞の作成
				String suffix = substring.substring(i);
				if (suffix.length() < length) {
					// 指定した文字数より小さい場合、リストに追加しない
					break;
				}

				
				// ハッシュコードがセットに存在しない場合
				int hashcode = suffix.substring(0, hashcodeLength).hashCode();
				if (!hashcodeSet.contains(hashcode)) {
					continue;
				}

				// 接尾辞インデックスの作成
				SuffixIndexEntity entity = new SuffixIndexEntity(suffix);
				entity.setDocumentId(documentId);
				entity.setPosition(split.getIndex() + i);
	
				// リストに追加
				list.add(entity);
			}
		}
	
		return list;
	}

	/**
	 * <p>
	 * コンテンツから、指定した文字で区切り、指定した文字数以上の、接尾辞インデックスを作成する.
	 * </p>
	 * @param content コンテンツ
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @return 接尾辞インデックス
	 */
	public static int getSuffixIndexSize(String content, int length, String delimitor) {
		return getSuffixIndexSize(content, length, delimitor, null);
	}

	/**
	 * <p>
	 * コンテンツから、指定した文字で区切り、指定した文字数以上の、指定した文字コードキーで始まる、接尾辞インデックスを作成する.
	 * </p>
	 * @param content コンテンツ
	 * @param length 文字数
	 * @param delimitor 区切り文字
	 * @param keys 文字コードキー
	 * @return 接尾辞インデックス
	 */
	public static int getSuffixIndexSize(String content, int length, String delimitor, List<CharacterCodeKeyEntity> keys) {
	
		int counter = 0;
		if ((content == null) || (content.length() < length)) {
			return counter;
		}
	
		// 区切り文字で区切る
		List<SubstringEntity> splits = StringUtil.split(content, delimitor);
	
		// 接尾辞インデックスリストの作成
		for (SubstringEntity split: splits) {
			int beginIndex = split.getIndex();
			int endIndex = beginIndex + split.getLength();
			String substring = content.substring(beginIndex, endIndex);

			for (int i = 0; i < split.getLength(); i++) {
				// 接尾辞の作成
				String suffix = substring.substring(i);
				if (suffix.length() < length) {
					// 指定した文字数より小さい場合、カウントしない
					break;
				}

				// 接頭辞で始まらない場合、カウントしない
				if (!startsWith(suffix, keys)) {
					continue;
				}

				// カウント
				++ counter;
			}
		}
	
		return counter;
	}

	/**
	 * <p>
	 * 接尾辞を取得する.
	 * </p>
	 * @param contents コンテンツ
	 * @param suffixIndex 接尾辞インデックス
	 * @return 接尾辞
	 */
	public static String getSuffix(String[] contents, int[] suffixIndex) {

		// パラメータチェック
		if ((contents == null) || (suffixIndex == null)
				|| (suffixIndex.length != SUFFIX_INDEX_SIZE)) {
			return null;
		}

		// ドキュメントインデックスのチェック
		int contentIndex = suffixIndex[DOCUMENT_INDEX];
		if ((0 > contentIndex) || (contentIndex >= contents.length)) {
			return null;
		}

		// コンテンツの取得
		String content = contents[contentIndex];
		if (content == null) {
			return null;
		}

		// コンテンツインデックスのチェック
		int beginIndex = suffixIndex[CONTENT_INDEX];
		int endIndex = beginIndex + suffixIndex[SUFFIX_LENGTH];
		if ((0 > beginIndex) || (beginIndex >= endIndex)
				|| (endIndex > content.length())) {
			return null;
		}
		
		return content.substring(beginIndex, endIndex);
	}

	/**
	 * <p>
	 * 接尾辞が文字コードキーで始まるかどうかを判定します.
	 * </p>
	 * @param suffix 接尾辞
	 * @param keys  文字コードキー
	 * @return true: 始まる, false: 始まらない
	 */
	public static boolean startsWith(String suffix, List<CharacterCodeKeyEntity> keys) {
		if (keys == null) return true;
		for (int i = 0; i < keys.size(); i++) {
			CharacterCodeKeyEntity key = keys.get(i);
			char c = suffix.charAt(i);
			if ((c < key.getFrom()) || (key.getTo() < c)) {
				return false;
			}
		}
		return true;
	}
}
