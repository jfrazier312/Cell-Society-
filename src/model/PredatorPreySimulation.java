package model;

import java.util.ArrayList;

import java.util.Random;

import config.ConfigurationLoader;

public class PredatorPreySimulation extends CellGrid implements view.GameWorld {

	private static final String EMPTY = "empty";
	private static final String FISH = "fish";
	// TODO: Jordan - should implement Gameworld and get constant for name
	public static final String SIMULATION_NAME = WATOR_WORLD;
	private static final String SHARK = "shark";
	private Random generator;

	public PredatorPreySimulation() {
		super();
	}
	
	public void initSimulation() {
		double percentEmptyCells = Double.parseDouble(ConfigurationLoader.getConfig().getCustomParam("percentEmpty"));
		double percentShark = Double.parseDouble(ConfigurationLoader.getConfig().getCustomParam("percentShark"));

		createGrid(percentEmptyCells, percentShark);
	}

	public void createGrid(double percentEmptyCells, double percentShark) {
		int reproductionTime = Integer.parseInt(ConfigurationLoader.getConfig().getAllStates()
				.getStateByName(SHARK).getAttributes().get("reproductionTime"));
		int timeToDeath = Integer.parseInt(ConfigurationLoader.getConfig().getAllStates()
				.getStateByName(SHARK).getAttributes().get("lifeTime"));
		Cell[][] myGrid = getGrid();
		generator = new Random();
		int size = getNumRows()*getNumCols();
		double numEmpty = percentEmptyCells*size;
		double numShark = percentShark*(size-numEmpty);
		double numFish = size-numEmpty-numShark;
		ArrayList<String> initialization = new ArrayList<String>();
		for(int i = 0; i<numEmpty; i++){
			initialization.add(EMPTY);
		}
		for(int i = 0; i<numShark; i++){
			initialization.add(SHARK);
		}
		for(int i = 0; i<numFish; i++){
			initialization.add(FISH);
		}
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				myGrid[i][j] = new Fish(i, j, reproductionTime, timeToDeath);
				if(initialization.size() == 0){
					myGrid[i][j].setCurrentstate(EMPTY);
				}
				else{
					int cellChoice = generator.nextInt(initialization.size());
					myGrid[i][j].setCurrentstate(initialization.get(cellChoice));
					initialization.remove(cellChoice);
				}
				myGrid[i][j].setFuturestate("");
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
				if(currentCell.getFuturestate().equals("")){
					currentCell.setFuturestate(EMPTY);
				}
				currentCell.setCurrentstate(currentCell.getFuturestate());
				currentCell.setFuturestate("");
			}
		}
	}
	
	public void updateFutureStates() {
		Cell[][] myGrid = getGrid();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = myGrid[i][j];
				if(currentCell.getCurrentstate().equals(SHARK)){
					updateCell(currentCell);
				}
			}
		}	
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = myGrid[i][j];
				if(!currentCell.getCurrentstate().equals(SHARK)){
					updateCell(currentCell);
				}
			}
		}
	}
	

	@Override
	public void updateCell(Cell myCell) {
		if(myCell.getFuturestate().equals("") && !myCell.getCurrentstate().equals(EMPTY)){
			moveCell(myCell);
		}
	}

	private void moveCell(Cell myCreature) {
		String state = myCreature.getCurrentstate();
		ArrayList<Cell> myFishFriends = getFishNeighbors(myCreature);
		if(state.equals(SHARK)){
			if (myFishFriends.size() > 0) {
				Cell newSharkCell = getNewCell(myFishFriends);
				//boolean to decide whether or not the shark ate
				transferInformation((Fish) myCreature, (Fish) newSharkCell, true);
				if(((Fish) myCreature).getReproductionTime()<=0){
					giveBirth((Fish) myCreature, SHARK);
				}
				return;
			}
			//kill shark if it runs out of life
			if(((Fish) myCreature).getTimeToDeath() == 1){
				myCreature.setFuturestate(EMPTY);
				return;
			}
		}
		ArrayList<Cell> availableCells = getAvailableCells(myCreature);
		if (availableCells.size() == 0) {
			noMove((Fish) myCreature);
			return;
		}
		Cell newCreatureCell = getNewCell(availableCells);
		//fish moving to same cell as a shark and getting eaten
		if(newCreatureCell.getFuturestate().equals(SHARK)){
			((Fish) newCreatureCell).increaseTimeToDeath();
			if(((Fish) myCreature).getReproductionTime()<=0){
				giveBirth((Fish) myCreature, FISH);
			}
			return;
		}
		transferInformation((Fish) myCreature, (Fish) newCreatureCell, false);
		if(((Fish) myCreature).getReproductionTime()<=0){
			if(state.equals(SHARK)){
				giveBirth((Fish) myCreature, SHARK);
				((Fish)newCreatureCell).resetReproductionTime();
			}
			else{
				giveBirth((Fish) myCreature, FISH);
				((Fish)newCreatureCell).resetReproductionTime();
			}		
		}
	}
	
	private void giveBirth(Fish myCell, String state){
		 myCell.setFuturestate(state); 
		 myCell.resetReproductionTime();
		 myCell.resetTimeToDeath(); 
	}

	private Cell getNewCell(ArrayList<Cell> availableCells) {
		int cellChoice = generator.nextInt(availableCells.size());
		Cell newCell = availableCells.get(cellChoice);
		return newCell;
	}

	private void noMove(Fish myCreature) {
		myCreature.setFuturestate(myCreature.getCurrentstate());
		myCreature.decrementReproductionTime();
		if (myCreature.getCurrentstate().equals(SHARK)) {
			myCreature.decrementTimeToDeath();
		}
	}

	private ArrayList<Cell> getAvailableCells(Cell myCreature) {
		ArrayList<Cell> neighbors = getNeighbors(myCreature);
		ArrayList<Cell> availableCells = new ArrayList<Cell>();
		for (Cell neighbor : neighbors) {
			if (neighbor.getCurrentstate().equals(EMPTY) &&
					!neighbor.getFuturestate().equals(myCreature.getCurrentstate())){
				availableCells.add(neighbor);
			}
		}
		return availableCells;
	}

	private void transferInformation(Fish prevCell, Fish newCell, boolean didEat) {
		newCell.setReproductionTime(prevCell.getReproductionTime()-1);
		newCell.setMaxReproductionTime(prevCell.getMaxReproductionTime());
		if(prevCell.getCurrentstate().equals(SHARK)){
			if(didEat){
				prevCell.increaseTimeToDeath();
				newCell.setTimeToDeath(prevCell.getTimeToDeath());
			}
			else{
				newCell.setTimeToDeath(prevCell.getTimeToDeath()-1);
			}
		}
		else{
			newCell.setTimeToDeath(prevCell.getTimeToDeath());
		}
		newCell.setFuturestate(prevCell.getCurrentstate());
	}

	private ArrayList<Cell> getFishNeighbors(Cell myShark) {
		ArrayList<Cell> neighbors = getNeighbors(myShark);
		ArrayList<Cell> myFishFriends = new ArrayList<Cell>();
		for (Cell neighbor : neighbors) {
			if (neighbor.getCurrentstate().equals(FISH) && neighbor.getFuturestate().equals("")) {
				myFishFriends.add(neighbor);
			}
		}
		return myFishFriends;
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
//				else if(myGrid[i][j].getCurrentstate().equals(FISH)){
//					System.out.print("F");
//				}
//				else{
//					System.out.print("S");
//				}
//			}
//			System.out.println();
//		}
//		System.out.println();
//	}
//	
//	public static void main(String[] args){
//		PredatorPreySimulation test = new PredatorPreySimulation();
//		int num = 0;
//		while(num<10){
//			test.printGrid();
//			test.updateGrid();
//			num++;
//		}
//	}
}