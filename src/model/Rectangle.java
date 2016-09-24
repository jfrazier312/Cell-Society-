package model;

import config.Configuration;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Rectangle extends Cell implements view.GameWorld {
	
	//included diagonals
	private int[] rowDeltas = {-1, 0, 1, 0, 1, 1, -1, -1};
	private int[] colDeltas = {0, -1, 0, 1, 1, -1, 1, -1};

	public Rectangle(int row, int col) {
		super(row, col);
		
	}

	@Override
	public Node render(String state) {
		// TODO: Jordan - update render for XML
		
/*   Need to add config.getNumberColumns() in there
		double height = calculateSize(config.getGridHeight(), config.getNumberRows());
		double width = calculateSize(config.getGridWidth(), config.getNumberCols());
		javafx.scene.shape.Rectangle a = new javafx.scene.shape.Rectangle(width, height);
*/
		double height = calculateSize(GRID_WIDTH, 10);
		double width = calculateSize(GRID_HEIGHT, 5);

		
		javafx.scene.shape.Rectangle a = new javafx.scene.shape.Rectangle(width, height);
		// based on size of grid
		// based on pixels on windows 
//		Configuration config = ConfigurationLoader.loader().getConfig();
//		String color = config.getAllStates().getColors().get(this.getCurrentstate());
		
		
		// Needs to be a hex value ( ##0000FF, or 0x0000FF)
		if(state.equals("ALIVE")){
			a.setFill(Color.RED);
		}else {
			a.setFill(Color.BLACK);
		}
		return a;
	}
	
	private double calculateSize(double edgeSize, double numSpots) {
		return edgeSize / numSpots;
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
