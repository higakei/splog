package jp.co.hottolink.splogfilter.learning.web.servlet.fileupload;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * <p>
 * multipart/form-dataのリクエストを解析するクラス.
 * </p>
 * @author higa
 */
public class MultipartRequestParser {

	/**
	 * <p>
	 * パラメータ.
	 * </p>
	 */
	private Map<String, List<FileItem>> parameterMap = null;

	/**
	 * <p>
	 * エンコーディング.
	 * </p>
	 */
	private String encoding = null;

	/**
	 * <p>
	 * multipart/form-dataのリクエストを解析する.
	 * </p>
	 * @param request リクエスト
	 * @throws FileUploadException
	 */
	@SuppressWarnings("unchecked")
	public void parse(HttpServletRequest request) throws FileUploadException {

		parameterMap = new HashMap<String, List<FileItem>>();
		if (request == null) {
			return;
		}

		// エンコーディングの設定
		encoding = request.getCharacterEncoding();

		// ServletFileUploadオブジェクトを生成
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

		// アップロードする際の基準値を設定
		factory.setSizeThreshold(1024);
		upload.setSizeMax(-1);

		// フォームデータ(FileItemオブジェクト)を取得し、
		// Listオブジェクトとして返す
		List formData = upload.parseRequest(request);

	    // フォームデータ(FileItemオブジェクト)を順に処理
	    Iterator iterator = formData.iterator();
	    while (iterator.hasNext()) {
	        FileItem item = (FileItem)iterator.next();
	        String fieldName = item.getFieldName();

	        List<FileItem> list = parameterMap.get(fieldName);
	        if (list == null) list = new ArrayList<FileItem>();

	        list.add(item);
	        parameterMap.put(fieldName, list);
	    }
	}

	/**
	 * <p>
	 * 指定したパラメータのフォームデータリストを取得する.
	 * </p>
	 * @param name パラメータ名
	 * @return フォームデータリスト
	 */
	public List<FileItem> getFileItemList(String name) {
		if (parameterMap == null) {
			return null;
		} else {
			return parameterMap.get(name);
		}
	}

	/**
	 * <p>
	 * 指定したパラメータのフォームデータを取得する.
	 * </p><pre>
	 * 指定したパラメータが複数ある場合、フォームデータリストの先頭を返す。
	 * </pre>
	 * @param name パラメータ名
	 * @return フォームデータ
	 */
	public FileItem getFileItem(String name) {
		List<FileItem> list = getFileItemList(name);
		if (list == null) {
			return null;
		} else {
			return list.get(0);
		}
	}

	/**
	 * <p>
	 * 指定したパラメータの値を取得する.
	 * </p><pre>
	 * 指定したパラメータがファイルの場合、nullを返す。
	 * 指定したパラメータが複数ある場合、フォームデータリストの先頭を返す。
	 * </pre>
	 * @param name パラメータ名
	 * @return パラメータの値
	 * @throws UnsupportedEncodingException
	 * @see javax.servlet.ServletRequest#getParameter(String)
	 */
	public String getParameter(String name) throws UnsupportedEncodingException {
		FileItem item = getFileItem(name);
		if ((item == null) || !item.isFormField()) {
			return null;
		} else {
			return getFormFieldValue(item);
		}
	}

	/**
	 * <p>
	 * 指定したパラメータの全ての値を取得する.
	 * </p><pre>
	 * 指定したパラメータがファイルの場合、nullを返す。
	 * </pre>
	 * @param name パラメータ名
	 * @return パラメータの全ての値
	 * @throws UnsupportedEncodingException
	 * @see javax.servlet.ServletRequest#getParameterValues(String)
	 */
	public String[] getParameterValues(String name) throws UnsupportedEncodingException {

		List<FileItem> list = getFileItemList(name);
		if (list == null) {
			return null;
		}

		List<String> values = new ArrayList<String>();
		for (FileItem item: list) {
			if (item.isFormField()) {
				values.add(getFormFieldValue(item));
			}
		}

		if (values.isEmpty()) {
			return null;
		}

		return values.toArray(new String[0]);
	}

	/**
	 * <p>
	 * フォームデータの値を取得する.
	 * </p>
	 * @param item フォームデータ
	 * @return フォームデータの値
	 * @throws UnsupportedEncodingException
	 */
	private String getFormFieldValue(FileItem item) throws UnsupportedEncodingException {

		if (item == null) {
			return null;
		}

		if (encoding == null) {
			return item.getString();
		}

		return item.getString(encoding);
	}
}
