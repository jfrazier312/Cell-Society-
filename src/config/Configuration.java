package config;

import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import utils.Utils;
import model.Cell;

public class Configuration {
	
	private String simulationName;
	private String author;
	private int gridWidth;
	private int gridHeight;
	private int sceneWidth;
	private int sceneHeight;
	private States allStates;
	private Neighborhood neighborhood;
	private Params customizedParams;
	private List<Cell> initialCells;
	private String defaultState;
	private boolean isRunning;
	private int framesPerSec;

	public Configuration(Document doc) {
		// TODO (cx15): have tags and attrs defined as const for validation and reference
		simulationName = Utils.getAttrFromFirstMatch(doc, "simulation", "name");
		author = Utils.getAttrFromFirstMatch(doc, "simulation", "author");
		gridWidth = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "grid", "width"));
		gridHeight = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "grid", "height"));
		sceneWidth = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "scene", "width"));
		sceneHeight = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "scene", "height"));
		framesPerSec = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "animation", "framesPerSec"));
		defaultState = Utils.getAttrFromFirstMatch(doc, "init", "default");
		sceneWidth = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "scene", "width"));
		sceneHeight = Integer.parseInt(Utils.getAttrFromFirstMatch(doc, "scene", "height"));
		allStates = new States().init(doc);
		neighborhood = new Neighborhood().init(doc);
		buildInitialCells(doc);
		isRunning = false;
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	public String getDefaultState() {
		return defaultState;
	}
	
	public int getFramesPerSec() {
		return framesPerSec;
	}

	public void setFramesPerSec(int framesPerSec) {
		this.framesPerSec = framesPerSec;
	}

	public String getCustomParam(String paramName) {
		return customizedParams.getCustomParam(paramName);
	}
	
	public String setCustomParam(String paramName, String value) {
		return customizedParams.setCustomParam(paramName, value);
	}
	
	public Set<String> getAllCustomParamNames() {
		return customizedParams.getAllParams();
	}
	
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
	
	private void buildInitialCells(Document doc) {
		initialCells = new ArrayList<Cell>();
		NodeList nl = doc.getElementsByTagName("cell");
		for (int i = 0; i < nl.getLength(); i++) {
			String state = Utils.getAttrFromNode(nl.item(i), "state");
			int row = Integer.parseInt(Utils.getAttrFromNode(nl.item(i), "row"));
			int col = Integer.parseInt(Utils.getAttrFromNode(nl.item(i), "col"));
			Cell c = new Cell(row, col);
			c.setCurrentstate(state);
			initialCells.add(c);
	    }
	}
}
