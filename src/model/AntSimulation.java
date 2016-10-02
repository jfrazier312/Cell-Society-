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

//set row deltas in each simulation based on what shape is chosen
//define all the row deltas in cell grid
public class AntSimulation extends CellGrid{

	private static final String SIMULATION_NAME = Simulations.ANT.getName();
	private static final String OPEN = "open";
	private static final String OBSTACLE = "obstacle";
	private static final int SOURCE_PHEROMONES =  1000;
	private static final String HOME_SOURCE = "home";
	private static final String FOOD_SOURCE = "food";
	private static final int VISION = 1;
	
	private static final String HOME_ANT = "home_ant";
	private static final String FOOD_ANT = "food_ant";	
	
	//have them set these
//	private int foodSourceRow;
//	private int foodSourceCol;
//	private int homeSourceRow;
//	private int homeSourceCol;
	
	private int pheromoneConstant;
	private int pheromoneLoss;
	private Random generator;
	
	public AntSimulation(Configuration config) {
		super(config);
		pheromoneConstant = SOURCE_PHEROMONES/((config.getNumRows()+config.getNumCols())/2);
		pheromoneLoss = pheromoneConstant/5;
	}
	
	public void createGrid(){
		generator = new Random();
		double percentObstacle = .3;
		List<String> myStates = getStartingStates(percentObstacle);
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				//have the xml set these
				if(i == 1 && j == 1){
					//homeSourceRow = i;
					//homeSourceCol = j;
					setGridCell(i, j, new AntCell(i, j, SOURCE_PHEROMONES, 0, SOURCE_PHEROMONES, 0, getConfig()));
					getGridCell(i, j).setCurrentstate(HOME_SOURCE);
					//getGridCell(i, j).setFuturestate(HOME_SOURCE);
				}
				//have the xml set these
				else if(i == getNumRows()-2 && j == getNumRows()-2){
					//foodSourceRow = i;
					//foodSourceCol = j;
					setGridCell(i, j, new AntCell(i, j, 0, SOURCE_PHEROMONES, 0, SOURCE_PHEROMONES, getConfig()));
					getGridCell(i, j).setCurrentstate(FOOD_SOURCE);
					//getGridCell(i, j).setFuturestate(FOOD_SOURCE);
				}
				else{
					int typeChoice = generator.nextInt(myStates.size());
					String type = myStates.get(typeChoice);
					myStates.remove(typeChoice);
					createRandomCell(i, j, type);
				}
			}
		}
	}
	
	private List<String> getStartingStates(double percentObstacles) {
		int size = getNumRows()*getNumCols();
		double numObstacles = size*percentObstacles;
		int numNonObstacles = (int) (size-numObstacles);
		List<String> obstacleOrNot = new ArrayList<String>();
		for(int i = 0; i<numNonObstacles-1; i++){
			obstacleOrNot.add(OBSTACLE);
		}
		for(int i = 0; i<numObstacles-1; i++){
			obstacleOrNot.add(OPEN);
		}
		return obstacleOrNot;
	}
	
	public void createRandomCell(int row, int col, String state){
		if(state.equals(OBSTACLE)){
			setGridCell(row, col, new AntCell(row, col, -1, -1, -1, -1, getConfig()));
			getGridCell(row, col).setCurrentstate(OBSTACLE);
			//getGridCell(row, col).setFuturestate(OBSTACLE);
		}
		else{
			setGridCell(row, col, new AntCell(row, col, 10, 10, 0, 0, getConfig()));
			getGridCell(row, col).setCurrentstate(OPEN);
			//getGridCell(row, col).setFuturestate(OPEN);
		}		
	}

	@Override
	public void updateGrid() {
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				updateCell(getGridCell(i, j));
			}
		}
		
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				updatePheromones((AntCell)(getGridCell(i, j)));
			}
		}	
	}
	
	@Override
	public void initSimulation() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void updateCell(Cell myCell) {
		if(!myCell.getCurrentstate().equals(OBSTACLE)){
			List<Ant> antList = ((AntCell) myCell).getAnts();
			for(Ant myAnt: antList){
				antForage(myAnt);
			}
		}
	}
	
	private void updatePheromones(AntCell myCell){
		if(myCell.gainedHomeAnts()){
			myCell.addHomePheromones();
		}
		if(myCell.gainedFoodAnts()){
			myCell.addFoodPheromones();
		}
		myCell.resetAntGain();
	}
	
	private void antForage(Ant myAnt){
		if(myAnt.getType().equals(FOOD_ANT)){
			antMove(myAnt, true);
		}
		else{
			antMove(myAnt, false);
		}
	}
	
	//for both food and home to reduce code
	private void antMove(Ant myAnt, boolean isFood){
		Cell myCurrentCell = getGridCell(myAnt.getRow(), myAnt.getCol());
		Cell bestNeighbor = getMaxPheromoneNeighbor(getNeighbors(myCurrentCell, VISION), isFood);
		ArrayList<Cell> neighbors = getNeighbors(myCurrentCell, VISION);
		List<Cell> forwardCells = myAnt.getForwardCells(neighbors, bestNeighbor);
		List<Cell> availableCells = getOpenCells(forwardCells);
		if(availableCells.size()==0){
			List<Cell> nonForwardCells = myAnt.getNonForwardCells(neighbors, bestNeighbor);
			availableCells = getOpenCells(nonForwardCells);
			if(availableCells.size()==0){				
				Cell newCell = getMaxPheromoneNeighbor(availableCells, isFood);
				moveAnt(myAnt, (AntCell) myCurrentCell, (AntCell) newCell, isFood);
			}
		}
		else{
			Cell newCell = getMaxPheromoneNeighbor(availableCells, isFood);
			moveAnt(myAnt, (AntCell) myCurrentCell, (AntCell) newCell, isFood);
		}
	}
	
	private void moveAnt(Ant movingAnt, AntCell currentCell, AntCell newCell, boolean isFood){
		newCell.addAnt(movingAnt);
		if(isFood){
			newCell.foodAntGain();
		}
		else{
			newCell.homeAntGain();
		}
		currentCell.removeAnt(movingAnt);
		movingAnt.setRow(newCell.getRowPos());
		movingAnt.setCol(newCell.getColPos());
		//when do I want to update the pheromones
		if(newCell.getCurrentstate().equals(HOME_SOURCE)){
			movingAnt.setType(FOOD_ANT);
		}
		if(newCell.getCurrentstate().equals(FOOD_SOURCE)){
			movingAnt.setType(HOME_ANT);
		}
	}
	
	private List<Cell> getOpenCells(List<Cell> neighbors){
		ArrayList<Cell> availableCells = new ArrayList<>();
		for(Cell neighbor: neighbors){
			if(!((AntCell) neighbor).isFull() || !neighbor.getCurrentstate().equals(OBSTACLE)){
				availableCells.add(neighbor);
			}
		}
		return availableCells;
	}
	
	private Cell getMaxPheromoneNeighbor(List<Cell> neighbors, boolean isFoodNeighbor){
		int highestPheromones = 0;
		int tempPheromones;
		Cell cellWithHighest = neighbors.get(0);
		for(Cell neighbor: neighbors){
			if(isFoodNeighbor){
				tempPheromones = ((AntCell)neighbors.get(0)).getFoodPheromones();
			}
			else{
				tempPheromones = ((AntCell)neighbors.get(0)).getHomePheromones();
			}
			if(tempPheromones>highestPheromones){
				highestPheromones = tempPheromones;
				cellWithHighest = neighbor;
			}
		}
		return cellWithHighest;	
	}

	@Override
	public String getSimulationName() {
		return SIMULATION_NAME;
	}
	
//	public void orientAnt(Ant myAnt, boolean isHome){
//		double rowDiff;
//		double colDiff;
//		if(isHome){
//			rowDiff = 1.0*myAnt.getRow() - homeSourceRow;
//			colDiff = 1.0*myAnt.getCol() - homeSourceCol;
//		}
//		else{
//			rowDiff = 1.0*myAnt.getRow() - foodSourceRow;
//			colDiff = 1.0*myAnt.getCol() - foodSourceCol;
//		}
//		myAnt.setRowOrientation((int)(-1*Math.signum(colDiff)));
//		myAnt.setColOrientation((int)(-1*Math.signum(rowDiff)));	
//	}

}
