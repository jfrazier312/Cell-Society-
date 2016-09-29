package model;

public class SugarPatch{
	private int mySugar;
	private int myMaxSugar;
	public SugarPatch(int sugar) {
		mySugar = sugar;
		myMaxSugar = sugar;
	}
	
	public int getSugar(){
		return mySugar;
	}
	
	public int getMaxSugar(){
		return myMaxSugar;
	}
	
	public void decreaseSugar(int amount){
		mySugar-= amount;
	}
	
	public void addSugar(int amount){
		mySugar+=amount;
		if(mySugar>myMaxSugar){
			mySugar = myMaxSugar;
		}
	}

}
