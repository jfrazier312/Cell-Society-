package model;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.w3c.dom.NodeList;

import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.State;

import config.Configuration;
//import config.ConfigurationLoader;
import config.XMLParser;
import exceptions.MalformedXMLSourceException;
import exceptions.QueryExpressionException;
import exceptions.UnrecognizedQueryMethodException;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import utils.Utils;

/**
 * author: Austin Gartside and Jordan Frazier
 */
public abstract class CellGrid extends GridPane {

	protected ResourceBundle myResources;
	public static final String RESRC_PATH = "resources/SimulationResources";	
	
	private Cell[][] grid;
	
	private String simulationName;
	
	private Configuration myConfig;
	
	public CellGrid(Configuration config) {
		myResources = ResourceBundle.getBundle(RESRC_PATH);
		myConfig = config;
		if (config.getNumRows() <= 0 || config.getNumCols() <= 0) {
			throw new IllegalArgumentException("Cannot have 0 or less rows/cols");
		}
		grid = new Cell[config.getNumRows()][config.getNumCols()];
	}

	public void renderGrid(GridPane cellPane, Configuration config) {
		for(int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = grid[i][j];
				Shape updatedCell = currentCell.render();	
				allowClickableCells(config, currentCell, updatedCell);
				cellPane.add(updatedCell, j, i);
			}
		}	
	}

	private void allowClickableCells(Configuration config, Cell currentCell, Shape updatedCell) {
		updatedCell.setOnMouseClicked(e -> {
//			for(config.State s : config.getAllStates()) {
//				if (s.getValue().equals(currentCell.getCurrentstate())) {
//					
//				}
//			}
			
//			for(int i = config.getAllStates().getList().(currentCell.getCurrentstate()); i < config.getAllStates().getLength(); i++) {
//				if (!currentCell.getCurrentstate().equals(config.getAllStates().get(0))){

//				if (!currentCell.getCurrentstate().equals(s.getValue())){
//					currentCell.setCurrentstate(s.getValue());
//					break;
//				}
//			}
//			updatedCell = currentCell.render();
			String color = myConfig.getAllStates().getStateByName(currentCell.getCurrentstate()).getAttributes()
					.get("color");
			updatedCell.setFill(Color.web(color));		
		});
	}
	
	/**
	 * Returns the neighbors of a shape. May need to change
	 * row/column deltas based on definition of 'neighbor'
	 * (diagonals or not)
	 * Returns the neighbors of a shape. May need to change
	 * row/column deltas based on definition of 'neighbor'
	 * (diagonals or not)
	 * 
	 * @param cell - the shape
	 * @return - ArrayList<Cell> of cell's neighbors
	 */
	
	//changed to protected so that the segregation simulation could see, not sure if that's good design
	protected ArrayList<Cell> getNeighbors(Cell cell, int vision) {
		// could change implementation based on definition of 'neighbor'
		ArrayList<Cell> neighbors = new ArrayList<>();
		int rowPos = cell.getRowPos();
		int colPos = cell.getColPos();
		for (int i = 0; i < cell.getRowDeltas().length; i++) {
			int newRowPos = rowPos + cell.getRowDeltas()[i];
			int newColPos = colPos + cell.getColDeltas()[i];
			if (isValidLocation(newRowPos, newColPos)) {
				neighbors.add(grid[newRowPos][newColPos]);
			}
		}
		return neighbors;
	}
	
	//assuming no diagonals
	public ArrayList<Cell>  getSugarNeighbors(Cell cell, int vision){
		ArrayList<Cell> neighbors = new ArrayList<>();
		int rowPos = cell.getRowPos();
		int colPos = cell.getColPos();
		//int vision = 5;
		for(int i = 0; i<cell.getRowDeltas().length; i++){
			for(int j = 1; j<=vision; j++){
				int newRowPos = rowPos + cell.getRowDeltas()[i]*j;
				int newColPos = colPos + cell.getColDeltas()[i]*j;
				if (isValidLocation(newRowPos, newColPos)) {
					neighbors.add(grid[newRowPos][newColPos]);
				}
			}
		}
		return neighbors;
	}

	/* backend does this too
	private void updateCurrentState(Cell cell) {
		cell.setCurrentstate(cell.getFuturestate());
	}
	/* backend does this too
	private void updateCurrentState(Cell cell) {
		cell.setCurrentstate(cell.getFuturestate());
	}

	private void setFutureState(Cell cell, String futurestate) {
		cell.setFuturestate(futurestate);
	}
	*/

	private void setFutureState(Cell cell, String futurestate) {
		cell.setFuturestate(futurestate);
	}

	private boolean isValidLocation(int x, int y) {
		return 0 <= x && 0 <= y && x < getNumRows()
				&& y < getNumCols();
	}
	

	public static List<Cell> buildNonDefaultInitialCells(XMLParser parser)
			throws QueryExpressionException, UnrecognizedQueryMethodException,
				   NumberFormatException, MalformedXMLSourceException {
		List<Cell> initialCells = new ArrayList<Cell>();
		if (parser.getItem("CellsMode").equals("enum")) {
			NodeList nl = parser.getNodeList("Cells");
			for (int i = 0; i < nl.getLength(); i++) {
				String state = Utils.getAttrFromNode(nl.item(i), "state");
				int row = Integer.parseInt(Utils.getAttrFromNode(nl.item(i), "row"));
				int col = Integer.parseInt(Utils.getAttrFromNode(nl.item(i), "col"));
				Cell c = new Cell(row, col);
				c.setCurrentstate(state);
				initialCells.add(c);
		    }
		}
		return initialCells;
	}
	
	public Cell[][] getGrid() {
		return grid;
	}

	public int getNumRows() {
		return grid.length;
	}

	public int getNumCols() {
		return grid[0].length;
	}
	
	public void setGridCell(int row, int col, Cell myCell){
		grid[row][col] = myCell;
	}
	
	public Cell getGridCell(int row, int col){
		return grid[row][col];
	}
	
	public Configuration getConfig() {
		return myConfig;
	}

	public abstract void updateGrid();

	public abstract void updateCell(Cell myCell);
	
	public abstract String getSimulationName();

	public abstract void initSimulation();

}
