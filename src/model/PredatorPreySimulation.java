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
public class PredatorPreySimulation extends CellGrid {

	private String EMPTY;
	private String FISH;
	private String SHARK;
	private static final String SIMULATION_NAME = Simulations.PREDATOR_PREY.getName();
	public static final int VISION = 1;
	private Random generator;

	public PredatorPreySimulation(Configuration config) {
		super(config);
		EMPTY = myResources.getString("Empty");
		FISH = myResources.getString("Fish");
		SHARK = myResources.getString("Shark");
	}
	
	public void initSimulation() {
		double percentEmptyCells = Double.parseDouble(getConfig().getCustomParam("percentEmpty"));
		double percentShark = Double.parseDouble(getConfig().getCustomParam("percentShark"));
		createGrid(percentEmptyCells, percentShark);
	}

	public void createGrid(double percentEmptyCells, double percentShark) {
		int reproductionTime = Integer.parseInt(getConfig().getAllStates()
				.getStateByName(SHARK).getAttributes().get("reproductionTime"));
		int timeToDeath = Integer.parseInt(getConfig().getAllStates()
				.getStateByName(SHARK).getAttributes().get("lifeTime"));
		generator = new Random();
		List<String> initialization = getStartingStateList(percentEmptyCells, percentShark);
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				createCell(reproductionTime, timeToDeath, initialization, i, j);
			}
		}
	}

	private void createCell(int reproductionTime, int timeToDeath, List<String> initialization, int i, int j) {
		setGridCell(i, j, new Fish(i, j, reproductionTime, timeToDeath, getConfig()));
		if(initialization.size() == 0){
			getGridCell(i, j).setCurrentstate(EMPTY);
		}
		else{
			int cellChoice = generator.nextInt(initialization.size());
			getGridCell(i, j).setCurrentstate(initialization.get(cellChoice));
			initialization.remove(cellChoice);
		}
		getGridCell(i, j).setFuturestate("");
	}

	private List<String> getStartingStateList(double percentEmptyCells, double percentShark) {
		int size = getNumRows()*getNumCols();
		double numEmpty = percentEmptyCells*size;
		double numShark = percentShark*(size-numEmpty);
		double numFish = size-numEmpty-numShark;
		List<String> initialization = new ArrayList<String>();
		for(int i = 0; i<numEmpty; i++){
			initialization.add(EMPTY);
		}
		for(int i = 0; i<numShark; i++){
			initialization.add(SHARK);
		}
		for(int i = 0; i<numFish; i++){
			initialization.add(FISH);
		}
		return initialization;
	}

	@Override
	public void updateGrid(){
		updateFutureStates();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = getGridCell(i, j);
				if(currentCell.getFuturestate().equals("")){
					currentCell.setFuturestate(EMPTY);
				}
				currentCell.setCurrentstate(currentCell.getFuturestate());
				currentCell.setFuturestate("");
			}
		}
	}
	
	public void updateFutureStates() {
		applyUpdate(SHARK);	
		applyUpdate(FISH);
	}

	private void applyUpdate(String state) {
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = getGridCell(i, j);
				if(!currentCell.getCurrentstate().equals(state)){
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
		List<Cell> myFishFriends = getFishNeighbors(myCreature);
		if(state.equals(SHARK)){
			if (myFishFriends.size() > 0) {
				eatFish(myCreature, myFishFriends);
				return;
			}
			//kill shark if it runs out of life
			if(((Fish) myCreature).getTimeToDeath() == 1){
				myCreature.setFuturestate(EMPTY);
				return;
			}
		}
		List<Cell> availableCells = getAvailableCells(myCreature);
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
			reproduce(myCreature, state, newCreatureCell);		
		}
	}

	private void reproduce(Cell myCreature, String state, Cell newCreatureCell) {
		if(state.equals(SHARK)){
			giveBirth((Fish) myCreature, SHARK);
			((Fish)newCreatureCell).resetReproductionTime();
		}
		else{
			giveBirth((Fish) myCreature, FISH);
			((Fish)newCreatureCell).resetReproductionTime();
		}
	}

	private void eatFish(Cell myCreature, List<Cell> myFishFriends) {
		Cell newSharkCell = getNewCell(myFishFriends);
		transferInformation((Fish) myCreature, (Fish) newSharkCell, true);
		if(((Fish) myCreature).getReproductionTime()<=0){
			giveBirth((Fish) myCreature, SHARK);
		}
	}
	
	private void giveBirth(Fish myCell, String state){
		 myCell.setFuturestate(state); 
		 myCell.resetReproductionTime();
		 myCell.resetTimeToDeath(); 
	}

	private Cell getNewCell(List<Cell> availableCells) {
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

	private List<Cell> getAvailableCells(Cell myCreature) {
		List<Cell> neighbors = getNeighbors(myCreature, VISION);
		List<Cell> availableCells = new ArrayList<Cell>();
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

	private List<Cell> getFishNeighbors(Cell myShark) {
		List<Cell> neighbors = getNeighbors(myShark, VISION);
		List<Cell> myFishFriends = new ArrayList<Cell>();
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