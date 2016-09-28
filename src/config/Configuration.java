package config;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;

import exceptions.InconsistentCrossReferenceInXMLException;
import exceptions.MalformedXMLSourceException;
import exceptions.UnrecognizedQueryMethodException;
import model.Cell;
import model.CellGrid;

public class Configuration {
	
	public static final String DATA_PATH_PREFIX = "data/";
	
	private XMLParser parser;
	
	private String simulationName;
	private String author;
	private int gridWidth;
	private int gridHeight;
	private States allStates;
	private Neighborhood neighborhood;
	private Params customizedParams;
	private List<Cell> initialCells;
	private State defaultInitState;
	private boolean isRunning;
	private int framesPerSec;
	
	// TODO: deserialize to new XML
	
	/**
	 * All getters and setters are thread safe / synchronized
	 * since event handlers runs on multiple different threads to not block UI thread
	 * and they all invoke setters here.
	 * Must synchronize such access to ensure atomicity.
	 */

	public Configuration(Document doc, String queryMethod)
			throws MalformedXMLSourceException {
		synchronized (this) {
			parser = new XMLParser(queryMethod, doc);
			try {
				simulationName = parser.getItem("SimulationName");
				author = parser.getItem("SimulationAuthor");
				gridWidth = parser.getItemAsInteger("GridWidth");
				gridHeight = parser.getItemAsInteger("GridHeight");
				framesPerSec = parser.getItemAsInteger("FramesPerSec");
				allStates = new States().init(parser);
				defaultInitState = allStates.getStateByName(parser.getItem("DefaultInitState"));
				neighborhood = new Neighborhood().init(parser);
				customizedParams = new Params(parser);
				initialCells = CellGrid.buildNonDefaultInitialCells(parser);
				isRunning = false;
			} catch (XPathExpressionException | UnrecognizedQueryMethodException 
					| NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void serializeTo(String fileName) {
		try {
			parser.updateDoc("SimulationName", simulationName);
			parser.updateDoc("SimulationAuthor", author);
			parser.updateDoc("GridWidth", gridWidth);
			parser.updateDoc("GridHeight", gridHeight);
			parser.updateDoc("FramesPerSec", framesPerSec);
			parser.updateDoc("DefaultInitState", defaultInitState.getValue());
			// TODO (cx15): CONTINUE SERIALIZATION STARTING FROM ALLSTATES
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(
					new DOMSource(parser.getDoc()),
					new StreamResult(new File(DATA_PATH_PREFIX + fileName))
			);
		} catch (TransformerFactoryConfigurationError | TransformerException 
				| UnrecognizedQueryMethodException e) {
			e.printStackTrace();
		}
	}
	
	// -------- ACCESSORS ---------
	public synchronized boolean isRunning() {
		return isRunning;
	}
	
	public synchronized State getDefaultInitState() {
		return defaultInitState;
	}
	
	public synchronized int getFramesPerSec() {
		return framesPerSec;
	}

	public synchronized String getCustomParam(String paramName) {
		return customizedParams.getCustomParam(paramName);
	}
	
	public synchronized Set<String> getAllCustomParamNames() {
		return customizedParams.getAllParams();
	}
	
	public synchronized String getSimulationName() {
		return simulationName;
	}

	public synchronized String getAuthor() {
		return author;
	}

	public synchronized int getGirdWidth() {
		return gridWidth;
	}

	public synchronized int getGirdHeight() {
		return gridHeight;
	}

	public synchronized States getAllStates() {
		return allStates;
	}

	public synchronized Neighborhood getNeighborhood() {
		return neighborhood;
	}
	
	public synchronized List<Cell> getInitialCells() {
		return initialCells;
	}

	// -------- MUTATORS ---------
	public synchronized Configuration setDefaultInitState(String defaultInitState)
			throws InconsistentCrossReferenceInXMLException {
		State s = allStates.getStateByName(defaultInitState);
		if (s == null) {
			throw new InconsistentCrossReferenceInXMLException();
		}
		this.defaultInitState = s;
		return this;
	}
	
	public synchronized Configuration setInitialCells(List<Cell> initialCells) {
		this.initialCells = initialCells;
		return this;
	}
	
	public synchronized Configuration setCustomParam(String paramName, String value) {
		customizedParams.setCustomParam(paramName, value);
		return this;
	}
	
	public synchronized Configuration setFramesPerSec(int framesPerSec) {
		this.framesPerSec = framesPerSec;
		return this;
	}
	
	public synchronized Configuration setRunning(boolean isRunning) {
		this.isRunning = isRunning;
		return this;
	}

	public synchronized Configuration setParser(XMLParser parser) {
		this.parser = parser;
		return this;
	}

	public synchronized Configuration setSimulationName(String simulationName) {
		this.simulationName = simulationName;
		return this;
	}

	public synchronized Configuration setAuthor(String author) {
		this.author = author;
		return this;
	}

	public synchronized Configuration setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
		return this;
	}

	public synchronized Configuration setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
		return this;
	}

	public synchronized Configuration setAllStates(States allStates) {
		this.allStates = allStates;
		return this;
	}

	public synchronized Configuration setNeighborhood(Neighborhood neighborhood) {
		this.neighborhood = neighborhood;
		return this;
	}

	public synchronized Configuration setCustomizedParams(Params customizedParams) {
		this.customizedParams = customizedParams;
		return this;
	}
}
