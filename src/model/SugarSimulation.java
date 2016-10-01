package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

//TODO: talk with Jordan to make it so that the agent only renders if it is alive
//right now every patch has an agent, but should only display if it's sugar is above 0
/**
 * @author austingartside
 *
 */
public class SugarSimulation extends CellGrid{

	public static final String SIMULATION_NAME = "SUGAR_SIMULATION";
	private static final String AGENT = "agent";
	private static final String NOAGENT = "no_agent";
	private int sugarGrowBackRate;
	Random generator;
	
	public SugarSimulation(int row, int col) {
		super(row, col);
		//change this for second model
		sugarGrowBackRate = 1;
	}
	
	public void createGrid(double percentAgent){
		List<Boolean> agentOrNot = getSugarStartingStates(percentAgent);
		int patchSugar = 10;
		//need to get patchSugar from the xml
		generator = new Random();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				int patchCheck = generator.nextInt(agentOrNot.size());
				boolean patch = agentOrNot.get(patchCheck);
				agentOrNot.remove(patchCheck);
				//change vision for second model
				int vision = generator.nextInt(6)+1;
				int metabolism = generator.nextInt(4)+1;
				int myPatchSugar = generator.nextInt(patchSugar)+1;
				if(patch){
					int sugarVal = generator.nextInt(21)+5;
					setGridCell(i, j, new SugarAgent(i, j, sugarVal, metabolism, vision, myPatchSugar));
					getGridCell(i, j).setCurrentstate(AGENT);
				}
				else{
					setGridCell(i, j, new SugarAgent(i, j, 0, metabolism, vision, myPatchSugar));
					getGridCell(i, j).setCurrentstate(NOAGENT);
				}
				getGridCell(i, j).setFuturestate("");
			}
		}
	}

	private List<Boolean> getSugarStartingStates(double percentAgents) {
		int size = getNumRows()*getNumCols();
		double numAgents = size*percentAgents;
		int numNonAgents = (int) (size-numAgents);
		List<Boolean> agentOrNot = new ArrayList<Boolean>();
		for(int i = 0; i<numNonAgents; i++){
			agentOrNot.add(false);
		}
		for(int i = 0; i<numAgents; i++){
			agentOrNot.add(true);
		}
		return agentOrNot;
	}
	
	@Override
	public String getSimulationName() {
		// TODO Auto-generated method stub
		return SIMULATION_NAME;
	}

	@Override
	public void initSimulation() {
		createGrid(.2);
	}
	
	//add check to see if a cell has a future state
	@Override
	public void updateGrid() {
		updateFutureStates();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				Cell currentCell = getGridCell(i, j);
				if(((SugarAgent) currentCell).isVacant()){
					currentCell.setFuturestate(currentCell.getCurrentstate());
				}
				currentCell.setCurrentstate(currentCell.getFuturestate());
				currentCell.setFuturestate("");
			}
		}
	}
	
	//do agent rules in random order
	public void updateFutureStates(){
		ArrayList<Cell> randomOrder = new ArrayList<Cell>();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				if(getGridCell(i,j).getCurrentstate().equals(AGENT)){
					randomOrder.add(getGridCell(i, j));
				}
			}
		}
		while(randomOrder.size()!=0){
			int cellChoice = generator.nextInt(randomOrder.size());
			Cell cellToUpdate = randomOrder.get(cellChoice);
			updateCell(cellToUpdate);
			randomOrder.remove(cellChoice);
		}
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				((SugarAgent) getGridCell(i, j)).getPatch().addSugar(sugarGrowBackRate);
			}
		}
	}
	
	//if my cell has an agent and doesn't move then i need to update it's future state
	@Override
	public void updateCell(Cell myCell) {
		if(!((SugarAgent) myCell).isDead()){
			List<Cell> neighbors = getNeighbors(myCell, ((SugarAgent) myCell).getVision());
			List<Cell> cellsWithHighestSugar = cellsWithMaxSugar(neighbors);
			Cell closestPatch = getClosest((SugarAgent) myCell, cellsWithHighestSugar);
			if(closestPatch != null){
				updateInfo((SugarAgent) myCell, (SugarAgent) closestPatch);
			}
			else{
				((SugarAgent) myCell).setFuturestate(((SugarAgent) myCell).getCurrentstate());
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
		if(!newCell.isDead()){
			newCell.setFuturestate(AGENT);
		}
		else{
			newCell.setFuturestate(NOAGENT);
		}
	}
	
	public int findHighestSugar(List<Cell> neighbors){
		int highestSugar = 0;
		for(Cell neighbor: neighbors){
			if(((SugarAgent) neighbor).isVacant() && neighbor.getCurrentstate().equals(NOAGENT)){
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
					&& ((SugarAgent) neighbor).isDead() && ((SugarAgent) neighbor).isVacant()&&
					neighbor.getCurrentstate().equals(NOAGENT)){
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
	
//	public void printGrid(){
//		for (int i = 0; i < getNumRows(); i++) {
//			for (int j = 0; j < getNumCols(); j++) {
//				System.out.print(((SugarAgent)getGridCell(i,j)).getSugar() + " ");
//			}
//			System.out.println();
//		}
//		System.out.println();
//		
//		for (int i = 0; i < getNumRows(); i++) {
//			for (int j = 0; j < getNumCols(); j++) {
//				System.out.print(((SugarAgent)getGridCell(i,j)).getPatch().getSugar() + " ");
//			}
//			System.out.println();
//		}
//		
//		for (int i = 0; i < getNumRows(); i++) {
//			for (int j = 0; j < getNumCols(); j++) {
//				if(getGridCell(i, j).getCurrentstate().equals(AGENT)){
//					System.out.print("A");
//				}
//				else{
//					System.out.print("E");
//				}
//			}
//			System.out.println();
//		}
//		//System.out.println();
//	}
//	
//	public static void main(String[] args){
//		SugarSimulation austin = new SugarSimulation(2, 2);
//		austin.createGrid(.3);
//		austin.printGrid();
//		System.out.println();
//		austin.updateGrid();
//		austin.printGrid();
//	}
}
