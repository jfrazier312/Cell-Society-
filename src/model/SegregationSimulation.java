package model;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

//Should we do getting neighbors in each simulation since the definition can vary?
public class SegregationSimulation extends CellGrid {
	
	public static final String EMPTY = "EMPTY";
	public static final String TYPE1 = "TYPE1";
	public static final String TYPE2 = "TYPE2";
	private int myProbability;
	PriorityQueue<Cell> myMovingCells;

	public SegregationSimulation(int rows, int cols, int probability) {
		//super(rows, cols);
		super();
		myProbability  = probability;
		myMovingCells = new PriorityQueue<Cell>();
	}
	
	public void updateGrid(){
		Cell[][] myGrid = this.getGrid();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = myGrid[i][j];
				if(currentCell.getCurrentstate() == EMPTY && myMovingCells.size()>0){
					currentCell.setCurrentstate(myMovingCells.poll().getCurrentstate());
				}
				else{
					updateCell(currentCell);
				}
			}
		}
	}
	
	
	public void updateCell(Cell myCell){
		//do nothing for an empty cell
		if(myCell.getCurrentstate() == EMPTY){
			return;
		}
		ArrayList<Cell> currentNeighbors = getRectangleNeighbors(myCell);
		double matchingCellCount = 0.0;
		//start at 1 to avoid divide by 0 error
		int nonEmptyCellCount = 1;
		for(int i = 0; i<currentNeighbors.size(); i++){
			String neighborState = currentNeighbors.get(i).getCurrentstate();
			//assuming empty cell state equals 0
			if(!neighborState.equals(EMPTY)){
				nonEmptyCellCount++;
				if(neighborState == myCell.getCurrentstate()){
					matchingCellCount++;
				}
			}		
		}
		if(matchingCellCount/(nonEmptyCellCount-1) < myProbability){
			myCell.setFuturestate(EMPTY);
		}
		else{
			myMovingCells.add(myCell);
			//need to move this state to a random cell somewhere else on the grid
			//Cell newLocation = findEmptyCell(myGrid);
			//newLocation.setFuturestate(myCell.getCurrentstate());
			
		}
	}

}
