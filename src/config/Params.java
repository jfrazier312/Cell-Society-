package config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import utils.Utils;

public class Params {
	
	private Map<String, String> customizedParams;
	
	public Set<String> getAllParams() {
		return customizedParams.keySet();
	}

	public Params(Document doc) {
		customizedParams = new HashMap<String, String>();
		NodeList nl = doc.getElementsByTagName("param");
		if (nl == null) return;
		for (int i = 0; i < nl.getLength(); i++) {
			String param = Utils.getAttrFromNode(nl.item(i), "name");
			String value = Utils.getAttrFromNode(nl.item(i), "value");
			customizedParams.put(param, value);
	    }
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
