package model;
import java.util.ArrayList;
import java.util.Random;

//Should we do getting neighbors in each simulation since the definition can vary?
public class SegregationSimulation extends CellGrid {
	
	public static final String EMPTY = "EMPTY";
	public static final String TYPE1 = "TYPE1";
	public static final String TYPE2 = "TYPE2";
	private double myProbability;
	ArrayList<Cell> myMovingCells;
	Random generator;

	public SegregationSimulation() {
		super();
		//myProbability  = probability;
		myMovingCells = new ArrayList<Cell>();
		createGrid((1.0/3.0), .5);
		
	}
	
	public void createGrid(double percentEmpty, double percentType1) {
		generator = new Random();
		int size = getNumRows()*getNumCols();
		double numEmpty = percentEmpty*size;
		double numType1 = percentType1*(size-numEmpty);
		double numType2 = size-numEmpty-numType1;
		ArrayList<String> initialization = new ArrayList<String>();
		for(int i = 0; i<numEmpty; i++){
			initialization.add(EMPTY);
		}
		for(int i = 0; i<numType1; i++){
			initialization.add(TYPE1);
		}
		for(int i = 0; i<numType2; i++){
			initialization.add(TYPE2);
		}
		Cell[][] myGrid = getGrid();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				myGrid[i][j] = new RectangleWithDiagonals(i, j);
				if(initialization.size() == 0){
					myGrid[i][j].setCurrentstate(EMPTY);
				}
				else{
					int cellChoice = generator.nextInt(initialization.size());
					myGrid[i][j].setCurrentstate(initialization.get(cellChoice));
					initialization.remove(cellChoice);
				}
			}
		}
	}
	
	@Override
	public void updateGrid(){
		updateFutureStates();
		Cell[][] myGrid = this.getGrid();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = myGrid[i][j];
				currentCell.setCurrentstate(currentCell.getFuturestate());
				
			}
		}
	}
	
	public void updateFutureStates(){
		Cell[][] myGrid = this.getGrid();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = myGrid[i][j];
				if(!currentCell.getCurrentstate().equals(EMPTY)){
					updateCell(currentCell);
				}
			}
		}
		
		ArrayList<Cell> cellsToMakeEmpty = new ArrayList<Cell>();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = myGrid[i][j];
				if(currentCell.getCurrentstate().equals(EMPTY)){
					currentCell.setFuturestate(EMPTY);
					if(myMovingCells.size()>0){
						int whichCell = generator.nextInt(myMovingCells.size());
						Cell changingCell = myMovingCells.get(whichCell);
						currentCell.setFuturestate(changingCell.getCurrentstate());
						myMovingCells.remove(whichCell);
						cellsToMakeEmpty.add(changingCell);
					}
				}
			}
		}
		if(myMovingCells.size()>0){
			for(Cell c: myMovingCells){
				c.setFuturestate(c.getCurrentstate());
			}
		}
		for(Cell c: cellsToMakeEmpty){
			c.setFuturestate(EMPTY);
		}
		myMovingCells = new ArrayList<Cell>();
	}
	
	@Override
	public void updateCell(Cell myCell){
		ArrayList<Cell> currentNeighbors = getNeighbors(myCell);
		double matchingCellCount = 0.0;
		double nonEmptyCellCount = 0.0;
		for(int i = 0; i<currentNeighbors.size(); i++){
			String neighborState = currentNeighbors.get(i).getCurrentstate();
			if(!neighborState.equals(EMPTY)){
				nonEmptyCellCount++;
				if(neighborState.equals(myCell.getCurrentstate())){
					matchingCellCount++;
				}
			}		
		}
		if(nonEmptyCellCount == 0){
			myCell.setFuturestate(myCell.getCurrentstate());
		}
		else if(matchingCellCount/(nonEmptyCellCount) < myProbability){
			myMovingCells.add(myCell);
		}
		else{
			myCell.setFuturestate(myCell.getCurrentstate());
		}
	}
	
//	public void printGrid(){
//		Cell[][] myGrid = getGrid();
//		for (int i = 0; i < getNumRows(); i++) {
//			for (int j = 0; j < getNumCols(); j++) {
//				if(myGrid[i][j].getCurrentstate().equals(EMPTY)){
//					System.out.print("E");
//				}
//				else if(myGrid[i][j].getCurrentstate().equals(TYPE1)){
//					System.out.print(1);
//				}
//				else{
//					System.out.print(2);
//				}
//			}
//			System.out.println();
//		}
//		System.out.println();
//	}
//
//	public static void main(String[] args){
//		SegregationSimulation test = new SegregationSimulation(3, 3, .4);
//		//test.createGrid((1.0/3.0), .5);
//		int num = 0;
//		while(num<10){
//			test.printGrid();
//			test.updateGrid();
//			num++;
//		}
//	}

}
