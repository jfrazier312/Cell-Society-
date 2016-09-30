package model;
import java.util.ArrayList;
import java.util.Random;

import config.ConfigurationLoader;

public class GameOfLifeSimulation extends CellGrid implements view.GameWorld {
	
	public static final String SIMULATION_NAME = GAME_OF_LIFE;
	private static final String DEAD = "dead";
	private static final String ALIVE = "alive";
	private int isEven;
	
	public GameOfLifeSimulation() {
		super();
		isEven = 0;
	}
	
	public void initSimulation() {
		double percentDead = Double.parseDouble(ConfigurationLoader.getConfig().getCustomParam("percentDead"));
		//double percentDead = .5;
		createGrid(percentDead);
	}
	
	public void createGrid(double percentDead){
		Random generator = new Random();
		Cell[][] myGrid = getGrid();
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
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				// TODO: Should be able to arbitrarily change shapes
				// possible just do a getconfig.getShape()? 
				myGrid[i][j] = new Triangle(i, j, isEven++);
				if(initialization.size() == 0){
					myGrid[i][j].setCurrentstate(DEAD);
				}
				else{
					int cellChoice = generator.nextInt(initialization.size());
					myGrid[i][j].setCurrentstate(initialization.get(cellChoice));
					initialization.remove(cellChoice);
				}	
			}
		}
	}
	
	public void updateFutureStates(){
		Cell[][] myGrid = getGrid();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				//RectangleWithDiagonals currentCell = (RectangleWithDiagonals)myGrid[i][j];
				Cell currentCell = myGrid[i][j];
				updateCell(currentCell);
				//currentCell.setCurrentstate(currentCell.getFuturestate());
			}
		}
	}
	
	@Override
	public void updateGrid(){
		Cell[][] myGrid = getGrid();
		updateFutureStates();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = myGrid[i][j];
				currentCell.setCurrentstate(currentCell.getFuturestate());
			}
		}
	}
	
	//public void updateCell(RectangleWithDiagonals myCell){
	public void updateCell(Cell myCell){
		String myState = myCell.getCurrentstate();
		//ArrayList<RectangleWithDiagonals> currentNeighbors = myCell.getNeighbors(myCell, myGrid);
		//ArrayList<RectangleWithDiagonals> currentNeighbors = getNeighbors(myCell);
		ArrayList<Cell> currentNeighbors = getNeighbors(myCell);
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
	
	public int countCellsOfState(ArrayList<Cell> currentNeighbors, String state){
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
	public void printGrid(){
		Random generator = new Random();
		Cell[][] myGrid = getGrid();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				if(myGrid[i][j].getCurrentstate().equals(DEAD)){
					System.out.print(0);
				}
				else{
					System.out.print(1);
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}
	
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
