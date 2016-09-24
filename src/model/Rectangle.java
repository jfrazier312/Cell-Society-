package model;

import config.Configuration;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Rectangle extends Cell implements view.GameWorld {

	// included diagonals
	private int[] rowDeltas = { -1, 0, 1, 0, 1, 1, -1, -1 };
	private int[] colDeltas = { 0, -1, 0, 1, 1, -1, 1, -1 };

	public Rectangle(int row, int col) {
		super(row, col);

	}

	@Override
	public Node render() {
		// TODO: Jordan - update render for XML, update row/cols

		/*
		 * Need to add config.getNumberColumns() in there double height =
		 * calculateSize(config.getGridHeight(), config.getNumberRows()); double
		 * width = calculateSize(config.getGridWidth(), config.getNumberCols());
		 * javafx.scene.shape.Rectangle a = new
		 * javafx.scene.shape.Rectangle(width, height);
		 */
		
		double rows = ConfigurationLoader.getConfig().getNumRows();
		double cols = ConfigurationLoader.getConfig().getNumCols();
		
		double height = calculateSize(GRID_WIDTH, rows);
		double width = calculateSize(GRID_HEIGHT, cols);

		javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(width, height);
		
		String color = ConfigurationLoader.getConfig().getAllStates().getStateByName(getCurrentstate()).getAttributes().get("color");
		rect.setFill(Color.web(color));
		return rect;
	}

	private double calculateSize(double edgeSize, double numSpots) {
		return edgeSize / numSpots;
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
