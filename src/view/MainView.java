package view;

import config.ConfigurationLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.CellGrid;
import model.FireSimulation;
import model.GameOfLifeSimulation;
import model.PredatorPreySimulation;
import model.SegregationSimulation;

public class MainView implements GameWorld {

	private Scene scene;
	private BorderPane root;
	private FlowPane cellPane;
	private CellGrid simulation;
	private boolean simIsRunning;
	private VBox buttonContainer;

	private static final double BUTTON_WIDTH = 200;

	private Timeline gameloop;

	private Insets buttonPadding = new Insets((SCENE_HEIGHT - GRID_HEIGHT) / 2, GRID_PADDING,
			(SCENE_HEIGHT - GRID_HEIGHT) / 2, -40);
	private Insets cellPanePadding = new Insets((SCENE_HEIGHT - GRID_HEIGHT) / 2, 0, (SCENE_HEIGHT - GRID_HEIGHT) / 2,
			GRID_PADDING);

	// TODO: Jordan: This will return a scene, and be called in Main.
	public Scene initSimulation(Stage primaryStage) throws Exception {
		// Do configuration loader to get the information for scene / etc.
		// ConfigurationLoader.loader().setSource("Game_Of_Life.xml").load();
		root = new BorderPane();
		scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		// scene = new Scene(root, config.getSceneWidth(),
		// config.getSceneHeight());

		// Creates initial cell grid
		createCellPane();

		// Creates initial simulation
		createSimulation();

		// add the buttons
		createAllButtons();
		setSimulationEventHandler();

		// create game loop
		createGameLoop();

		// Checks if slider needs to reset grid
		createResetTimelineChecker();

		return scene;

	}

	private void createResetTimelineChecker() {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000/60), e-> {
			if (SliderCreator.reset) {
				try {
//					root.getChildren().removeAll(root.getChildren());
					createCellPane();
					createSimulation();
					createCustomButtons();
					SliderCreator.reset = false;
					gameloop.pause();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}));
		timeline.play();
	}

	private void createSimulation() {
		String simulationName = ConfigurationLoader.getConfig().getSimulationName();
		// sets simulation to correct simulation
		findSimulation(simulationName);
		simulation.initSimulation();
		simulation.renderGrid(cellPane);
		cellPane.setPadding(cellPanePadding);
		root.setLeft(cellPane);
	}

	private void findSimulation(String sim) {
		if (sim.equals(FIRE_SIMULATION)) {
			simulation = new FireSimulation();
		} else if (sim.equals(GAME_OF_LIFE)) {
			simulation = new GameOfLifeSimulation();
		} else if (sim.equals(SEGREGATION_SIMULATION)) {
			simulation = new SegregationSimulation();
		} else if (sim.equals(WATOR_WORLD)) {
			simulation = new PredatorPreySimulation();
		}
	}

	private void createGameLoop() {
		// TODO: Duration should come from XML frames/sec
		gameloop = new Timeline();
		gameloop.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
			createKeyFrameEvents();
		}));
	}

	private void createAllButtons() throws Exception {
		buttonContainer = new VBox(PADDING);
		simIsRunning = false;

		// TODO: Jordan - set padding on buttons and button size from XML
		HBox hbox1 = new HBox(PADDING);
		HBox hbox2 = new HBox(PADDING);
		VBox vbox3 = new VBox(PADDING);

		// Sets simulation combo box action
		// setSimulationEventHandler();

		SimulationButton playBtn = new SimulationButton(PLAY);
		setStartEventHandler(playBtn);

		SimulationButton pauseBtn = new SimulationButton(PAUSE);
		setStopEventHandler(pauseBtn);
		hbox1.getChildren().addAll(playBtn, pauseBtn);

		SimulationButton resetBtn = new SimulationButton(RESET);
		setStopEventHandler(resetBtn);

		SimulationButton stepBtn = new SimulationButton(STEP);
		setStepEventHandler(stepBtn);
		
		hbox2.getChildren().addAll(stepBtn, resetBtn);
		
		SimulationSlider rowsSlider = new SimulationSlider(1.0, 100.0, ConfigurationLoader.getConfig().getNumRows(), "Rows", false);
		setRowsEventHandler(rowsSlider.getSlider());

		SimulationSlider colsSlider = new SimulationSlider(1.0, 100.0, ConfigurationLoader.getConfig().getNumCols(), "Cols", false);
		setColumnsEventHandler(colsSlider.getSlider());
		
		vbox3.getChildren().addAll(rowsSlider.getHbox(), colsSlider.getHbox());

		SimulationSlider fpsSlider = new SimulationSlider(1.0, 60.0, ConfigurationLoader.getConfig().getFramesPerSec(), "FPS", false);
		setFPSEventHandler(fpsSlider.getSlider());
	
		VBox basicBtnBox = new VBox(PADDING);
		basicBtnBox.getChildren().addAll(SIMULATIONS, hbox1, hbox2, vbox3, fpsSlider.getHbox());
		basicBtnBox.setMinWidth(300);
		
		buttonContainer.getChildren().add(basicBtnBox);

		createCustomButtons();

		// Right inset will be the same padding used on the left side of grid
		buttonContainer.setPadding(buttonPadding);
		buttonContainer.setMaxWidth(250);
		buttonContainer.setMinWidth(250);
		root.setRight(buttonContainer);
	}

	private void createCustomButtons() {
		VBox custom = new VBox(PADDING);
		// loop through the rest of the custom parameters
		for (String str : ConfigurationLoader.getConfig().getAllCustomParamNames()) {
			SimulationSlider slider = new SimulationSlider(str);
			custom.getChildren().add(slider.getVBox());
		}
		// this is so poorly designed
		if (buttonContainer.getChildren().size() > 1) {
			buttonContainer.getChildren().remove(1);
		}
		buttonContainer.getChildren().add(custom);
	}
	
	private void setRowsEventHandler(Slider sizeSlider) {
		sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				gameloop.pause();
				double newval = (double) newValue;
				ConfigurationLoader.getConfig().setNumRows((int) newval);
				createCellPane();
				createCustomButtons();
				createSimulation();
			}
		});
	}
	
	private void setColumnsEventHandler(Slider sizeSlider) {
		sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				gameloop.pause();
				double newval = (double) newValue;
				ConfigurationLoader.getConfig().setNumCols((int) newval);
				createCellPane();
				createCustomButtons();
				createSimulation();
			}
		});
	}

	private void setFPSEventHandler(Slider fpsSlider) {
		fpsSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				double fpsdouble = (double) 1000 / (double) newValue;
				ConfigurationLoader.getConfig().setFramesPerSec((int)fpsdouble); 
				gameloop.pause();
				gameloop.getKeyFrames().remove(0);
				gameloop.getKeyFrames().add(new KeyFrame(Duration.millis(fpsdouble), e -> {
					createKeyFrameEvents();
				}));
				if (simIsRunning) {
					gameloop.playFromStart();
				}
			}
		});
	}

	private void createKeyFrameEvents() {
		cellPane.getChildren().removeAll(cellPane.getChildren());
		simulation.updateGrid();
		simulation.renderGrid(cellPane);
		root.setLeft(cellPane);
	}

	private void setSimulationEventHandler() {
		SIMULATIONS.setValue(ConfigurationLoader.getConfig().getSimulationName());
		SIMULATIONS.setMinWidth(BUTTON_WIDTH + PADDING);
		SIMULATIONS.setMaxWidth(BUTTON_WIDTH + PADDING);
		SIMULATIONS.valueProperty().addListener(e -> {
			gameloop.stop();
			try {
				ConfigurationLoader.loader().setSource(SIMULATIONS.getValue() + ".xml").load();
				root.getChildren().removeAll(root.getChildren());
				createCellPane();
				createAllButtons();
				createSimulation();
				createGameLoop();
				createResetTimelineChecker();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});
	}

	private void setStartEventHandler(SimulationButton btn) {
		setDimensions(btn);
		btn.setOnAction(e -> {
			simIsRunning = true;
			gameloop.setCycleCount(Timeline.INDEFINITE);
			gameloop.playFromStart();
		});
	}

	private void setStepEventHandler(SimulationButton btn) {
		setDimensions(btn);
		btn.setOnAction(e -> {
			gameloop.pause();
			simIsRunning = false;
			stepOnce();
		});
	}

	private void setStopEventHandler(SimulationButton btn) {
		setDimensions(btn);
		btn.setOnAction(e -> {
			// If button is reset, then reset parameters back to what's on XML
			if (btn.getDisplayName().equals(RESET)) {
				try {
					// ConfigurationLoader.loader().setSource(SIMULATIONS.getValue()
					// + ".xml").load();
					ConfigurationLoader.getConfig();
					createCellPane();
					createCustomButtons();
					createSimulation();
//					createGameLoop();
//					createResetTimelineChecker();
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new IllegalArgumentException("Failed loading XML file");
				}
			}
			gameloop.pause();
			simIsRunning = false;
		});
	}

	private void stepOnce() {
		cellPane.getChildren().removeAll(cellPane.getChildren());
		simulation.updateGrid();
		simulation.renderGrid(cellPane);
		root.setLeft(cellPane);
	}

	private void setDimensions(SimulationButton btn) {
		btn.setMinWidth(BUTTON_WIDTH / 2);
		btn.setMaxWidth(BUTTON_WIDTH / 2);
	}

	private void createCellPane() {
		// TODO: Jordan Set cellpane parameters based on XML
		cellPane = new FlowPane(0.0, 0.0);

		cellPane.setMaxWidth(GRID_WIDTH + GRID_PADDING);
		cellPane.setMinWidth(GRID_WIDTH + GRID_PADDING);

		cellPane.setMaxHeight(GRID_HEIGHT);
		cellPane.setMinHeight(GRID_HEIGHT);

		// cellPane.setPrefWrapLength(50);
		// cellPane.setStyle("-fx-border-color: black; -fx-border-width: 2;");
	}

}