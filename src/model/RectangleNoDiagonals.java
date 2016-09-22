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
	

}
