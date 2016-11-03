/*package model;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

@Deprecated
public class Hexagon extends Cell {

	@Override
	public Node render() {
		
		HEX_WIDTH = GRID_WIDTH / numRows;
		HEX_SIZE = HEX_WIDTH / Math.sqrt(3);

		Polygon hexagon = new Polygon();
		double[] center = new double[2];

		if (i % (numPolygons * 2) == 0) {
			System.out.println("i: " + i);
			Node b = renderOffset();
			flowpane.getChildren().add(b);
		} else {
			Node a = renderHex();
			flowpane.getChildren().add(a);
		}

		// hexagon.setStroke(Color.WHITE);
		// hexagon.setStrokeWidth(.1);
		for (int i = 0; i < 6; i++) {
			Double[] a = getPoint(center, size, i);
			hexagon.getPoints().addAll(a[0], a[1]);
		}

		return hexagon;
	}

	private Double[] getPoint(double[] center, double size, double i) {
		Double[] a = new Double[2];
		double angle_deg = 60 * i + 30;
		double angle_rad = Math.PI / 180 * angle_deg;
		a[0] = center[0] + size * Math.cos(angle_rad);
		a[1] = center[1] + size * Math.sin(angle_rad);
		return a;
	}

	private Node renderOffset() {
		Line offset = new Line(0.0, HEX_WIDTH / 4, HEX_WIDTH / 2, HEX_WIDTH / 4);
		offset.setFill(Color.WHITE);
		return offset;
	}

	// @Override
	// public Node render() {
	// Polygon hexagon = new Polygon();
	// hexagon.getPoints().addAll(hexagonPoints);
	//
	// return hexagon;
	// }

	@Override
	public int[] getRowDeltas() {
		return rowDeltas;
	}

	@Override
	public int[] getColDeltas() {
		return colDeltas;
	}
}*/
