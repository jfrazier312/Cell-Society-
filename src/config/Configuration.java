package config;

import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;

import exceptions.MalformedXMLSourceException;
import exceptions.UnrecognizedQueryMethodException;
import model.Cell;
import model.CellGrid;

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
	
	// TODO: deserialize to new XML
	// TODO: synchronize to make thread safe

	public Configuration(Document doc, String queryMethod)
			throws MalformedXMLSourceException {
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
			initialCells = CellGrid.buildNonDefaultInitialCells(parser);
			isRunning = false;
		} catch (XPathExpressionException | UnrecognizedQueryMethodException | NumberFormatException e) {
			e.printStackTrace();
		}
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
	
	public List<Cell> getInitialCells() {
		return initialCells;
	}

	public void setInitialCells(List<Cell> initialCells) {
		this.initialCells = initialCells;
	}
}
