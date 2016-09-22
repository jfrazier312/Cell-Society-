package config;

import org.w3c.dom.Document;

import utils.Utils;

public class Neighborhood {

	private int size;
	private String edgeType;
	private String edgeValue;
	
	public Neighborhood init(Document doc) {
		size = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "grid", "height"));
		edgeType = Utils.getAttrFromFirstMatch(doc, "edge", "type");
		edgeValue = Utils.getAttrFromFirstMatch(doc, "edge", "value");
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
