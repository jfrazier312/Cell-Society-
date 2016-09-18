package config;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import utils.Utils;

public class Neighborhood {

	private int size;
	private String edgeType;
	private String edgeValue;
	
	public Neighborhood init(Document doc) {
		size = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "grid", "height"));
		edgeType = Utils.getAttrFromFirstMatch(doc, "neighborhood", "type");
		edgeValue = Utils.getAttrFromFirstMatch(doc, "neighborhood", "value");
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
