package model;
import java.util.ArrayList;
import java.util.Random;

import config.Configuration;
import config.State;
import view.Simulations;

/**
 * @author austingartside
 *
 */
public class FireSimulation extends CellGrid {
	public static final String SIMULATION_NAME = Simulations.FIRE.getName();
	private String EMPTY;
	private String TREE;
	private String BURNING;
	private static final int VISION = 1;
	private double probOfBurning;
	Random generator;
	
	public FireSimulation(Configuration config) {
		super(config);
		EMPTY = myResources.getString("Empty");
		TREE = myResources.getString("Tree");
		BURNING = myResources.getString("Burning");
	}
	
	@Override
	public void load() {
		for (State s :getConfig().getInitialCells()) {
			int row = Integer.parseInt(s.getAttributes().get("row"));
			int col = Integer.parseInt(s.getAttributes().get("col"));
			RectangleNoDiagonals r = new RectangleNoDiagonals(row, col, getConfig());
			r.setCurrentstate(s.getAttributes().get("currentState"));
			setGridCell(row, col, r);
		}
	}
	
	public void initSimulation() {
		createGrid();
//		load(); // if initial cells are empty, will not overwrite cell
		probOfBurning = Double.parseDouble(getConfig().getCustomParam("probability"));
		//probOfBurning = .5;
	}
	
	public void createGrid() {
		generator = new Random();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				setGridCell(i, j, new RectangleNoDiagonals(i, j, getConfig()));
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
		String myState = myCell.getCurrentstate();
		ArrayList<Cell> currentNeighbors = getNeighbors(myCell, VISION);
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
