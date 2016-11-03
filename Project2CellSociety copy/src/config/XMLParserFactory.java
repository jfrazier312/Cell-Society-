package config;

import exceptions.UnrecognizedQueryMethodException;
import exceptions.XMLParserException;

/**
 * @author CharlesXu
 */
public class XMLParserFactory {
	
	public static XMLParser build(XMLQueryMethod method, String srcPath)
			throws XMLParserException, UnrecognizedQueryMethodException {
		switch (method) {
			case XPATH: 
				return new XpathXMLParser(srcPath);
			case XQUERY:
			case XSLT:
			case DOM:
			default:
				throw new UnrecognizedQueryMethodException();
		}
	}

}
