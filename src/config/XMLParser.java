package config;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
		this. queryMethod = queryMethod;
		queries = ResourceBundle.getBundle(RESRC_PREFIX + queryMethod);
		this.doc = doc;
	}
	
	public String getItem(String itemName)
			throws UnrecognizedQueryMethodException, XPathExpressionException {
		if (queryMethod.equals("Xpath")) {
			return XPATH_ENGINE.compile(queries.getString(itemName)).evaluate(doc);
		}
		throw new UnrecognizedQueryMethodException();
	}
	
	public int getItemAsInteger(String itemName)
			throws NumberFormatException, XPathExpressionException,
				   UnrecognizedQueryMethodException {
		return Integer.parseInt(getItem(itemName));
	}
	
	public NodeList getNodeList(String itemName)
			throws UnrecognizedQueryMethodException, XPathExpressionException {
		if (queryMethod.equals("Xpath")) {
			return (NodeList) XPATH_ENGINE
					.compile(queries.getString(itemName))
					.evaluate(doc, XPathConstants.NODESET);
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

	// TODO (cx15) validate an XML, exception to front end if xml invalid
	public static boolean validate(Document doc) throws Exception {
		return true;
	}
}
