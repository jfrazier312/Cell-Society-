package config;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exceptions.MalformedXMLSourceException;
import exceptions.QueryExpressionException;
import exceptions.UnrecognizedQueryMethodException;

public abstract class CompositeData {
	
	protected XMLParser parser;
	
	/**
	 * Load from document to populate all instance variables
	 * of subclasses. 
	 * @param parser
	 * @return
	 * @throws XPathExpressionException
	 * @throws UnrecognizedQueryMethodException
	 * @throws MalformedXMLSourceException
	 */
	public CompositeData load(XMLParser parser)
			throws QueryExpressionException, UnrecognizedQueryMethodException,
			MalformedXMLSourceException {
		this.parser = parser;
		return this;
	}

	/**
	 * Removes all state children from old DOM tree and write
	 * the updated instance variables to replace the old elements.
	 * @throws XPathExpressionException
	 * @throws UnrecognizedQueryMethodException
	 * @throws MalformedXMLSourceException
	 * @throws QueryExpressionException 
	 */
	public abstract CompositeData save()
			throws UnrecognizedQueryMethodException, MalformedXMLSourceException,
			QueryExpressionException;
	
	/**
	 * Use the input Node to get necessary information to populate the instance
	 * variables of subclasses. This method is called for each Node as children
	 * @param n
	 */
	public abstract void populate(Node n);
	

	/**
	 * Return the parent Node in DOM tree. Final, do not override
	 * @param item
	 * @param removeAll
	 * @return
	 * @throws XPathExpressionException
	 * @throws UnrecognizedQueryMethodException
	 * @throws MalformedXMLSourceException
	 */
	public final Node traverseChildren(String item, boolean removeAll)
			throws QueryExpressionException, UnrecognizedQueryMethodException,
			MalformedXMLSourceException {
		NodeList nl = parser.getNodeList(item);
		Node parent = nl.item(0).getParentNode();
		for (int i = 0; i < nl.getLength(); i++) {
			if (removeAll) {
				parent.removeChild(nl.item(i));
			} else {
				populate(nl.item(i));
			}
	    }
		return parent;
	}
}
