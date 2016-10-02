package config;

import java.util.HashSet;
import java.util.Iterator;
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
public class Cells extends CompositeData implements Iterable<State>{
	
	private final String ITEMS = "Cells";
	private Set<State> cellsOnGrid;
	
	@Override
	public Cells load(XMLParser parser)
			throws QueryExpressionException, UnrecognizedQueryMethodException,
			MalformedXMLSourceException {
		super.load(parser);
		clear();
		if (parser.getItem("CellsMode").equals("enum"))
			super.traverseChildren(ITEMS, false);
		return this;
	}

	@Override
	public Cells save()
			throws UnrecognizedQueryMethodException, MalformedXMLSourceException,
			QueryExpressionException {
		Node parent = Utils.getNode(parser.getDoc(), "init");
		if (parser.getItem("CellsMode").equals("enum"))
			parent = traverseChildren(ITEMS, true);
		for (State s : cellsOnGrid) {
			Element p = parser.getDoc().createElement("cell"); 
			for (String attr : s.getAttributes().keySet()) {
				p.setAttribute(attr, s.getAttributes().get(attr));
			}
			parent.insertBefore(p, null);
		}
		return this;
	}

	@Override
	public void populate(Node n) {
		State s = new State();
		for (int i = 0 ; i < n.getAttributes().getLength(); i++) {
			Node attr = n.getAttributes().item(i);
			s.getAttributes().put(attr.getNodeName(), attr.getNodeValue());
		}
		add(s);
	}
	
	public void add(State s) {
		cellsOnGrid.add(s);
	}
	
	public void clear() {
		cellsOnGrid = new HashSet<>();
	}

	@Override
	public Iterator<State> iterator() {
		return cellsOnGrid.iterator();
	}
}
