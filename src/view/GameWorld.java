package view;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import model.CellGrid;
import model.FireSimulation;
import model.GameOfLifeSimulation;
import model.PredatorPreySimulation;
import model.SegregationSimulation;

public interface GameWorld {
	
	public static final String RESUME = "Resume";
	public static final String PLAY = "Play";
	public static final String PAUSE = "Pause";
	public static final String RESET = "Reset";
	public static final String STEP = "Step";
	
	public static double SCENE_WIDTH = 800;
	public static double SCENE_HEIGHT = 500;
	
	public static double GRID_WIDTH = 400;
	public static double GRID_HEIGHT = 400;
	
	public static final String GAME_OF_LIFE = "Game_Of_Life";
	public static final String WATOR_WORLD = "Wa-Tor_World";
	public static final String FIRE_SIMULATION = "Fire_Simulation";
	public static final String SEGREGATION_SIMULATION = "Segregation_Simulation";
	
	public static final double GRID_PADDING = SCENE_WIDTH / 20;

	
	/*
	 * Add simulations to this combobox when implemented
	 */
	ObservableList<String> SIMULATION_OPTIONS = FXCollections.observableArrayList(
			"Game_Of_Life",
			"Wa-Tor_World",
			"Fire_Simulation",
			"Segregation_Simulation");
	
	CellGrid[] SIMULATION_LIST = { new GameOfLifeSimulation(), 
			new PredatorPreySimulation(),
			new FireSimulation(),
			new SegregationSimulation()
			};
	
	public static final ComboBox<String> SIMULATIONS = new ComboBox<>(SIMULATION_OPTIONS);
	
	public static final int PADDING = 10;

}
