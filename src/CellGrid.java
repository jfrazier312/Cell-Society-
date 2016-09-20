import java.util.ArrayList;

import javafx.scene.layout.GridPane;

//need to create the grid as well

public class CellGrid extends GridPane {

	private Cell[][] grid; // probably should rename Cell

	public CellGrid(int rows, int cols) {
		if (rows <= 0 || cols <= 0) {
			throw new IllegalArgumentException("Cannot have 0 or less rows/cols");
		}
		grid = new Cell[rows][cols];
		// set row/column constraints?
	}
	
	//added so that I can find an empty cell in the grid to update future value of in segregation model
	public Cell[][] getGrid(){
		return grid;
	}
	
	private void renderGrid() {
		// render the grid graphically somehow...
	}

	private void updateGrid(boolean diagonalIncluded) {
		// touch each cell and figure out future state 
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = grid[i][j];
				setNeighbors(currentCell, diagonalIncluded);
				// update future state based on simulation rules;
				// which is done in rules engine class? 
			}
		}
		
		// loop and update each cell
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = grid[i][j];
				updateCurrentState(currentCell);
			}
		}
	}

	private void setNeighbors(Cell cell, boolean diagonalIncluded) {
		// need this in case user updates cell row/col to illegal spot
		if (!isValidLocation(cell)) {
			throw new IllegalArgumentException("Location not valid");
		}
		ArrayList<Cell> neighbors = getNeighbors(cell, diagonalIncluded);
		cell.setNeighbors(neighbors);
	}

	/**
	 * Add to this method if implementing new shape
	 * 
	 * @param cell
	 * @return
	 */
	private ArrayList<Cell> getNeighbors(Cell cell, boolean diagonalsIncluded) {
		ArrayList<Cell> neighbors = new ArrayList<>();
		if (cell.getShape() == Shapes.RECTANGLE) {
			neighbors = getRectangleNeighbors(cell, diagonalsIncluded);
		}
		else if (cell.getShape() == Shapes.OTHERSHIT) {
			// neighbors = getOtherShit(cell);
		}
		return neighbors;
	}
	
	//changed to protected so that the segregation simulation could see, not sure if that's good design
	protected ArrayList<Cell> getRectangleNeighbors(Cell cell, boolean diagonalsIncluded) {
		// could change implementation based on definition of 'neighbor'
		ArrayList<Cell> neighbors = new ArrayList<>();
		int rowPos = cell.getRowPos();
		int colPos = cell.getColPos();
		//added check for valid location
		for(int i = 0; i < cell.getRowDeltas(false).length; i++) {
			int newRowPos = rowPos + cell.getRowDeltas(diagonalsIncluded)[i];
			int newColPos = colPos + cell.getColDeltas(diagonalsIncluded)[i];
			if(isValidLocation(grid[newRowPos][newColPos])){ 
				neighbors.add(grid[newRowPos][newColPos]);
			}
		}
		return neighbors;
	}

	private void updateCurrentState(Cell cell) {
		cell.setCurrentstate(cell.getFuturestate());
	}

	private void setFutureState(Cell cell, int futurestate) {
		cell.setFuturestate(futurestate);
	}

	private boolean isValidLocation(Cell cell) {
		return 0 <= cell.getRowPos() && 0 <= cell.getColPos() && cell.getRowPos() < getNumRows()
				&& cell.getColPos() < getNumCols();
	}

	public int getNumRows() {
		return grid.length;
	}

	public int getNumCols() {
		return grid[0].length;
	}

}
