package model;

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

}
