package config;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import exceptions.MalformedXMLSourceException;
import exceptions.SourcePathFoundNoFileException;
import exceptions.SourcePathNotInitialized;
import exceptions.UnrecognizedQueryMethodException;

public class XMLParser {
	
	//TODO (cx15): Abstract XMLParser and subclass XpathXMLParser
	
	public static final String RESRC_PREFIX = "recourses/";
	private static XPath XPATH_ENGINE = XPathFactory.newInstance().newXPath();
	
	private String queryMethod;
	private ResourceBundle queries;
	private Document doc;
	
	
	public XMLParser(String queryMethod, Document doc) {
		this.queryMethod = queryMethod;
		queries = ResourceBundle.getBundle(RESRC_PREFIX + queryMethod);
		this.doc = doc;
	}
	
	private Object xpathEval(String itemName, QName type)
			throws XPathExpressionException, MalformedXMLSourceException {
		Object ret =  XPATH_ENGINE
				.compile(queries.getString(itemName))
				.evaluate(this.doc, type);
		if (ret == null || isEmpty(ret))
			throw new MalformedXMLSourceException();
		return ret;
	}
	
	public void updateDoc(String itemName, String value)
			throws UnrecognizedQueryMethodException, MalformedXMLSourceException,
			XPathExpressionException {
		if (queryMethod.equals("Xpath")) {
			Node result = (Node) xpathEval(itemName, XPathConstants.NODE);
			result.setNodeValue(value);
			
		} else throw new UnrecognizedQueryMethodException();
	}
	
	public void updateDoc(String itemName, int value)
			throws UnrecognizedQueryMethodException, MalformedXMLSourceException,
			XPathExpressionException {
		updateDoc(itemName, value + "");
	}
	
	private boolean isEmpty(Object o) {
		if (o instanceof String) {
			return ((String) o).isEmpty();
		} else if (o instanceof NodeList){
			return ((NodeList) o).getLength() == 0;
		} else return false;
	}
	
	private Object getItem(String itemName, QName type)
			throws XPathExpressionException, MalformedXMLSourceException,
			UnrecognizedQueryMethodException {
		if (queryMethod.equals("Xpath")) {
			Object result = xpathEval(itemName, type);
			return result;
		}
		throw new UnrecognizedQueryMethodException();
	}
	
	public String getItem(String itemName)
			throws UnrecognizedQueryMethodException,
					XPathExpressionException,
					MalformedXMLSourceException {
		return (String) getItem(itemName, XPathConstants.STRING);
	}
	
	public int getItemAsInteger(String itemName)
			throws NumberFormatException, XPathExpressionException,
				   UnrecognizedQueryMethodException, MalformedXMLSourceException {
		return Integer.parseInt(getItem(itemName));
	}
	
	public NodeList getNodeList(String itemName)
			throws UnrecognizedQueryMethodException, XPathExpressionException,
				   MalformedXMLSourceException {
		return (NodeList) getItem(itemName, XPathConstants.NODESET);
	}
	
	// TODO make this non static and call once and parse
	public static Document parse(String sourcePath)
				throws SourcePathFoundNoFileException, SourcePathNotInitialized {
		if (sourcePath == null)
			throw new SourcePathNotInitialized();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(sourcePath));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		if (doc == null)
			// TODO throw this exception in catch
			throw new SourcePathFoundNoFileException();
		doc.getDocumentElement().normalize();
		return doc;
	}
	
	public Document getDoc() {
		return doc;
	}
}
