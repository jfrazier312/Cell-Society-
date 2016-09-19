package config;

import org.w3c.dom.Document;
import utils.Utils;

public class ModelConfiguration {
	
	private String simulationName;
	private String author;
	private int girdWidth;
	private int girdHeight;
	private States allStates;
	private Neighborhood neighborhood;
	// TODO: build initial CellGrid
	
	public ModelConfiguration(Document doc) {
		// TODO (cx15): have tags and attrs defined as const for validation and reference
		simulationName = Utils.getAttrFromFirstMatch(doc, "simulation", "name");
		author = Utils.getAttrFromFirstMatch(doc, "simulation", "author");
		girdWidth = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "grid", "width"));
		girdHeight = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "grid", "height"));
		allStates = new States().init(doc);
		neighborhood = new Neighborhood().init(doc);
	}

	// expose only limited setters to frontend, add here as needed
	// every other setting should be set by loading xml using loader, set once update everywhere
	
	public String getSimulationName() {
		return simulationName;
	}

	public String getAuthor() {
		return author;
	}

	public int getGirdWidth() {
		return girdWidth;
	}

	public int getGirdHeight() {
		return girdHeight;
	}

	public States getAllStates() {
		return allStates;
	}

	public Neighborhood getNeighborhood() {
		return neighborhood;
	}
}
