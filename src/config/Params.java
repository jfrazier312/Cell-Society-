package config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import exceptions.MalformedXMLSourceException;
import exceptions.QueryExpressionException;
import exceptions.UnrecognizedQueryMethodException;
import utils.Utils;

/**
 * @author CharlesXu
 */
public class Params extends CompositeData {
	
	private final String ITEMS = "Params",
						ATTR_NAME = "name",
						ATTR_VALUE = "value";
	private Map<String, String> customizedParams;
	
	@Override
	public Params load(XMLParser parser)
			throws QueryExpressionException, UnrecognizedQueryMethodException,
			MalformedXMLSourceException {
		super.load(parser);
		customizedParams = new HashMap<String, String>();
		super.traverseChildren(ITEMS, false);
		return this;
	}
	
	@Override
	public void populate(Node n) {
		String param = Utils.getAttrFromNode(n, ATTR_NAME);
		String value = Utils.getAttrFromNode(n, ATTR_VALUE);
		customizedParams.put(param, value);
	}
	
	@Override
	public CompositeData save()
			throws QueryExpressionException, UnrecognizedQueryMethodException,
			MalformedXMLSourceException {
		Node parent = traverseChildren(ITEMS, true);
		for (String param : customizedParams.keySet()) {
			Element p = parser.getDoc().createElement("param"); 
			p.setAttribute(ATTR_NAME, param);
			p.setAttribute(ATTR_VALUE, customizedParams.get(param));
			parent.insertBefore(p, null);
		}
		return null;
	}
	
	public Set<String> getAllParams() {
		assert customizedParams != null;
		return customizedParams.keySet();
	}
	
	public String getCustomParam(String paramName) {
		assert customizedParams != null;
		return customizedParams.get(paramName);
	}
	
	public Params setCustomParam(String paramName, String value) {
		assert customizedParams != null;
		customizedParams.put(paramName, value);
		return this;
	}
}
