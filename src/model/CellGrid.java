package model;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.NodeList;

import config.ConfigurationLoader;
//import config.ConfigurationLoader;
import config.XMLParser;
import exceptions.UnrecognizedQueryMethodException;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import utils.Utils;

public abstract class CellGrid extends GridPane {

	private Cell[][] grid;
	
	private String simulationName;
	
	// TODO: Jordan: change parameters 
	//       number of initially empty (resets grid)
	// 		 percentage of states to each other (resets)
	// 		 step delay
	// 		 size of cells
	// 		 have a percentage of satisfied cells (dynamically)	 

	public CellGrid() {
		int rows = ConfigurationLoader.getConfig().getNumRows();
		int cols = ConfigurationLoader.getConfig().getNumCols();
		if (rows <= 0 || cols <= 0) {
			throw new IllegalArgumentException("Cannot have 0 or less rows/cols");
		}
		grid = new Cell[rows][cols];
		// TODO: Jordan set row/column constraints. maybe
	}

	// Need to change spacing in flowpane if shape is different than rectangle
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
	protected ArrayList<Cell> getNeighbors(Cell cell) {
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
	
	public Cell[][] getGrid() {
		return grid;
	}

	public int getNumRows() {
		return grid.length;
	}

	public int getNumCols() {
		return grid[0].length;
	}

	public abstract void updateGrid();

	public abstract void updateCell(Cell myCell);
	
	public abstract String getSimulationName();
	
	public abstract void initSimulation();
	
	public static List<Cell> buildNonDefaultInitialCells(XMLParser parser)
			throws XPathExpressionException, UnrecognizedQueryMethodException {
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
}
