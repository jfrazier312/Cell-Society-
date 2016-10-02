package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import config.Configuration;
import view.Simulations;

/**
 * @author austingartside
 *
 */
public class SegregationSimulation extends CellGrid {
	
	private static final String SIMULATION_NAME = Simulations.SEGREGATION.getName();
	public static final int VISION = 1;
	private double myProbability;
	
	private static final int[] ROW_DELTAS = {-1, -1, 0, 1, 1, 1, 0, -1};
	private static final int[] COL_DELTAS = {0, -1, -1, -1, 0, 1, 1, 1};
	
	private String EMPTY;
	private String TYPEA;
	private String TYPEB;

	List<Cell> myMovingCells;
	List<Cell> cellsToMakeEmpty;
	List<Cell> emptyCells;
	Random generator;
	
	public SegregationSimulation(Configuration config) {
		super(config);
		EMPTY = myResources.getString("Empty");
		TYPEA = myResources.getString("TypeA");
		TYPEB = myResources.getString("TypeB");
	}
	
	public void initSimulation() {
		setDeltas(ROW_DELTAS, COL_DELTAS);
		myProbability  = Double.parseDouble(getConfig().getCustomParam("probability"));
		myMovingCells = new ArrayList<Cell>();
		double percentEmptyCells = Double.parseDouble(getConfig().getCustomParam("percentEmpty"));
		double percenttypeA = Double.parseDouble(getConfig().getCustomParam("percentTypeA"));
		createGrid(percentEmptyCells, percenttypeA);
	}
	
	private void createGrid(double percentEmpty, double percenttypeA) {
		generator = new Random();
		int size = getNumRows()*getNumCols();
		List<String> initialization = getStartingStateList(percentEmpty, percenttypeA, size);
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				createCell(initialization, i, j);
			}
		}
	}

	private void createCell(List<String> initialization, int i, int j) {
		setGridCell(i, j, new RectangleWithDiagonals(i, j, getConfig()));
		if(initialization.size() == 0){
			getGridCell(i, j).setCurrentstate(EMPTY);
		}
		else{
			int cellChoice = generator.nextInt(initialization.size());
			getGridCell(i, j).setCurrentstate(initialization.get(cellChoice));
			initialization.remove(cellChoice);
		}
	}

	private List<String> getStartingStateList(double percentEmpty, double percenttypeA, int size) {
		double numEmpty = percentEmpty*size;
		double numtypeA = percenttypeA*(size-numEmpty);
		double numtypeB = size-numEmpty-numtypeA;
		List<String> initialization = new ArrayList<String>();
		for(int i = 0; i<numEmpty; i++){
			initialization.add(EMPTY);
		}
		for(int i = 0; i<numtypeA; i++){
			initialization.add(TYPEA);
		}
		for(int i = 0; i<numtypeB; i++){
			initialization.add(TYPEB);
		}
		return initialization;
	}
	
	@Override
	public void updateGrid(){
		updateFutureStates();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = getGridCell(i, j);
				currentCell.setCurrentstate(currentCell.getFuturestate());
				
			}
		}
	}
	
	private void updateFutureStates(){
		changeCells();	
		moveNonEmptyCells();
		if(myMovingCells.size()>0){
			for(Cell tempCell: myMovingCells){
				tempCell.setFuturestate(tempCell.getCurrentstate());
			}
		}
		if(emptyCells.size()>0){
			for(Cell leftoverEmptyCell: emptyCells){
				leftoverEmptyCell.setFuturestate(EMPTY);
			}
		}
		for(Cell c: cellsToMakeEmpty){
			c.setFuturestate(EMPTY);
		}
		myMovingCells = new ArrayList<Cell>();
	}

	private void changeCells() {
		emptyCells = new ArrayList<Cell>();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = getGridCell(i, j);
				if(!currentCell.getCurrentstate().equals(EMPTY)){
					updateCell(currentCell);
				}
				else{
					emptyCells.add(currentCell);
				}
			}
		}
	}

	private void moveNonEmptyCells() {
		cellsToMakeEmpty = new ArrayList<Cell>();
		while(myMovingCells.size()>0 && emptyCells.size()>0){
			int emptyCellChoice = generator.nextInt(emptyCells.size());
			Cell currentCell = emptyCells.get(emptyCellChoice);
			currentCell.setFuturestate(EMPTY);
			
			int whichCell = generator.nextInt(myMovingCells.size());
			Cell changingCell = myMovingCells.get(whichCell);
			currentCell.setFuturestate(changingCell.getCurrentstate());
			
			myMovingCells.remove(whichCell);
			emptyCells.remove(emptyCellChoice);
			cellsToMakeEmpty.add(changingCell);			
		}
	}
	
	@Override
	public void updateCell(Cell myCell){
		List<Cell> currentNeighbors = getNeighbors(myCell, VISION);
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
		changeState(myCell, matchingCellCount, nonEmptyCellCount);
	}

	private void changeState(Cell myCell, double matchingCellCount, double nonEmptyCellCount) {
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
	
	public String getSimulationName() {
		return SIMULATION_NAME;
	}
	
//	public void printGrid(){
//		Cell[][] myGrid = getGrid();
//		for (int i = 0; i < getNumRows(); i++) {
//			for (int j = 0; j < getNumCols(); j++) {
//				if(myGrid[i][j].getCurrentstate().equals(EMPTY)){
//					System.out.print("E");
//				}
//				else if(myGrid[i][j].getCurrentstate().equals(typeA)){
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
//		SegregationSimulation test = new SegregationSimulation();
//		int num = 0;
//		while(num<10){
//			test.printGrid();
//			test.updateGrid();
//			num++;
//		}
//	}

}
