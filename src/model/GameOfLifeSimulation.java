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
public class GameOfLifeSimulation extends CellGrid {
	
	private static final String SIMULATION_NAME = Simulations.GAME_OF_LIFE.getName();
	private String DEAD;
	private String ALIVE;
	
	private String neighborConvention;
	private List<Integer> numsToSurvive;
	private List<Integer> numsToBeBorn;
	
	private static final int[] ROW_DELTAS = {-1, -1, 0, 1, 1, 1, 0, -1};
	private static final int[] COL_DELTAS = {0, -1, -1, -1, 0, 1, 1, 1};
		
	public GameOfLifeSimulation(Configuration config) {
		super(config);
		DEAD = myResources.getString("Dead");
		ALIVE = myResources.getString("Alive");
		//could change here to decide the neighbor convention
		neighborConvention = "B3 S23";
		getNeighborConvention();
	}
	
	public void getNeighborConvention(){
		String[] neighborConventionList = neighborConvention.split(" ");
		numsToSurvive = new ArrayList<Integer>();
		numsToBeBorn = new ArrayList<Integer>();
		if(neighborConventionList[0].length()>0){
			String numsForBirth = neighborConventionList[0].substring(1);
			for(int i = 0; i<numsForBirth.length(); i++){
				numsToBeBorn.add(Character.getNumericValue(numsForBirth.charAt(i)));
			}
		}
		if(neighborConventionList[1].length()>0){
			String numsForSurvival = neighborConventionList[1].substring(1);
			for(int i = 0; i<numsForSurvival.length(); i++){
				numsToSurvive.add(Character.getNumericValue(numsForSurvival.charAt(i)));
			}
		}
	}
	
	public void initSimulation() {
		super.initSimulation();
		double percentDead = Double.parseDouble(getConfig().getCustomParam("percentDead"));
		setDeltas(ROW_DELTAS, COL_DELTAS);
		createGrid(percentDead);
	}
	
	
	public void createGrid(double percentDead){
		Random generator = new Random();
		List<String> initialization = getStartingStateList(percentDead);
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				setGridCell(i, j, new Rectangle(i, j, getConfig()));
				if(initialization.size() == 0){
					getGridCell(i, j).setCurrentstate(DEAD);
				}
				else{
					int cellChoice = generator.nextInt(initialization.size());
					getGridCell(i, j).setCurrentstate(initialization.get(cellChoice));
					initialization.remove(cellChoice);
				}	
			}
		}
	}

	private List<String> getStartingStateList(double percentDead) {
		int size = getNumRows()*getNumCols();
		double numDead = percentDead*size;
		double numAlive = size-numDead;
		List<String> initialization = new ArrayList<String>();
		for(int i = 0; i<numDead; i++){
			initialization.add(DEAD);
		}
		for(int i = 0; i<numAlive; i++){
			initialization.add(ALIVE);
		}
		return initialization;
	}
	
	private void updateFutureStates(){
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				updateCell(getGridCell(i, j));
			}
		}
	}
	
	@Override
	public void updateGrid(){
		updateFutureStates();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = getGridCell(i, j);
				currentCell.setCurrentstate(currentCell.getFuturestate());
			}
		}
	}
	
	@Override
	public void updateCell(Cell myCell){
		String myState = myCell.getCurrentstate();
		List<Cell> currentNeighbors = getNeighbors(myCell, 1);
		int liveCount = countCellsOfState(currentNeighbors, ALIVE);
		if(myState.equals(DEAD)){
			deadCellUpdate(myCell, liveCount);	
		}
		else{
			liveCellUpdate(myCell, liveCount);
		}
	}
	
	private void liveCellUpdate(Cell myCell, int liveCount){
		myCell.setFuturestate(DEAD);
		if(numsToSurvive.contains(liveCount)){
			myCell.setFuturestate(ALIVE);
		}
	}

	private void deadCellUpdate(Cell myCell, int liveCount){
		myCell.setFuturestate(DEAD);
		if(numsToBeBorn.contains(liveCount)){
			myCell.setFuturestate(ALIVE);
		}
	}
	
	private int countCellsOfState(List<Cell> currentNeighbors, String state){
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
}
