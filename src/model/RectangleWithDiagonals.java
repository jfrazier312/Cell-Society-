package model;

import config.Configuration;

public class RectangleWithDiagonals extends Rectangle {

	private int[] rowDeltas = {-1, -1, 0, 1, 1, 1, 0, -1};
	private int[] colDeltas = {0, -1, -1, -1, 0, 1, 1, 1};
	
	public RectangleWithDiagonals(int row, int col, Configuration config) {
		super(row, col, config);
	}
	
	public int[] getRowDeltas(){
		return rowDeltas;
	}
	
	public int[] getColDeltas(){
		return colDeltas;
	}

}
