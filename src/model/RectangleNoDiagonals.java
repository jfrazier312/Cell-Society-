package model;

import org.w3c.dom.Element;

import config.Configuration;
import config.XMLParser;

public class RectangleNoDiagonals extends Rectangle {

	private int[] noDiagonalRowDeltas = {-1, 0, 1, 0,};
	private int[] noDiagonalColDeltas = {0, -1, 0, 1};
	
	public RectangleNoDiagonals(int row, int col, Configuration config) {
		super(row, col, config);
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
