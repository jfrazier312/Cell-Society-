package config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import exceptions.UnrecognizedQueryMethodException;
import model.XMLParser;
import utils.Utils;

public class Params {
	
	private Map<String, String> customizedParams;
	
	public Params(XMLParser parser)
			throws XPathExpressionException, UnrecognizedQueryMethodException {
		customizedParams = new HashMap<String, String>();
		NodeList nl = parser.getNodeList("Params");
		if (nl == null) return;
		for (int i = 0; i < nl.getLength(); i++) {
			String param = Utils.getAttrFromNode(nl.item(i), "name");
			String value = Utils.getAttrFromNode(nl.item(i), "value");
			customizedParams.put(param, value);
	    }
	}
	
	public Set<String> getAllParams() {
		return customizedParams.keySet();
	}
	
	public String getCustomParam(String paramName) {
		if (customizedParams == null)
			return null;
		return customizedParams.get(paramName);
	}
	
	public String setCustomParam(String paramName, String value) {
		if (customizedParams == null)
			return null;
		return customizedParams.get(paramName);
	}
}
