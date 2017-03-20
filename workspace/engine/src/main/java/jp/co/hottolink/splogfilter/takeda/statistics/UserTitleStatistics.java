package jp.co.hottolink.splogfilter.takeda.statistics;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 投稿者のタイトルの統計データクラス
 * </p>
 * @author higa
 */
public class UserTitleStatistics implements Serializable {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * タイトルの統計データ.
	 * </p>
	 */
	private List<TitleStatistics> titles = null;

	/**
	 * <p>
	 * 単語の出現数の統計データ.
	 * </p>
	 */
	private List<WordCountStatistics> words = null;

	
	/**
	 * <p>
	 * タイトルの統計データを取得する.
	 * </p>
	 * @return 統計データ
	 */
	public List<TitleStatistics> getTitles() {
		return titles;
	}

	/**
	 * <p>
	 * タイトルの統計データを設定する.
	 * </p>
	 * @param titles 統計データ
	 */
	public void setTitles(List<TitleStatistics> titles) {
		this.titles = titles;
	}

	/**
	 * <p>
	 * 単語の出現数の統計データを取得する.
	 * </p>
	 * @return 統計データ
	 */
	public List<WordCountStatistics> getWords() {
		return words;
	}

	/**
	 * <p>
	 * 単語の出現数の統計データを設定する.
	 * </p>
	 * @param words 統計データ
	 */
	public void setWords(List<WordCountStatistics> words) {
		this.words = words;
	}

	/**
	 * <p>
	 * スパムワードがタイトルに含まれるブログ数を取得する.
	 * </p>
	 * @return ブログ数
	 */
	public int getSpamWordBlogCount() {
		int spamWordBlogCount = 0;
		if (titles != null) {
			for (TitleStatistics title: titles) {
				int spamWordCount = title.getSpamWordCount();
				if (spamWordCount > 0) ++spamWordBlogCount;
			}
		}
		return spamWordBlogCount;
	}

	/**
	 * <p>
	 * タイトルに含まれるスパムワード数を取得する.
	 * </p>
	 * @return ブログ数
	 */
	public int getSpamWordCount() {
		int spamWordCount = 0;
		if (titles != null) {
			for (TitleStatistics title: titles) {
				spamWordCount += title.getSpamWordCount();
			}
		}
		return spamWordCount;
	}

	/**
	 * <p>
	 * ブログ数を取得する.
	 * </p>
	 * @return ブログ数
	 */
	public int getBlogCount() {
		if (titles == null) {
			return 0;
		} else {
			return titles.size();
		}
	}

	/**
	 * <p>
	 * 指定した文字数以上のスパムワードがタイトルに含まれるブログ数を取得する.
	 * </p>
	 * @param length 文字数
	 * @param isSpace true:スペースで区切る, false:スペースで区切らない
	 * @return ブログ数
	 */
	public int getMoreSpamWordBlogCount(int length, boolean isSpace) {
		int moreSpamWordBlogCount = 0;
		if (titles != null) {
			for (TitleStatistics title: titles) {
				int spamWordsLength = title.getSpamWordsLength(isSpace);
				if (spamWordsLength > length) ++moreSpamWordBlogCount;
			}
		}
		return moreSpamWordBlogCount;
	}

	/**
	 * <p>
	 * タイトルに含まれるスパムワードの総文字数を取得する.
	 * </p>
	 * @param isSpace true:スペースで区切る, false:スペースで区切らない
	 * @return 総文字数
	 */
	public int getSpamWordsLength(boolean isSpace) {
		int spamWordsLength = 0;
		if (titles != null) {
			for (TitleStatistics title: titles) {
				spamWordsLength += title.getSpamWordsLength(isSpace);
			}
		}
		return spamWordsLength;
	}

	/**
	 * <p>
	 * 単語数を取得する.
	 * </p>
	 * @return 単語数
	 */
	public int getWordsSize() {
		if (words == null) {
			return 0;
		} else {
			return words.size();
		}
	}

	/**
	 * <p>
	 * 最も多く出現した単語の統計データを取得する.
	 * </p>
	 * @return 統計データ
	 */
	public WordCountStatistics getMostFrequenceWord() {
		int wordsSize = getWordsSize();
		if (wordsSize > 0) {
			return words.get(0);
		} else {
			return null;
		}
	}

	/**
	 * <p>
	 * タイトル中に最も多く出現した単語の出現したブログ数を取得する.
	 * </p>
	 * @return ブログ数
	 */
	public int getMostFrequenceWordBlogCount() {
		WordCountStatistics word = getMostFrequenceWord();
		if (word == null) {
			return 0;
		} else {
			return word.getBlogCount();
		}
	}

	/**
	 * <p>
	 * タイトル中に最も多く出現した単語の文字数.
	 * </p>
	 * @return ブログ数
	 */
	public int getMostFrequenceWordLength() {
		WordCountStatistics word = getMostFrequenceWord();
		if (word == null) {
			return 0;
		} else {
			return word.getWordLength();
		}
	}

	/**
	 * <p>
	 * タイトル中に出現した単語の（出現回数×文字数）の総数を取得する.
	 * </p>
	 * @return （出現回数×文字数）
	 */
	public int getBlogCountTimesWordLength() {
		int score = 0;
		if (words != null) {
			for (WordCountStatistics word: words) {
				int blogCount = word.getBlogCount();
				int wordLength = word.getWordLength();
				score += blogCount*wordLength;
			}
		}
		return score;
	}

	/**
	 * <p>
	 * タイトルの総文字数を取得する.
	 * </p>
	 * @return 総文字数
	 */
	public int getTitlesLength() {
		int length = 0;
		if (titles != null) {
			for (TitleStatistics title: titles) {
				length += title.getTitleLength();
			}
		}
		return length;
	}

	/**
	 * <p>
	 * タイトルに含まれるスパム記号の出現数を取得する.
	 * </p>
	 * @return 出現数
	 */
	public int getSpamSignsCount() {
		int count = 0;
		if (titles != null) {
			for (TitleStatistics title: titles) {
				count += title.getSpamSignsCount();
			}
		}
		return count;
	}

	/**
	 * <p>
	 * タイトルに含まれるスペースの出現数を取得する.
	 * </p>
	 * @return 出現数
	 */
	public int getSpaceCount() {
		int count = 0;
		if (titles != null) {
			for (TitleStatistics title: titles) {
				count += title.getSpaceCount();
			}
		}
		return count;
	}

	/**
	 * <p>
	 * スパム記号の総文字数を取得する.
	 * </p>
	 * @return 総文字数
	 */
	public int getSpamSignsLength() {
		int length = 0;
		if (titles != null) {
			for (TitleStatistics title: titles) {
				length += title.getSpamSignsLength();
			}
		}
		return length;
	}
}
