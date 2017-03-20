package jp.co.hottolink.splogfilter.tools.bayes.extractors;

import java.io.IOException;
import java.util.Collection;

import net.java.sen.Token;

/**
 * <p>
 * タイトルのSenWordListerクラス.
 * </p>
 * @author higa
 */
public class TitleSenWordLister extends SenWordLister {

	/**
	 * <p>
	 * コンストラクター.
	 * </p>
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public TitleSenWordLister() throws IllegalArgumentException, IOException {
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
				if (pos.startsWith("名詞")
						|| pos.startsWith("記号")
						|| pos.startsWith("助動詞")
						|| pos.startsWith("動詞")
				) {
					collection.add(token.getSurface());
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e); 
		}
	}
}
