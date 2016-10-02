package model;

import java.util.ArrayList;
import java.util.List;

import config.Configuration;
import config.State;
/**
 * @author austingartside
 *
 */
public class AntCell extends Cell{

	private static final int myMaxPheromones = 1000;
	
	List<Ant> myAnts;
	private int myHomePheromones;
	private int myFoodPheromones;
	private int myMaxHomePheromones;
	private int myMaxFoodPheromones;
	private boolean gainedHomeAnts;
	private boolean gainedFoodAnts;
	
	Configuration myConfig;
	
	public AntCell(int row, int col, int homePheromones, int foodPheromones, int maxHomePheromones, int maxFoodPheromones) {
		super(row, col);
		myAnts = new ArrayList<Ant>();
		myHomePheromones = homePheromones;
		myFoodPheromones = foodPheromones;
		myMaxHomePheromones = maxHomePheromones;
		myMaxFoodPheromones = maxFoodPheromones;
		gainedHomeAnts = false;
		gainedFoodAnts = false;
	}
	public List<Ant> getAnts(){
		return myAnts;
	}
	
	public void loseFoodPheromones(int lostFood){
		myFoodPheromones-=lostFood;
		if(myFoodPheromones<0){
			myFoodPheromones = 0;
		}
	}
	
	public void loseHomePheromones(int lostHome){
		myFoodPheromones-=lostHome;
		if(myHomePheromones<0){
			myHomePheromones = 0;
		}
	}
	
	public void addFoodPheromones(){
		myFoodPheromones = myMaxFoodPheromones;
	}
	
	public void addHomePheromones(){
		myHomePheromones = myMaxHomePheromones;
	}
	
	public int getHomePheromones(){
		return myHomePheromones;
	}
	
	public int getFoodPheromones(){
		return myFoodPheromones;
	}
	
	public int getMaxHomePheromones(){
		return myMaxHomePheromones;
	}
	
	public int getMaxFoodPheromones(){
		return myMaxFoodPheromones;
	}
	
	public void setMaxHomePheromones(int value){
		myMaxHomePheromones = value;
	}
	
	public void setMaxFoodPheromones(int value){
		myMaxFoodPheromones = value;
	}
	
	public void addAnt(Ant newAnt){
		myAnts.add(newAnt);
	}
	
	public void removeAnt(Ant movingAnt){
		myAnts.remove(movingAnt);
	}
	
	public boolean isFull(){
		return myAnts.size()>=10;
	}
	
	public boolean gainedHomeAnts(){
		return gainedHomeAnts;
	}
	
	public boolean gainedFoodAnts(){
		return gainedFoodAnts;
	}
	
	public void homeAntGain(){
		gainedHomeAnts = true;
	}
	
	public void foodAntGain(){
		gainedFoodAnts = true;
	}
	
	public void resetAntGain(){
		gainedHomeAnts = false;
		gainedFoodAnts = false;
	}
	
	public boolean hasAnts(){
		return myAnts.size()>0;
	}
	@Override
	public int[] getRowDeltas() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int[] getColDeltas() {
		// TODO Auto-generated method stub
		return null;
	}

}
