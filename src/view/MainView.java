package view;

import java.util.Set;

import config.Configuration;
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

	// TODO: put into resource bundle
	private static final double BUTTON_WIDTH = 200;

	private Insets buttonPadding = new Insets((SCENE_HEIGHT - GRID_HEIGHT) / 2, GRID_PADDING,
			(SCENE_HEIGHT - GRID_HEIGHT) / 2, -40);
	private Insets cellPanePadding = new Insets((SCENE_HEIGHT - GRID_HEIGHT) / 2, 0, (SCENE_HEIGHT - GRID_HEIGHT) / 2,
			GRID_PADDING);

	public Scene initScene() throws Exception {
		myRoot = new Group();
		myScene = new Scene(myRoot, SCENE_WIDTH, SCENE_HEIGHT);
		// isGridLinesVisible = false;
		initSimulation();
		return myScene;

	}

	private void initSimulation() {
		Configuration config = new Configuration();
		createCellPane();
		createSimulation();
		createAllButtons(config);
		setSimulationEventHandler();
		createGameLoop();
		createResetTimelineChecker();
	}

	private void createResetTimelineChecker() {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		// TODO: get rid of magic number
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 60), e -> {
			if (SliderCreator.reset) {
				resetGrid();
			}
		}));
		timeline.play();
	}

	private void resetGrid() {
		myRoot.getChildren().remove(myCellPane);
		createCellPane();
		createSimulation();
		SliderCreator.reset = false;
		myGameloop.pause();
		simIsRunning = false;
	}

	private void createSimulation() {
		Configuration config = new Configuration();
		// String simulationName =
		// ConfigurationLoader.getConfig().getSimulationName();
		findSimulation(config.getSimulationName());
		mySimulation.initSimulation();
		mySimulation.renderGrid(myCellPane);
	}

	private void findSimulation(String sim) {
		if (sim.equals(FIRE_SIMULATION)) {
			mySimulation = new FireSimulation(config);
		} else if (sim.equals(GAME_OF_LIFE)) {
			mySimulation = new GameOfLifeSimulation(config);
		} else if (sim.equals(SEGREGATION_SIMULATION)) {
			mySimulation = new SegregationSimulation(config);
		} else if (sim.equals(WATOR_WORLD)) {
			mySimulation = new PredatorPreySimulation(config);
		}
	}

	private void createGameLoop() {
		// TODO: Duration should come from XML frames/sec
		myGameloop = new Timeline();
		myGameloop.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
			updateGrid();
		}));
	}

	// private void createKeyFrameEvents() {
	// // root.getChildren().remove(cellPane);
	// updateGrid();
	//// if (isGridLinesVisible) {
	//// myCellPane.setGridLinesVisible(true);
	//// } else {
	//// myCellPane.setGridLinesVisible(false);
	////
	//// }
	//
	// // root.getChildren().add(cellPane);
	// }

	private void updateGrid() {
		myCellPane.getChildren().removeAll(myCellPane.getChildren());
		mySimulation.updateGrid();
		mySimulation.renderGrid(myCellPane);
	}

	private void createAllButtons(Configuration config) {
		myButtonContainer = new VBox(PADDING);

		HBox hbox1 = new HBox(PADDING);
		HBox hbox2 = new HBox(PADDING);
		VBox vbox3 = new VBox(PADDING);

		SimulationButton playBtn = new SimulationButton(GenericButton.PLAY);
		setStartEventHandler(playBtn);

		SimulationButton pauseBtn = new SimulationButton(GenericButton.PAUSE);
		setStopEventHandler(pauseBtn);

		SimulationButton resetBtn = new SimulationButton(GenericButton.RESET);
		setStopEventHandler(resetBtn);

		SimulationButton stepBtn = new SimulationButton(GenericButton.STEP);
		setStepEventHandler(stepBtn);

		// TODO: Magic numbers
		SimulationSlider rowsSlider = new SimulationSlider(1.0, 50.0, ConfigurationLoader.getConfig().getNumRows(),
				GenericButton.ROWS, false);
		setRowAndColEventHandler(rowsSlider.getSlider(), true);

		SimulationSlider colsSlider = new SimulationSlider(1.0, 50.0, ConfigurationLoader.getConfig().getNumCols(),
				GenericButton.COLS, false);
		setRowAndColEventHandler(colsSlider.getSlider(), false);

		SimulationSlider fpsSlider = new SimulationSlider(1.0, 60.0, ConfigurationLoader.getConfig().getFramesPerSec(),
				GenericButton.FPS, false);
		setFPSEventHandler(fpsSlider.getSlider());

		// TODO: Grid lines don't work
		SimulationButton gridVisible = new SimulationButton(GenericButton.GRID_LINES);
		// setGridLinesVisible(gridVisible);

		hbox1.getChildren().addAll(playBtn, pauseBtn);
		hbox2.getChildren().addAll(stepBtn, resetBtn);
		vbox3.getChildren().addAll(rowsSlider.getHbox(), colsSlider.getHbox());

		VBox basicBtnBox = new VBox(PADDING);
		basicBtnBox.getChildren().addAll(SIMULATIONS, hbox1, hbox2, vbox3, fpsSlider.getHbox(), gridVisible);
		// TODO: Magic numbers
		basicBtnBox.setMinWidth(300);
		myButtonContainer.getChildren().add(basicBtnBox);

		createCustomButtons(config);
		setButtonContainerParameters();
	}

	private void setButtonContainerParameters() {
		// TODO: Magic numbers
		myButtonContainer.setPadding(buttonPadding);
		myButtonContainer.setMaxWidth(250);
		myButtonContainer.setMinWidth(250);
		myButtonContainer.setLayoutX(SCENE_WIDTH - myButtonContainer.getMaxWidth());
		myRoot.getChildren().add(myButtonContainer);
	}

	private void createCustomButtons(Configuration config) {
		VBox custom = new VBox(PADDING);
		for (String str : config.getAllCustomParamNames()) {
			SimulationSlider slider = new SimulationSlider(str);
			custom.getChildren().add(slider.getVBox());
		}
		myButtonContainer.getChildren().add(custom);
	}

	private void setRowAndColEventHandler(Slider sizeSlider, boolean isRow) {
		sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				myGameloop.pause();
				int newval = (int) newValue;
				if (isRow) {
					ConfigurationLoader.getConfig().setNumRows(newval);
				} else {
					ConfigurationLoader.getConfig().setNumCols(newval);
				}
			}
		});
	}

	private void setFPSEventHandler(Slider fpsSlider) {
		fpsSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double fpsdouble = 1000.0 / (double) newValue;
				ConfigurationLoader.getConfig().setFramesPerSec((int) fpsdouble);
				myGameloop.pause();
				myGameloop.getKeyFrames().remove(0);
				myGameloop.getKeyFrames().add(new KeyFrame(Duration.millis(fpsdouble), e -> {
					updateGrid();
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
			//TODO: config area
			myGameloop.stop();
			myRoot.getChildren().removeAll(myRoot.getChildren());
			try {
				ConfigurationLoader.loader().setSource(SIMULATIONS.getValue() + ".xml").load();
				initSimulation();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				throw new NullPointerException("Unable to load simulation");
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
			updateGrid();
		});
	}

	private void setStopEventHandler(SimulationButton btn) {
		setDimensions(btn);
		btn.setOnAction(e -> {
			if (btn.getDisplayName().equals(GenericButton.RESET.toString())) {
				try {
					initSimulation();
				} catch (Exception e1) {
					throw new NullPointerException("Unable to init simulation");
				}
				// ConfigurationLoader.getConfig();
				// myCellPane.getChildren().removeAll(myCellPane.getChildren());
				// createCellPane();
				// createAllButtons(config);
				// createSimulation();
				// createGameLoop();
				// createResetTimelineChecker();
			}
			myGameloop.pause();
			simIsRunning = false;
		});
	}

	// private void setGridLinesVisible(SimulationButton btn) {
	// setDimensions(btn);
	// btn.setOnAction(e -> {
	// if (!gridLinesVisible) {
	// setGridLines(true);
	// } else {
	// setGridLines(false);
	// }
	// });
	// }
	//
	// private void setGridLines(boolean isVisible) {
	// if (isVisible) {
	// cellPane.setGridLinesVisible(true);
	// gridLinesVisible = true;
	// } else {
	// cellPane.setGridLinesVisible(false);
	// gridLinesVisible = false;
	// }
	// }c

	private void setDimensions(SimulationButton btn) {
		btn.setMinWidth(BUTTON_WIDTH / 2);
		btn.setMaxWidth(BUTTON_WIDTH / 2);
	}

	/**
	 * Creates a new GridPane for your simulation and adds it to the root
	 */
	private void createCellPane() {
		myCellPane = new GridPane();
		myCellPane.setMaxWidth(GRID_WIDTH + GRID_PADDING);
		// cellPane.setMinWidth(GRID_WIDTH + GRID_PADDING);
		myCellPane.setMaxHeight(GRID_HEIGHT);
		// cellPane.setMinHeight(GRID_HEIGHT);
		myCellPane.setPadding(cellPanePadding);
		myRoot.getChildren().add(myCellPane);
	}

}