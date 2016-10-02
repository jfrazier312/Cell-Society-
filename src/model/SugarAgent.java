package model;
import config.State;
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
	
	public State serialize() {
		State s = new State();
		s.getAttributes().put("mySugar", mySugar + "");
		s.getAttributes().put("mySugarMetabolism", mySugarMetabolism + "");
		s.getAttributes().put("myVision", myVision + "");
		s.getAttributes().put("myPatchSugar", myPatch.getSugar() + "");
		s.getAttributes().put("myPatchMaxSugar", myPatch.getMaxSugar() + "");
		return s;
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

	@Override
	public int[] getRowDeltas() {
		return null;
	}

	@Override
	public int[] getColDeltas() {
		return null;
	}
}
