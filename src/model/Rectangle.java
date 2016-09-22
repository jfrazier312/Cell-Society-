package model;

import config.Configuration;
import javafx.scene.Node;

public class Rectangle extends Cell {
	
	//included diagonals
	private int[] rowDeltas = {-1, 0, 1, 0, 1, 1, -1, -1};
	private int[] colDeltas = {0, -1, 0, 1, 1, -1, 1, -1};

	public Rectangle(int row, int col) {
		super(row, col);
		
	}

	@Override
	public Node render() {
		javafx.scene.shape.Rectangle a = new javafx.scene.shape.Rectangle(10, 10);
		// based on size of grid
		// based on pixels on windows 
		Configuration config = ConfigurationLoader.loader().getConfig();
		String color = config.getAllStates().getColors().get(this.getCurrentstate());
		
		//figure out how to change string into color
		a.setFill(color);
		
		return a;
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
