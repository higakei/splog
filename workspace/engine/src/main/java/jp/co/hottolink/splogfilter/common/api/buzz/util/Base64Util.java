package jp.co.hottolink.splogfilter.common.api.buzz.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

public class Base64Util {

	/**
	 * <p>
	 * デフォルトエンコーディング.
	 * </p>
	 */
	private static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * <p>
	 * Base64のエンコーディング名.
	 * </p>
	 */
	private static final String BASE64 = "base64";

	/**
	 * <p>
	 * Base64エンコードを行う.
	 * </p>
	 * @param string エンコードする文字列
	 * @return エンコードした文字列
	 * @throws MessagingException 
	 * @throws IOException 
	 */
	public static String encode(String string) throws MessagingException, IOException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		OutputStream os = MimeUtility.encode(bao, BASE64);
		os.write(string.getBytes(DEFAULT_ENCODING));
		os.flush();
		os.close();
		return bao.toString();
		//return bao.toString("iso-8859-1");
	}

	/**
	 * <p>
	 * Base64デコードを行う.
	 * </p>
	 * @param string デコードする文字列
	 * @return デコードした文字列
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static String decode(String string) throws MessagingException, IOException {

		InputStream bi = new ByteArrayInputStream(string.getBytes());
		InputStream is = MimeUtility.decode(bi, BASE64);
		Reader reader = new BufferedReader(new InputStreamReader(is, DEFAULT_ENCODING));

		CharArrayWriter builder = new CharArrayWriter();
		int res = 0;
		while( (res = reader.read()) != -1 ) {
			builder.write(res);
			builder.flush();
		}

		return builder.toString();
	}
}
