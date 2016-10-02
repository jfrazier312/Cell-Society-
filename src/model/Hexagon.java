//package model;
//
//import javafx.scene.Node;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Line;
//import javafx.scene.shape.Polygon;
//
//public class Hexagon extends Cell implements view.GameWorld {
//
//	private double HEX_WIDTH;
//	private int[] rowDeltas = { -1, -1, 0, 1, 1, 0};
//	private int[] colDeltas = { 0, -1, -1, 0, 1, 1};
//
//	public final static boolean orFLAT = true;
//	public final static boolean orPOINT = false;
//
//	public static boolean XYVertex = true; // true: x,y are the co-ords of the
//											// first vertex.
//	// false: x,y are the co-ords of the top left rect. co-ord.
//
//	private static int BORDERS = 50; // default number of pixels for the border.
//
//	private static double HEX_SIZE = 0; // length of one side
//	private static double HEX_SMALL_SIDE = 0; // short side of 30o triangle outside of each hex
//	private static double r = 0; // radius of inscribed circle (centre to middle of
//								// each side). r= h/2
//	private static double h = 0; // height. Distance between centres of two
//								// adjacent hexes. Distance between two opposite
//								// sides in a hex.
//
//	// TODO: Jordan: find size of hexagon
//
//	public Hexagon(int row, int col, boolean isEven) {
//		super(row, col);
//		
//		double baseX = GRID_PADDING;
//		double baseY = (SCENE_HEIGHT - GRID_HEIGHT) / 2 +  HEX_SMALL_SIDE; 
//		
//		int numRows = 5;
//		HEX_WIDTH = GRID_WIDTH / numRows;
//		HEX_SIZE = HEX_WIDTH / Math.sqrt(3);
//		HEX_SMALL_SIDE = HEX_SIZE / 2;
//		
//		double x0 = (row * HEX_WIDTH);
//		double y0 = (col * (HEX_SIZE + HEX_SMALL_SIDE));
//
//		double y = y0 + baseY;
//		double x = x0 + baseX;
//
//		double[] cx, cy;
//
//		if (XYVertex)
//			cx = new double[] { x, x + HEX_SIZE, x + HEX_SIZE + HEX_SMALL_SIDE, x + HEX_SIZE, x, x - HEX_SMALL_SIDE };
//		else
//			cx = new double[] { x + HEX_SMALL_SIDE, x + HEX_SIZE + HEX_SMALL_SIDE, x + HEX_SIZE + HEX_SMALL_SIDE + HEX_SMALL_SIDE, x + HEX_SIZE + HEX_SMALL_SIDE, x + HEX_SMALL_SIDE, x }; 
//
//		cy = new double[] { y, y, y + r, y + r + r, y + r + r, y + r };
//
//	}
//
//	public static void setXYasVertex(boolean b) {
//		XYVertex = b;
//	}
//
//	public static void setBorders(int b) {
//		BORDERS = b;
//	}
//
//	/**
//	 * This functions takes the Side length in pixels and uses that as the basic
//	 * dimension of the hex. It calculates all other needed constants from this
//	 * dimension.
//	 */
//	public static void setSide(int side) {
//		HEX_SIZE = side;
//		HEX_SMALL_SIDE = (HEX_SIZE / 2); // t = s sin(30) = (int) CalculateH(s);
//		r = (HEX_SIZE * 0.8660254037844); // r = s cos(30) = (int) CalculateR(s);
//		h = 2 * r;
//	}
//
//	public static void setHeight(int height) {
//		h = height; // h = basic dimension: height (distance between two adj
//					// centresr aka size)
//		r = h / 2; // r = radius of inscribed circle
//		HEX_SIZE = (h / 1.73205); // s = (h/2)/cos(30)= (h/2) / (sqrt(3)/2) = h /
//									// sqrt(3)
//		HEX_SMALL_SIDE = (r / 1.73205); // t = (h/2) tan30 = (h/2) 1/sqrt(3) = h / (2
//									// sqrt(3)) = r / sqrt(3)
//	}
//
//
//		if (XYVertex)
//			cx = new double[] { x, x + HEX_SIZE, x + HEX_SIZE + HEX_SMALL_SIDE, x + HEX_SIZE, x, x - HEX_SMALL_SIDE };
//		else
//			cx = new double[] { x + HEX_SMALL_SIDE, x + HEX_SIZE + HEX_SMALL_SIDE, x + HEX_SIZE + HEX_SMALL_SIDE + HEX_SMALL_SIDE, x + HEX_SIZE + HEX_SMALL_SIDE, x + HEX_SMALL_SIDE, x }; 
//
//		cy = new double[] { y, y, y + r, y + r + r, y + r + r, y + r };
//
//	}
//
//	public static void setXYasVertex(boolean b) {
//		XYVertex = b;
//	}
//
//	public static void setBorders(int b) {
//		BORDERS = b;
//	}
//
//	/**
//	 * This functions takes the Side length in pixels and uses that as the basic
//	 * dimension of the hex. It calculates all other needed constants from this
//	 * dimension.
//	 */
//	public static void setSide(int side) {
//		HEX_SIZE = side;
//		HEX_SMALL_SIDE = (HEX_SIZE / 2); // t = s sin(30) = (int) CalculateH(s);
//		r = (HEX_SIZE * 0.8660254037844); // r = s cos(30) = (int) CalculateR(s);
//		h = 2 * r;
//	}
//
//	public static void setHeight(int height) {
//		h = height; // h = basic dimension: height (distance between two adj
//					// centresr aka size)
//		r = h / 2; // r = radius of inscribed circle
//		HEX_SIZE = (h / 1.73205); // s = (h/2)/cos(30)= (h/2) / (sqrt(3)/2) = h /
//									// sqrt(3)
//		HEX_SMALL_SIDE = (r / 1.73205); // t = (h/2) tan30 = (h/2) 1/sqrt(3) = h / (2
//									// sqrt(3)) = r / sqrt(3)
//	}
//
//	@Override
//	public Node render() {
//		/*
//		 * Need to add config.getNumberColumns() in there Configuration config =
//		 * ConfigurationLoader.loader().getConfig(); double height =
//		 * calculateSize(config.getGridHeight(), config.getNumberRows()); double
//		 * width = calculateSize(config.getGridWidth(), config.getNumberCols());
//		 */
//
//		// double hexWidth = GRID_WIDTH / config.getNumberRows();
//		// double size = HEX_WIDTH / Math.sqrt(3);
//
//		int numRows = 5;
//		HEX_WIDTH = GRID_WIDTH / numRows;
//		HEX_SIZE = HEX_WIDTH / Math.sqrt(3);
//
//		Polygon hexagon = new Polygon();
//		double[] center = new double[2];
//
//		if (i % (numPolygons * 2) == 0) {
//			System.out.println("i: " + i);
//			Node b = renderOffset();
//			flowpane.getChildren().add(b);
//		} else {
//			Node a = renderHex();
//			flowpane.getChildren().add(a);
//		}
//
//		// hexagon.setStroke(Color.WHITE);
//		// hexagon.setStrokeWidth(.1);
//		for (int i = 0; i < 6; i++) {
//			Double[] a = getPoint(center, size, i);
//			hexagon.getPoints().addAll(a[0], a[1]);
//		}
//
//		return hexagon;
//	}
//
//	private Double[] getPoint(double[] center, double size, double i) {
//		Double[] a = new Double[2];
//		double angle_deg = 60 * i + 30;
//		double angle_rad = Math.PI / 180 * angle_deg;
//		a[0] = center[0] + size * Math.cos(angle_rad);
//		a[1] = center[1] + size * Math.sin(angle_rad);
//		return a;
//	}
//
//	private Node renderOffset() {
//		Line offset = new Line(0.0, HEX_WIDTH / 4, HEX_WIDTH / 2, HEX_WIDTH / 4);
//		offset.setFill(Color.WHITE);
//		return offset;
//	}
//
//	// @Override
//	// public Node render() {
//	// Polygon hexagon = new Polygon();
//	// hexagon.getPoints().addAll(hexagonPoints);
//	//
//	// return hexagon;
//	// }
//
//	@Override
//	public int[] getRowDeltas() {
//		return rowDeltas;
//	}
//
//	@Override
//	public int[] getColDeltas() {
//		return colDeltas;
//	}
//}
