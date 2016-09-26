package model;
import java.util.ArrayList;

import java.util.Random;

import config.ConfigurationLoader;

/*
 * Should I be setting the future state to be the same as the current state, 
 * or will the front end do that by default?
 * 
 */ 

public class FireSimulation extends CellGrid implements view.GameWorld {
	public static final String SIMULATION_NAME = FIRE_SIMULATION ;
	private static final String EMPTY = "empty";
	private static final String TREE = "tree";
	private static final String BURNING = "burning";
	private double probOfBurning;
	Random generator;

	public FireSimulation() {
		super();

	}
	
	public void initSimulation() {
		createGrid();
//		probOfBurning = Double.parseDouble(ConfigurationLoader.getConfig().getCustomParam("probability"));
		//probOfBurning = .5;
	}
	
	public void createGrid() {
		Cell[][] myGrid = getGrid();
		generator = new Random();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				myGrid[i][j] = new RectangleNoDiagonals(i, j);
				if(i==0 || j==0 || i==getNumRows()-1 || j == getNumCols()-1){
					myGrid[i][j].setCurrentstate(EMPTY);
				}
				else if(i == getNumRows()/2 && j == getNumCols()/2){
					myGrid[i][j].setCurrentstate(BURNING);
				}
				else{
					myGrid[i][j].setCurrentstate(TREE);
				}
			}
		}
	}
	
	@Override
	public void updateGrid() {
		probOfBurning = Double.parseDouble(ConfigurationLoader.getConfig().getCustomParam("probability"));
		Cell[][] myGrid = getGrid();
		updateFutureStates();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = myGrid[i][j];
				currentCell.setCurrentstate(currentCell.getFuturestate());
			}
		}
	}
	
	private void updateFutureStates(){
		Cell[][] myGrid = getGrid();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				updateCell(myGrid[i][j]);
			}
		}
	}
	
	@Override
	public void updateCell(Cell myCell){
		String myState = myCell.getCurrentstate();
		ArrayList<Cell> currentNeighbors = getNeighbors(myCell);
		if(myState.equals(BURNING)){
			myCell.setFuturestate(EMPTY);
		}
		else if(myState.equals(TREE)){
			for(Cell neighbor: currentNeighbors){
				if(neighbor.getCurrentstate() == BURNING){
					int seeIfBurn = generator.nextInt(100);
					if(seeIfBurn<(probOfBurning*100)){
						myCell.setFuturestate(BURNING);
						return;
					}
				}
			}
			myCell.setFuturestate(TREE);
		}
		else{
			myCell.setFuturestate(EMPTY);
		}
		
	}

	@Override
	public String getSimulationName() {
		return SIMULATION_NAME;
	}
	
//	public void printGrid(){
//		Random generator = new Random();
//		Cell[][] myGrid = getGrid();
//		for (int i = 0; i < getNumRows(); i++) {
//			for (int j = 0; j < getNumCols(); j++) {
//				if(myGrid[i][j].getCurrentstate().equals(EMPTY)){
//					System.out.print("E");
//				}
//				else if(myGrid[i][j].getCurrentstate().equals(BURNING)){
//					System.out.print("B");
//				}
//				else{
//					System.out.print("T");
//				}
//			}
//			System.out.println();
//		}
//		System.out.println();
//	}
//	
//	public static void main(String[] args){
//		FireSimulation test = new FireSimulation();
//		int num = 0;
//		while(num<10){
//			test.printGrid();
//			test.updateGrid();
//			num++;
//		}
//	}
}
