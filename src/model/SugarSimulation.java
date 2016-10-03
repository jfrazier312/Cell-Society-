package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import config.Configuration;

/**
 * @author austingartside
 *
 */
public class SugarSimulation extends CellGrid{

	public static final String SIMULATION_NAME = "SUGAR_SIMULATION";
	private static final String AGENT = "agent";
	private static final String NOAGENT = "no_agent";
	private static final int MAX_METABOLISM = 4;
	private static final int MAX_PATCH_SUGAR = 6;
	
	private int sugarGrowBackCount;
	private int sugarGrowBackRate;
	private double sugarGrowBackInterval;
	private int maxVision;
	Random generator;
	
	public SugarSimulation(Configuration config) {
		super(config);
		sugarGrowBackRate = 1;
		sugarGrowBackCount = 0;
		sugarGrowBackInterval = 1.0;
		maxVision = (int) ((int) 100*Double.parseDouble(getConfig().getCustomParam("sugarGrowBackInterval")));
	}
	
	@Override
	public void initSimulation() {
		super.initSimulation();
		double percentAgent = Double.parseDouble(getConfig().getCustomParam("percentAgent"));
		createGrid(percentAgent);
	}
	
	public void createGrid(double percentAgent){
		List<Boolean> agentOrNot = getSugarStartingStates(percentAgent);
		generator = new Random();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				createCell(agentOrNot, i, j);
			}
		}
	}

	private void createCell(List<Boolean> agentOrNot, int i, int j) {
		int patchCheck = generator.nextInt(agentOrNot.size());
		boolean patch = agentOrNot.get(patchCheck);
		agentOrNot.remove(patchCheck);
		int vision = generator.nextInt(maxVision)+1;
		int metabolism = generator.nextInt(MAX_METABOLISM)+1;
		int myPatchSugar = generator.nextInt(MAX_PATCH_SUGAR)+1;
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
	
	//add check to see if a cell has a future state
	@Override
	public void updateGrid() {
		sugarGrowBackInterval = 100*Double.parseDouble(getConfig().getCustomParam("sugarGrowBackInterval"))+1;
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
		sugarGrowBackCount++;
		List<Cell> randomOrder = getAgentCells();
		updateRandomCell(randomOrder);
		restoreSugar();
	}

	private void restoreSugar() {
		if(sugarGrowBackCount >= sugarGrowBackInterval){
			for (int i = 0; i < getNumRows(); i++) {
				for (int j = 0; j < getNumCols(); j++) {
					((SugarAgent) getGridCell(i, j)).getPatch().addSugar(sugarGrowBackRate);
				}
			}
			sugarGrowBackCount = 0;
		}
	}

	private List<Cell> getAgentCells() {
		List<Cell> randomOrder = new ArrayList<Cell>();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				if(getGridCell(i,j).getCurrentstate().equals(AGENT)){
					randomOrder.add(getGridCell(i, j));
				}
			}
		}
		return randomOrder;
	}

	private void updateRandomCell(List<Cell> randomOrder) {
		while(randomOrder.size()!=0){
			int cellChoice = generator.nextInt(randomOrder.size());
			Cell cellToUpdate = randomOrder.get(cellChoice);
			updateCell(cellToUpdate);
			randomOrder.remove(cellChoice);
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
//	
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
