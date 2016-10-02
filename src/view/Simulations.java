package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public enum Simulations {

	GAME_OF_LIFE("Game_Of_Life"), 
	PREDATOR_PREY("Predator_Prey"), 
	FIRE("Fire"), 
	SEGREGATION("Segregation"),
	ANT("Ant"),
	SUGAR("Sugar"),
	COMBOBOX;

	String name;
	ComboBox<String> SIMULATIONS;
	
	Simulations() {
		ObservableList<String> simulationOptions = FXCollections.observableArrayList("Game_Of_Life",
				"Predator_Prey", "Fire", "Segregation", "Ant", "Sugar");
		SIMULATIONS = new ComboBox<>(simulationOptions);
	}

	Simulations(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public ComboBox<String> getSimulationCheckBox() {
		return SIMULATIONS;
	}
}
