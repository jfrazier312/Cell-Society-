package config;

import java.util.ResourceBundle;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exceptions.MalformedXMLSourceException;
import exceptions.QueryExpressionException;
import exceptions.XMLParserException;
import exceptions.UnrecognizedQueryMethodException;

public class XpathXMLParser extends XMLParser {
	
	private static XPath XPATH_ENGINE = XPathFactory.newInstance().newXPath();
	private final String QUERY_IMPL = "Xpath";
	
	private ResourceBundle queries;
	
	public XpathXMLParser(String srcPath) throws XMLParserException {
		super(srcPath);
		queries = ResourceBundle.getBundle(RESRC_PREFIX + QUERY_IMPL);
	}
	
	private Object xpathEval(String itemName, QName type)
			throws MalformedXMLSourceException, QueryExpressionException {
		Object ret = null;
		try {
			ret = XPATH_ENGINE
					.compile(queries.getString(itemName))
					.evaluate(this.doc, type);
			if (ret == null || isEmpty(ret))
				throw new MalformedXMLSourceException();
		} catch (XPathExpressionException e) {
			throw new QueryExpressionException();
		}
		return ret;
	}
	
	@Override
	public void updateDoc(String itemName, String value)
			throws UnrecognizedQueryMethodException, MalformedXMLSourceException,
			QueryExpressionException {
		try {
			Node result = (Node) xpathEval(itemName, XPathConstants.NODE);
			result.setNodeValue(value);
		} catch (QueryExpressionException e) {
			throw new QueryExpressionException();
		}
	}
	
	@Override
	public void updateDoc(String itemName, int value)
			throws UnrecognizedQueryMethodException, MalformedXMLSourceException,
			QueryExpressionException {
		updateDoc(itemName, value + "");
	}
	
	private Object getItem(String itemName, QName type)
			throws QueryExpressionException, MalformedXMLSourceException,
			UnrecognizedQueryMethodException {
		Object result = xpathEval(itemName, type);
		return result;
	}
	
	@Override
	public String getItem(String itemName)
			throws UnrecognizedQueryMethodException,
					QueryExpressionException, MalformedXMLSourceException {
		return (String) getItem(itemName, XPathConstants.STRING);
	}
	
	@Override
	public int getItemAsInteger(String itemName)
			throws NumberFormatException, QueryExpressionException,
				   UnrecognizedQueryMethodException, MalformedXMLSourceException {
		return Integer.parseInt(getItem(itemName));
	}
	
	@Override
	public NodeList getNodeList(String itemName)
			throws UnrecognizedQueryMethodException, QueryExpressionException,
				   MalformedXMLSourceException {
		return (NodeList) getItem(itemName, XPathConstants.NODESET);
	}
}
