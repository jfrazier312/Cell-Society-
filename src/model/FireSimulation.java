package model;
import java.util.List;
import java.util.Random;

import config.Configuration;
import config.State;
import view.Simulations;

/**
 * @author austingartside
 *
 * Creates and updates grid for the fire simulation
 */
public class FireSimulation extends CellGrid {
	public static final String SIMULATION_NAME = Simulations.FIRE.getName();
	private String EMPTY;
	private String TREE;
	private String BURNING;
	private static final int VISION = 1;
	private double probOfBurning;
	private int initBurningRow;
	private int initBurningCol;
	Random generator;
	
	public FireSimulation(Configuration config) {
		super(config);
		EMPTY = myResources.getString("Empty");
		TREE = myResources.getString("Tree");
		BURNING = myResources.getString("Burning");
		generator = new Random();
	}
	
	public void getBurningTreePos(){
		for (State s : getConfig().getInitialCells()) {
			initBurningRow = Integer.parseInt(s.getAttributes().get("row"));
			initBurningCol = Integer.parseInt(s.getAttributes().get("col"));
		}
	}
	
	public void initSimulation() {
		super.initSimulation();
		if(isToroidal()){
			getBurningTreePos();
			createToroidalGrid();
		}
		else{
			createGrid();
		}
	}
	
	public void createGrid() {
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				setGridCell(i, j, new Rectangle(i, j, getConfig()));
				if(i==0 || j==0 || i==getNumRows()-1 || j == getNumCols()-1){
					getGridCell(i, j).setCurrentstate(EMPTY);
				}
				else if(i == getNumRows()/2 && j == getNumCols()/2){
					getGridCell(i, j).setCurrentstate(BURNING);
				}
				else{
					getGridCell(i, j).setCurrentstate(TREE);
				}
			}
		}
	}
	
	public void createToroidalGrid() {
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				setGridCell(i, j, new Rectangle(i, j, getConfig()));
				if(i == initBurningRow && j == initBurningCol){
					getGridCell(i, j).setCurrentstate(BURNING);
				}
				else{
					getGridCell(i, j).setCurrentstate(TREE);
				}
			}
		}
	}
	
	@Override
	public void updateGrid() {
		updateFutureStates();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = getGridCell(i, j);
				currentCell.setCurrentstate(currentCell.getFuturestate());
			}
		}
	}
	
	private void updateFutureStates(){
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				updateCell(getGridCell(i, j));
			}
		}
	}
	
	@Override
	public void updateCell(Cell myCell){
		probOfBurning = Double.parseDouble(getConfig().getCustomParam("probability"));
		String myState = myCell.getCurrentstate();
		List<Cell> currentNeighbors = getNeighbors(myCell, VISION);
		if(myState.equals(BURNING)){
			myCell.setFuturestate(EMPTY);
		}
		else if(myState.equals(TREE)){
			treeUpdate(myCell, currentNeighbors);
		}
		else{
			myCell.setFuturestate(EMPTY);
		}
		
	}

	private void treeUpdate(Cell myCell, List<Cell> currentNeighbors) {
		myCell.setFuturestate(TREE);
		for(Cell neighbor: currentNeighbors){
			if(neighbor.getCurrentstate().equals(BURNING)){
				int seeIfBurn = generator.nextInt(100);
				if(seeIfBurn<(probOfBurning*100)){
					myCell.setFuturestate(BURNING);
					break;
				}
			}
		}
	}

	@Override
	public String getSimulationName() {
		return SIMULATION_NAME;
	}
}
