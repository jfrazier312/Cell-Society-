package model;

import config.ConfigurationLoader;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Triangle extends Cell implements view.GameWorld {

	private int[] rowDeltas = { -1, 0, 1 };
	private int isEven;

	private int[] evenColDeltas = { 0, 1, 0 };
	private int[] oddColDeltas = { 0, -1, 0 };

	// Will need to scale this based on XML inputs?
	private Double[] normalTrianglePoints = { 10.0, 0.0, 0.0, 20.0, 20.0, 20.0 };
	private Double[] upsideDownTrianglePoints = { 0.0, 0.0, 20.0, 0.0, 10.0, 20.0 };

	public Triangle(int row, int col, int isEven) {
		super(row, col);
		this.isEven = isEven;
	}

	// Need to change spacing in flowpane if its a triangle
	@Override
	public Node render() {
		double rows = ConfigurationLoader.getConfig().getNumRows();
		double cols = ConfigurationLoader.getConfig().getNumCols();
		
		if(cols % 2 != 0) {
			// make size slightly larger to fill space? 
		}
		
//		double width = calculateSize(GRID_WIDTH, cols);
//		double height = calculateSize(GRID_HEIGHT, rows);
		
		for(int i = 0; i < normalTrianglePoints.length; i++) {
			// in this case, if columns goes down to 10, double all to fit
			normalTrianglePoints[i] = normalTrianglePoints[i] / ( 20 / (GRID_WIDTH / cols))  ;
			upsideDownTrianglePoints[i] = upsideDownTrianglePoints[i] / ( 20 / (GRID_WIDTH / cols))  ;
		}

		Polygon triangle = new Polygon();
//		rect.setStroke(Color.RED);
//		rect.setStrokeWidth(0.1);
		String color = ConfigurationLoader.getConfig().getAllStates().getStateByName(getCurrentstate()).getAttributes().get("color");
		triangle.setFill(Color.web(color));
		
		if (isEven % 2 == 0) {
			triangle.getPoints().addAll(normalTrianglePoints);
		} else {
			triangle.getPoints().addAll(upsideDownTrianglePoints);
		}
		return triangle;
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
