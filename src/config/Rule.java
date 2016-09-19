package config;

import model.Cell;

public abstract class Rule {
	private String applicableState;
	private String nextState;

	public String getApplicableState() {
		return applicableState;
	}
	public void setApplicableState(String applicableState) {
		this.applicableState = applicableState;
	}
	public void setNextState(String nextState) {
		this.nextState = nextState;
	}
	public String getNextState() {
		return nextState;
	}
	public boolean isApplicable(Cell cell) {
		return cell.getCurrentstate().equals(applicableState);
	}
}

class DirectMapped extends Rule{
	private String neighborStateList;

	public String getNeighborStateList() {
		return neighborStateList;
	}
	public void setNeighborStateList(String neighborStateList) {
		this.neighborStateList = neighborStateList;
	}
	@Override
	public boolean isApplicable(Cell cell) {
		// TODO implement
		return false;
	}
}

class Count extends Rule{
	private String neighborState;
	private int lowerBound;
	private int upperBound;
	
	public String getNeighborState() {
		return neighborState;
	}
	public void setNeighborState(String neighborState) {
		this.neighborState = neighborState;
	}
	public int getLowerBound() {
		return lowerBound;
	}
	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}
	public int getUpperBound() {
		return upperBound;
	}
	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}
	@Override
	public boolean isApplicable(Cell cell) {
		// TODO implement
		super.isApplicable(cell);
		return false;
	}
}

class Match extends Rule{
	private int neighborIndex;
	private String neighborState;
	
	public int getNeighborIndex() {
		return neighborIndex;
	}
	public void setNeighborIndex(int neighborindex) {
		this.neighborIndex = neighborindex;
	}
	public String getNeighborState() {
		return neighborState;
	}
	public void setNeighborState(String neighborState) {
		this.neighborState = neighborState;
	}
	@Override
	public boolean isApplicable(Cell cell) {
		// TODO implement
		return false;
	}
}
