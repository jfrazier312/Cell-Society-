package model;
import java.util.ArrayList;

import javafx.scene.Node;

public class Cell {
	
	private int rowPos;
	private int colPos;
	private ArrayList<Cell> neighbors;
	
	private String futurestate;
	private String currentstate;
	
	public Cell(int row, int col){
		rowPos = row;
		colPos = col;
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
	
	public Node render(String a) {
		return null;
	}
	
	public Node render() { return null; }
	
	public int[] getRowDeltas() { return null; }
	
	public int[] getColDeltas() { return null; }
}