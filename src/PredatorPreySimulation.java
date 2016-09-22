import java.util.ArrayList;
import java.util.Random;
public class PredatorPreySimulation extends CellGrid {

	public static final String EMPTY = "EMPTY";
	public static final String FISH = "FISH";
	public static final String SHARK = "SHARK";
	private Random generator;
	
	//instead of changing the state, do I want to physically make a copy of my cell?
	//because needs to retain reproduction time and time to birth etc. 
	
	public PredatorPreySimulation(int rows, int cols, int energyIncrease) {
		super(rows, cols);
	}
	
	public void updateCell(Fish myCell, int energyIncrease){
		generator = new Random();
		String state = myCell.getCurrentstate();
		ArrayList<Cell> neighbors = getRectangleNeighbors(myCell);
		if(state == SHARK){
			sharkMove(neighbors, myCell);
		}
	}
	
	public void fishMove(ArrayList<Cell> myNeighbors, Fish myCell){
		int numEmptyNeighbors = countEmpty(myNeighbors);
		int direction;
		if(numEmptyNeighbors == 0 && myCell.getFuturestate() != "NULL"){
			myCell.setFuturestate(FISH);
		}
		else{
			ArrayList<Integer> myFriends = getNonEmptyNeighbors(myNeighbors, SHARK);
			direction = generator.nextInt(myFriends.size());
			//myCell.decrementReproductionTime();
			//direction = myNeighbors.get(direction);
			Fish cellToUpdate = (Fish) getRectangleNeighbors(myCell).get(direction);
		}
	}
	
	public void sharkMove(ArrayList<Cell> myNeighbors, Fish myCell){
		ArrayList<Integer> myFriends= getNonEmptyNeighbors(myNeighbors, SHARK);
		changeCellToShark(myCell, myFriends);
	}
	
	public ArrayList<Integer> getNonEmptyNeighbors(ArrayList<Cell> myNeighbors, String state){
		ArrayList<Integer> myFriends = new ArrayList<Integer>();
		for(int i = 0; i<myNeighbors.size(); i++){
			if(myNeighbors.get(i).getCurrentstate().equals(state)){
				myFriends.add(i);
			}
		}
		return myFriends;
	}
	
	public int countEmpty(ArrayList<Cell> myNeighbors){
		int emptyCount = 0;
		for(Cell neighbor: myNeighbors){
			if(neighbor.getCurrentstate() == EMPTY){
				emptyCount++;
			}
		}
		return emptyCount;
	}
	
	public void transferInformation(Fish prevCell, Fish newCell){
		newCell.setReproductionTime(prevCell.getReproductionTime());
		newCell.setMaxReproductionTime(prevCell.getMaxReproductionTime());
		newCell.setTimeToDeath(prevCell.getTimeToDeath());
	}
	
	//can't move somewhere with another shark
	public void changeCellToShark(Fish myCell, ArrayList<Integer> myFishFriends){
		int direction;
		//eats something
		if(myFishFriends.size() == 0){
			if(myCell.getTimeToDeath() == 1){
				myCell.setFuturestate(EMPTY);
				myCell.setCurrentstate(EMPTY);
				return;
			}
			direction = generator.nextInt(4);
			myCell.decrementReproductionTime();
		}
		//doesn't eat something
		else{
			if(myFishFriends.size() == 1){
				direction = myFishFriends.get(0);
			}
			else{
				int directionIndex = generator.nextInt(myFishFriends.size());
				direction = myFishFriends.get(directionIndex);
			}
			myCell.increaseTimeToDeath();
		}
		if(myCell.getReproductionTime() == 0){
			giveBirth(myCell, SHARK);
		}
		else{
			myCell.setCurrentstate(EMPTY);
		}
		Fish cellToUpdate = (Fish) getRectangleNeighbors(myCell).get(direction);
		cellToUpdate.setFuturestate(SHARK);
		transferInformation(myCell, cellToUpdate);
	}
	
	public void giveBirth(Fish myCell, String state){
		myCell.setFuturestate(state);
		myCell.resetReproductionTime();
		myCell.resetTimeToDeath();
	}
}
