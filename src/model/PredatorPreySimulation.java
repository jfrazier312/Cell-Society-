package model;

import java.util.ArrayList;

import java.util.Random;

public class PredatorPreySimulation extends CellGrid {

	public static final String EMPTY = "EMPTY";
	public static final String FISH = "FISH";
	public static final String SHARK = "SHARK";
	private int energyIncrease = 10;
	private Random generator;

	// RectangleNoDiagonals[][] myGrid;

	// instead of changing the state, do I want to physically make a copy of my
	// cell?
	// because needs to retain reproduction time and time to birth etc.

	@Override
	public void updateGrid() {
		// TODO Auto-generated method stub

	}

	public PredatorPreySimulation(int rows, int cols) {
		super(rows, cols);
		// super();
		// myGrid = new RectangleNoDiagonals[rows][cols];
	}

	// public int getNumRows() {
	// return myGrid.length;
	// }
	//
	// public int getNumCols() {
	// return myGrid[0].length;
	// }

	public void createGrid() {
		Random generator = new Random();
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
		myGrid[row][col] = new RectangleNoDiagonals(row, col);
		myGrid[row][col].setCurrentstate(state);
		myGrid[row][col].setFuturestate("");
	}

	@Override
	public void updateCell(Cell myCell) {
		if (myCell.getCurrentstate().equals(SHARK)) {

		}
	}

	private void moveShark(Cell myShark) {
		ArrayList<Cell> myFishFriends = getFishNeighbors(myShark);
		if (myFishFriends.size() > 0) {
			Cell newSharkCell = getNewCell(myFishFriends);
			;
			transferInformation((Fish) myShark, (Fish) newSharkCell);
		} else {
			ArrayList<Cell> availableCells = getAvailableCells(myShark);
			if (availableCells.size() == 0) {
				noMove((Fish) myShark);
				return;
			}
			Cell newSharkCell = getNewCell(availableCells);
			transferInformation((Fish) myShark, (Fish) newSharkCell);
		}
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
			if (neighbor.getCurrentstate().equals("")
					&& !neighbor.getFuturestate().equals(myCreature.getCurrentstate())) {
				availableCells.add(neighbor);
			}
		}
		return availableCells;
	}

	public void transferInformation(Fish prevCell, Fish newCell) {
		newCell.setReproductionTime(prevCell.getReproductionTime());
		newCell.setMaxReproductionTime(prevCell.getMaxReproductionTime());
		newCell.setTimeToDeath(prevCell.getTimeToDeath());
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

	/*
	 * public void updateCell(Fish myCell){ generator = new Random(); String
	 * state = myCell.getCurrentstate(); ArrayList<RectangleNoDiagonals>
	 * neighbors = myCell.getNeighbors(myCell, myGrid); if(state == SHARK){
	 * sharkMove(neighbors, myCell); } }
	 * 
	 * public void fishMove(ArrayList<RectangleNoDiagonals> myNeighbors, Fish
	 * myCell){ int numEmptyNeighbors = countEmpty(myNeighbors); int direction;
	 * if(numEmptyNeighbors == 0 && myCell.getFuturestate() != "NULL"){
	 * myCell.setFuturestate(FISH); } else{ ArrayList<Integer> myFriends =
	 * getNonEmptyNeighbors(myNeighbors, SHARK); direction =
	 * generator.nextInt(myFriends.size());
	 * //myCell.decrementReproductionTime(); //direction =
	 * myNeighbors.get(direction); Fish cellToUpdate = (Fish)
	 * myCell.getNeighbors().get(direction); } }
	 * 
	 * public void sharkMove(ArrayList<RectangleNoDiagonals> myNeighbors, Fish
	 * myCell){ ArrayList<Integer> myFriends= getNonEmptyNeighbors(myNeighbors,
	 * SHARK); changeCellToShark(myCell, myFriends); }
	 * 
	 * public ArrayList<Integer>
	 * getNonEmptyNeighbors(ArrayList<RectangleNoDiagonals> myNeighbors, String
	 * state){ ArrayList<Integer> myFriends = new ArrayList<Integer>(); for(int
	 * i = 0; i<myNeighbors.size(); i++){
	 * if(myNeighbors.get(i).getCurrentstate().equals(state)){ myFriends.add(i);
	 * } } return myFriends; }
	 * 
	 * public int countEmpty(ArrayList<RectangleNoDiagonals> myNeighbors){ int
	 * emptyCount = 0; for(Cell neighbor: myNeighbors){
	 * if(neighbor.getCurrentstate() == EMPTY){ emptyCount++; } } return
	 * emptyCount; }
	 * 
	 * public void transferInformation(Fish prevCell, Fish newCell){
	 * newCell.setReproductionTime(prevCell.getReproductionTime());
	 * newCell.setMaxReproductionTime(prevCell.getMaxReproductionTime());
	 * newCell.setTimeToDeath(prevCell.getTimeToDeath()); }
	 * 
	 * //can't move somewhere with another shark public void
	 * changeCellToShark(Fish myCell, ArrayList<Integer> myFishFriends){
	 * //ArrayList<> int direction; //eats something if(myFishFriends.size() ==
	 * 0){ if(myCell.getTimeToDeath() == 1){ myCell.setFuturestate(EMPTY);
	 * //myCell.setCurrentstate(EMPTY); return; } ArrayList<Integer> emptyCells
	 * = countCellsOfState(myCell.getNeighbors(myCell, myGrid), EMPTY);
	 * if(emptyCells.size() == 0){ myCell.setFuturestate(SHARK); } direction =
	 * generator.nextInt(emptyCells.size()); myCell.decrementReproductionTime();
	 * } //doesn't eat something else{ if(myFishFriends.size() == 1){ direction
	 * = myFishFriends.get(0); } else{ int directionIndex =
	 * generator.nextInt(myFishFriends.size()); direction =
	 * myFishFriends.get(directionIndex); } myCell.increaseTimeToDeath(); }
	 * if(myCell.getReproductionTime() == 0){ giveBirth(myCell, SHARK); } else{
	 * myCell.setCurrentstate(EMPTY); } Fish cellToUpdate = (Fish)
	 * myCell.getNeighbors(myCell, myGrid).get(direction);
	 * cellToUpdate.setFuturestate(SHARK); transferInformation(myCell,
	 * cellToUpdate); }
	 * 
	 * public ArrayList<Integer>
	 * countCellsOfState(ArrayList<RectangleNoDiagonals> currentNeighbors,
	 * String state){ ArrayList<Integer> emptyCells = new ArrayList<Integer>();
	 * for(int i = 0; i<currentNeighbors.size(); i++){
	 * if(currentNeighbors.get(i).getCurrentstate().equals(state)){
	 * emptyCells.add(i); } } return emptyCells; }
	 * 
	 * public void giveBirth(Fish myCell, String state){
	 * myCell.setFuturestate(state); myCell.resetReproductionTime();
	 * myCell.resetTimeToDeath(); }
	 */
}