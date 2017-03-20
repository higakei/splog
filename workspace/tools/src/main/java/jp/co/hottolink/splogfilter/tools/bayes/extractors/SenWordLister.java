package jp.co.hottolink.splogfilter.tools.bayes.extractors;

import java.io.IOException;
import java.util.Collection;

import com.enigmastation.extractors.impl.SimpleWordLister;

import net.java.sen.StringTagger;
import net.java.sen.Token;

/**
 * <p>
 * Senの形態素解析器を使用するWordListerクラス.
 * </p>
 * @author higa
 */
public class SenWordLister extends SimpleWordLister {

	/**
	 * <p>
	 * 形態素解析器.
	 * </p>
	 */
	protected StringTagger tagger = null;

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public SenWordLister() throws IllegalArgumentException, IOException {
		tagger = StringTagger.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.enigmastation.extractors.impl.SimpleWordLister#addWords(java.lang.Object, java.util.Collection)
	 */
	@Override
	public void addWords(Object document, Collection<String> collection) {
		try {
			if (document == null) {
				return;
			}

			Token[] tokens = tagger.analyze(document.toString());
			for (Token token : tokens) {
				collection.add(token.getSurface());
			}

		} catch (IOException e) {
			throw new RuntimeException(e); 
		}
	}
}
