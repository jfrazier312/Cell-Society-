package view;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class SimulationSlider extends SliderCreator {

	private Slider slider;
	private HBox hbox;
	
	public SimulationSlider(double min, double max, double defaultVal, String displayName) {
		super(min, max, defaultVal, displayName);
		slider = super.getSlider();
		hbox = super.getHbox();
	}

	public SimulationSlider(String displayName) {
		super(displayName);
		slider = super.getSlider();
		hbox = super.getHbox();
	}
	
	public Slider getSlider() {
		return slider;
	}
	
	public HBox getHbox(){
		return hbox;
	}


}
