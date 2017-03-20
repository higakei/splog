package jp.co.hottolink.splogfilter.learning.logic.entity;

/**
 * <p>
 * 学習データのデータセットのEntityクラス.
 * </p>
 * @author higa
 */
public class DatasetEntity {

	/**
	 * <p>
	 * データセットID.
	 * </p>
	 */
	private Integer id = null;

	/**
	 * <p>
	 * データセット名.
	 * </p>
	 */
	private String name = null;

	/**
	 * <p>
	 * データセットIDを取得する.
	 * </p>
	 * @return データセットID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * <p>
	 * データセットIDを設定する.
	 * </p>
	 * @param id データセットID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * <p>
	 * データセット名を取得する.
	 * </p>
	 * @return データセット名
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>
	 * データセット名を設定する.
	 * </p>
	 * @param name データセット名
	 */
	public void setName(String name) {
		this.name = name;
	}
}
