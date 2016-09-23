package model;

import config.Configuration;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Fish extends RectangleNoDiagonals {

	private int[] noDiagonalRowDeltas = { -1, 0, 1, 0, };
	private int[] noDiagonalColDeltas = { 0, -1, 0, 1 };
	private int myReproductionTime;
	private int myMaxReproductionTime;
	private int myTimeToDeath;

	public Fish(int row, int col, int reproductionTime, int timeToDeath) {
		super(row, col);
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

	@Override
	public Node render() {
		javafx.scene.shape.Rectangle a = new javafx.scene.shape.Rectangle(10, 10);
		// based on size of grid
		// based on pixels on windows
		Configuration config = ConfigurationLoader.loader().getConfig();
		String color = config.getAllStates().getColors().get(this.getCurrentstate());

		// Needs to be a hex value ( ##0000FF, or 0x0000FF)
		a.setFill(Color.web(color));
		return a;
	}

	@Override
	public int[] getRowDeltas() {
		return noDiagonalRowDeltas;
	}

	@Override
	public int[] getColDeltas() {
		return noDiagonalColDeltas;
	}

	// shark stuff

	public void increaseTimeToDeath() {
		myTimeToDeath += 10;
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
