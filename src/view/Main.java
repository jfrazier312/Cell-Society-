package view;
import config.Configuration;
import config.ConfigurationLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application implements GameWorld {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
//		Configuration config;
		try {
			ConfigurationLoader.loader().setSource("Game_Of_Life.xml").load();
//			config = ConfigurationLoader.getConfig();
//			System.out.println(config.getAllStates());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MainView sim = new MainView();
		Scene scene = sim.initSimulation(primaryStage);
		
		primaryStage.setTitle("Simulations");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
}