import java.util.ArrayList;

/*
 * for convention purposes:
 * 0 = dead
 * 1 = alive
 * could use booleans to simplify code, but only works for this sim with two states
 * how do we want to deal with edges?
 */

public class GameOfLifeSimulation extends CellGrid {
	
	public static final int DEAD = 0;
	public static final int ALIVE = 1;
	
	public GameOfLifeSimulation(int rows, int cols) {
		super(rows, cols);
	}
	
	public void updateCell(Cell myCell){
		int myState = myCell.getCurrentstate();
		ArrayList<Cell> currentNeighbors = getRectangleNeighbors(myCell, false);
		int liveCount = 0;
		for(Cell neighborCell: currentNeighbors){
			if(neighborCell.getCurrentstate() == ALIVE){
				liveCount++;
			}
		}
		if(myState == DEAD){
			if(liveCount == 3){
				myCell.setFuturestate(ALIVE);
			}
			else{
				myCell.setFuturestate(DEAD);
			}
			
		}
		else{
			if(liveCount<2){
				myCell.setFuturestate(DEAD);
			}
			if(liveCount>=2 || liveCount <=3){
				myCell.setFuturestate(ALIVE);
			}
			else{
				myCell.setFuturestate(DEAD);
			}
		}
	}
}
