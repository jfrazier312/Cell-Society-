
public abstract class Rectangle extends Cell {
	
	//included diagonals
//	private int[] rowDeltas = {-1, 0, 1, 0, 1, 1, -1, -1};
//	private int[] colDeltas = {0, -1, 0, 1, 1, -1, 1, -1};
	//for predator prey sim

	
	public Rectangle(int row, int col) {
		super(row, col);
		//do some other rectangular things?
	}
	
//	private boolean isOutOfBounds(){
//		return myOutOfBounds;
//	}

	@Override
	public void render() {
		// yeah
	}
	
	@Override
	public abstract int[] getRowDeltas();
	
	@Override
	public abstract int[] getColDeltas();

	@Override
	public Shapes getShape() {
		return Shapes.RECTANGLE;
	}	

}
