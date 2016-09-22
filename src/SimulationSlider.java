import javafx.scene.layout.HBox;

public class SimulationSlider extends SliderCreator {

	private HBox hbox;

	public SimulationSlider(String displayName) {
		super(displayName);
	}
	
	public HBox getSlider() {
		return hbox;
	}


}
