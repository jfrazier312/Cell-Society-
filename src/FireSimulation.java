import java.util.ArrayList;
import java.util.Random;

/*
 * Should I be setting the future state to be the same as the current state, 
 * or will the front end do that by default?
 * 
 */ 

public class FireSimulation extends CellGrid {
	public static final int EMPTY = 0;
	public static final int TREE = 1;
	public static final int BURNING = 2;
	Random generator;

	public FireSimulation(int rows, int cols) {
		super(rows, cols);
	}
	
	public void updateCell(Cell myCell, int probOfBurning){
		generator = new Random();
		int myState = myCell.getCurrentstate();
		ArrayList<Cell> currentNeighbors = getRectangleNeighbors(myCell, false);
		if(myState == BURNING){
			myCell.setFuturestate(EMPTY);
		}
		else if(myState == TREE){
			for(Cell neighbor: currentNeighbors){
				if(neighbor.getCurrentstate() == BURNING){
					int seeIfBurn = generator.nextInt(100);
					if(seeIfBurn<(probOfBurning*100)){
						myCell.setFuturestate(BURNING);
						break;
					}
				}
			}
		}
		else{
			myCell.setFuturestate(EMPTY);
		}
		
	}

}
