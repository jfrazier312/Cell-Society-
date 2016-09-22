
public class RectangleWithDiagonals extends Rectangle {

	private int[] rowDeltas = {-1, 0, 1, 0, 1, 1, -1, -1};
	private int[] colDeltas = {0, -1, 0, 1, 1, -1, 1, -1};
	
	public RectangleWithDiagonals(int row, int col, boolean isOutOfBounds) {
		super(row, col);
	}
	
	public int[] getRowDeltas(){
		return rowDeltas;
	}
	
	public int[] getColDeltas(){
		return colDeltas;
	}

}
