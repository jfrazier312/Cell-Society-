package model;

public class Triangle extends Cell {

	private int[] rowDeltas = { -1, 0, 1 };
	private boolean isEven;

	/*
	 * I am not sure about this. The issue is that triangles will have a
	 * different neighbor based on whether its upside down or not. Guess we
	 * could set it so a triangle always starts right side up at index column =
	 * 0, so (isEven = true) for the first, then false for second. Definitely
	 * open to thoughts. Haven't looked too deeply into this
	 */
	private int[] evenColDeltas = { 0, 1, 0 };
	private int[] oddColDeltas = { 0, -1, 0 };

	public Triangle(int row, int col, boolean isEven) {
		super(row, col);
		this.isEven = isEven;
	}

	@Override
	public void render() {
		if (isEven) {
			// render it rightside up
		} else {
			// render upside down
		}
	}

	@Override
	public int[] getRowDeltas() {
		return rowDeltas;
	}

	@Override
	public int[] getColDeltas() {
		return isEven ? evenColDeltas : oddColDeltas;
	}

	@Override
	public Shapes getShape() {
		return Shapes.TRIANGLE;
	}
}
