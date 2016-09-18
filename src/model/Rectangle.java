package model;

public class Rectangle extends Cell {
	
	private int[] rowDeltas = {-1, 0, 1, 0};
	private int[] colDeltas = {0, -1, 0, 1};

	public Rectangle(int row, int col) {
		super(row, col);
		//do some other rectangular things?
	}

	@Override
	public void render() {
		// yeah
	}
	
	@Override
	public int[] getRowDeltas() {
		return rowDeltas;
	}
	
	@Override
	public int[] getColDeltas() {
		return colDeltas;
	}

	@Override
	public Shapes getShape() {
		return Shapes.RECTANGLE;
	}	

}
