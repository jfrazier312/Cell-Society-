package model;

import java.util.Arrays;

import config.Configuration;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import view.SceneConstant;

public class Render {

	private Configuration myConfig;
	private int isEven;

	private static final double TRIANGLE_DIMENSION = 20.0;
	private Double[] normalTrianglePoints = { TRIANGLE_DIMENSION / 2, 0.0, 0.0, TRIANGLE_DIMENSION, TRIANGLE_DIMENSION,
			TRIANGLE_DIMENSION };
	private Double[] upsideDownTrianglePoints = { 0.0, 0.0, TRIANGLE_DIMENSION, 0.0, TRIANGLE_DIMENSION / 2,
			TRIANGLE_DIMENSION };

	private String[] colorList = { "#f6edd6", "#f4e5c0", "#f4dea9", "#f4d790", "#e7c675", "#ceaa55", "#b8943d",
			"#a58026", "#967013", "#7e5900", "#9c5f00" };

	public Render(Configuration config) {
		myConfig = config;
	}

	public Shape chooseRender(Cell cell, String shape, int isEven, double x, double y, int i, int j) {
		this.isEven = isEven;
		Shape renderedShape = null;
		if (shape.equals("rectangle")) {
			renderedShape = renderRectangle(cell, x, y, i, j);
		} else if (shape.equals("triangle")) {
			renderedShape = renderTriangle(cell, x, y, i, j);
		} else if (shape.equals("hexagon")) {
			renderedShape = renderHexagon(cell, x, y, i, j);
		} else {
			// fuck = new Polygon();
		}
		return renderedShape;
	}

	private Shape renderRectangle(Cell cell, double x, double y, int i, int j) {
		double rows = myConfig.getNumRows();
		double cols = myConfig.getNumCols();
		double width = calculateSize(SceneConstant.GRID_WIDTH.getValue(), cols);
		double height = calculateSize(SceneConstant.GRID_HEIGHT.getValue(), rows);

		Shape rect = new javafx.scene.shape.Rectangle(x + (width * i), y + (width * j), width, height);
		String color = myConfig.getAllStates().getStateByName(cell.getCurrentstate()).getAttributes().get("color");
		if (cell.isSugarCell()) {
			color = setSugarColor(cell, color);
		}
		if (cell.isAntCell() && !isSource(cell)) {
			color = setAntColor(cell, color);
		}
		rect.setFill(Color.web(color));
		return rect;
	}

	private Shape renderTriangle(Cell cell, double x, double y, int i, int j) {
		int rows = myConfig.getNumRows();
		int cols = myConfig.getNumCols();
		double width = calculateSize(SceneConstant.GRID_WIDTH.getValue(), cols);
		double height = calculateSize(SceneConstant.GRID_HEIGHT.getValue(), rows);

		Polygon triangle = new Polygon();

		String color = myConfig.getAllStates().getStateByName(cell.getCurrentstate()).getAttributes().get("color");
		if (cell.isSugarCell()) {
			color = setSugarColor(cell, color);
		}
		triangle.setFill(Color.web(color));
		Double[] adjustedNormalTrianglePoints = { (x + width * j), (y + height * i), (x + width * j) + width,
				(y + height * i), (x + width * j) + width / 2, (y + height * i) + height };
		Double[] adjustedUpsideDownTrianglePoints = { (x + width * j) + (width / 2), (y + height * (i + 1)),
				(x + width * (j + 1)), (y + height * i), (x + width * (j + 1)) + width / 2, (y + height * (i + 1)) };

		if (isEven % 2 == 0) {
			triangle.getPoints().addAll(adjustedNormalTrianglePoints);
		} else {
			triangle.getPoints().addAll(adjustedUpsideDownTrianglePoints);
		}
		return triangle;
	}

	@Deprecated
	private Shape renderTriangle(Cell cell) {
		int rows = myConfig.getNumRows();
		int cols = myConfig.getNumCols();
		if (cols % 2 == 0) {
			cols--;
			myConfig.setNumCols(cols);
		}
		Double[] adjustedNormalTrianglePoints = getAdjustedPoints(true, rows, cols);
		Double[] adjustedUpsideDownTrianglePoints = getAdjustedPoints(false, rows, cols);

		Polygon triangle = new Polygon();
		String color = myConfig.getAllStates().getStateByName(cell.getCurrentstate()).getAttributes().get("color");
		if (cell.isSugarCell()) {
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

	public Shape renderHexagon(Cell cell, double x, double y, int i, int j) {
		double rows = myConfig.getNumRows();
		double cols = myConfig.getNumCols();
		double width = calculateSize(SceneConstant.GRID_WIDTH.getValue(), cols);
		double height = calculateSize(SceneConstant.GRID_HEIGHT.getValue(), rows);

		Polygon hexagon = new Polygon();
		double[] center = new double[2];
		if (isEven % 2 == 0) {
			center[0] = x + width * j;
			center[1] = y + height * i;
		} else {
			center[0] = x + width * j;
			center[1] = (y + height * i) + height/2;
		}

		for (int k = 0; k < 6; k++) {
			Double[] a = getPoint(center, width / 2, k);
			hexagon.getPoints().addAll(a[0], a[1]);
		}
		
//		if (isEven % cols == cols - 1 || (isEven != 0 && isEven % cols == 0)) {
//			return renderOffset(width / 2);
//		} else {
//			for (int k = 0; k < 6; k++) {
//				Double[] a = getPoint(center, width / 2, k);
//				hexagon.getPoints().addAll(a[0], a[1]);
//			}
//		}
		String color = myConfig.getAllStates().getStateByName(cell.getCurrentstate()).getAttributes().get("color");
		if (cell.isSugarCell()) {
			color = setSugarColor(cell, color);
		}
		hexagon.setFill(Color.web(color));
		return hexagon;
	}

	private Double[] getPoint(double[] center, double size, double i) {
		Double[] a = new Double[2];
		double angle_deg = 60 * i;
		double angle_rad = Math.PI / 180 * angle_deg;
		a[0] = center[0] + size * Math.cos(angle_rad);
		a[1] = center[1] + size * Math.sin(angle_rad);
		return a;
	}

	private Shape renderOffset(double width) {
		Line offset = new Line(0.0, width / 4, width / 2, width / 4);
		offset.setFill(Color.WHITE);
		return offset;
	}

	private String setSugarColor(Cell cell, String color) {
		int sugarLevel = ((SugarAgent) cell).getPatch().getSugar();
		color = colorList[sugarLevel - 1];
		return color;
	}

	private String setAntColor(Cell cell, String color) {
		int antLevel = ((AntCell) cell).getAnts().size();
		color = colorList[antLevel];
		return color;
	}

	private boolean isSource(Cell cell) {
		return cell.getCurrentstate().equals("home") || cell.getCurrentstate().equals("food")
				|| cell.getCurrentstate().equals("obstacle");
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
			adjusted = adjustTriangle(rows, cols, adjusted, normalTrianglePoints, 4, 0, 3, 5);
		} else {
			adjusted = Arrays.copyOf(upsideDownTrianglePoints, upsideDownTrianglePoints.length);
			adjusted = adjustTriangle(rows, cols, adjusted, upsideDownTrianglePoints, 4, 2, 3, 5);
		}
		return adjusted;
	}

	private Double[] adjustTriangle(int rows, int cols, Double[] adjusted, Double[] original, int xpoint1, int xpoint2,
			int ypoint1, int ypoint2) {
		adjusted[xpoint1] = original[xpoint1]
				/ (TRIANGLE_DIMENSION / ((SceneConstant.GRID_WIDTH.getValue() - cols) / cols));
		adjusted[xpoint2] = original[xpoint2]
				/ (TRIANGLE_DIMENSION / ((SceneConstant.GRID_WIDTH.getValue() - cols) / cols));
		adjusted[ypoint1] = original[ypoint1]
				/ (TRIANGLE_DIMENSION / ((SceneConstant.GRID_HEIGHT.getValue() - rows) / rows));
		adjusted[ypoint2] = original[ypoint2]
				/ (TRIANGLE_DIMENSION / ((SceneConstant.GRID_HEIGHT.getValue() - rows) / rows));
		return adjusted;
	}

}
