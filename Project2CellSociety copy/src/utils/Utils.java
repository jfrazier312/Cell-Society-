package utils;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author CharlesXu
 */
public class Utils {
	
	public static String getAttrFromFirstMatch(Document doc, String tag, String attr) {
		return getNode(doc, tag).getAttributes().getNamedItem(attr).getTextContent();
	}
	
	public static Node getNode(Document doc, String tag) {
		return doc.getElementsByTagName(tag).item(0);
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
