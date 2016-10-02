package model;
/*
 * author: Austin Gartside
 */
import java.util.ArrayList;

import config.Configuration;
/**
 * @author austingartside
 *
 */
public class SugarAgent extends Cell{
	
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
	
	public void setSugar(int sugar){
		mySugar = sugar;
	}
	
	public int getSugarMetabolism(){
		return mySugarMetabolism;
	}
	
	public void setSugarMetabolism(int newMetabolism){
		mySugarMetabolism = newMetabolism;
	}
	
	public int getVision(){
		return myVision;
	}
	
	public void updateSugar(int amount){
		mySugar+=amount;
		mySugar-=mySugarMetabolism;
	}
	
	public void killAgent(){
		mySugar = 0;
	}
	
	public boolean isDead(){
		return mySugar<=0;
	}
	
	public boolean isVacant(){
		return getFuturestate().equals("");
	}
	
	
	
	

}
