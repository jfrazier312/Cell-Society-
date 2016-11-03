package model;

import config.Configuration;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import view.SceneConstant;

//This entire file is part of my masterpiece.
//Jordan Frazier

/**
 * Defines the calculations to render different shapes. To add new shapes,
 * define the shape's calculations here. Used by CellGrid.java to render
 * specified shapes onto the UI.
 * 
 * This class serves as the method to render any shape. Simply create a new instance of
 * this class class, then call chooseRender() with a few inputs to return a Shape that
 * you can immediately place into your data structure for viewing. This class has zero
 * code duplication in it, each block of code that can be reused is place inside of a 
 * method. Easily extendable, this class allows new shapes to be created quickly and easily.
 * This class was created late in the project, after realizing the necessity of a Render class,
 * which replaced the splitting of the functionality of rendering each shape into its own class. 
 * 
 * @author Jordan Frazier
 *
 */
public class Render {

	private String[] colorList = { "#f6edd6", "#f4e5c0", "#f4dea9", "#f4d790", "#e7c675", "#ceaa55", "#b8943d",
			"#a58026", "#967013", "#7e5900", "#9c5f00" };

	private Configuration myConfig;
	private int isEvenColumn;
	private double numRows;
	private double numCols;
	private double cellWidth;
	private double cellHeight;

	public Render(Configuration config) {
		myConfig = config;
		numRows = myConfig.getNumRows();
		numCols = myConfig.getNumCols();
		cellWidth = calculateSize(SceneConstant.GRID_WIDTH.getValue(), numCols);
		cellHeight = calculateSize(SceneConstant.GRID_HEIGHT.getValue(), numRows);
	}

	/**
	 * Chooses which shape to render. Must add to this method when new shapes are
	 * implemented
	 * 
	 * @param cell - the cell to render
	 * @param shape - the shape of the cell
	 * @param isEvenColumn  - specifies whether the cell is in an even column
	 * @param xPos
	 * @param yPos
	 * @param xIndex
	 * @param yIndex
	 * @return the rendered shape
	 */
	public Shape chooseRender(Cell cell, String shape, int isEvenColumn, double xPos, double yPos, int yIndex,
			int xIndex) {
		
		this.isEvenColumn = isEvenColumn;
		Shape renderedShape = null;
		if (shape.equals("rectangle")) {
			renderedShape = renderRectangle(cell, xPos, yPos, xIndex, yIndex);
		} else if (shape.equals("triangle")) {
			renderedShape = renderTriangle(cell, xPos, yPos, xIndex, yIndex);
		} else if (shape.equals("hexagon")) {
			renderedShape = renderHexagon(cell, xPos, yPos, xIndex, yIndex);
		} else {
			// new shapes go here
		}
		return renderedShape;
	}

	private Shape renderRectangle(Cell cell, double xPos, double yPos, int xIndex, int yIndex) {
		double newXPos = xPos + (cellWidth * xIndex);
		double newYPos = yPos + (cellHeight * yIndex);
		Shape rect = new javafx.scene.shape.Rectangle(newXPos, newYPos, cellWidth, cellHeight);
		setColorOnShape(cell, rect);
		return rect;
	}

	private Shape renderTriangle(Cell cell, double xPos, double yPos, int xIndex, int yIndex) {
		checkOddColumn();
		Polygon triangle = new Polygon();
		setTrianglePoints(triangle, xPos, yPos, xIndex, yIndex);
		setColorOnShape(cell, triangle);
		return triangle;
	}

	private Shape renderHexagon(Cell cell, double xPos, double yPos, int xIndex, int yIndex) {
		Polygon hexagon = new Polygon();
		double[] center = getHexagonCenter(xPos, yPos, xIndex, yIndex);
		setHexagonPoints(hexagon, center);
		setColorOnShape(cell, hexagon);
		return hexagon;
	}

	private void setColorOnShape(Cell cell, Shape shape) {
		String color = getColorFromConfig(cell);
		shape.setFill(Color.web(color));
	}

	private String getColorFromConfig(Cell cell) {
		String color = myConfig.getAllStates().getStateByName(cell.getCurrentstate()).getAttributes().get("color");
		color = checkSugarOrAntCell(cell, color);
		return color;
	}

	private void setTrianglePoints(Polygon triangle, double xPos, double yPos, int xIndex, int yIndex) {
		cellWidth = cellWidth * 2;
		double initialXPos = xPos + (xIndex*cellWidth)/2;
		double initialYPos = yPos + (yIndex*cellHeight);
		double xPos1 = initialXPos - cellWidth/2;
		double xPos2 = initialXPos + cellWidth/2;
		double yPos1 = initialYPos - cellHeight/2;
		double yPos2 = initialYPos + cellHeight/2;
		setTriangleOrientation(triangle, initialXPos, xPos1, xPos2, yPos1, yPos2);
	}

	private void setTriangleOrientation(Polygon triangle, double initialXPos, double xPos1, double xPos2,
			double yPos1, double yPos2) {
		if (isEvenColumn % 2 == 0) {
			setTrianglePointsArray(triangle, xPos1, yPos2, xPos2, yPos2, initialXPos, yPos1);
		} else {
			setTrianglePointsArray(triangle, xPos1, yPos1, xPos2, yPos1, initialXPos, yPos2);
		}
	}

	private void setTrianglePointsArray(Polygon triangle, double x1, double y1, double x2, double y2, double x3,
			double y3) {
		Double[] points = { x1, y1, x2, y2, x3, y3 };
		triangle.getPoints().addAll(points);
	}

	private double[] getHexagonCenter(double xPos, double yPos, int xIndex, int yIndex) {
		double[] center = new double[2];
		double newCenterXPos = xPos + cellWidth * xIndex;
		double newCenterYPos = yPos + cellHeight * yIndex;
		if (isEvenColumn % 2 == 0) {
			center[0] = newCenterXPos;
			center[1] = newCenterYPos;
		} else {
			center[0] = newCenterXPos;
			center[1] = newCenterYPos + cellHeight / 2;
		}
		return center;
	}
	
	/**
	 * The loop is currently hard coded to loop 6 times to create the 6 points on a hexagon. 
	 * Theoretically, this method can be altered easily to accommodate more polygons.  
	 * @param hexagon
	 * @param center
	 */
	private void setHexagonPoints(Polygon hexagon, double[] center) {
		for (int k = 0; k < 6; k++) {
			Double[] a = getPoint(center, cellWidth / 2, k);
			hexagon.getPoints().addAll(a[0], a[1]);
		}
	}
	
	private String checkSugarOrAntCell(Cell cell, String color) {
		if (cell.isSugarCell()) {
			color = setSugarColor(cell, color);
		}
		if (cell.isAntCell() && !isSource(cell)) {
			color = setAntColor(cell, color);
		}
		return color;
	}

	/**
	 * This is a code hack. Used by triangles, this method is a current workaround to determining
	 * how to orient the first triangle on each row. If a user specifies an even amount of rows,
	 * then the triangles will stack up on each other, instead of alternating upside down as
	 * they should. 
	 */
	private void checkOddColumn() {
		if (numCols % 2 == 0) {
			numCols--;
			myConfig.setNumCols((int) numCols);
		}
	}

	/**
	 * By changing the angle, this method potentially can be applied to polygons of several sides.
	 * @param center
	 * @param size
	 * @param i
	 * @return
	 */
	private Double[] getPoint(double[] center, double size, double i) {
		Double[] a = new Double[2];
		double angle_deg = 60 * i;
		double angle_rad = Math.PI / 180 * angle_deg;
		a[0] = center[0] + size * Math.cos(angle_rad);
		a[1] = center[1] + size * Math.sin(angle_rad);
		return a;
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

	/**
	 * Renders the 'patch' shape used in sugar and ant simulations
	 * 
	 * @param cell - the cell to get the color
	 * @param xPos - x coordinate of the cell
	 * @param yPos - y coordinate of the cell
	 * @param xIndex - x index of the cell in grid 
	 * @param yIndex - y index of the cell in grid
	 * @return
	 */
	public Shape renderPatch(Cell cell, double xPos, double yPos, int xIndex, int yIndex) {
		double radiusX = calculateSize(SceneConstant.GRID_WIDTH.getValue(), numCols) / 5;
		double radiusY = calculateSize(SceneConstant.GRID_HEIGHT.getValue(), numRows) / 5;
		Shape ellipse = new Ellipse(xPos + (cellWidth * xIndex) + cellWidth / 2,
				yPos + (cellHeight * yIndex) + cellHeight / 2, radiusX, radiusY);
		// Set patch cell to darkest color allowed by sugar simulation
		ellipse.setFill(Color.web(colorList[10]));
		return ellipse;
	}

	private double calculateSize(double edgeSize, double numSpots) {
		return (edgeSize / numSpots);
	}

}
