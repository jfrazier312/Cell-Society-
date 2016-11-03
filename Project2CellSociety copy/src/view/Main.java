package view;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * 
 * @author Jordan Frazier, Charles Xu, Austin Gartside
 *
 */

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MainView sim = new MainView();
		primaryStage.setTitle("Simulations");
		primaryStage.setScene(sim.initScene());
		primaryStage.show();
	}
}