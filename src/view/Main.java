package view;
import config.Configuration;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import config.ConfigurationLoader;

public class Main extends Application {
	
	private Stage stage;
	private Scene scene;
	private Group root;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// testing purpose, will change
		Configuration config;
		try {
			ConfigurationLoader.loader().setSource("testxml.xml").load();
			config = ConfigurationLoader.getConfig();
			System.out.println(config.getAllStates());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}