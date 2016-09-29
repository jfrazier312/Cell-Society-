package model;

import java.util.ArrayList;
import java.util.Random;

public class SugarSimulation extends CellGrid{

	public static final String SIMULATION_NAME = "SUGAR_SIMULATION";
	private int sugarGrowBackRate;
	Random generator;
	
	public SugarSimulation(int row, int col) {
		super(row, col);
		//sugarGrowBackRate = however you get it from the xml
		sugarGrowBackRate = 10;
	}
	
	public void createGrid(int percentPatches){
		int size = getNumRows()*getNumCols();
		double numPatches = size*percentPatches;
		double numNonPatches = size-(int) numPatches;
		int vision = 5;
		int metabolism = 1;
		int patchSugar = 10;
		ArrayList<Boolean> patchOrNot = new ArrayList<Boolean>();
		for(int i = 0; i<numNonPatches; i++){
			patchOrNot.add(false);
		}
		for(int i = 0; i<numPatches; i++){
			patchOrNot.add(true);
		}
		//need to get vision sugar and metabolism from the xml
		generator = new Random();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = 0; j < getNumCols(); j++) {
				int patchCheck = generator.nextInt(patchOrNot.size());
				boolean patch = patchOrNot.get(patchCheck);
				patchOrNot.remove(patchCheck);
				if(patch){
					int sugarVal = generator.nextInt(10)+1;
					setGridCell(i, j, new SugarAgent(i, j, sugarVal, metabolism, vision, patchSugar));
				}
				else{
					setGridCell(i, j, new SugarAgent(i, j, 0, metabolism, vision, patchSugar));
				}
			}
		}
	}
	
	@Override
	public String getSimulationName() {
		// TODO Auto-generated method stub
		return SIMULATION_NAME;
	}

	@Override
	public void initSimulation() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void updateGrid() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCell(Cell myCell) {
		if(!((SugarAgent) myCell).isDead()){
			ArrayList<Cell> neighbors = getSugarNeighbors(myCell, ((SugarAgent) myCell).getVision());
			ArrayList<Cell> cellsWithHighestSugar = cellsWithMaxSugar(neighbors);
			Cell closestPatch = getClosest((SugarAgent) myCell, cellsWithHighestSugar);
			//add sugar to copy cell
			//subtract metabolism
			//if alive move the copy cell to its new location
			//take sugar away from patch?
		}	
	}
	
	public int findHighestSugar(ArrayList<Cell> neighbors){
		int highestSugar = 0;
		for(Cell neighbor: neighbors){
			int sugarLevel = ((SugarAgent) neighbor).getPatch().getSugar();
			if(sugarLevel>highestSugar){
				highestSugar = sugarLevel;
			}
		}
		return highestSugar;
	}
	
	public ArrayList<Cell> cellsWithMaxSugar(ArrayList<Cell> neighbors){
		ArrayList<Cell> highestPatches = new ArrayList<Cell>();
		int highestSugar = findHighestSugar(neighbors);
		for(Cell neighbor: neighbors){
			if(((SugarAgent) neighbor).getPatch().getSugar() == highestSugar){
				highestPatches.add(neighbor);
			}
		}
		return highestPatches;
	}
	
	public double distance(int x1, int y1, int x2, int y2){
		return Math.sqrt(1.0*((x1-x2)*(x1-x2) + (y2-y1)*(y2-y1)));
	}
	
	public Cell getClosest(SugarAgent myAgent, ArrayList<Cell> myNeighbors){
		int myRow = myAgent.getRowPos();
		int myCol = myAgent.getColPos();
		Cell bestCell = myNeighbors.get(0);
		double bestDistance = distance(bestCell.getRowPos(), bestCell.getColPos(),
				myRow, myCol);
		for(Cell neighbor: myNeighbors){
			int compRow = neighbor.getRowPos();
			int compCol = neighbor.getColPos();
			double distance = distance(myRow, myCol, compRow, compCol);
			if(distance<bestDistance){
				bestCell = neighbor;
			}	
		}
		return bestCell;
	}

}
