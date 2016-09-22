import config.Configuration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ConfigurationLoader;

public class MainView extends Application implements ButtonHandler {
	
	private Stage stage;
	private Scene scene;
	private BorderPane root;
	private FlowPane cellPane;
	Configuration config;
		
	public static void main(String[] args) {
		launch(args);
	}
                   
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Do configuration loader to get the information for scene / etc. 
		config = ConfigurationLoader.loader().setSource("testxml.xml").load().getConfig();
		root = new BorderPane();
		scene = new Scene(root, 800, 500);
//		scene = new Scene(root, config.getSceneWidth(), config.getSceneHeight());
		stage = primaryStage;
		cellPane.setPrefWidth(config.getGridWidth());
		cellPane.setPrefHeight(config.getGridHeight());
		
		//Configuration.getConfig();
		
		// add the buttons
		createAllButtons();
		
		//create timeline 
		
		stage.setScene(scene);
		stage.show();
		
	}
	
	
	public void createAllButtons() throws Exception { 
		VBox buttonContainer = new VBox(PADDING);
		
		SimulationButton playBtn = new SimulationButton("Play");
		setStartEventHandler(playBtn);
	
		SimulationButton resumeBtn = new SimulationButton("Resume");
		setStartEventHandler(resumeBtn);
		
		SimulationButton pauseBtn = new SimulationButton("Pause");
		setStopEventHandler(pauseBtn);
		
		SimulationButton resetBtn = new SimulationButton("Reset");
		setStopEventHandler(resetBtn);
		
		VBox basicBtnBox = new VBox(PADDING); 
		basicBtnBox.getChildren().addAll(playBtn, pauseBtn, resumeBtn, resetBtn);
		VBox additionalSliders = new VBox(PADDING);
		
//		 loop through the rest of the things needed from config.getShit, create necessary sliders
		for(String str : config.getAllParams()) {
			SimulationSlider slider = new SimulationSlider(str);
			additionalSliders.getChildren().add(slider);
		}
		
		buttonContainer.getChildren().addAll(basicBtnBox, additionalSliders);
		root.setRight(buttonContainer);
		
	}
	
	public void setStartEventHandler(SimulationButton btn) {
		btn.setOnAction(e -> {
			config.setRunning(true);
		});
	}
	
	public void setStopEventHandler(SimulationButton btn) {
		btn.setOnAction(e -> {
			// If button is reset, then reset parameters back to what's on XML file
			if (btn.getDisplayName().equals("Reset")) {
				try {
					config = ConfigurationLoader.loader().setSource("testxml.xml").load().getConfig();
				} catch (Exception e1) {
					throw new IllegalArgumentException("Failed loading XML file");
				}
			}
			config.setRunning(false);
		});
	}

}
