import java.util.ArrayList;

public abstract class Cell {

	private int rowPos;
	private int colPos;
	private ArrayList<Cell> neighbors;
	
	// TODO: change state to class in charles branch
	private int futurestate;
	private int currentstate;
	
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

	public int getFuturestate() {
		return futurestate;
	}

	public void setFuturestate(int futurestate) {
		this.futurestate = futurestate;
	}

	public int getCurrentstate() {
		return currentstate;
	}

	public void setCurrentstate(int currentstate) {
		this.currentstate = currentstate;
	}
	
	public ArrayList<Cell> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Cell> neighbors) {
		this.neighbors = neighbors;
	}
	
	public abstract void render();
	
	public abstract int[] getRowDeltas();
	
	public abstract int[] getColDeltas();

	public abstract Shapes getShape();
	
}
