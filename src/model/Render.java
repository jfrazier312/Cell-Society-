package model;

import java.util.Arrays;

import config.Configuration;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import view.SceneConstant;

public class Render {

	private Configuration myConfig;
	private int isEven;

	private static final double TRIANGLE_WIDTH = 20.0;
	private Double[] normalTrianglePoints = { TRIANGLE_WIDTH / 2, 0.0, 0.0, TRIANGLE_WIDTH, TRIANGLE_WIDTH,
			TRIANGLE_WIDTH };
	private Double[] upsideDownTrianglePoints = { 0.0, 0.0, TRIANGLE_WIDTH, 0.0, TRIANGLE_WIDTH / 2, TRIANGLE_WIDTH };

	private String[] colorList = { "#f6edd6", "#f4e5c0", "#f4dea9", "#f4d790", "#e7c675", "#ceaa55", "#b8943d",
			"#a58026", "#967013", "#7e5900" };

	public Render(Configuration config) {
		myConfig = config;
	}

	public Shape chooseRender(Cell cell, String shape) {
		Shape fuck = null;
		if (shape.equals("rectangle")) {
			fuck = renderRectangle(cell);
		} else if (shape.equals("triangle")) {
			fuck = renderTriangle(cell);
		} else if (shape.equals("hexagon")) {
			// fuck = //fuck;
		} else {
			// fuck = new Polygon();
		}
		return fuck;
	}

	private Shape renderRectangle(Cell cell) {
		double rows = myConfig.getNumRows();
		double cols = myConfig.getNumCols();
		double width = calculateSize(SceneConstant.GRID_WIDTH.getValue(), cols);
		double height = calculateSize(SceneConstant.GRID_HEIGHT.getValue(), rows);

		Shape rect = new javafx.scene.shape.Rectangle(width, height);
		String color = myConfig.getAllStates().getStateByName(cell.getCurrentstate()).getAttributes().get("color");
		if(cell.isSugarCell()) {
			color = setSugarColor(cell, color);
		}
		rect.setFill(Color.web(color));
		return rect;
	}

	private Shape renderTriangle(Cell cell) {
		double rows = myConfig.getNumRows();
		double cols = myConfig.getNumCols();
		Double[] adjustedNormalTrianglePoints = getAdjustedPoints(true, (int) rows, (int) cols);
		Double[] adjustedUpsideDownTrianglePoints = getAdjustedPoints(false, (int) rows, (int) cols);

		Polygon triangle = new Polygon();
		String color = myConfig.getAllStates().getStateByName(cell.getCurrentstate()).getAttributes().get("color");
		if(cell.isSugarCell()) {
			color = setSugarColor(cell, color);
		}
		triangle.setFill(Color.web(color));

		if (isEven % 2 == 0) {
			triangle.getPoints().addAll(adjustedNormalTrianglePoints);
		} else {
			triangle.getPoints().addAll(adjustedUpsideDownTrianglePoints);
		}
		return triangle;
	}

	private String setSugarColor(Cell cell, String color) {
		int sugarLevel = ((SugarAgent) cell).getPatch().getSugar();
		color = colorList[sugarLevel - 1];
		return color;
	}

	public Shape renderPatch(Cell cell) {
		double rows = myConfig.getNumRows();
		double cols = myConfig.getNumCols();
		double radiusX = calculateSize(SceneConstant.GRID_WIDTH.getValue(), cols) / 5;
		double radiusY = calculateSize(SceneConstant.GRID_HEIGHT.getValue(), rows) / 5;

		Shape ellipse = new Ellipse(radiusX, radiusY);
		String color = myConfig.getAllStates().getStateByName(cell.getCurrentstate()).getAttributes().get("color");
		ellipse.setFill(Color.web(color));

		return ellipse;

	}

	private double calculateSize(double edgeSize, double numSpots) {
		return (edgeSize / numSpots);
	}

	private Double[] getAdjustedPoints(boolean normal, int rows, int cols) {
		Double[] adjusted = new Double[normalTrianglePoints.length];
		if (normal) {
			adjusted = Arrays.copyOf(normalTrianglePoints, normalTrianglePoints.length);
			adjusted[4] = normalTrianglePoints[4]
					/ (TRIANGLE_WIDTH / ((SceneConstant.GRID_WIDTH.getValue() - cols) / cols));
			adjusted[0] = normalTrianglePoints[0]
					/ (TRIANGLE_WIDTH / ((SceneConstant.GRID_WIDTH.getValue() - cols) / cols));
		} else {
			adjusted = Arrays.copyOf(upsideDownTrianglePoints, upsideDownTrianglePoints.length);
			adjusted[4] = upsideDownTrianglePoints[4]
					/ (TRIANGLE_WIDTH / ((SceneConstant.GRID_WIDTH.getValue() - cols) / cols));
			adjusted[2] = upsideDownTrianglePoints[2]
					/ (TRIANGLE_WIDTH / ((SceneConstant.GRID_WIDTH.getValue() - cols) / cols));
		}
		return adjusted;
	}

}
