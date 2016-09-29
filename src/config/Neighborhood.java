package config;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import exceptions.MalformedXMLSourceException;
import exceptions.UnrecognizedQueryMethodException;

public class Neighborhood extends CompositeData {
	
	private int size;
	private String edgeType;
	private String edgeValue; //only make sense if constant edge state
	
	@Override
	public Neighborhood load(XMLParser parser)
			throws NumberFormatException, XPathExpressionException,
			UnrecognizedQueryMethodException, MalformedXMLSourceException {
		super.load(parser);
		size = parser.getItemAsInteger("NeighborhoodSize");
		edgeType = parser.getItem("NeighborhoodEdgeType");
		edgeValue = parser.getItem("NeighborhoodEdgeValue");
		return this;
	}

	@Override
	public Neighborhood save() throws UnrecognizedQueryMethodException {
		parser.updateDoc("NeighborhoodSize", size);
		parser.updateDoc("NeighborhoodEdgeType", edgeType);
		parser.updateDoc("NeighborhoodEdgeValue", edgeValue);
		return this;
	}

	public int getSize() {
		return size;
	}
	
	public Neighborhood setSize(int size) {
		this.size = size;
		return this;
	}
	
	public String getEdgeType() {
		return edgeType;
	}
	
	public Neighborhood setEdgeType(String edgeType) {
		this.edgeType = edgeType;
		return this;
	}
	
	public String getEdgeValue() {
		return edgeValue;
	}
	
	public Neighborhood setEdgeValue(String edgeValue) {
		this.edgeValue = edgeValue;
		return this;
	}

	@Override
	public void populate(Node n) {}
}
