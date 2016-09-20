
public class Rectangle extends Cell {
	
	//included diagonals
	private int[] rowDeltas = {-1, 0, 1, 0, 1, 1, -1, -1};
	private int[] colDeltas = {0, -1, 0, 1, 1, -1, 1, -1};
	private int[] noDiagonalRowDeltas = {-1, 0, 1, 0,};
	private int[] noDiagonalColDeltas = {0, -1, 0, 1};

	public Rectangle(int row, int col) {
		super(row, col);
		//do some other rectangular things?
	}

	@Override
	public void render() {
		// yeah
	}
	
	@Override
	public int[] getRowDeltas(boolean diagonalIncluded) {
		if(diagonalIncluded){
			return rowDeltas;
		}
		return noDiagonalRowDeltas;
	}
	
	@Override
	public int[] getColDeltas(boolean diagonalIncluded) {
		if(diagonalIncluded){
			return colDeltas;
		}
		return noDiagonalColDeltas;
	}

	@Override
	public Shapes getShape() {
		return Shapes.RECTANGLE;
	}	

}
