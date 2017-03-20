package jp.co.hottolink.splogfilter.extension.fusion.core.util.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlResponse {
	
    private final Document doc;
    private final XPath xpath;
    

    public XmlResponse(String xmlString) throws SAXException, IOException, ParserConfigurationException{	
		InputSource is = new InputSource(new StringReader(xmlString));
		this.doc  = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        this.xpath = XPathFactory.newInstance().newXPath();
    }
    
    public XmlResponse(InputStream in) throws SAXException, IOException, ParserConfigurationException{
    	InputStreamReader ir = new InputStreamReader(in);
		InputSource is = new InputSource(ir);
		this.doc  = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        this.xpath = XPathFactory.newInstance().newXPath();
    }
 
    public XmlResponse(InputStreamReader ir) throws SAXException, IOException, ParserConfigurationException{
		InputSource is = new InputSource(ir);
		this.doc  = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        this.xpath = XPathFactory.newInstance().newXPath();
    }
    public XmlResponse(Document doc){
      this.doc = doc;
      this.xpath = XPathFactory.newInstance().newXPath();
    }

    public int getLength(String expression) throws XPathExpressionException{
        NodeList nodelist = (NodeList)xpath.evaluate(expression, doc, XPathConstants.NODESET);
        if(nodelist != null){
          return nodelist.getLength();
        }else{
          return 0;
        }
      }
   
    /**
	 * @return doc
	 */
	public Document getDoc() {
		return doc;
	}

	/**
	 * @return xpath
	 */
	public XPath getXpath() {
		return xpath;
	}

	public String getString(String expression) throws XPathExpressionException{
        return xpath.evaluate(expression, doc);
    }

	public NodeList getNodeList(String expression) throws XPathExpressionException {
		return (NodeList)xpath.evaluate(expression, doc, XPathConstants.NODESET);
	}
	
	public String toString(){
        StringWriter sw;
		try {
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			transformer.transform(source, result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        return sw.toString();
	}
}