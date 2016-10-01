package config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import exceptions.MalformedXMLSourceException;
import exceptions.QueryExpressionException;
import exceptions.UnrecognizedQueryMethodException;

/**
 * @author CharlesXu
 */
public class States extends CompositeData implements Iterable<State>{
	
	private final String ITEMS = "States";
	private final String INIT_MODE = "InitStateMode";
	private State defaultState;
	private List<State> states;
	
	@Override
	public States load(XMLParser parser)
			throws QueryExpressionException, UnrecognizedQueryMethodException,
			MalformedXMLSourceException {
		super.load(parser);
		this.states = new ArrayList<State>();
		super.traverseChildren(ITEMS, false);
		if (parser.getItem(INIT_MODE).equals("enum")) {
			defaultState = getStateByName(parser.getItem("DefaultInitState"));
		}
		return this;
	}
	
	public State getDefaultState() {
		return defaultState;
	}

	public void setDefaultState(State defaultState) {
		this.defaultState = defaultState;
	}
	
	@Override
	public States save()
			throws QueryExpressionException, UnrecognizedQueryMethodException,
			MalformedXMLSourceException {
		Node parent = traverseChildren(ITEMS, true);
		for (State s : states) {
			Text a = parser.getDoc().createTextNode(s.getValue()); 
			Element p = parser.getDoc().createElement("state"); 
			for (String attr : s.getAttributes().keySet()) {
				p.setAttribute(attr, s.getAttributes().get(attr));
			}
			p.appendChild(a); 
			parent.insertBefore(p, null);
		}
		return this;
	}
	
	@Override
	public void populate(Node n) {
		assert states != null;
		State s = new State().setValue(n.getTextContent());
		states.add(s);
		for (int j = 0; j < n.getAttributes().getLength(); j++) {
			Node attr = n.getAttributes().item(j);
			s.getAttributes().put(attr.getNodeName(), attr.getTextContent());
		}
	}
	
	public State getStateByName(String name) {
		assert states != null;
		for (State s : this.states) {
			if (s.getValue().equals(name)) return s;
		}
		return null;
	}

	@Override
	public Iterator<State> iterator() {
		assert states != null;
		return states.iterator();
	}
}
