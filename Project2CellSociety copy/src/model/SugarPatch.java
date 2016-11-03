package model;
/**
 * @author austingartside
 *
 */
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
	
	public void decreaseSugar(){
		mySugar = 1;
	}
	
	public void addSugar(int amount){
		mySugar+=amount;
		if(mySugar>myMaxSugar){
			mySugar = myMaxSugar;
		}
	}

}
