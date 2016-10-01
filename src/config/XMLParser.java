package config;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import exceptions.MalformedXMLSourceException;
import exceptions.QueryExpressionException;
import exceptions.XMLParserException;
import exceptions.UnrecognizedQueryMethodException;

public abstract class XMLParser {
	
	//TODO (cx15): JAVA DOC + generalize exceptions and not just xpath
	
	public static final String RESRC_PREFIX = "resources/";
	
	protected Document doc;
	
	public XMLParser(String scrPath)
			throws XMLParserException {
		parse(scrPath);
	}
	
	public abstract void updateDoc(String itemName, String value)
			throws UnrecognizedQueryMethodException, MalformedXMLSourceException,
			QueryExpressionException;
	
	public abstract void updateDoc(String itemName, int value)
			throws UnrecognizedQueryMethodException, MalformedXMLSourceException,
			QueryExpressionException;
	
	public abstract String getItem(String itemName)
			throws UnrecognizedQueryMethodException,
					QueryExpressionException, MalformedXMLSourceException;
	
	public abstract int getItemAsInteger(String itemName)
			throws NumberFormatException, QueryExpressionException,
				   UnrecognizedQueryMethodException, MalformedXMLSourceException;
	
	public abstract NodeList getNodeList(String itemName)
			throws UnrecognizedQueryMethodException, QueryExpressionException,
				   MalformedXMLSourceException;
	
	private void parse(String sourcePath)
				throws XMLParserException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse(new File(sourcePath));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new XMLParserException();
		}
		this.doc.getDocumentElement().normalize();
	}
	
	protected Document getDoc() {
		return doc;
	}
	
	/**
	 * The if tree is necessary since String and NodeList has different interface
	 * to check if its content is empty.
	 * @param o
	 * @return
	 */
	protected boolean isEmpty(Object o) {
		if (o instanceof String) 
			return ((String) o).isEmpty();
		else if (o instanceof NodeList)
			return ((NodeList) o).getLength() == 0;
		else return false;
	}
}
