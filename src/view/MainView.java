package view;

import java.util.ResourceBundle;

import config.ConfigurationLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.CellGrid;
import model.FireSimulation;
import model.GameOfLifeSimulation;
import model.PredatorPreySimulation;
import model.SegregationSimulation;

/**
 * 
 * @author Jordan Frazier (jrf30)
 *
 */

public class MainView implements GameWorld {

	private Scene myScene;
	private Group myRoot;
	private GridPane myCellPane;
	private CellGrid mySimulation;
	private Timeline myGameloop;
	private VBox myButtonContainer;

	private boolean isGridLinesVisible;
	private boolean simIsRunning;

	private ResourceBundle myResources;
	public static final String RESRC_PATH = "resources/GridResources";

	private Insets buttonPadding = new Insets((SceneConstant.SCENE_HEIGHT.getValue() - SceneConstant.GRID_HEIGHT.getValue()) / 2, SceneConstant.GRID_PADDING.getValue(),
			(SceneConstant.SCENE_HEIGHT.getValue() - SceneConstant.GRID_HEIGHT.getValue()) / 2, -1 * SceneConstant.GRID_PADDING.getValue());
	private Insets cellPanePadding = new Insets((SceneConstant.SCENE_HEIGHT.getValue() - SceneConstant.GRID_HEIGHT.getValue()) / 2, 0, (SceneConstant.SCENE_HEIGHT.getValue() - SceneConstant.GRID_HEIGHT.getValue()) / 2,
			SceneConstant.GRID_PADDING.getValue());

	public Scene initScene() throws Exception {
		myRoot = new Group();
		myScene = new Scene(myRoot, SceneConstant.SCENE_WIDTH.getValue(), SceneConstant.SCENE_HEIGHT.getValue());
		myResources = ResourceBundle.getBundle(RESRC_PATH);
		// isGridLinesVisible = false;
		beginInitialSetup();

		return myScene;
	}

	private void beginInitialSetup() {
		initSimulation();
		setSimulationEventHandler();
		createResetTimelineChecker();
		createGameLoop();
	}

	private void initSimulation() {
		// Configuration config = new Configuration();
		myRoot.getChildren().removeAll(myRoot.getChildren());
		createCellPane();
		createSimulation();
		// createAllButtons(config);
		createAllButtons();
		// createResetTimelineChecker();
	}

	private void createResetTimelineChecker() {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		// TODO: get rid of magic number
		timeline.getKeyFrames()
				.add(new KeyFrame(Duration.millis(Double.parseDouble(myResources.getString("MaxFPS"))), e -> {
					if (SimulationSlider.reset) {
						resetGrid();
					}
				}));
		timeline.play();
	}

	private void resetGrid() {
		initSimulation();
		SimulationSlider.reset = false;
		myGameloop.pause();
		simIsRunning = false;
	}

	private void createSimulation() {
		// Configuration config = new Configuration();
		String simulationName = ConfigurationLoader.getConfig().getSimulationName();
		// findSimulation(config.getSimulationName());
		findSimulation(simulationName);
		mySimulation.initSimulation();
		mySimulation.renderGrid(myCellPane);
	}

	// private void findSimulation(String sim) {
	// if (sim.equals(FIRE_SIMULATION)) {
	// mySimulation = new FireSimulation(config);
	// } else if (sim.equals(GAME_OF_LIFE)) {
	// mySimulation = new GameOfLifeSimulation(config);
	// } else if (sim.equals(SEGREGATION_SIMULATION)) {
	// mySimulation = new SegregationSimulation(config);
	// } else if (sim.equals(WATOR_WORLD)) {
	// mySimulation = new PredatorPreySimulation(config);
	// }
	// }

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
			updateGrid();
		}));
	}

	private void updateGrid() {
		myCellPane.getChildren().removeAll(myCellPane.getChildren());
		mySimulation.updateGrid();
		mySimulation.renderGrid(myCellPane);
	}

	// private void createAllButtons(Configuration config) {
	private void createAllButtons() {
		myButtonContainer = new VBox(SceneConstant.PADDING.getValue());

		HBox hbox1 = new HBox(SceneConstant.PADDING.getValue());
		HBox hbox2 = new HBox(SceneConstant.PADDING.getValue());
		VBox vbox3 = new VBox(SceneConstant.PADDING.getValue());

		SimulationButton playBtn = makeButton("Play", new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playButtonHandler();
			}
		});

		SimulationButton pauseBtn = makeButton("Pause", new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				pauseButtonHandler();
			}
		});

		SimulationButton resetBtn = makeButton("Reset", new  EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resetButtonHandler();
			}
		});

		SimulationButton stepBtn = makeButton("Step", new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stepButtonHandler();
			}
		});
		
		SimulationSlider rowsSlider = new SimulationSlider(SceneConstant.SLIDER_MINIMUM, SceneConstant.SLIDER_MAXIMUM, ConfigurationLoader.getConfig().getNumRows(),
				myResources.getString("Rows"), false);
		setRowAndColEventHandler(rowsSlider.getSlider(), true);

		SimulationSlider colsSlider = new SimulationSlider(SceneConstant.SLIDER_MINIMUM, SceneConstant.SLIDER_MAXIMUM, ConfigurationLoader.getConfig().getNumCols(),
				myResources.getString("Columns"), false);
		setRowAndColEventHandler(colsSlider.getSlider(), false);

		SimulationSlider fpsSlider = new SimulationSlider(SceneConstant.SLIDER_MINIMUM, SceneConstant.SLIDER_MAXIMUM, ConfigurationLoader.getConfig().getFramesPerSec(),
				myResources.getString("FPS"), false);
		setFPSEventHandler(fpsSlider.getSlider());

		// TODO: Grid lines
		SimulationButton gridVisible = new SimulationButton(myResources.getString("GridLines"));
		// setGridLinesVisible(gridVisible);

		hbox1.getChildren().addAll(playBtn, pauseBtn);
		hbox2.getChildren().addAll(stepBtn, resetBtn);
		vbox3.getChildren().addAll(rowsSlider.getGenericSlider(), colsSlider.getGenericSlider());

		VBox basicBtnBox = new VBox(SceneConstant.PADDING.getValue());
		basicBtnBox.getChildren().addAll(SIMULATIONS, hbox1, hbox2, vbox3, fpsSlider.getGenericSlider(), gridVisible);
		// TODO: Magic numbers
		basicBtnBox.setMinWidth(300);
		myButtonContainer.getChildren().add(basicBtnBox);

		createCustomButtons();
		// createCustomButtons(config);
		setButtonContainerParameters();
	}

	/**
	 * Creates button and sets event handler
	 * 
	 * @param name - name of the button
	 * @param handler - the event handler of the button
	 * @return
	 */
	private SimulationButton makeButton(String name, EventHandler<ActionEvent> handler) {
		SimulationButton button = new SimulationButton(myResources.getString(name));
		setDimensions(button);
		button.setOnAction(handler);
	}
	
	private void playButtonHandler() {
		simIsRunning = true;
		myGameloop.setCycleCount(Timeline.INDEFINITE);
		myGameloop.playFromStart();
	}

	private void stepButtonHandler() {
		pauseGrid();
		updateGrid();
	}

	private void pauseButtonHandler() {
		pauseGrid();
	}

	private void resetButtonHandler() {
		try {
			initSimulation();
		} catch (Exception e1) {
			throw new NullPointerException("Unable to init simulation");
		}
		pauseGrid();
	}
	
	private void pauseGrid() {
		myGameloop.pause();
		simIsRunning = false;
	}


	private void setButtonContainerParameters() {
		// TODO: Magic numbers
		myButtonContainer.setPadding(buttonPadding);
		myButtonContainer.setMaxWidth(SceneConstant.BUTTON_CONTAINER_WIDTH.getValue());
		myButtonContainer.setMinWidth(SceneConstant.BUTTON_CONTAINER_WIDTH.getValue());
		myButtonContainer.setLayoutX(Integer.parseInt(myResources.getString("SceneWidth")) - SceneConstant.BUTTON_CONTAINER_WIDTH.getValue());
		myRoot.getChildren().add(myButtonContainer);
	}

	// private void createCustomButtons(Configuration config) {
	private void createCustomButtons() {
		VBox custom = new VBox(SceneConstant.PADDING.getValue());
		// for (String str : config.getAllCustomParamNames()) {
		for (String str : ConfigurationLoader.getConfig().getAllCustomParamNames()) {
			SimulationSlider slider = new SimulationSlider(str);
			custom.getChildren().add(slider.getCustomSlider());
		}
		myButtonContainer.getChildren().add(custom);
	}

	private void setRowAndColEventHandler(Slider sizeSlider, boolean isRow) {
		sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				myGameloop.pause();
				int newval = (int) ((double) newValue);
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
				// TODO: Config area
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
		SIMULATIONS.setMinWidth(SceneConstant.BUTTON_CONTAINER_WIDTH.getValue() + SceneConstant.PADDING.getValue());
		SIMULATIONS.setMaxWidth(SceneConstant.BUTTON_CONTAINER_WIDTH.getValue() + SceneConstant.PADDING.getValue());
		SIMULATIONS.valueProperty().addListener(e -> {
			// TODO: config area
			myGameloop.stop();
			myRoot.getChildren().removeAll(myRoot.getChildren());
			try {
				ConfigurationLoader.loader().setSource(SIMULATIONS.getValue() + ".xml").load();
				initSimulation();
			} catch (Exception e1) {
				throw new NullPointerException("Unable to load simulation");
			}
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
		btn.setMinWidth(SceneConstant.BUTTON_CONTAINER_WIDTH.getValue() / 2);
		btn.setMaxWidth(SceneConstant.BUTTON_CONTAINER_WIDTH.getValue() / 2);
	}

	/**
	 * Creates a new GridPane for your simulation and adds it to the root
	 */
	private void createCellPane() {
		myCellPane = new GridPane();
		myCellPane.setMaxWidth(SceneConstant.GRID_WIDTH.getValue() + SceneConstant.GRID_PADDING.getValue());
		// cellPane.setMinWidth(GRID_WIDTH + GRID_PADDING);
		myCellPane.setMaxWidth(SceneConstant.GRID_WIDTH.getValue() + SceneConstant.GRID_PADDING.getValue());
		myCellPane.setMinWidth(SceneConstant.GRID_WIDTH.getValue() + SceneConstant.GRID_PADDING.getValue());

		myCellPane.setMaxHeight(SceneConstant.GRID_HEIGHT.getValue());
		myCellPane.setMinHeight(SceneConstant.GRID_HEIGHT.getValue());

		// cellPane.setPrefWrapLength(50);
		// cellPane.setStyle("-fx-border-color: black; -fx-border-width: 2;");
	}

}