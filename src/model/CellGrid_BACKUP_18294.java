package model;
import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public abstract class CellGrid extends GridPane {

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
<<<<<<< HEAD
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
	
=======
		// set row/column constraints?
	}
	
	//added so that I can find an empty cell in the grid to update future value of in segregation model
	public Cell[][] getGrid(){
		return grid;
	}
	
	private void renderGrid(FlowPane cellPane) {
		for(int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = grid[i][j];
				Node updatedCell = currentCell.render();
				cellPane.getChildren().add(updatedCell);
			}
		}
	}

	public abstract void updateGrid(); //{
		// touch each cell and figure out future state 
//		for (int i = 0; i < getNumRows(); i++) {
//			for (int j = 0; j < getNumCols(); j++) {
//				Cell currentCell = grid[i][j];
//				setNeighbors(currentCell);
//				// update future state based on simulation rules;
//				// which is done in rules engine class? 
//			}
//		}
		
		// loop and update each cell
//		for (int i = 0; i < getNumRows(); i++) {
//			for (int j = 0; j < getNumCols(); j++) {
//				Cell currentCell = grid[i][j];
//				updateCurrentState(currentCell);
//			}
//		}
	//s}

//	private void setNeighbors(Cell cell) {
//		// need this in case user updates cell row/col to illegal spot
//		if (!isValidLocation(cell)) {
//			throw new IllegalArgumentException("Location not valid");
//		}
//		ArrayList<Cell> neighbors = getNeighbors(cell);
//		cell.setNeighbors(neighbors);
//	}

>>>>>>> austin
	/**
	 * Returns the neighbors of a shape. May need to change
	 * row/column deltas based on definition of 'neighbor'
	 * (diagonals or not)
	 * 
	 * @param cell - the shape
	 * @return - ArrayList<Cell> of cell's neighbors
	 */
<<<<<<< HEAD
	protected ArrayList<Cell> getNeighbors(Cell cell) {
=======
	
	//changed to protected so that the segregation simulation could see, not sure if that's good design
	protected ArrayList<Cell> getNeighbors(Cell cell) {
		// could change implementation based on definition of 'neighbor'
>>>>>>> austin
		ArrayList<Cell> neighbors = new ArrayList<>();
		int rowPos = cell.getRowPos();
		int colPos = cell.getColPos();
		for (int i = 0; i < cell.getRowDeltas().length; i++) {
			int newRowPos = rowPos + cell.getRowDeltas()[i];
			int newColPos = colPos + cell.getColDeltas()[i];
<<<<<<< HEAD
			if (isValidLocation(grid[newRowPos][newColPos])) {
=======
			if(isValidLocation(newRowPos, newColPos)){ 
>>>>>>> austin
				neighbors.add(grid[newRowPos][newColPos]);
			}
		}
		return neighbors;
	}
	
	public abstract void updateCell(Cell myCell);

<<<<<<< HEAD
	/* backend does this too
	private void updateCurrentState(Cell cell) {
		cell.setCurrentstate(cell.getFuturestate());
	}

	private void setFutureState(Cell cell, String futurestate) {
		cell.setFuturestate(futurestate);
	}
	*/
=======
//	private void updateCurrentState(Cell cell) {
//		cell.setCurrentstate(cell.getFuturestate());
//	}
//
>>>>>>> austin

//	private boolean isValidLocation(Cell cell) {
//		return 0 <= cell.getRowPos() && 0 <= cell.getColPos() && cell.getRowPos() < getNumRows()
//				&& cell.getColPos() < getNumCols();
//	}
	private boolean isValidLocation(int row, int col) {
		return 0 <= row && 0 <= col && row < getNumRows()
				&& col < getNumCols();
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
