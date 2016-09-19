package config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class States {
	
	private List<String> values;
	private Map<String, String> colors;
	
	public List<String> getValues() {
		return values;
	}
	
	public void setValues(List<String> values) {
		this.values = values;
	}
	
	public Map<String, String> getColors() {
		return colors;
	}
	
	public void setColors(Map<String, String> colors) {
		this.colors = colors;
	}
	
	public States init(Document doc) {
		NodeList nl = doc.getElementsByTagName("state");
		values = new ArrayList<String>();
		colors = new HashMap<String, String>();
		for (int i = 0; i < nl.getLength(); i++) {
			String val = nl.item(i).getAttributes().getNamedItem("value").getTextContent();
			values.add(val);
			colors.put(val, nl.item(i).getAttributes().getNamedItem("color").getTextContent());
	    }
		return this;
	}
}
