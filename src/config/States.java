package config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exceptions.MalformedXMLSourceException;
import exceptions.UnrecognizedQueryMethodException;

public class States implements Iterable<State>{
	
	private List<State> states;
	
	public States init(XMLParser parser)
			throws XPathExpressionException, UnrecognizedQueryMethodException,
			MalformedXMLSourceException {
		this.states = new ArrayList<State>();
		NodeList nl = parser.getNodeList("States");
		for (int i = 0; i < nl.getLength(); i++) {
//			Node n = parser.getDoc().createElement(tagName)
			State s = new State().setValue(nl.item(i).getTextContent());
			states.add(s);
			for (int j = 0; j < nl.item(i).getAttributes().getLength(); j++) {
				Node attr = nl.item(i).getAttributes().item(j);
				s.getAttributes().put(attr.getNodeName(), attr.getTextContent());
			}
	    }
		return this;
	}
	
	public State getStateByName(String name) {
		for (State s : this.states) {
			if (s.getValue().equals(name)) return s;
		}
		return null;
	}

	@Override
	public Iterator<State> iterator() {
		return states.iterator();
	}
}
