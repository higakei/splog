package jp.co.hottolink.splogfilter.tools.bayes.extractors;

import java.io.IOException;
import java.util.Collection;

import net.java.sen.Token;

/**
 * <p>
 * 本文のSenWordListerクラス.
 * </p>
 * @author higa
 */
public class ContentSenWordLister extends SenWordLister {

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public ContentSenWordLister() throws IllegalArgumentException, IOException {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.tools.bayes.extractors.SenWordLister#addWords(java.lang.Object, java.util.Collection)
	 */
	@Override
	public void addWords(Object document, Collection<String> collection) {
		try {
			if (document == null) {
				return;
			}

			Token[] tokens = tagger.analyze(document.toString());
			for (Token token : tokens) {
				String pos = token.getPos(); 
				if (pos.startsWith("名詞-固有名詞")
						|| pos.startsWith("名詞-非自立")
						|| pos.startsWith("感動詞")
				) {
					collection.add(token.getSurface());
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e); 
		}
	}
}
