
public class Shark extends Fish{

	private int[] noDiagonalRowDeltas = {-1, 0, 1, 0,};
	private int[] noDiagonalColDeltas = {0, -1, 0, 1};
	private int myTimeToDeath;
	
	public Shark(int row, int col, int reproductionTime, int timeToDeath) {
		super(row, col, reproductionTime);
		myTimeToDeath = timeToDeath;	
	}
	
	public void increaseTimeToDeath(int energyGained){
		myTimeToDeath+=energyGained;
	}
	
	public void decrementTimeToDeath(){
		myTimeToDeath--;
	}
	
	public boolean isDead(){
		return myTimeToDeath == 0;
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
