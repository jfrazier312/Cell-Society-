package utils;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


public class Utils {
	
	public static String getAttrFromFirstMatch(Document doc, String tag, String attr) {
		return doc.getElementsByTagName(tag).item(0).getAttributes().getNamedItem(attr).getTextContent();
	}
	
	public static String getAttrFromNode(Node n, String attr) {
		return n.getAttributes().getNamedItem(attr).getTextContent();
	}
	
	public static List<String> getAttrFromAllMatches(Document doc, String tag, String attr) {
		NodeList nl = doc.getElementsByTagName(tag);
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < nl.getLength(); i++) {
	        ret.add(nl.item(i).getAttributes().getNamedItem(attr).getTextContent());
	    }
		return ret;
	}
}
