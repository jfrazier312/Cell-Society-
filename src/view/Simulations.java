package view;

public enum Simulations {

	GAME_OF_LIFE("Game_Of_Life"), 
	PREDATOR_PREY("Predator_Prey"), 
	FIRE("Fire"), 
	SEGREGATION("Segregation");

	String name;

	Simulations(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
