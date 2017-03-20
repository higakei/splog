package jp.co.hottolink.splogfilter.api.servlet;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import jp.co.hottolink.splogfilter.api.xml.parser.BlogFeedParser;

import org.xml.sax.InputSource;

public class TestServlet extends HttpServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * <p>
	 * XMLコーディング.
	 * </p>
	 */
	private static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * <p>
	 * XMLバージョン.
	 * </p>
	 */
	private static final String XML_VERSION = "1.0";

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// エンコーディングの設定
			String encoding = request.getCharacterEncoding();
			if (encoding == null) {
				request.setCharacterEncoding(DEFAULT_ENCODING);
			}

			// パラメータの取得
			String blogFeed = request.getParameter("blogFeed");
			if ((blogFeed == null) || blogFeed.isEmpty()) {
				outputErrorXML(response, DEFAULT_ENCODING, "no data");
				return;
			}

			// ブログフィードの取得
			StringReader reader = new StringReader(blogFeed);
			InputSource source = new InputSource(reader);
			BlogFeedParser parser = new BlogFeedParser();
			parser.parse(source);
			List<Map<String, String>> documents = parser.getDocuments();

			for (Map<String, String> document: documents) {
				System.out.println("documentId:" + document.get("documentId"));
				System.out.println("authorId:" + document.get("authorId"));
				System.out.println("url:" + document.get("url"));
				System.out.println("date:" + document.get("date"));
				System.out.println("title:" + document.get("title"));
				System.out.println("bodey:" + document.get("body"));
			}

			// 結果の出力
			outputXML(response, DEFAULT_ENCODING);

		} catch (Exception e) {
			e.printStackTrace();
			outputErrorXML(response, DEFAULT_ENCODING, "analyze failed");
		}
	}

	private void outputXML(HttpServletResponse response, String encoding) throws IOException {

		XMLStreamWriter writer = null;

		try {
			// レスポンスの設定
			response.setContentType("text/xml");
			response.setHeader("charset", encoding); 

			ServletOutputStream out = response.getOutputStream();
			XMLOutputFactory factory = XMLOutputFactory.newInstance();

			writer = factory.createXMLStreamWriter(out, encoding);
			write(writer, encoding);
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			try { writer.close(); } catch (XMLStreamException e) {}
		}
	}

	private void write(XMLStreamWriter writer, String encoding) throws XMLStreamException {

		writer.writeStartDocument(encoding, XML_VERSION);
		writer.writeStartElement("result");

		writer.writeStartElement("author");
		writer.writeAttribute("content", "49");
		writer.writeAttribute("id", "fc2.com:oolongtea2001");
		writer.writeAttribute("interval", "0");
		writer.writeAttribute("score", "-45000");
		writer.writeAttribute("title", "-45000");

		writer.writeStartElement("document");
		writer.writeAttribute("id", "fc2.com:oolongtea2001:ADA2F1F54252A96E060BB0ED2B079ECA8B1CC25E;20090101");
		writer.writeAttribute("score", "比嘉");
		writer.writeEndElement();

		writer.writeStartElement("document");
		writer.writeAttribute("id", "fc2.com:oolongtea2001:0E61FCEC712A9250100E7A1CDFC310B42C7D9393;20090101");
		writer.writeAttribute("score", "-42");
		writer.writeEndElement();

		writer.writeEndElement();
		writer.writeEndDocument();
	}

	private void outputErrorXML(HttpServletResponse response, String encoding, String message) throws IOException {

		XMLStreamWriter writer = null;

		try {
			// レスポンスの設定
			response.setContentType("text/xml");
			response.setHeader("charset", encoding); 

			ServletOutputStream out = response.getOutputStream();
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			writer = factory.createXMLStreamWriter(out, encoding);

			writeError(writer, encoding, message);
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			try { writer.close(); } catch (XMLStreamException e) {}
		}
	}

	private void writeError(XMLStreamWriter writer, String encoding, String message) throws XMLStreamException {
		writer.writeStartDocument(encoding, XML_VERSION);
		writer.writeStartElement("error");
		writer.writeStartElement("message");
		writer.writeCharacters(message);
		writer.writeEndElement();
		writer.writeEndElement();
		writer.writeEndDocument();
	}
}
