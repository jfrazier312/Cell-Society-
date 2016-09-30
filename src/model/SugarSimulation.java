package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
/*
 * author: Austin Gartside
 * 
 * 
 */

//TODO: talk with Jordan to make it so that the agent only renders if it is alive
//right now every patch has an agent, but should only display if it's sugar is above 0
public class SugarSimulation extends CellGrid{

	public static final String SIMULATION_NAME = "SUGAR_SIMULATION";
	private static final String AGENT = "agent";
	private static final String NOAGENT = "no_agent";
	private int sugarGrowBackRate;
	Random generator;
	
	public SugarSimulation(int row, int col) {
		super(row, col);
		//sugarGrowBackRate = however you get it from the xml
		sugarGrowBackRate = 1;
	}
	
	public void createGrid(double percentPatches){
		List<Boolean> agentOrNot = getSugarStartingStates(percentPatches);
		int patchSugar = 10;
		//need to get patchSugar from the xml
		generator = new Random();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				int patchCheck = generator.nextInt(agentOrNot.size());
				boolean patch = agentOrNot.get(patchCheck);
				agentOrNot.remove(patchCheck);
				int vision = generator.nextInt(6)+1;
				int metabolism = generator.nextInt(4)+1;
				int myPatchSugar = generator.nextInt(patchSugar)+1;
				if(patch){
					int sugarVal = generator.nextInt(21)+5;
					setGridCell(i, j, new SugarAgent(i, j, sugarVal, metabolism, vision, myPatchSugar));
					getGridCell(i, j).setCurrentstate("AGENT");
				}
				else{
					setGridCell(i, j, new SugarAgent(i, j, 0, metabolism, vision, myPatchSugar));
					getGridCell(i, j).setCurrentstate("NOAGENT");
				}
				getGridCell(i, j).setFuturestate("");
			}
		}
	}

	private List<Boolean> getSugarStartingStates(double percentPatches) {
		int size = getNumRows()*getNumCols();
		double numPatches = size*percentPatches;
		double numNonPatches = size-(int) numPatches;
		List<Boolean> patchOrNot = new ArrayList<Boolean>();
		for(int i = 0; i<numNonPatches; i++){
			patchOrNot.add(false);
		}
		for(int i = 0; i<numPatches; i++){
			patchOrNot.add(true);
		}
		return patchOrNot;
	}
	
	@Override
	public String getSimulationName() {
		// TODO Auto-generated method stub
		return SIMULATION_NAME;
	}

	@Override
	public void initSimulation() {
		// TODO Auto-generated method stub
		createGrid(.2);
	}
	
	//add check to see if a cell has a future state
	@Override
	public void updateGrid() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateCell(Cell myCell) {
		if(!((SugarAgent) myCell).isDead()){
			List<Cell> neighbors = getSugarNeighbors(myCell, ((SugarAgent) myCell).getVision());
			List<Cell> cellsWithHighestSugar = cellsWithMaxSugar(neighbors);
			Cell closestPatch = getClosest((SugarAgent) myCell, cellsWithHighestSugar);
			if(closestPatch != null){
				updateInfo((SugarAgent) myCell, (SugarAgent) closestPatch);
			}
		}	
	}
	
	public void updateInfo(SugarAgent prevCell, SugarAgent newCell){
		prevCell.updateSugar(newCell.getPatch().getSugar());
		newCell.setSugar(prevCell.getSugar());
		newCell.setSugarMetabolism(prevCell.getSugarMetabolism());
		newCell.getPatch().decreaseSugar();
		prevCell.killAgent();
		prevCell.setFuturestate(NOAGENT);
		newCell.setFuturestate(AGENT);
	}
	
	public int findHighestSugar(List<Cell> neighbors){
		int highestSugar = 0;
		for(Cell neighbor: neighbors){
			if(((SugarAgent) neighbor).isVacant()){
				int sugarLevel = ((SugarAgent) neighbor).getPatch().getSugar();
				if(sugarLevel>highestSugar){
					highestSugar = sugarLevel;
				}
			}
		}
		return highestSugar;
	}
	
	public List<Cell> cellsWithMaxSugar(List<Cell> neighbors){
		List<Cell> highestPatches = new ArrayList<Cell>();
		int highestSugar = findHighestSugar(neighbors);
		for(Cell neighbor: neighbors){
			if(((SugarAgent) neighbor).getPatch().getSugar() >= highestSugar
					&& ((SugarAgent) neighbor).isDead() && ((SugarAgent) neighbor).isVacant()){
				highestPatches.add(neighbor);
			}
		}
		return highestPatches;
	}
	
	public double distance(int x1, int y1, int x2, int y2){
		return Math.sqrt(1.0*((x1-x2)*(x1-x2) + (y2-y1)*(y2-y1)));
	}
	
	public Cell getClosest(SugarAgent myAgent, List<Cell> myNeighbors){
		if(myNeighbors.size() == 0){
			return null;
		}
		int myRow = myAgent.getRowPos();
		int myCol = myAgent.getColPos();
		Cell bestCell = myNeighbors.get(0);
		double bestDistance = distance(bestCell.getRowPos(), bestCell.getColPos(),
				myRow, myCol);
		for(Cell neighbor: myNeighbors){
			if(!((SugarAgent) myAgent).isDead()){
				int compRow = neighbor.getRowPos();
				int compCol = neighbor.getColPos();
				double distance = distance(myRow, myCol, compRow, compCol);
				if(distance<bestDistance){
					bestCell = neighbor;
				}	
			}
		}
		return bestCell;
	}
}
