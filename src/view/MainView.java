package view;

import config.ConfigurationLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
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
	private Group root;
	private GridPane cellPane;
	private CellGrid simulation;
	private boolean simIsRunning;
	private VBox buttonContainer;

	private boolean gridLinesVisible;

	private static final double BUTTON_WIDTH = 200;

	private Timeline gameloop;

	private Insets buttonPadding = new Insets((SCENE_HEIGHT - GRID_HEIGHT) / 2, GRID_PADDING,
			(SCENE_HEIGHT - GRID_HEIGHT) / 2, -40);
	private Insets cellPanePadding = new Insets((SCENE_HEIGHT - GRID_HEIGHT) / 2, 0, (SCENE_HEIGHT - GRID_HEIGHT) / 2,
			GRID_PADDING);

	public Scene initSimulation(Stage primaryStage) throws Exception {
		// Do configuration loader to get the information for scene / etc.
		// ConfigurationLoader.loader().setSource("Game_Of_Life.xml").load();
		root = new Group();
		scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		gridLinesVisible = false;
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
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 60), e -> {
			if (SliderCreator.reset) {
				try {
					root.getChildren().remove(cellPane);
					cellPane.getChildren().removeAll(cellPane.getChildren());
					createCellPane();
					createSimulation();

					SliderCreator.reset = false;
					gameloop.pause();
					simIsRunning = false;
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

	private void createKeyFrameEvents() {
		// root.getChildren().remove(cellPane);
		cellPane.getChildren().removeAll(cellPane.getChildren());
		simulation.updateGrid();
		simulation.renderGrid(cellPane);
		if(gridLinesVisible) {
			cellPane.setGridLinesVisible(true);
		} else {
			cellPane.setGridLinesVisible(false);

		}

		// root.getChildren().add(cellPane);
	}

	// TODO: Jordan: Somehow need to put button creation somewhere else
	private void createAllButtons() throws Exception {
		buttonContainer = new VBox(PADDING);

		HBox hbox1 = new HBox(PADDING);
		HBox hbox2 = new HBox(PADDING);
		VBox vbox3 = new VBox(PADDING);

		// Sets simulation combo box action
		// setSimulationEventHandler();

		SimulationButton playBtn = new SimulationButton(GenericButton.PLAY);
		setStartEventHandler(playBtn);

		SimulationButton pauseBtn = new SimulationButton(GenericButton.PAUSE);
		setStopEventHandler(pauseBtn);
		hbox1.getChildren().addAll(playBtn, pauseBtn);

		SimulationButton resetBtn = new SimulationButton(GenericButton.RESET);
		setStopEventHandler(resetBtn);

		SimulationButton stepBtn = new SimulationButton(GenericButton.STEP);
		setStepEventHandler(stepBtn);

		hbox2.getChildren().addAll(stepBtn, resetBtn);

		SimulationSlider rowsSlider = new SimulationSlider(1.0, 50.0, ConfigurationLoader.getConfig().getNumRows(),
				GenericButton.ROWS, false);
		setRowsEventHandler(rowsSlider.getSlider());

		SimulationSlider colsSlider = new SimulationSlider(1.0, 50.0, ConfigurationLoader.getConfig().getNumCols(),
				GenericButton.COLS, false);
		setColumnsEventHandler(colsSlider.getSlider());

		vbox3.getChildren().addAll(rowsSlider.getHbox(), colsSlider.getHbox());

		SimulationSlider fpsSlider = new SimulationSlider(1.0, 60.0, ConfigurationLoader.getConfig().getFramesPerSec(),
				GenericButton.FPS, false);
		setFPSEventHandler(fpsSlider.getSlider());

		SimulationButton gridVisible = new SimulationButton(GenericButton.GRID_LINES);
//		setGridLinesVisible(gridVisible);

		VBox basicBtnBox = new VBox(PADDING);
		basicBtnBox.getChildren().addAll(SIMULATIONS, hbox1, hbox2, vbox3, fpsSlider.getHbox(), gridVisible);
		basicBtnBox.setMinWidth(300);

		buttonContainer.getChildren().add(basicBtnBox);

		createCustomButtons();

		// Right inset will be the same padding used on the left side of grid
		buttonContainer.setPadding(buttonPadding);
		buttonContainer.setMaxWidth(250);
		buttonContainer.setMinWidth(250);
		buttonContainer.setLayoutX(SCENE_WIDTH - buttonContainer.getMaxWidth());
		root.getChildren().add(buttonContainer);
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
				// This next part is all done in resetChecker timeline, caused
				// by simulation slider
				// createCellPane();
				// createCustomButtons();
				// createSimulation();
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
				// createCellPane();
				// createCustomButtons();
				// createSimulation();
			}
		});
	}

	private void setFPSEventHandler(Slider fpsSlider) {
		fpsSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				double fpsdouble = 1000.0 / (double) newValue;
				ConfigurationLoader.getConfig().setFramesPerSec((int) fpsdouble);
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
			if (btn.getDisplayName().equals(GenericButton.RESET.toString())) {
				try {
					// ConfigurationLoader.getConfig();
					createCellPane();
					createCustomButtons();
					createSimulation();
					// createGameLoop();
					// createResetTimelineChecker();
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new IllegalArgumentException("Failed loading XML file");
				}
			}
			gameloop.pause();
			simIsRunning = false;
		});
	}

//	private void setGridLinesVisible(SimulationButton btn) {
//		setDimensions(btn);
//		btn.setOnAction(e -> {
//			if (!gridLinesVisible) {
//				setGridLines(true);
//			} else {
//				setGridLines(false);
//			}
//		});
//	}
//
//	private void setGridLines(boolean isVisible) {
//		if (isVisible) {
//			cellPane.setGridLinesVisible(true);
//			gridLinesVisible = true;
//		} else {
//			cellPane.setGridLinesVisible(false);
//			gridLinesVisible = false;
//		}
//	}

	private void stepOnce() {
		// root.getChildren().remove(cellPane);
		cellPane.getChildren().removeAll(cellPane.getChildren());
		simulation.updateGrid();
		simulation.renderGrid(cellPane);
		// root.getChildren().add(cellPane);
	}

	private void setDimensions(SimulationButton btn) {
		btn.setMinWidth(BUTTON_WIDTH / 2);
		btn.setMaxWidth(BUTTON_WIDTH / 2);
	}

	private void createCellPane() {
		// TODO: Jordan Set cellpane parameters based on XML
		cellPane = new GridPane();

		cellPane.setMaxWidth(GRID_WIDTH + GRID_PADDING);
		// cellPane.setMinWidth(GRID_WIDTH + GRID_PADDING);
		cellPane.setMaxHeight(GRID_HEIGHT);
		// cellPane.setMinHeight(GRID_HEIGHT);

		cellPane.setPadding(cellPanePadding);

		root.getChildren().add(cellPane);

		// cellPane.setPrefWrapLength(50);
		// cellPane.setStyle("-fx-border-color: black; -fx-border-width: 2;");
	}

}