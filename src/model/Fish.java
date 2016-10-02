package model;

import config.Configuration;

/**
 * @author austingartside
 *
 */
public class Fish extends RectangleNoDiagonals{
//public class Fish extends Cell{

	//private int[] noDiagonalRowDeltas = { -1, 0, 1, 0};
	///private int[] noDiagonalColDeltas = { 0, -1, 0, 1 };
	private int myReproductionTime;
	private int myMaxReproductionTime;
	private int myTimeToDeath;

	public Fish(int row, int col, int reproductionTime, int timeToDeath, Configuration config) {
		super(row, col, config);
		myReproductionTime = reproductionTime;
		myMaxReproductionTime = reproductionTime;
		myTimeToDeath = timeToDeath;
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

//	@Override
//	public int[] getRowDeltas() {
//		return noDiagonalRowDeltas;
//	}
//
//	@Override
//	public int[] getColDeltas() {
//		return noDiagonalColDeltas;
//	}

	// shark stuff

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

}
