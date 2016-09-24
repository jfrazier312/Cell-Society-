package model;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Hexagon extends Cell implements view.GameWorld {

	private double HEX_WIDTH;
	private int[] rowDeltas = { -1, 0, 1, 0, 1, 1, -1, -1 };
	private int[] colDeltas = { 0, -1, 0, 1, 1, -1, 1, -1 };

	// TODO: Jordan: find size of hexagon


	// private Double[] hexagonPoints = {10.0, 10.0,
	// 10.0, 10.0,
	// 20.0, 20.0,
	// 30.0, 30.0,
	// 10.0 , 10.0,
	// 20.0, 12.0
	// };

	public Hexagon(int row, int col, boolean isEven) {
		super(row, col);
	}

	@Override
	public Node render() {
		/*   Need to add config.getNumberColumns() in there
		Configuration config = ConfigurationLoader.loader().getConfig();
		double height = calculateSize(config.getGridHeight(), config.getNumberRows());
		double width = calculateSize(config.getGridWidth(), config.getNumberCols());
		 */
		
//		double hexWidth = GRID_WIDTH / config.getNumberRows();
//		double size = HEX_WIDTH / Math.sqrt(3);
		
		int numRows = 5;
		HEX_WIDTH = GRID_WIDTH / numRows;
		double size = HEX_WIDTH / Math.sqrt(3);
		
		Polygon hexagon = new Polygon();
		double[] center = new double[2];

		
		if (i % (numPolygons*2) == 0) {
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
}
