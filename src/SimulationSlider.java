import javafx.scene.layout.HBox;

public class SimulationSlider extends SliderCreator {

	private HBox hbox;

	public SimulationSlider(String displayName) {
		super(displayName);
		hbox = super.getSlider();
	}
	
	public HBox getSlider() {
		return hbox;
	}


}
