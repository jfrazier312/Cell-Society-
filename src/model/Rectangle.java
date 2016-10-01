package model;

import config.Configuration;
import config.ConfigurationLoader;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * 
 * @author Jordan Frazier (jrf30)
 *
 */
public class Rectangle extends Cell implements view.GameWorld {

	// included diagonals
	private int[] rowDeltas = { -1, 0, 1, 0, 1, 1, -1, -1 };
	private int[] colDeltas = { 0, -1, 0, 1, 1, -1, 1, -1 };
	private Configuration myConfig;

	public Rectangle(int row, int col, Configuration config) {
		super(row, col);
		myConfig = config;
	}

	@Override
	public Node render() {
		double rows = myConfig.getNumRows();
		double cols = myConfig.getNumCols();

		double width = calculateSize(GRID_WIDTH, cols);
		double height = calculateSize(GRID_HEIGHT, rows);

		javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(width, height);

		String color = myConfig.getAllStates().getStateByName(getCurrentstate()).getAttributes()
				.get("color");
		rect.setFill(Color.web(color));
		return rect;
	}

	private double calculateSize(double edgeSize, double numSpots) {
		return (edgeSize / numSpots);
	}

	@Override
	public int[] getRowDeltas() {
		return rowDeltas;
	}

	@Override
	public int[] getColDeltas() {
		return colDeltas;
	}
}
