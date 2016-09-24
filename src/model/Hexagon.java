package model;

import javafx.scene.Node;
import javafx.scene.shape.Polygon;

public class Hexagon extends Cell {

	private int[] rowDeltas = {-1, 0, 1, 0, 1, 1, -1, -1};
	private int[] colDeltas = {0, -1, 0, 1, 1, -1, 1, -1};
	
	private Double[] hexagonPoints = {10.0, 10.0,
			10.0, 10.0,
			20.0, 20.0,
			30.0, 30.0, 
			10.0 , 10.0, 
			20.0, 12.0
	};
	
	public Hexagon(int row, int col, boolean isEven) {
		super(row, col);
	}

	@Override
	public Node render() {
		Polygon hexagon = new Polygon();
		hexagon.getPoints().addAll(hexagonPoints);
		
		return hexagon;
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
