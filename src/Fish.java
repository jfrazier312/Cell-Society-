
public class Fish extends Cell{
	
	private int[] noDiagonalRowDeltas = {-1, 0, 1, 0,};
	private int[] noDiagonalColDeltas = {0, -1, 0, 1};
	private int myReproductionTime;
	private int myMaxReproductionTime;
	
	public Fish(int row, int col, int reproductionTime) {
		super(row, col);
		myReproductionTime = reproductionTime;
		myMaxReproductionTime = reproductionTime;
	}
	
	public int getReproductionTime(){
		return myReproductionTime;
	}
	
	public void decrementReproductionTime(){
		myReproductionTime--;
	}
	
	public void resetReproductionTime(){
		myReproductionTime = myMaxReproductionTime;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
	}

	@Override
	public int[] getRowDeltas(boolean diagonalIncluded) {
		return noDiagonalRowDeltas;
	}

	@Override
	public int[] getColDeltas(boolean diagonalIncluded) {
		return noDiagonalColDeltas;
	}

	@Override
	public Shapes getShape() {
		return Shapes.RECTANGLE;
	}

}
