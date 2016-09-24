package view;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class Tester extends Application {

	boolean isEven = true;
	// scale should be gotten form xml
	double scale = 4.0;
	int numPolygons;

	double SIZE = 10;
	double WIDTH = 2 * SIZE * Math.sqrt(3) / 2;
	double PREF_SIZE = 300 + SIZE;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		Group root = new Group();
		Scene scene = new Scene(root, 800, 500);

		// test rendering shapes here

		SimulationSlider slider = new SimulationSlider("fea");
		root.getChildren().add(slider.getSlider());

		GridPane gridpane = new GridPane();
		gridpane.setPrefSize(300, 400);
		
		FlowPane flowpane = new FlowPane(0.0, -1.0);
		flowpane.setPrefSize(PREF_SIZE, 400);
		flowpane.setPrefWrapLength(PREF_SIZE);

		numPolygons = (int) ((PREF_SIZE) / (WIDTH));
		
		for(int i = 0; i < 100; i++) {
			if (i % (numPolygons*2) == 0) {
				System.out.println("i: " + i);
				Node b = renderOffset();
				flowpane.getChildren().add(b);
			} else {
				Node a = renderHex();
				flowpane.getChildren().add(a);
			}
			
		}
		
		boolean asdf = true;

//		for (int i = 0; i < 10; i++) {
//			for (int j = 0; j < 50; j++) {
//				// Node a = renderTriangle();
//				// if( i % rowSize == 0) {
//				// add offset;
//
//				if (i % 2 == 0 && asdf) {
//					System.out.println("i: " + i + "j: " + j);
//					Node b = renderOffset();
//					gridpane.add(b, i, j);
//					asdf = false;
//				} else {
//
//					Node a = renderHex();
//					gridpane.add(a, i, j);
//				}
//			}
//			asdf = true;
//			
//		}

		// for (int i = 0; i < 1000; i++) {
		// Rectangle a = new Rectangle(10, 10);
		// //set fill specified by state
		// a.setFill(Color.web("0x0000FF"));
		// flowpane.getChildren().add(a);
		// }
		
		//root.getChildre().add9lgjieawg
		root.getChildren().add(flowpane);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public Node renderOffset() {
		Line offset = new Line(0.0, WIDTH/4, WIDTH/2, WIDTH/4);
		offset.setFill(Color.WHITE);
//		offset.setStrokeWidth(0);

		/*
		 * Polygon offset = new Polygon(); offset.setFill(Color.WHITE);
		 * offset.setStrokeWidth(.001); double[] center = {0,0}; for(int i = -2;
		 * i < 2; i++) { Double[] a = getPoint(center, 10.0, i);
		 * offset.getPoints().addAll(a[0], a[1]); }
		 */
		return offset;
	}

	public Node renderTriangle() {
		Polygon triangle = new Polygon();
		triangle.setStroke(Color.WHITE);
		triangle.setStrokeWidth(.001);
		if (isEven) {
			isEven = false;
			triangle.getPoints().addAll(new Double[] { 10.0, 0.0, 0.0, 20.0, 20.0, 20.0 });
		} else {
			isEven = true;
			triangle.getPoints().addAll(new Double[] { 0.0, 0.0, 20.0, 0.0, 10.0, 20.0 });
		}

		// this doenst actually do anything
		triangle.setLayoutX(scale * triangle.getLayoutX());
		triangle.setLayoutY(scale * triangle.getLayoutY());
		return triangle;
	}

	public Node renderHexagon() {
		Polygon hexagon = new Polygon();
		hexagon.setStroke(Color.WHITE);
		hexagon.setStrokeWidth(.001);
		hexagon.getPoints()
				.addAll(new Double[] { 6.67, 0.0, 0.0, 10.0, 6.67, 20.0, 13.34, 20.0, 20.0, 10.00, 13.34, 0.0 });
		return hexagon;

	}

	public Node renderHex() {
		Polygon hexagon = new Polygon();
		double[] center = new double[2];
		// if(isEven){ //doesn't matter what center is because flowpane fixes it
		// center[0] = 40.0;
		// center[1] = 40.0;
		// isEven = false;
		// }
		// hexagon.setStroke(Color.WHITE);
		// hexagon.setStrokeWidth(.1);
		for (int i = 0; i < 6; i++) {
			Double[] a = getPoint(center, SIZE, i);
			hexagon.getPoints().addAll(a[0], a[1]);
		}

		return hexagon;
	}

	public Double[] getPoint(double[] center, double size, double i) {
		Double[] a = new Double[2];
		double angle_deg = 60 * i + 30;
		double angle_rad = Math.PI / 180 * angle_deg;
		a[0] = center[0] + size * Math.cos(angle_rad);
		a[1] = center[1] + size * Math.sin(angle_rad);
		return a;
	}

	/*
	 * octagon 6.67, 0.0, 0.0, 6.67.0, 0.0, 13.34, 6.67, 20.0, 13.34, 20.0,
	 * 20.0, 13.34, 20.0, 6.67, 13.34, 0.0
	 */

}
