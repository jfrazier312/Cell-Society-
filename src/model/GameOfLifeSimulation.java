package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import config.Configuration;
import view.Simulations;

/**
 * @author austingartside
 *
 */
public class GameOfLifeSimulation extends CellGrid {
	
	private static final String SIMULATION_NAME = Simulations.GAME_OF_LIFE.getName();
	private String DEAD;
	private String ALIVE;
	private int isEven;
	
	private String neighborConvention;
	private ArrayList<Integer> numsToSurvive;
	private ArrayList<Integer> numsToBeBorn;
	
	private static final int[] ROW_DELTAS = {-1, -1, 0, 1, 1, 1, 0, -1};
	private static final int[] COL_DELTAS = {0, -1, -1, -1, 0, 1, 1, 1};
		
	public GameOfLifeSimulation(Configuration config) {
		super(config);
		isEven = 0;
		DEAD = myResources.getString("Dead");
		ALIVE = myResources.getString("Alive");
		neighborConvention = "B2 S23";
		getNeighborConvention();
	}
	
	public void getNeighborConvention(){
		String[] neighborConventionList = neighborConvention.split(" ");
		numsToSurvive = new ArrayList<Integer>();
		numsToBeBorn = new ArrayList<Integer>();
		if(neighborConventionList[0].length()>0){
			String numsForBirth = neighborConventionList[0].substring(1);
			for(int i = 0; i<numsForBirth.length(); i++){
				numsToBeBorn.add(Character.getNumericValue(numsForBirth.charAt(i)));
			}
		}
		if(neighborConventionList[1].length()>0){
			String numsForSurvival = neighborConventionList[1].substring(1);
			for(int i = 0; i<numsForSurvival.length(); i++){
				numsToSurvive.add(Character.getNumericValue(numsForSurvival.charAt(i)));
			}
		}
	}
	
	public void initSimulation() {
		double percentDead = Double.parseDouble(getConfig().getCustomParam("percentDead"));
		setDeltas(ROW_DELTAS, COL_DELTAS);
		createGrid(percentDead);
	}
	
	
	public void createGrid(double percentDead){
		Random generator = new Random();
		ArrayList<String> initialization = getStartingStateList(percentDead);
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				setGridCell(i, j, new RectangleWithDiagonals(i, j, getConfig()));
				if(initialization.size() == 0){
					getGridCell(i, j).setCurrentstate(DEAD);
				}
				else{
					int cellChoice = generator.nextInt(initialization.size());
					getGridCell(i, j).setCurrentstate(initialization.get(cellChoice));
					initialization.remove(cellChoice);
				}	
			}
		}
	}

	private ArrayList<String> getStartingStateList(double percentDead) {
		int size = getNumRows()*getNumCols();
		double numDead = percentDead*size;
		double numAlive = size-numDead;
		ArrayList<String> initialization = new ArrayList<String>();
		for(int i = 0; i<numDead; i++){
			initialization.add(DEAD);
		}
		for(int i = 0; i<numAlive; i++){
			initialization.add(ALIVE);
		}
		return initialization;
	}
	
	private void updateFutureStates(){
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				updateCell(getGridCell(i, j));
			}
		}
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
	
	@Override
	public void updateCell(Cell myCell){
		String myState = myCell.getCurrentstate();
		ArrayList<Cell> currentNeighbors = getNeighbors(myCell, 1);
		int liveCount = countCellsOfState(currentNeighbors, ALIVE);
		if(myState.equals(DEAD)){
			deadCellUpdate(myCell, liveCount);	
		}
		else{
			liveCellUpdate(myCell, liveCount);
		}
	}
	
	
//	private void liveCellUpdate(Cell myCell, int liveCount) {
//		if(liveCount<2){
//			myCell.setFuturestate(DEAD);
//		}
//		else if(liveCount>=2 && liveCount <=3){
//			myCell.setFuturestate(ALIVE);
//		}
//		else{
//			myCell.setFuturestate(DEAD);
//		}
//	}
	private void liveCellUpdate(Cell myCell, int liveCount){
		myCell.setFuturestate(DEAD);
		if(numsToSurvive.contains(liveCount)){
			myCell.setFuturestate(ALIVE);
		}
	}

	
	private void deadCellUpdate(Cell myCell, int liveCount){
		myCell.setFuturestate(DEAD);
		if(numsToBeBorn.contains(liveCount)){
			myCell.setFuturestate(ALIVE);
		}
	}
//	private void deadCellUpdate(Cell myCell, int liveCount) {
//		if(liveCount == 3){
//			myCell.setFuturestate(ALIVE);
//		}
//		else{
//			myCell.setFuturestate(DEAD);
//		}
//	}
	
	public int countCellsOfState(List<Cell> currentNeighbors, String state){
		int stateCount = 0;
		for(Cell neighborCell: currentNeighbors){
			if(neighborCell.getCurrentstate().equals(state)){
				stateCount++;
			}
		}
		return stateCount;
	}

	@Override
	public String getSimulationName() {
		return SIMULATION_NAME;
	}
	/**
	 * This method is used for testing purposes to print grid locally
	 */
	
	/**
	 * This method is used for testing purposes to print grid locally
	 */
//	public void printGrid(){
//		Random generator = new Random();
//		Cell[][] myGrid = getGrid();
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
	
	/**
	 * Uncomment this method to test simulation locally
	 * @param args
	 */
	/*
	public static void main(String[] args){
		GameOfLifeSimulation test = new GameOfLifeSimulation();
		int num = 0;
		while(num<10){
			test.printGrid();
			test.updateGrid();
			num++;
		}
	}
	*/
}
