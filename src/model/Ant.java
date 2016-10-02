package model;

import java.util.ArrayList;
import java.util.List;
/**
 * @author austingartside
 *
 */
public class Ant{
	
	
	private int rowPos;
	private int colPos;
	private int myRowOrientation;
	private int myColOrientation;
	private String myType;
	
	//there are obstacles and ant limits
	
	//public Ant(int row, int col, int rowOrientation, int colOrientation, int pheromones, String type){
	public Ant(int row, int col, String type){
		rowPos = row;
		colPos = col;
		//myRowOrientation = rowOrientation;
		//myColOrientation = colOrientation;
		myType = type;
	}
	
	public List<Cell> getForwardCells(List<Cell> neighbors, Cell myOrientationCell){
		int myCellIndex = neighbors.indexOf(myOrientationCell);
		List<Cell> forward = new ArrayList<Cell>();
		forward.add(neighbors.get(myCellIndex));
		if(myCellIndex == 0){
			forward.add(neighbors.get(neighbors.size()-1));
		}
		else{
			forward.add(neighbors.get(myCellIndex-1));
		}
		if(myCellIndex == neighbors.size()-1){
			forward.add(neighbors.get(0));
		}
		else{
			forward.add(neighbors.get(myCellIndex+1));
		}
		return forward;
	}
	
	public List<Cell> getNonForwardCells(List<Cell> neighbors, Cell myOrientationCell){
		int myCellIndex = neighbors.indexOf(myOrientationCell);
		List<Cell> nonForward = new ArrayList<Cell>();
		List<Integer> badCellIndices = new ArrayList<Integer>();
		if(myCellIndex == 0){
			badCellIndices.add(neighbors.size()-1);
		}
		else{
			badCellIndices.add(myCellIndex-1);
		}
		if(myCellIndex == neighbors.size()-1){
			badCellIndices.add(0);
		}
		else{
			badCellIndices.add(myCellIndex+1);
		}
		for(int i = 0; i<neighbors.size(); i++){
			if(!badCellIndices.contains(i)){
				nonForward.add(neighbors.get(i));
			}
		}
		return nonForward;
	}
	
	public int getRow(){
		return rowPos;
	}
	
	public int getCol(){
		return colPos;
	}
	
	public void setRow(int row){
		rowPos = row;
	}
	
	public void setCol(int col){
		colPos = col;
	}
	
	public String getType(){
		return myType;
	}
	
	public void setType(String newType){
		myType = newType;
	}
	
//	public void setRowOrientation(int newOrientation){
//		myRowOrientation = newOrientation;
//	}
//	
//	public void setColOrientation(int newOrientation){
//		myColOrientation = newOrientation;
//	}
//	
//	public int getRowOrientation(){
//		return myRowOrientation;
//	}
//	
//	public int getColOrientation(){
//		return myColOrientation;
//	}
	
	

}
