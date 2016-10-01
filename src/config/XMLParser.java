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
	
	public static final String RESRC_PREFIX = "resources/";
	
	protected Document doc;
	
	public XMLParser(String scrPath)
			throws XMLParserException {
		parse(scrPath);
	}
	
	/**
	 * Update the value of the element identified by itemName with String
	 * value passed in
	 * @param itemName
	 * @param value
	 * @throws UnrecognizedQueryMethodException
	 * @throws MalformedXMLSourceException
	 * @throws QueryExpressionException
	 */
	public abstract void updateDoc(String itemName, String value)
			throws UnrecognizedQueryMethodException, MalformedXMLSourceException,
			QueryExpressionException;
	
	/**
	 * Update the value of the element identified by itemName with int
	 * value passed in
	 * @param itemName
	 * @param value
	 * @throws UnrecognizedQueryMethodException
	 * @throws MalformedXMLSourceException
	 * @throws QueryExpressionException
	 */
	public abstract void updateDoc(String itemName, int value)
			throws UnrecognizedQueryMethodException, MalformedXMLSourceException,
			QueryExpressionException;
	
	/**
	 * Return the String value of element identified by itemName in doc
	 * @param itemName
	 * @return
	 * @throws UnrecognizedQueryMethodException
	 * @throws QueryExpressionException
	 * @throws MalformedXMLSourceException
	 */
	public abstract String getItem(String itemName)
			throws UnrecognizedQueryMethodException,
					QueryExpressionException, MalformedXMLSourceException;
	
	/**
	 * Return the int value of element identified by itemName in doc
	 * @param itemName
	 * @return
	 * @throws NumberFormatException
	 * @throws QueryExpressionException
	 * @throws UnrecognizedQueryMethodException
	 * @throws MalformedXMLSourceException
	 */
	public abstract int getItemAsInteger(String itemName)
			throws NumberFormatException, QueryExpressionException,
				   UnrecognizedQueryMethodException, MalformedXMLSourceException;
	
	/**
	 * 
	 * @param itemName
	 * @return
	 * @throws UnrecognizedQueryMethodException
	 * @throws QueryExpressionException
	 * @throws MalformedXMLSourceException
	 */
	public abstract NodeList getNodeList(String itemName)
			throws UnrecognizedQueryMethodException, QueryExpressionException,
				   MalformedXMLSourceException;
	
	/**
	 * Parse the XML file specified at sourcePath to create a Document
	 * used to initialize this.doc
	 * @param sourcePath
	 * @throws XMLParserException
	 */
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
