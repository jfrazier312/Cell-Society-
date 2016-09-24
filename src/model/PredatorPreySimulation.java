package model;

import java.util.ArrayList;

import java.util.Random;

public class PredatorPreySimulation extends CellGrid {

	public static final String EMPTY = "EMPTY";
	public static final String FISH = "FISH";
	public static final String SHARK = "SHARK";
	private Random generator;

	public PredatorPreySimulation(int rows, int cols) {
		super(rows, cols);
		createGrid();
	}

	public void createGrid() {
		generator = new Random();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				int deadChance = generator.nextInt(3);
				if (deadChance == 0) {
					intializeState(i, j, FISH);
				} else if (deadChance == 1) {
					intializeState(i, j, SHARK);
				} else {
					intializeState(i, j, EMPTY);
				}
			}
		}
	}

	public void intializeState(int row, int col, String state) {
		Cell[][] myGrid = getGrid();
		myGrid[row][col] = new Fish(row, col, 2, 10);
		myGrid[row][col].setCurrentstate(state);
		myGrid[row][col].setFuturestate("");
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
	
	public void giveBirth(Fish myCell, String state){
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

	public void transferInformation(Fish prevCell, Fish newCell, boolean didEat) {
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
//		PredatorPreySimulation test = new PredatorPreySimulation(2,2);
//		int num = 0;
//		while(num<10){
//			test.printGrid();
//			test.updateGrid();
//			num++;
//		}
//	}
}