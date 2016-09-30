package model;

import java.util.ArrayList;

public class SugarAgent extends RectangleNoDiagonals{
	
	private int mySugar;
	private int mySugarMetabolism;
	private int myVision;
	SugarPatch myPatch;
	
	public SugarAgent(int row, int col, int initSugar, int sugarMetabolism, int vision, int patchSugar){
		super(row, col);
		mySugar = initSugar;
		mySugarMetabolism = sugarMetabolism;
		myVision = vision;
		myPatch = new SugarPatch(patchSugar);
	}
	
	public SugarPatch getPatch(){
		return myPatch;
	}
	
	public int getSugar(){
		return mySugar;
	}
	
	public int getSugarMetabolism(){
		return mySugarMetabolism;
	}
	
	public int getVision(){
		return myVision;
	}
	
	public void updateSugar(int amount){
		mySugar+=amount;
		mySugar-=mySugarMetabolism;
	}
	
	public boolean isDead(){
		return mySugar<=0;
	}
	
	
	
	

}
