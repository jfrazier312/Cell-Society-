import java.util.ArrayList;
import java.util.Random;

/*
 * Should I be setting the future state to be the same as the current state, 
 * or will the front end do that by default?
 * 
 */ 

public class FireSimulation extends CellGrid {
	public static final String EMPTY = "EMPTY";
	public static final String TREE = "TREE";
	public static final String BURNING = "BURNING";
	Random generator;

	public FireSimulation(int rows, int cols) {
		super(rows, cols);
	}
	
	public void updateCell(Cell myCell, int probOfBurning){
		generator = new Random();
		String myState = myCell.getCurrentstate();
		ArrayList<Cell> currentNeighbors = getRectangleNeighbors(myCell);
		if(myState.equals(BURNING)){
			myCell.setFuturestate(EMPTY);
		}
		else if(myState.equals(TREE)){
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
