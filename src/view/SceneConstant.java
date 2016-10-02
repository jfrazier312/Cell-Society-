package view;

/**
 * 
 * @author Jordan Frazier
 *
 */
public enum SceneConstant {

	GRID_HEIGHT(400),
	GRID_WIDTH(400),
	
	SCENE_HEIGHT(500),
	SCENE_WIDTH(800),
	
	GRID_PADDING(40),
	PADDING(10),
	BUTTON_CONTAINER_WIDTH(250),
	
	SLIDER_MINIMUM(1.0),
	SLIDER_MAXIMUM(50.0),
	
	MAX_FPS(17.0);
	
	double myVal;
	
	SceneConstant(double val) {
		myVal = val;
	}
	
	public double getValue() {
		return myVal;
	}
	
	
}
