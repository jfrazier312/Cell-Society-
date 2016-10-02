package config;

import org.w3c.dom.Node;

import exceptions.MalformedXMLSourceException;
import exceptions.QueryExpressionException;
import exceptions.UnrecognizedQueryMethodException;

/**
 * @author CharlesXu
 */
public class Neighborhood extends CompositeData {
	
	private int size;
	private String edgeType;
	private String edgeValue;
	
	@Override
	public Neighborhood load(XMLParser parser)
			throws NumberFormatException, QueryExpressionException,
			UnrecognizedQueryMethodException, MalformedXMLSourceException {
		super.load(parser);
		size = parser.getItemAsInteger("NeighborhoodSize");
		edgeType = parser.getItem("NeighborhoodEdgeType");
		edgeValue = parser.getItem("NeighborhoodEdgeValue");
		return this;
	}

	@Override
	public Neighborhood save()
			throws UnrecognizedQueryMethodException, MalformedXMLSourceException,
			QueryExpressionException {
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
