package config;

import java.util.List;

import org.w3c.dom.Document;

import utils.Utils;

public class Configuration {
	
	private String simulationName;
	private String author;
	private int gridWidth;
	private int gridHeight;
	private int sceneWidth;
	private int sceneHeight;
	private States allStates;
	private Neighborhood neighborhood;
	// TODO: >>>>>>>>> build initial CellGrid
	
	public Configuration(Document doc) {
		// TODO (cx15): have tags and attrs defined as const for validation and reference
		simulationName = Utils.getAttrFromFirstMatch(doc, "simulation", "name");
		author = Utils.getAttrFromFirstMatch(doc, "simulation", "author");
		gridWidth = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "grid", "width"));
		gridHeight = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "grid", "height"));
		sceneWidth = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "scene", "width"));
		sceneHeight = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "scene", "height"));
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

	public int getGridWidth() {
		return gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}
	
	public int getSceneHeight() {
		return sceneHeight;
	}
	
	public int getSceneWidth() {
		return sceneWidth;
	}

	public States getAllStates() {
		return allStates;
	}

	public Neighborhood getNeighborhood() {
		return neighborhood;
	}
}
