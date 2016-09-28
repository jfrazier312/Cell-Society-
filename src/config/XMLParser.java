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
			throws XPathExpressionException {
		return XPATH_ENGINE
				.compile(queries.getString(itemName))
				.evaluate(this.doc, type);
	}
	
	public void updateDoc(String itemName, String value)
			throws UnrecognizedQueryMethodException {
		if (queryMethod.equals("Xpath")) {
			try {
				Node result = (Node) xpathEval(itemName, XPathConstants.NODE);
				result.setNodeValue(value);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		} else throw new UnrecognizedQueryMethodException();
	}
	
	public void updateDoc(String itemName, int value)
			throws UnrecognizedQueryMethodException {
		updateDoc(itemName, value + "");
	}
	
	public String getItem(String itemName)
			throws UnrecognizedQueryMethodException,
					XPathExpressionException,
					MalformedXMLSourceException {
		if (queryMethod.equals("Xpath")) {
			String result = (String) xpathEval(itemName, XPathConstants.STRING);
			if (result == null || result.isEmpty()) {
				throw new MalformedXMLSourceException();
			}
			return result;
		}
		throw new UnrecognizedQueryMethodException();
	}
	
	public int getItemAsInteger(String itemName)
			throws NumberFormatException, XPathExpressionException,
				   UnrecognizedQueryMethodException, MalformedXMLSourceException {
		return Integer.parseInt(getItem(itemName));
	}
	
	public NodeList getNodeList(String itemName)
			throws UnrecognizedQueryMethodException, XPathExpressionException,
				   MalformedXMLSourceException {
		if (queryMethod.equals("Xpath")) {
			NodeList result = (NodeList) xpathEval(itemName, XPathConstants.NODESET);
			if (result == null || result.getLength() == 0) {
				throw new MalformedXMLSourceException();
			}
			return result;
		}
		throw new UnrecognizedQueryMethodException();
	}
	
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
			throw new SourcePathFoundNoFileException();
		doc.getDocumentElement().normalize();
		return doc;
	}
	
	public Document getDoc() {
		return doc;
	}
}
