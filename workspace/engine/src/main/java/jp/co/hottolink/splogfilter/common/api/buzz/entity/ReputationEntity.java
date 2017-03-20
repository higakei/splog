package jp.co.hottolink.splogfilter.common.api.buzz.entity;

import java.io.IOException;
import java.io.Serializable;

import javax.mail.MessagingException;

import jp.co.hottolink.splogfilter.common.api.buzz.constants.BuzzAPIConstants;
import jp.co.hottolink.splogfilter.common.api.buzz.util.Base64Util;

/**
 * <p>
 * 評判のEntityクラス.
 * </p>
 * @author higa
 */
public class ReputationEntity implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 1292700232117836038L;

	/**
	 * <p>
	 * 対象語.
	 * </p>
	 */
	private String word = null;

	/**
	 * <p>
	 * 評判.
	 * </p>
	 */
	private int reputation = 0;

	/**
	 * <p>
	 * 対象語を取得する.
	 * </p>
	 * @return 対象語
	 */
	public String getWord() {
		return word;
	}

	/**
	 * <p>
	 * 対象語を設定する.
	 * </p>
	 * @param word 対象語
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * <p>
	 * 評判を取得する.
	 * </p>
	 * @return 評判
	 */
	public int getReputation() {
		return reputation;
	}

	/**
	 * <p>
	 * 評判を設定する.
	 * </p>
	 * @param reputation 評判
	 */
	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	/**
	 * <p>
	 * APIパラメータを作成する.
	 * </p>
	 * @return APIパラメータ
	 * @throws MessagingException
	 * @throws IOException
	 */
	public String toParameter() throws MessagingException, IOException {
		return Base64Util.encode(word) + "," + reputation;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		// 対象語
		StringBuffer buffer = new StringBuffer();
		if (word == null) {
			buffer.append("null");
		} else {
			buffer.append(word);
		}

		// 評判
		if (reputation == BuzzAPIConstants.REPUTATION_POSITIVE) {
			buffer.append(",ポジティブ");
		} else if (reputation == BuzzAPIConstants.REPUTATION_NEGATIVE) {
			buffer.append(",ネガティブ");
		} else if (reputation == BuzzAPIConstants.REPUTATION_FLAT) {
			buffer.append(",中立");
		} else {
			buffer.append("," + reputation);
		}

		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + reputation;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReputationEntity))
			return false;
		final ReputationEntity other = (ReputationEntity) obj;
		if (reputation != other.reputation)
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
}
