package config;

import javax.xml.xpath.XPathExpressionException;

import exceptions.UnrecognizedQueryMethodException;
import model.XMLParser;

public class Neighborhood {

	private int size;
	private String edgeType;
	private String edgeValue; //only make sense if constant edge state
	
	public Neighborhood init(XMLParser parser)
			throws NumberFormatException, XPathExpressionException,
			UnrecognizedQueryMethodException {
		size = parser.getItemAsInteger("NeighborhoodSize");
		edgeType = parser.getItem("NeighborhoodEdgeType");
		edgeValue = parser.getItem("NeighborhoodEdgeValue");
		return this;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public String getEdgeType() {
		return edgeType;
	}
	
	public void setEdgeType(String edgeType) {
		this.edgeType = edgeType;
	}
	
	public String getEdgeValue() {
		return edgeValue;
	}
	
	public void setEdgeValue(String edgeValue) {
		this.edgeValue = edgeValue;
	}
}
