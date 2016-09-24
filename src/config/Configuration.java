package config;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import exceptions.UnrecognizedQueryMethodException;
import utils.Utils;
import model.Cell;
import model.XMLParser;

public class Configuration {
	
	private String simulationName;
	private String author;
	private int girdWidth;
	private int girdHeight;
	private States allStates;
	private Neighborhood neighborhood;
	private Params customizedParams;
	private List<Cell> initialCells;
	private State defaultInitState;
	private boolean isRunning;
	private int framesPerSec;
	
	// TODO: param 
	
	// TODO: deserialize to new XML

	public Configuration(Document doc, String queryMethod) {
		XMLParser parser = new XMLParser(queryMethod, doc);
		try {
			simulationName = parser.getItem("SimulationName");
			author = parser.getItem("SimulationAuthor");
			girdWidth = parser.getItemAsInteger("GirdWidth");
			girdHeight = parser.getItemAsInteger("GirdHeight");
			framesPerSec = parser.getItemAsInteger("FramesPerSec");
			allStates = new States().init(parser);
			defaultInitState = allStates.getStateByName(parser.getItem("DefaultInitState"));
			neighborhood = new Neighborhood().init(parser);
			customizedParams = new Params(parser);
		} catch (XPathExpressionException | UnrecognizedQueryMethodException | NumberFormatException e) {
			e.printStackTrace();
		}
		buildNonDefaultInitialCells(doc);
		isRunning = false;
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	public State getDefaultInitState() {
		return defaultInitState;
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
	
	// TODO: refactor, test, and move to CellGrid
	private void buildNonDefaultInitialCells(Document doc) {
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
