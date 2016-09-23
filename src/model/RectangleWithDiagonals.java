package model;

import java.util.ArrayList;

public class RectangleWithDiagonals extends Rectangle {

	private int[] rowDeltas = {-1, 0, 1, 0, 1, 1, -1, -1};
	private int[] colDeltas = {0, -1, 0, 1, 1, -1, 1, -1};
	
	public RectangleWithDiagonals(int row, int col) {
		super(row, col);
	}
	
	public int[] getRowDeltas(){
		return rowDeltas;
	}
	
	public int[] getColDeltas(){
		return colDeltas;
	}
	
	public ArrayList<RectangleWithDiagonals> getNeighbors(Cell myCell, RectangleWithDiagonals[][] grid) {
		ArrayList<RectangleWithDiagonals> neighbors = new ArrayList<RectangleWithDiagonals>();
		int rowPos = myCell.getRowPos();
		int colPos = myCell.getColPos();
		for(int i = 0; i < myCell.getRowDeltas().length; i++) {
			int newRowPos = rowPos + myCell.getRowDeltas()[i];
			int newColPos = colPos + myCell.getColDeltas()[i];
			if(isValidLocation(newRowPos, newColPos, grid)){ 
				neighbors.add(grid[newRowPos][newColPos]);
			}
		}
		return neighbors;
	}
	
	private boolean isValidLocation(int row, int col, Cell[][] grid) {
		return 0 <= row && 0 <= col && row < grid.length
				&& col < grid[0].length;
	}

}
