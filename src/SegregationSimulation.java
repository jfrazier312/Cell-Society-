import java.util.ArrayList;
/*
 * for convention going to assume the following
 * 0 = empty
 * 1 = type 1
 * 2 = type 2
 * 
 * gonna need some check in cellgrid to see if the simulation is done or not
 */

//Should we do getting neighbors in each simulation since the definition can vary?
public class SegregationSimulation extends CellGrid {
	
	public static final int EMPTY = 0;
	public static final int TYPE1 = 1;
	public static final int TYPE2 = 2;

	public SegregationSimulation(int rows, int cols) {
		super(rows, cols);
	}
	
	
	public void updateCell(int probability, Cell myCell, CellGrid myGrid){
		//do nothing for an empty cell
		if(myCell.getCurrentstate() == EMPTY){
			return;
		}
		ArrayList<Cell> currentNeighbors = getRectangleNeighbors(myCell, true);
		double matchingCellCount = 0.0;
		//start at 1 to avoid divide by 0 error
		int nonEmptyCellCount = 1;
		for(int i = 0; i<currentNeighbors.size(); i++){
			int neighborState = currentNeighbors.get(i).getCurrentstate();
			//assuming empty cell state equals 0
			if(neighborState!=EMPTY){
				nonEmptyCellCount++;
				if(neighborState == myCell.getCurrentstate()){
					matchingCellCount++;
				}
			}		
		}
		if(matchingCellCount/(nonEmptyCellCount-1) < probability){
			myCell.setFuturestate(EMPTY);
		}
		else{
			//need to move this state to a random cell somewhere else on the grid
			Cell newLocation = findEmptyCell(myGrid);
			newLocation.setFuturestate(myCell.getCurrentstate());
		}
	}
	
	//algorithm says if current cell "isn't satisfied" to move it to an empty cell, so need to find an empty cell
	public Cell findEmptyCell(CellGrid myGrid){
		for(int i = 0; i<myGrid.getNumRows(); i++){
			for(int j = 0; j<myGrid.getNumCols(); j++){
				if(myGrid.getGrid()[i][j].getCurrentstate() == EMPTY){
					return myGrid.getGrid()[i][j];
				}
			}
		}
		return null;
	}

}