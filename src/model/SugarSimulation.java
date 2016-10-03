package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import config.Configuration;

/**
 * @author austingartside
 *
 */
public class SugarSimulation extends CellGrid{

	public static final String SIMULATION_NAME = "SUGAR_SIMULATION";
	private static final int MAX_METABOLISM = 4;
	private static final int MAX_PATCH_SUGAR = 6;
	
	private int sugarGrowBackCount;
	private int sugarGrowBackRate;
	private double sugarGrowBackInterval;
	private int maxVision;
	
	private String AGENT;
	private String NOAGENT;
	Random generator;
	
	public SugarSimulation(Configuration config) {
		super(config);
		sugarGrowBackRate = 1;
		sugarGrowBackCount = 0;
		sugarGrowBackInterval = 1.0;
		AGENT = myResources.getString("Agent");
		NOAGENT = myResources.getString("NoAgent");
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
		return SIMULATION_NAME;
	}
	
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
	
	private void updateInfo(SugarAgent prevCell, SugarAgent newCell){
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
	
	private int findHighestSugar(List<Cell> neighbors){
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
	
	private List<Cell> cellsWithMaxSugar(List<Cell> neighbors){
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
	
	private Cell getClosest(SugarAgent myAgent, List<Cell> myNeighbors){
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
