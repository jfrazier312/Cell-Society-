package model;
import java.util.ArrayList;

import config.XMLParser;
import javafx.scene.Node;

import config.State;

/**
 * 
 * @author Jordan Frazier (jrf30)
 * @author Charles Xu (cx15)
 *
 */

//put all row deltas here nad have the get row delta methods be here
public abstract class Cell {
	
	//make getters and setters for all of these and put them in the classes?
//	private int[] noDiagonalRowDeltas = { -1, 0, 1, 0, };
//	private int[] noDiagonalColDeltas = { 0, -1, 0, 1 };
//	
//	private int[] diagonalRowDeltas = {-1, 0, 1, 0, 1, 1, -1, -1};
//	private int[] diagonalColDeltas = {0, -1, 0, 1, 1, -1, 1, -1};
//	
//	private int[] rowDeltas = { -1, 0, 1 };
//	private int[] evenColDeltas = { 0, 1, 0 };
//	private int[] oddColDeltas = { 0, -1, 0 };
	
	private int rowPos;
	private int colPos;
	private ArrayList<Cell> neighbors;
	
	private String futurestate;
	private String currentstate;
	//private String myShape;
	//public Cell(int row, int col, String shape){
	public Cell(int row, int col){
		rowPos = row;
		colPos = col;
		//myShape = shape;
	}
	
	public int getRowPos() {
		return rowPos;
	}

	public void setRowPos(int rowPos) {
		this.rowPos = rowPos;
	}

	public int getColPos() {
		return colPos;
	}

	public void setColPos(int colPos) {
		this.colPos = colPos;
	}

	public String getFuturestate() {
		return futurestate;
	}

	public void setFuturestate(String futurestate) {
		this.futurestate = futurestate;
	}

	public String getCurrentstate() {
		return currentstate;
	}

	public void setCurrentstate(String currentstate) {
		this.currentstate = currentstate;
	}
	
	public ArrayList<Cell> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Cell> neighbors) {
		this.neighbors = neighbors;
	}
	
	public abstract Node render();
	
	public abstract int[] getRowDeltas();
	
	public abstract int[] getColDeltas();
	
	/**
	 * Serialize to an element in XML
	 * @param parser
	 */
	public State serialize() {
		State s = new State();
		s.getAttributes().put("row", rowPos + "");
		s.getAttributes().put("col", colPos + "");
		s.getAttributes().put("currentState", currentstate);
		return s;
	}
}
