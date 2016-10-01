package model;

import java.util.Arrays;

import config.Configuration;
import config.ConfigurationLoader;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/** 
 * 
 * @author Jordan Frazier (jrf30)
 *
 */
public class Triangle extends Cell implements view.GameWorld {

	private int isEven;
	private int[] rowDeltas = { -1, 0, 1 };
	private int[] evenColDeltas = { 0, 1, 0 };
	private int[] oddColDeltas = { 0, -1, 0 };
	
	Configuration myConfig;

	private static final double TRIANGLE_WIDTH = 20;
	private Double[] normalTrianglePoints = { TRIANGLE_WIDTH / 2, 0.0, 0.0, 20.0, TRIANGLE_WIDTH, 20.0 };
	private Double[] upsideDownTrianglePoints = { 0.0, 0.0, TRIANGLE_WIDTH, 0.0, TRIANGLE_WIDTH / 2, 20.0 };

	public Triangle(int row, int col, int isEven, Configuration config) {
		super(row, col);
		this.isEven = isEven;
		myConfig = config;
	}

	@Override
	public Node render() {
		// TODO: Configuration area
		double rows = myConfig.getNumRows();
		double cols = myConfig.getNumCols();

		Double[] adjustedNormalTrianglePoints = getAdjustedPoints(true, (int) rows, (int) cols);
		Double[] adjustedUpsideDownTrianglePoints = getAdjustedPoints(false, (int) rows, (int) cols);

		Polygon triangle = new Polygon();
		String color = myConfig.getAllStates().getStateByName(getCurrentstate()).getAttributes()
				.get("color");
		triangle.setFill(Color.web(color));

		if (isEven % 2 == 0) {
			triangle.getPoints().addAll(adjustedNormalTrianglePoints);
		} else {
			triangle.getPoints().addAll(adjustedUpsideDownTrianglePoints);
		}
		return triangle;
	}

	private Double[] getAdjustedPoints(boolean normal, int rows, int cols) {
		Double[] adjusted = new Double[normalTrianglePoints.length];
		if (normal) {
			adjusted = Arrays.copyOf(normalTrianglePoints, normalTrianglePoints.length);
			adjusted[4] = normalTrianglePoints[4] / (20 / ((GRID_WIDTH - cols) / cols));
			adjusted[0] = normalTrianglePoints[0] / (20 / ((GRID_WIDTH - cols) / cols));
		} else {
			adjusted = Arrays.copyOf(upsideDownTrianglePoints, upsideDownTrianglePoints.length);
			adjusted[4] = upsideDownTrianglePoints[4] / (20 / ((GRID_WIDTH - cols) / cols));
			adjusted[2] = upsideDownTrianglePoints[2] / (20 / ((GRID_WIDTH - cols) / cols));
		}
		return adjusted;
	}

	@Override
	public int[] getRowDeltas() {
		return rowDeltas;
	}

	@Override
	public int[] getColDeltas() {
		return (isEven % 2 == 0) ? evenColDeltas : oddColDeltas;
	}
}
