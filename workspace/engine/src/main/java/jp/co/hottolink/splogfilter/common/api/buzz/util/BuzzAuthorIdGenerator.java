package jp.co.hottolink.splogfilter.common.api.buzz.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * buzzの著者IDを生成するクラス.
 * </p>
 * @author higa
 */
public class BuzzAuthorIdGenerator {

	/**
	 * <p>
	 * URLから著者IDを生成する.
	 * </p>
	 * @param url URL
	 * @return 著者ID
	 * @throws URISyntaxException 
	 */
	public static String generate(String url) throws URISyntaxException {

		if (url == null) {
			return null;
		}

		URI uri = new URI(url);
		String host = uri.getHost();
		String path = uri.getPath();

		String string = null;
		if (host == null) {
			string = path;
		} else {
			string = host + path;
		}

		// ameblo.jp
		Pattern pattern = Pattern.compile("^(ameblo.jp)/([^/]+?)(?:/.*)?");
		Matcher matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}

		// livedoor.jp
		pattern = Pattern.compile("^[^/.]+.(livedoor.jp)/([^/]+?)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}

		// livedoor.biz
		pattern = Pattern.compile("^([^/.]+).livedoor.biz(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return "livedoor.jp:" + matcher.group(1);
		}

		// yahoo.co.jp
		pattern = Pattern.compile("^[^/.]+.(yahoo.co.jp)/([^/]+?)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}

		// seesaa.net
		pattern = Pattern.compile("^([^/.]+).(seesaa.net)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(2) + ":" + matcher.group(1);
		}

		// rakuten.co.jp
		pattern = Pattern.compile("^[^/.]+(?:.[^/.]+)?.(rakuten.co.jp)/([^/]+?)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}

		// hatena.ne.jp
		pattern = Pattern.compile("^[^/.]+.(hatena.ne.jp)/([^/]+?)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}

		// goo.ne.jp
		pattern = Pattern.compile("^blog.(goo.ne.jp)/([^/]+?)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}

		// exblog.jp
		pattern = Pattern.compile("^([^/.]+).(exblog.jp)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(2) + ":" + matcher.group(1);
		}

		// cocolog-nifty.com
		pattern = Pattern.compile("^([^/.]+).[a-z]+-nifty.com(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return "cocolog-nifty.com:" + matcher.group(1);
		}

		// yaplog.jp
		pattern = Pattern.compile("^(yaplog.jp)/([^/]+?)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}

		// fc2.com1
		pattern = Pattern.compile("^([^/.]+).blog[0-9]+.(fc2.com)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(2) + ":" + matcher.group(1);
		}

		// fc2.com2
		pattern = Pattern.compile("^blog[0-9]+.(fc2.com)/([^/.]+)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}

		// webry.info
		pattern = Pattern.compile("^([^/.]+).[^/.]+.(webry.info)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(2) + ":" + matcher.group(1);
		}

		// dtiblog.com
		pattern = Pattern.compile("^([^/.]+)(?:.[^/.]+)?.(dtiblog.com)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(2) + ":" + matcher.group(1);
		}

		// dion.ne.jp
		pattern = Pattern.compile("^blogs.(dion.ne.jp)/([^/]+?)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}

		// drecom.jp
		pattern = Pattern.compile("^([^/.]+).blog.(drecom.jp)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(2) + ":" + matcher.group(1);
		}

		// doblog.com
		pattern = Pattern.compile("^www.(doblog.com)/weblog/myblog/([0-9]+)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}

		// jugem.jp
		pattern = Pattern.compile("^([^/.]+).(jugem.jp)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(2) + ":" + matcher.group(1);
		}

		// blog.auone.jp
		pattern = Pattern.compile("^(blog.auone.jp)/([^/]+?)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}

		// so-net.ne.jp
		pattern = Pattern.compile("^([^/.]+).blog.(so-net.ne.jp)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(2) + ":" + matcher.group(1);
		}

		// cururu.jp
		pattern = Pattern.compile("^[^/.]+.(cururu.jp)/([^/]+?)(?:/.*)?");
		matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}

		// blog.excite.co.jp
		//pattern = Pattern.compile("^blog.excite.co.jp/([^/]+?)(?:/.*)?");
		//matcher = pattern.matcher(string);
		//if (matcher.matches()) {
			//	return "exblog.jp:" + matcher.group(1);
		//}

		return null;
	}
}
