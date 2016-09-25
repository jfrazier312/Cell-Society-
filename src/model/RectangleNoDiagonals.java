package model;

public class RectangleNoDiagonals extends Rectangle {

	private int[] noDiagonalRowDeltas = {-1, 0, 1, 0,};
	private int[] noDiagonalColDeltas = {0, -1, 0, 1};
	
	public RectangleNoDiagonals(int row, int col) {
		super(row, col);
	}
	
	@Override
	public int[] getRowDeltas(){
		return noDiagonalRowDeltas;
	}
	
	@Override
	public int[] getColDeltas(){
		return noDiagonalColDeltas;
	}
	
//	public ArrayList<RectangleNoDiagonals> getNeighbors(RectangleNoDiagonals[][] grid) {
//		ArrayList<RectangleNoDiagonals> neighbors = new ArrayList<RectangleNoDiagonals>();
//		int rowPos = getRowPos();
//		int colPos = getColPos();
//		for(int i = 0; i < getRowDeltas().length; i++) {
//			int newRowPos = rowPos + getRowDeltas()[i];
//			int newColPos = colPos + getColDeltas()[i];
//			if(isValidLocation(newRowPos, newColPos, grid)){ 
//				neighbors.add(grid[newRowPos][newColPos]);
//			}
//		}
//		return neighbors;
//	}
//	
//	private boolean isValidLocation(int row, int col, Cell[][] grid) {
//		return 0 <= row && 0 <= col && row < grid.length
//				&& col < grid[0].length;
//	}

}
