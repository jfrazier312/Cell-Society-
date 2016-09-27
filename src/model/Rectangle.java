package model;

import config.ConfigurationLoader;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Rectangle extends Cell implements view.GameWorld {

	// included diagonals
	private int[] rowDeltas = { -1, 0, 1, 0, 1, 1, -1, -1 };
	private int[] colDeltas = { 0, -1, 0, 1, 1, -1, 1, -1 };

	public Rectangle(int row, int col) {
		super(row, col);
	}

	@Override
	public Node render() {
		
		double rows = ConfigurationLoader.getConfig().getNumRows();
		double cols = ConfigurationLoader.getConfig().getNumCols();
		
		double width = calculateSize(GRID_WIDTH, cols);
		double height = calculateSize(GRID_HEIGHT, rows);

		javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(width, height);
		rect.setStroke(Color.RED);
		rect.setStrokeWidth(0.1);
		String color = ConfigurationLoader.getConfig().getAllStates().getStateByName(getCurrentstate()).getAttributes().get("color");
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
