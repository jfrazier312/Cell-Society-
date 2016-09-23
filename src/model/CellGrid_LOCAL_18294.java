package model;
import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class CellGrid extends GridPane {

	private Cell[][] grid;
	
	// TODO: Jordan: change parameters 
	//       number of initially empty (resets grid)
	// 		 percentage of states to each other (resets)
	// 		 step delay
	// 		 size of cells
	// 		 have a percentage of satisfied cells (dynamically)	 

	public CellGrid(int rows, int cols) {
		if (rows <= 0 || cols <= 0) {
			throw new IllegalArgumentException("Cannot have 0 or less rows/cols");
		}
		grid = new Cell[rows][cols];
		// TODO: Jordan set row/column constraints. maybe
	}

	// Need to change spacing in flowpane if shape is different than rectangle
	public void renderGrid(FlowPane cellPane) {
		// loop through 2d grid, render each cell. should have already set up state correctly, 
		// this just needs to display it.
		
		for(int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = grid[i][j];
				Node updatedCell = currentCell.render();
				cellPane.getChildren().add(updatedCell);
			}
		}	
	}
	
	// TODO: Jordan Not needed. backend will just call getNeighbors()
	private void setNeighbors(Cell cell) {
		// need this in case user updates cell row/col to illegal spot?
		if (!isValidLocation(cell)) {
			throw new IllegalArgumentException("Location not valid");
		}
		ArrayList<Cell> neighbors = getNeighbors(cell);
		cell.setNeighbors(neighbors);
	}
	
	/**
	 * Returns the neighbors of a shape. May need to change
	 * row/column deltas based on definition of 'neighbor'
	 * (diagonals or not)
	 * 
	 * @param cell - the shape
	 * @return - ArrayList<Cell> of cell's neighbors
	 */
	protected ArrayList<Cell> getNeighbors(Cell cell) {
		ArrayList<Cell> neighbors = new ArrayList<>();
		int rowPos = cell.getRowPos();
		int colPos = cell.getColPos();
		for (int i = 0; i < cell.getRowDeltas().length; i++) {
			int newRowPos = rowPos + cell.getRowDeltas()[i];
			int newColPos = colPos + cell.getColDeltas()[i];
			if (isValidLocation(grid[newRowPos][newColPos])) {
				neighbors.add(grid[newRowPos][newColPos]);
			}
		}
		return neighbors;
	}

	/* backend does this too
	private void updateCurrentState(Cell cell) {
		cell.setCurrentstate(cell.getFuturestate());
	}

	private void setFutureState(Cell cell, String futurestate) {
		cell.setFuturestate(futurestate);
	}
	*/

	private boolean isValidLocation(Cell cell) {
		return 0 <= cell.getRowPos() && 0 <= cell.getColPos() && cell.getRowPos() < getNumRows()
				&& cell.getColPos() < getNumCols();
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

}
