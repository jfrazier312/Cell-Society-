package model;

import config.State;

/**
 * @author austingartside
 *
 * Cell used in the PredatprPrey Simulation
 * Controls the attributes of the fish and sharks such as time to birth and death
 */
public class Fish extends Cell{

	private int myReproductionTime;
	private int myMaxReproductionTime;
	private int myTimeToDeath;

	public Fish(int row, int col, int reproductionTime, int timeToDeath) {
		super(row, col);
		myReproductionTime = reproductionTime;
		myMaxReproductionTime = reproductionTime;
		myTimeToDeath = timeToDeath;
	}
	
	@Override
	public State serialize() {
		State s = super.serialize();
		s.getAttributes().put("myReproductionTime", myReproductionTime + "");
		s.getAttributes().put("myMaxReproductionTime", myMaxReproductionTime + "");
		s.getAttributes().put("myTimeToDeath", myTimeToDeath + "");
		return s;
	}

	public void setReproductionTime(int reproductionTime) {
		myReproductionTime = reproductionTime;
	}

	public void setMaxReproductionTime(int maxReproductionTime) {
		myMaxReproductionTime = maxReproductionTime;
	}

	public void setTimeToDeath(int timeToDeath) {
		myTimeToDeath = timeToDeath;
	}

	public int getReproductionTime() {
		return myReproductionTime;
	}

	public int getMaxReproductionTime() {
		return myMaxReproductionTime;
	}

	public void decrementReproductionTime() {
		myReproductionTime--;
	}

	public void resetReproductionTime() {
		myReproductionTime = myMaxReproductionTime;
	}

	public void increaseTimeToDeath() {
		myTimeToDeath += 1;
	}

	public void decrementTimeToDeath() {
		myTimeToDeath--;
	}

	public int getTimeToDeath() {
		return myTimeToDeath;
	}

	public int resetTimeToDeath() {
		return myTimeToDeath;
	}

	@Deprecated
	public int[] getRowDeltas() {
		return null;
	}

	@Deprecated
	public int[] getColDeltas() {
		return null;
	}

}
