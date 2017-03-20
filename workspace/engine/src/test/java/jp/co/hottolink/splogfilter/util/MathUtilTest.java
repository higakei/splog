package jp.co.hottolink.splogfilter.util;

/**
 * <p>
 * MathUtilのテストクラス.
 * </p>
 * @author higa
 */
public class MathUtilTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double logarithm = Math.log10(Double.valueOf(args[0]));
		System.out.println(MathUtil.toExponent(logarithm));
	}

}
