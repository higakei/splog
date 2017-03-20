package jp.co.hottolink.splogfilter.sen;

import java.util.Arrays;

import net.java.sen.StringTagger;
import net.java.sen.Token;

public class SenTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			StringTagger tagger = StringTagger.getInstance();
			//Token[] tokens = tagger.analyze(args[0]);
			//System.out.println(Arrays.asList(tokens));
			for (String arg: args) {
				Token[] tokens = tagger.analyze(arg);
				System.out.println(Arrays.asList(tokens));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
