package model;
public class Hexagon extends Cell {

	private int[] rowDeltas = {-1, 0, 1, 0, 1, 1, -1, -1};
	private int[] colDeltas = {0, -1, 0, 1, 1, -1, 1, -1};
	
	public Hexagon(int row, int col, boolean isEven) {
		super(row, col);
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
		return Shapes.HEXAGON;
	}
}
