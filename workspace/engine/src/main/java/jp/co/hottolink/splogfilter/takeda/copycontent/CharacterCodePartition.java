package jp.co.hottolink.splogfilter.takeda.copycontent;

import java.util.ArrayList;
import java.util.List;

import jp.co.hottolink.splogfilter.takeda.copycontent.entity.CharacterCodeKeyEntity;

/**
 * <p>
 * 文字コードのパーティションのクラス.
 * </p>
 * @author higa
 */
public class CharacterCodePartition {

	/**
	 * <p>
	 * 分析2の文字コードのパーティション.
	 * </p>
	 */
	public static final int[][] PARTITION_CHARACTER_CODE2 = {
		{0x0000, 0x001F},
		{0x0020, 0x002F},
		{0x0030, 0x003F},
		{0x0040, 0x004F},
		{0x0050, 0x005F},
		{0x0060, 0x006F},
		{0x0070, 0x0FFF},
		{0x1000, 0x1FFF},
		{0x2000, 0x2FFF},
		{0x3000, 0x300F},
		{0x3010, 0x303F},
		{0x3040, 0x304F},
		{0x3050, 0x305F},
		{0x3060, 0x3065},
		{0x3066, 0x3066},
		{0x3067, 0x3067},
		{0x3068, 0x3069},
		{0x306A, 0x306A},
		{0x306B, 0x306D},
		{0x306E, 0x306E},
		{0x306F, 0x306F},
		{0x3070, 0x307F},
		{0x3080, 0x308F},
		{0x3090, 0x309F},
		{0x30A0, 0x30AF},
		{0x30B0, 0x30BF},
		{0x30C0, 0x30CF},
		{0x30D0, 0x30DF},
		{0x30E0, 0x30EF},
		{0x30F0, 0x30FF},
		{0x3100, 0x3FFF},
		{0x4000, 0x4FFF},
		{0x5000, 0x5FFF},
		{0x6000, 0x6FFF},
		{0x7000, 0x7FFF},
		{0x8000, 0x8FFF},
		{0x9000, 0x9FFF},
		{0xA000, 0xEFFF},
		{0xF000, 0xFFFF}
	};

	/**
	 * <p>
	 * 分析3の文字コードのパーティション.
	 * </p>
	 */
	public static final int[][] PARTITION_CHARACTER_CODE3 = {
		{0x0000, 0x0020},
		{0x0021, 0x002F},
		{0x0030, 0x005F},
		{0x0060, 0x006F},
		{0x0070, 0x2FFF},
		{0x3000, 0x304F},
		{0x3050, 0x305F},
		{0x3060, 0x306F},
		{0x3070, 0x309F},
		{0x30A0, 0x30CF},
		{0x30D0, 0x3FFF},
		{0x4000, 0x5FFF},
		{0x6000, 0x6FFF},
		{0x7000, 0xFFFF}
	};

	/**
	 * <p>
	 * 開始文字コードのインデックス.
	 * </p><pre>
	 * 文字コードのパーティションのインデックス
	 * </pre>
	 */
	public static final int PARTITION_INDEX_FROM = 0;

	/**
	 * <p>
	 * 終了文字コードのインデックス.
	 * </p><pre>
	 * 文字コードのパーティションのインデックス
	 * </pre>
	 */
	public static final int PARTITION_INDEX_TO = 1;

	/**
	 * <p>
	 * 指定した文字の文字コードインデックスを取得する.
	 * </p>
	 * @param partitions 文字コードのパーティション
	 * @param c 文字
	 * @return インデックス
	 */
	public static int getCharacterCodeIndex(int[][] partitions, char c) {
		for (int i = 0; i < partitions.length; i++) {
			if ((partitions[i][0] <= c) && (c <= partitions[i][1])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * <p>
	 * 分析2の接尾辞の指定した位置の文字コードインデックスを取得する.
	 * </p>
	 * @param suffix 接尾辞
	 * @param position 接尾辞の位置
	 * @return 文字コードインデックス
	 */
	public static int getCharacterCodeIndex2(String suffix, int position) {
		return getCharacterCodeIndex(PARTITION_CHARACTER_CODE2, suffix.charAt(position));
	}

	/**
	 * <p>
	 * 分析3の接尾辞の指定した位置の文字コードインデックスを取得する.
	 * </p>
	 * @param suffix 接尾辞
	 * @param position 接尾辞の位置
	 * @return 文字コードインデックス
	 */
	public static int getCharacterCodeIndex3(String suffix, int position) {
		return getCharacterCodeIndex(PARTITION_CHARACTER_CODE3, suffix.charAt(position));
	}
	
	/**
	 * <p>
	 * intから16進4桁に変換する.
	 * </p>
	 * @param value int
	 * @return 16進4桁
	 */
	public static String intToHex4(int value) {
		char HEX4[] = { Character.forDigit((value >> 12) & 0x0F, 16),
				Character.forDigit((value >> 8) & 0x0F, 16),
				Character.forDigit((value >> 4) & 0x0F, 16),
				Character.forDigit(value & 0x0F, 16) };
		String Hex4Str = new String(HEX4);
		return Hex4Str.toUpperCase();
	}

	/**
	 * <p>
	 * 指定したインデックスの文字コードパーティションのラベルを取得する.
	 * </p>
	 * @param partitions 文字コードパーティション
	 * @param index インデックス
	 * @return ラベル
	 */
	public static String getCharacterCodeLabel(int[][] partitions, int index) {
		String from = intToHex4(partitions[index][PARTITION_INDEX_FROM]);
		String to = intToHex4(partitions[index][PARTITION_INDEX_TO]);
		return from + "-" + to;
	}


	/**
	 * <p>
	 * 指定したインデックスの分析2の文字コードパーティションのラベルを取得する.
	 * </p>
	 * @param index インデックス
	 * @return ラベル
	 */
	public static String getCharacterCodeLabel2(int index) {
		return getCharacterCodeLabel(PARTITION_CHARACTER_CODE2, index);
	}

	/**
	 * <p>
	 * 指定したインデックスの分析3の文字コードパーティションのラベルを取得する.
	 * </p>
	 * @param index インデックス
	 * @return ラベル
	 */
	public static String getCharacterCodeLabel3(int index) {
		return getCharacterCodeLabel(PARTITION_CHARACTER_CODE3, index);
	}

	/**
	 * <p>
	 * 文字コードキーのクローンを作成する.
	 * </p>
	 * @param original オリジナル
	 * @return クローン
	 */
	public static List<CharacterCodeKeyEntity> clone(List<CharacterCodeKeyEntity> original) {
		if (original == null) return null;
		List<CharacterCodeKeyEntity> clone = new ArrayList<CharacterCodeKeyEntity>(0);
		for (CharacterCodeKeyEntity element: original) {
			clone.add(element);
		}
		return clone;
	}

	/**
	 * <p>
	 * 文字コードキーを追加する.
	 * </p>
	 * @param partition 文字コードのパーティション
	 * @param keys 追加する文字コードキー
	 * @return 追加された文字コードキー
	 */
	public static List<CharacterCodeKeyEntity> addKey(int index, int[][] partitions, List<CharacterCodeKeyEntity> keys) {
	
		// クローンの作成
		List<CharacterCodeKeyEntity> clone = clone(keys);
		if (clone == null) {
			clone = new ArrayList<CharacterCodeKeyEntity>(0);
		}
	
		// 文字コードキーの作成
		CharacterCodeKeyEntity key = new CharacterCodeKeyEntity();
		key.setFrom(partitions[index][PARTITION_INDEX_FROM]);
		key.setTo(partitions[index][PARTITION_INDEX_TO]);
		key.setIndex(index);
	
		// 文字コードキーの追加
		clone.add(key);
	
		return clone;
	}

	/**
	 * <p>
	 * 文字コードキーからパーティションのラベルを取得する.
	 * </p>
	 * @param keys 文字コードキー
	 * @return ラベル
	 */
	public static String getPartitionLabel(List<CharacterCodeKeyEntity> keys) {
	
		if ((keys == null) || (keys.isEmpty())) {
			return "(all)";
		}
		
		String label = "(";
		for (int i = 0; i < keys.size(); i++) {
			CharacterCodeKeyEntity key = keys.get(i);
			if (i > 0) label += "-";
			label += key.getIndex() + 1;
		}
		label += ")";
	
		return label;
	}
}
