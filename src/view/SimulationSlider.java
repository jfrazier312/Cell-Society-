package view;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SimulationSlider extends SliderCreator {

	private Slider slider;
	private HBox hbox;
	private VBox vbox;
	
	public SimulationSlider(double min, double max, double defaultVal, String displayName, boolean set) {
		super(min, max, defaultVal, displayName, set);
		slider = super.getSlider();
		hbox = super.getHbox();
	}

	public SimulationSlider(String displayName) {
		super(displayName);
		slider = super.getSlider();
		hbox = super.getHbox();
		vbox = super.getVBox();
	}
	
	public Slider getSlider() {
		return slider;
	}
	
	public HBox getHbox(){
		return hbox;
	}
	
	public VBox getVbox() {
		return vbox;
	}


}
