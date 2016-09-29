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

	private Scene myScene;
	private Group myRoot;
	private GridPane myCellPane;
	private CellGrid mySimulation;
	private Timeline myGameloop;
	
	private boolean isGridLinesVisible;
	private boolean simIsRunning;
	private VBox myButtonContainer;

	//TODO: put into resource bundle
	private static final double BUTTON_WIDTH = 200;


	private Insets buttonPadding = new Insets((SCENE_HEIGHT - GRID_HEIGHT) / 2, GRID_PADDING,
			(SCENE_HEIGHT - GRID_HEIGHT) / 2, -40);
	private Insets cellPanePadding = new Insets((SCENE_HEIGHT - GRID_HEIGHT) / 2, 0, (SCENE_HEIGHT - GRID_HEIGHT) / 2,
			GRID_PADDING);

	public Scene initSimulation() throws Exception {
		// Do configuration loader to get the information for scene / etc.
		// ConfigurationLoader.loader().setSource("Game_Of_Life.xml").load();
		myRoot = new Group();
		myScene = new Scene(myRoot, SCENE_WIDTH, SCENE_HEIGHT);
		isGridLinesVisible = false;
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

		return myScene;

	}

	private void createResetTimelineChecker() {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 60), e -> {
			if (SliderCreator.reset) {
				try {
					myRoot.getChildren().remove(myCellPane);
					myCellPane.getChildren().removeAll(myCellPane.getChildren());
					createCellPane();
					createSimulation();

					SliderCreator.reset = false;
					myGameloop.pause();
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
		mySimulation.initSimulation();
		mySimulation.renderGrid(myCellPane);
	}

	private void findSimulation(String sim) {
		if (sim.equals(FIRE_SIMULATION)) {
			mySimulation = new FireSimulation();
		} else if (sim.equals(GAME_OF_LIFE)) {
			mySimulation = new GameOfLifeSimulation();
		} else if (sim.equals(SEGREGATION_SIMULATION)) {
			mySimulation = new SegregationSimulation();
		} else if (sim.equals(WATOR_WORLD)) {
			mySimulation = new PredatorPreySimulation();
		}
	}

	private void createGameLoop() {
		// TODO: Duration should come from XML frames/sec
		myGameloop = new Timeline();
		myGameloop.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
			createKeyFrameEvents();
		}));
	}

	private void createKeyFrameEvents() {
		// root.getChildren().remove(cellPane);
		myCellPane.getChildren().removeAll(myCellPane.getChildren());
		mySimulation.updateGrid();
		mySimulation.renderGrid(myCellPane);
		if(isGridLinesVisible) {
			myCellPane.setGridLinesVisible(true);
		} else {
			myCellPane.setGridLinesVisible(false);

		}

		// root.getChildren().add(cellPane);
	}

	// TODO: Jordan: Somehow need to put button creation somewhere else
	private void createAllButtons() throws Exception {
		myButtonContainer = new VBox(PADDING);

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

		myButtonContainer.getChildren().add(basicBtnBox);

		createCustomButtons();

		// Right inset will be the same padding used on the left side of grid
		myButtonContainer.setPadding(buttonPadding);
		myButtonContainer.setMaxWidth(250);
		myButtonContainer.setMinWidth(250);
		myButtonContainer.setLayoutX(SCENE_WIDTH - myButtonContainer.getMaxWidth());
		myRoot.getChildren().add(myButtonContainer);
	}

	private void createCustomButtons() {
		VBox custom = new VBox(PADDING);
		// loop through the rest of the custom parameters
		for (String str : ConfigurationLoader.getConfig().getAllCustomParamNames()) {
			SimulationSlider slider = new SimulationSlider(str);
			custom.getChildren().add(slider.getVBox());
		}
		// this is so poorly designed
		if (myButtonContainer.getChildren().size() > 1) {
			myButtonContainer.getChildren().remove(1);
		}
		myButtonContainer.getChildren().add(custom);
	}

	private void setRowsEventHandler(Slider sizeSlider) {
		sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				myGameloop.pause();
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
				myGameloop.pause();
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
				myGameloop.pause();
				myGameloop.getKeyFrames().remove(0);
				myGameloop.getKeyFrames().add(new KeyFrame(Duration.millis(fpsdouble), e -> {
					createKeyFrameEvents();
				}));

				if (simIsRunning) {
					myGameloop.playFromStart();
				}
			}
		});
	}

	private void setSimulationEventHandler() {
		SIMULATIONS.setValue(ConfigurationLoader.getConfig().getSimulationName());
		SIMULATIONS.setMinWidth(BUTTON_WIDTH + PADDING);
		SIMULATIONS.setMaxWidth(BUTTON_WIDTH + PADDING);
		SIMULATIONS.valueProperty().addListener(e -> {
			myGameloop.stop();
			try {
				ConfigurationLoader.loader().setSource(SIMULATIONS.getValue() + ".xml").load();
				myRoot.getChildren().removeAll(myRoot.getChildren());
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
			myGameloop.setCycleCount(Timeline.INDEFINITE);
			myGameloop.playFromStart();
		});
	}

	private void setStepEventHandler(SimulationButton btn) {
		setDimensions(btn);
		btn.setOnAction(e -> {
			myGameloop.pause();
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
					myCellPane.getChildren().removeAll(myCellPane.getChildren());
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
			myGameloop.pause();
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
		myCellPane.getChildren().removeAll(myCellPane.getChildren());
		mySimulation.updateGrid();
		mySimulation.renderGrid(myCellPane);
		// root.getChildren().add(cellPane);
	}

	private void setDimensions(SimulationButton btn) {
		btn.setMinWidth(BUTTON_WIDTH / 2);
		btn.setMaxWidth(BUTTON_WIDTH / 2);
	}

	private void createCellPane() {
		// TODO: Jordan Set cellpane parameters based on XML
		myCellPane = new GridPane();

		myCellPane.setMaxWidth(GRID_WIDTH + GRID_PADDING);
		// cellPane.setMinWidth(GRID_WIDTH + GRID_PADDING);
		myCellPane.setMaxHeight(GRID_HEIGHT);
		// cellPane.setMinHeight(GRID_HEIGHT);

		myCellPane.setPadding(cellPanePadding);

		myRoot.getChildren().add(myCellPane);

		// cellPane.setPrefWrapLength(50);
		// cellPane.setStyle("-fx-border-color: black; -fx-border-width: 2;");
	}

}