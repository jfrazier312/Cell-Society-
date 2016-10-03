package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import config.Configuration;
import config.State;
import view.Simulations;
/**
 * @author austingartside
 *
 */

//set row deltas in each simulation based on what shape is chosen
//define all the row deltas in cell grid
public class AntSimulation extends CellGrid {

	private static final String SIMULATION_NAME = Simulations.ANT.getName();
	private static final String OPEN = "open";
	private static final String OBSTACLE = "obstacle";
	private static final int SOURCE_PHEROMONES =  1000;
	private static final String HOME_SOURCE = "home";
	private static final String FOOD_SOURCE = "food";
	private static final int VISION = 1;
	
	private static final int[] ROW_DELTAS = {-1, -1, 0, 1, 1, 1, 0, -1};
	private static final int[] COL_DELTAS = {0, -1, -1, -1, 0, 1, 1, 1};
	
	private static final String HOME_ANT = "home_ant";
	private static final String FOOD_ANT = "food_ant";	
	
	private int pheromoneConstant;
	private int pheromoneLoss;
	private int foodRow;
	private int foodCol;
	private int homeRow;
	private int homeCol;
	private List<Ant> antsToRemove;
	private Random generator;
	
	public AntSimulation(Configuration config) {
		super(config);
		pheromoneConstant = SOURCE_PHEROMONES/getNumRows();
		pheromoneLoss = pheromoneConstant/5;
	}
	
	public void getFoodSourceLocation(){
		for (State s : getConfig().getInitialCells()) {
			foodRow = Integer.parseInt(s.getAttributes().get("row"));
			foodCol = Integer.parseInt(s.getAttributes().get("col"));
		}
	}
	
	@Override
	public void initSimulation(){
		super.initSimulation();
		setDeltas(ROW_DELTAS, COL_DELTAS);
		getFoodSourceLocation();
		//should get this from config
		double percentObstacles = Double.parseDouble(getConfig().getCustomParam("percentObstacles"));;
		createGrid(percentObstacles);
	}
	
	public void createGrid(double percentObstacles){
		generator = new Random();
		List<String> myStates = getStartingStates(percentObstacles);
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				makeStartingCells(myStates, i, j);
			}
		}
		initMaxFoodPheromones();
		initMaxHomePheromones();
		initAllFoodPheromones();
		initAllHomePheromones();
	}

	private void makeStartingCells(List<String> myStates, int i, int j) {
		if(i == foodRow && j == foodCol){
			setGridCell(i, j, new AntCell(i, j, SOURCE_PHEROMONES, 0, SOURCE_PHEROMONES, 0));
			getGridCell(i, j).setCurrentstate(HOME_SOURCE);
		}
		//have the xml set these
		else if(i == getNumRows()-2 && j == getNumRows()-2){
			homeRow = i;
			homeCol = j;
			setGridCell(i, j, new AntCell(i, j, 0, SOURCE_PHEROMONES, 0, SOURCE_PHEROMONES));
			getGridCell(i, j).setCurrentstate(FOOD_SOURCE);
		}
		else{
			int typeChoice = generator.nextInt(myStates.size());
			String type = myStates.get(typeChoice);
			myStates.remove(typeChoice);
			createRandomCell(i, j, type);
		}
	}
	
	public void createRandomCell(int row, int col, String state){
		if(state.equals(OBSTACLE)){
			setGridCell(row, col, new AntCell(row, col, -1, -1, -1, -1));
			getGridCell(row, col).setCurrentstate(OBSTACLE);
		}
		else{
			setGridCell(row, col, new AntCell(row, col, -1, -1, -1, -1));
			getGridCell(row, col).setCurrentstate(OPEN);
		}		
	}
	
	public void initMaxHomePheromones(){
		Cell home = getGridCell(homeRow, homeCol);
		List<Cell> homeNeighbors = getNeighbors(home, VISION);
		for(Cell neighbor: homeNeighbors){
			((AntCell)neighbor).setMaxHomePheromones(SOURCE_PHEROMONES - pheromoneConstant);
			//System.out.println(((AntCell)neighbor).getMaxHomePheromones());
		}
	}
	
	public void initMaxFoodPheromones(){
		Cell foodSource = getGridCell(foodRow, foodCol);
		List<Cell> homeNeighbors = getNeighbors(foodSource, VISION);
		for(Cell neighbor: homeNeighbors){
			((AntCell)neighbor).setMaxFoodPheromones(SOURCE_PHEROMONES - pheromoneConstant);
			//((AntCell)neighbor).setMaxFoodPheromones(SOURCE_PHEROMONES - pheromoneConstant);
			//System.out.println(((AntCell)neighbor).getMaxFoodPheromones());
		}
	}
	
	public void initAllFoodPheromones(){
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				AntCell currentCell = (AntCell) getGridCell(i, j);
				List<Cell> neighbors = getNeighbors(currentCell, VISION);
				if(!currentCell.getCurrentstate().equals(OBSTACLE)){
					if(currentCell.getMaxFoodPheromones() < 0){
						AntCell highestNeighbor = (AntCell) getMaxPheromoneNeighbor(neighbors, true);
						currentCell.setMaxFoodPheromones(highestNeighbor.getMaxFoodPheromones() - pheromoneConstant);
					}
					putAntsInCell(i, j, currentCell);
					//System.out.println(currentCell.getMaxFoodPheromones());
				}
			}
		}
	}
	
	public void initAllHomePheromones(){
		for (int i = getNumRows()-1; i >= 0; i--) {
			for (int j = getNumCols()-1; j >= 0; j--) {
				AntCell currentCell = (AntCell) getGridCell(i, j);
				List<Cell> neighbors = getNeighbors(currentCell, VISION);
				if(!currentCell.getCurrentstate().equals(OBSTACLE)){
					if(currentCell.getMaxHomePheromones() < 0){
						AntCell highestNeighbor = (AntCell) getMaxPheromoneNeighbor(neighbors, false);
						currentCell.setMaxHomePheromones(highestNeighbor.getMaxHomePheromones() - pheromoneConstant);
					}
					//putAntsInCell(i, j, currentCell);
					System.out.println(currentCell.getMaxHomePheromones());
				}
			}
		}
	}
	
	public void putAntsInCell(int row, int col, AntCell myCell){
		int numToAdd = generator.nextInt(3);
		int numAdded = 0;
		while(numAdded<numToAdd){
			int antTypeChoice = generator.nextInt(2);
			Ant antToAdd;
			if(antTypeChoice == 0){
				antToAdd = new Ant(row, col, FOOD_ANT);
			}
			else{
				antToAdd = new Ant(row, col, HOME_ANT);
			}
			myCell.addAnt(antToAdd);
			numAdded++;
		}
	}
	
	private List<String> getStartingStates(double percentObstacles) {
		int size = getNumRows()*getNumCols();
		double numObstacles = size*percentObstacles;
		int numNonObstacles = (int) (size-numObstacles);
		List<String> obstacleOrNot = new ArrayList<String>();
		for(int i = 0; i<numObstacles-1; i++){
			obstacleOrNot.add(OBSTACLE);
		}
		for(int i = 0; i<numNonObstacles-1; i++){
			obstacleOrNot.add(OPEN);
		}
		return obstacleOrNot;
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
	public void updateCell(Cell myCell) {
		if(!myCell.getCurrentstate().equals(OBSTACLE)){
			List<Ant> antList = ((AntCell) myCell).getAnts();
			antsToRemove = new ArrayList<Ant>();
			for(Ant myAnt: antList){
				antForage(myAnt);
			}
			for(Ant myAnt: antsToRemove){
				antList.remove(myAnt);
			}
		}
	}
	
	private void updatePheromones(AntCell myCell){
		if(myCell.gainedHomeAnts()){
			myCell.addHomePheromones();
		}
		else{
			myCell.loseHomePheromones(pheromoneLoss);
		}
		if(myCell.gainedFoodAnts()){
			myCell.addFoodPheromones();
		}
		else{
			myCell.loseFoodPheromones(pheromoneLoss);
		}
		myCell.resetAntGain();
	}
	
	private void antForage(Ant myAnt){
		if(myAnt.getType().equals(FOOD_ANT)){
			getAntMove(myAnt, true);
		}
		else{
			getAntMove(myAnt, false);
		}
	}
	
	//for both food and home to reduce code
	private void getAntMove(Ant myAnt, boolean isFood){
		Cell myCurrentCell = getGridCell(myAnt.getRow(), myAnt.getCol());
		Cell bestNeighbor = getMaxPheromoneNeighbor(getNeighbors(myCurrentCell, VISION), isFood);
		List<Cell> neighbors = getNeighbors(myCurrentCell, VISION);
		List<Cell> forwardCells = myAnt.getForwardCells(neighbors, bestNeighbor);
		List<Cell> availableCells = getOpenCells(forwardCells);
		if(availableCells.size()==0){
			List<Cell> nonForwardCells = myAnt.getNonForwardCells(neighbors, bestNeighbor);
			availableCells = getOpenCells(nonForwardCells);
			if(availableCells.size()!=0){				
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
		//currentCell.removeAnt(movingAnt);
		antsToRemove.add(movingAnt);
		movingAnt.setRow(newCell.getRowPos());
		movingAnt.setCol(newCell.getColPos());
		if(newCell.getCurrentstate().equals(HOME_SOURCE)){
			movingAnt.setType(FOOD_ANT);
		}
		if(newCell.getCurrentstate().equals(FOOD_SOURCE)){
			movingAnt.setType(HOME_ANT);
		}
	}
	
	private List<Cell> getOpenCells(List<Cell> neighbors){
		List<Cell> availableCells = new ArrayList<>();
		for(Cell neighbor: neighbors){
			if(!((AntCell) neighbor).isFull() && !neighbor.getCurrentstate().equals(OBSTACLE)){
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
	
//	public void printGrid(){
//	for (int i = 0; i < getNumRows(); i++) {
//		for (int j = 0; j < getNumCols(); j++) {
//			System.out.print(((SugarAgent)getGridCell(i,j)).getSugar() + " ");
//		}
//		System.out.println();
//	}
//	System.out.println();
//	
//	for (int i = 0; i < getNumRows(); i++) {
//		for (int j = 0; j < getNumCols(); j++) {
//			System.out.print(((SugarAgent)getGridCell(i,j)).getPatch().getSugar() + " ");
//		}
//		System.out.println();
//	}
//	
//	for (int i = 0; i < getNumRows(); i++) {
//		for (int j = 0; j < getNumCols(); j++) {
//			if(getGridCell(i, j).getCurrentstate().equals(AGENT)){
//				System.out.print("A");
//			}
//			else{
//				System.out.print("E");
//			}
//		}
//		System.out.println();
//	}
//	//System.out.println();
//}
//
//public static void main(String[] args){
//	SugarSimulation austin = new SugarSimulation(2, 2);
//	austin.createGrid(.3);
//	austin.printGrid();
//	System.out.println();
//	austin.updateGrid();
//	austin.printGrid();
//}

}
