package model;
import java.util.ArrayList;
import java.util.Random;

public class GameOfLifeSimulation{
	
	public static final String DEAD = "DEAD";
	public static final String ALIVE = "ALIVE";
	RectangleWithDiagonals[][] myGrid;
	
	public GameOfLifeSimulation(int rows, int cols) {
		//super(rows, cols);
		myGrid = new RectangleWithDiagonals[rows][cols];
	}
	
	public int getNumRows() {
		return myGrid.length;
	}

	public int getNumCols() {
		return myGrid[0].length;
	}
	
	public void createGrid(){
		Random generator = new Random();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				int deadChance = generator.nextInt(2);
				if(deadChance == 0){
					myGrid[i][j] = new RectangleWithDiagonals(i, j);
					myGrid[i][j].setCurrentstate(DEAD);
				}
				else{
					myGrid[i][j] = new RectangleWithDiagonals(i, j);
					myGrid[i][j].setCurrentstate(ALIVE);
				}			
			}
		}
	}
	//for testing
//	public void printGrid(){
//		Random generator = new Random();
//		//Cell[][] myGrid = this.getGrid();
//		for (int i = 0; i < getNumRows(); i++) {
//			for (int j = 0; j < getNumCols(); j++) {
//				if(myGrid[i][j].getCurrentstate().equals(DEAD)){
//					System.out.print(0);
//				}
//				else{
//					System.out.print(1);
//				}
//			}
//			System.out.println();
//		}
//		System.out.println();
//		System.out.println();
//	}
	
	public void updateFutureStates(){
		//Cell[][] myGrid = this.getGrid();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				RectangleWithDiagonals currentCell = myGrid[i][j];
				updateCell(currentCell);
				//currentCell.setCurrentstate(currentCell.getFuturestate());
			}
		}
	}
	
	public void updateGrid(){
		//Cell[][] myGrid = this.getGrid();
		updateFutureStates();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = myGrid[i][j];
				//updateCell(currentCell);
				currentCell.setCurrentstate(currentCell.getFuturestate());
			}
		}
	}
	
	public void updateCell(RectangleWithDiagonals myCell){
		String myState = myCell.getCurrentstate();
		ArrayList<RectangleWithDiagonals> currentNeighbors = myCell.getNeighbors(myCell, myGrid);
		int liveCount = countCellsOfState(currentNeighbors, ALIVE);
		if(myState.equals(DEAD)){
			if(liveCount == 3){
				myCell.setFuturestate(ALIVE);
			}
			else{
				myCell.setFuturestate(DEAD);
			}
			
		}
		else{
			if(liveCount<2){
				myCell.setFuturestate(DEAD);
			}
			else if(liveCount>=2 && liveCount <=3){
				myCell.setFuturestate(ALIVE);
			}
			else{
				myCell.setFuturestate(DEAD);
			}
		}
	}
	
	public int countCellsOfState(ArrayList<RectangleWithDiagonals> currentNeighbors, String state){
		int stateCount = 0;
		for(Cell neighborCell: currentNeighbors){
			if(neighborCell.getCurrentstate().equals(state)){
				stateCount++;
			}
		}
		return stateCount;
	}
	
	//for testing
//	public static void main(String[] args){
//		GameOfLifeSimulation test = new GameOfLifeSimulation(3,3);
//		test.createGrid();
//		int num = 0;
//		while(num<10){
//			test.printGrid();
//			test.updateGrid();
//			num++;
//		}
//	}
}
