package config;

import java.io.File;
import java.util.Set;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import exceptions.MalformedXMLSourceException;
import exceptions.QueryExpressionException;
import exceptions.UnrecognizedQueryMethodException;
import exceptions.XMLParserException;

/**
    ,____________________________________________________________.
   (_\     													      \
      |  All getters and setters are thread safe / synchronized    |
      |  since event handlers runs on multiple different threads   |
      |  not to block UI thread and they all invoke setters here.  |
      |  So must synchronize such access to ensure atomicity.      |
     _|      												       |
    (_/___________________________________________________________/
 
 * @author CharlesXu
 */
public class Configuration {
	
	public static final String DATA_PATH_PREFIX = "data/";
	
	private XMLParser parser;
	private String srcPath;
	
	private String simulationName;
	private String author;
	private int numCols;
	private int numRows;
	private States allStates;
	private Neighborhood neighborhood;
	private Params customizedParams;
	private Cells initialCells;
	private boolean isRunning;
	private int framesPerSec;
	private String shape;
	
	public Configuration(String src)
			throws MalformedXMLSourceException, XMLParserException,
			UnrecognizedQueryMethodException, QueryExpressionException, NumberFormatException {
		srcPath = buildSourcePath(src);
		reset();
	}
	
	/**
	 * Reset all private states to spec read form XML.
	 * @return
	 * @throws XMLParserException
	 * @throws UnrecognizedQueryMethodException
	 * @throws QueryExpressionException
	 * @throws MalformedXMLSourceException
	 */
	public synchronized Configuration reset()
			throws XMLParserException, UnrecognizedQueryMethodException,
			QueryExpressionException, MalformedXMLSourceException {
		parser = XMLParserFactory.build(XMLQueryMethod.XPATH, srcPath);
		simulationName = parser.getItem("SimulationName");
		author = parser.getItem("SimulationAuthor");
		numCols = parser.getItemAsInteger("GridWidth");
		numRows = parser.getItemAsInteger("GridHeight");
		framesPerSec = parser.getItemAsInteger("FramesPerSec");
		shape = parser.getItem("Shape");
		allStates = new States().load(parser);
		neighborhood = new Neighborhood().load(parser);
		customizedParams = new Params().load(parser);
		initialCells = new Cells().load(parser);
		isRunning = false;
		return this;
	}
	
	/**
	 * Pickling/flatting/marshalling/serializing to durable storage on disk
	 * in the form of XML
	 * @param fileName
	 * @throws QueryExpressionException 
	 */
	public synchronized void serializeTo(String fileName)
			throws QueryExpressionException {
		try {
			parser.updateDoc("SimulationName", simulationName);
			parser.updateDoc("SimulationAuthor", author);
			parser.updateDoc("GridWidth", numCols);
			parser.updateDoc("GridHeight", numRows);
			parser.updateDoc("FramesPerSec", framesPerSec);
			parser.updateDoc("Shape", shape);
			allStates.save();
			neighborhood.save();
			customizedParams.save();
			initialCells.save();
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(
					new DOMSource(parser.getDoc()),
					new StreamResult(new File(DATA_PATH_PREFIX + fileName))
			);
		} catch (TransformerFactoryConfigurationError | TransformerException 
				| UnrecognizedQueryMethodException | QueryExpressionException
				| MalformedXMLSourceException e) {
			e.printStackTrace();
		}
	}
	
	private static String buildSourcePath(String src) {
		return DATA_PATH_PREFIX + src;
	}
	
	// -------- ACCESSORS ---------
	public synchronized boolean isRunning() {
		return isRunning;
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

	public synchronized States getAllStates() {
		return allStates;
	}

	public synchronized Neighborhood getNeighborhood() {
		return neighborhood;
	}
	
	public synchronized Cells getInitialCells() {
		return initialCells;
	}
	
	public synchronized int getNumCols() {
		return numCols;
	}

	public synchronized int getNumRows() {
		return numRows;
	}
	
	public synchronized XMLParser getParser() {
		return parser;
	}
	
	public synchronized String getShape() {
		return shape;
	}
	
	public synchronized String getWrapping() {
		return neighborhood.getEdgeType();
	}

	// -------- MUTATORS ---------
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

	public synchronized Configuration setSimulationName(String simulationName) {
		this.simulationName = simulationName;
		return this;
	}

	public synchronized Configuration setAuthor(String author) {
		this.author = author;
		return this;
	}

	public synchronized Configuration setNumCols(int numCols) {
		this.numCols = numCols;
		return this;
	}

	public synchronized Configuration setNumRows(int numRows) {
		this.numRows = numRows;
		return this;
	}
	
	public synchronized Configuration setShape(String shape) {
		this.shape = shape;
		return this;
	}
	
	public synchronized Configuration setWrapping(String wrapping) {
		neighborhood.setEdgeType(wrapping);
		return this;
	}
}
