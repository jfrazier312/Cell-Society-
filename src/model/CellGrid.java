package model;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import config.Cells;
import config.Configuration;
//import config.ConfigurationLoader;
import config.XMLParser;
import exceptions.MalformedXMLSourceException;
import exceptions.QueryExpressionException;
import exceptions.UnrecognizedQueryMethodException;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import utils.Utils;

/**
 * author: Austin Gartside, Jordan Frazier and Charles Xu
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

	public void renderGrid(GridPane cellPane) {
		for(int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
//				ColumnConstraints colC = new ColumnConstraints();
//				colC.setPercentWidth(100);
//				cellPane.getColumnConstraints().add(colC);
//				RowConstraints rowC = new RowConstraints();
//				rowC.setPercentHeight(100);
//				cellPane.getRowConstraints().add(rowC);
//				
				Cell currentCell = grid[i][j];
				Node updatedCell = currentCell.render();
				cellPane.add(updatedCell, j, i);
			}
		}	
		
		
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
	
	/**
	 * Save each cell to the configuration which then could be serialized 
	 * @return
	 */
	public Configuration save() {
		myConfig.getInitialCells().clear();
		for(int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				myConfig.getInitialCells().add(grid[i][j].serialize());
			}
		}
		return myConfig;
	}
	
	/**
	 * Load cellgrid from config
	 */
	public abstract void load();
	
	// TODO (cx15) deserialize grid. each cell does not need a deserialize
	
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
