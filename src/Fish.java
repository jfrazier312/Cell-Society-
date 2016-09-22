
public class Fish extends Cell{
	
	private int[] noDiagonalRowDeltas = {-1, 0, 1, 0,};
	private int[] noDiagonalColDeltas = {0, -1, 0, 1};
	private int myReproductionTime;
	private int myMaxReproductionTime;
	private int myTimeToDeath;
	
	public Fish(int row, int col, int reproductionTime, int timeToDeath) {
		super(row, col);
		myReproductionTime = reproductionTime;
		myMaxReproductionTime = reproductionTime;
		myTimeToDeath = timeToDeath;
		
	}
	
	public void setReproductionTime(int reproductionTime){
		myReproductionTime = reproductionTime;
		myMaxReproductionTime = reproductionTime;
	}
	
	public void setTimeToDeath(int timeToDeath){
		myTimeToDeath = timeToDeath;
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
	public int[] getRowDeltas() {
		return noDiagonalRowDeltas;
	}

	@Override
	public int[] getColDeltas() {
		return noDiagonalColDeltas;
	}

	@Override
	public Shapes getShape() {
		return Shapes.RECTANGLE;
	}
	
	//shark stuff
	
	public void increaseTimeToDeath(int energyGained){
		myTimeToDeath+=energyGained;
	}
	
	public void decrementTimeToDeath(){
		myTimeToDeath--;
	}
	
	public boolean isDead(){
		return myTimeToDeath == 0;
	}

}
