package jp.co.hottolink.splogfilter.common.api.buzz.parameter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import jp.co.hottolink.splogfilter.common.api.buzz.entity.ReputationEntity;

/**
 * <p>
 * 属性指定のパラメータクラス.
 * </p>
 * @author higa
 */
public class AttributeParameter {

	/**
	 * <p>
	 * 属性AND指定.
	 * </p>
	 */
	public static final int INCLUDE = 1;

	/**
	 * <p>
	 * 属性NOT指定.
	 * </p>
	 */
	public static final int EXCLUDE = 2;

	/**
	 * <p>
	 * 評判の属性名.
	 * </p>
	 */
	private static final String REPUTATION = "rps";

	/**
	 * <p>
	 * 評判.
	 * </p>
	 */
	private Set<ReputationEntity> rps = null;

	/**
	 * <p>
	 * 評判以外の属性.
	 * </p>
	 */
	private Map<String, Object> others = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 */
	public AttributeParameter() {
		rps = new HashSet<ReputationEntity>();
		others = new HashMap<String, Object>();
	}

	/**
	 * <p>
	 * 属性の値を設定する.
	 * </p><pre>
	 * 評判以外の属性
	 * </pre>
	 * @param key 属性名
	 * @param value 値
	 * @return 設定前の属性の値（未設定の場合は<tt>null</tt>）
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object set(String key, Object value) {
		if (others == null) others = new HashMap<String, Object>();
		return others.put(key, value);
	}

	/**
	 * <p>
	 * 評判の属性の値を設定する.
	 * </p>
	 * @param word 対象語
	 * @param reputation 評判
	 */
	public void setReputation(String word, int reputation) {

		ReputationEntity entity = new ReputationEntity();
		entity.setWord(word);
		entity.setReputation(reputation);

		if (rps == null) rps = new HashSet<ReputationEntity>();
		rps.add(entity);
	}

	/**
	 * <p>
	 * 属性AND指定の有無を判定する.
	 * </p>
	 * @return <tt>true</tt>無, <tt>false</tt>有
	 */
	public boolean isEmpty() {
		if ((others == null) && (rps == null)) {
			return true;
		} else if ((others == null) && rps.isEmpty()) {
			return true;
		} else if (others.isEmpty() && (rps == null)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * 属性AND指定のAPIパラメータを作成する.
	 * </p><pre>
	 * buzzapiのincludeパラメータ
	 * </pre>
	 * @return APIパラメータ
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	public String toParameter() throws MessagingException, IOException {

		StringBuffer parameter = new StringBuffer();
		if (isEmpty()) {
			return parameter.toString();
		}

		// 評判
		if ((rps != null) && !rps.isEmpty()){
			parameter.append("&");
			parameter.append(REPUTATION);
			parameter.append("=");
			parameter.append(toReptationParameter(rps));
		}

		// 評判以外の属性
		for (String key: others.keySet()) {
			Object value = others.get(key);
			if (value == null) {
				continue;
			}

			parameter.append("&");
			parameter.append(key);
			parameter.append("=");
			parameter.append(others.get(key));
		}

		return parameter.substring(1);
	}

	/**
	 * <p>
	 * 評判のAPIパラメータを作成する.
	 * </p>
	 * @param rps 評判
	 * @return APIパラメータ
	 * @throws MessagingException
	 * @throws IOException
	 */
	private String toReptationParameter(Set<ReputationEntity> rps)
			throws MessagingException, IOException {

		StringBuffer parameter = new StringBuffer();
		if ((rps == null) || rps.isEmpty()){
			return parameter.toString();
		}

		for (ReputationEntity reputation: rps) {
			parameter.append("|");
			parameter.append(reputation.toParameter());
		}

		return parameter.substring(1);
	}
}
