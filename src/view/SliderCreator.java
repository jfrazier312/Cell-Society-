package view;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import config.ConfigurationLoader;

public class SliderCreator extends Slider {

	private HBox hbox;
	private Slider slider;
	
	public SliderCreator(double min, double max, double defaultVal, String displayName) {
		
		hbox = new HBox(10);
		Label lbl = new Label(String.valueOf(defaultVal));
		Label display = new Label(displayName);
		slider = new Slider(min, max, defaultVal);
	
		
		hbox.getChildren().addAll(display, slider, lbl);

		slider.valueProperty().addListener(e -> {
			Double newValue = (double) Math.round(slider.getValue() * 10.0) / 10.0;
			lbl.textProperty().setValue(String.valueOf(newValue));
		});
		
	}

	public SliderCreator(String text) {
		
		double defaultValue = Double.parseDouble(ConfigurationLoader.getConfig().getCustomParam(text));
		
		// TODO: Jordan: Fix dimensions on this piece of shit
		hbox = new HBox(20);
		Label lbl = new Label(String.valueOf(defaultValue));
		lbl.setPrefWidth(50);
		slider = new Slider();
		slider.setMax(100);
		slider.setMin(0);
		slider.setValue(defaultValue);

		hbox.getChildren().addAll(lbl, slider);

		slider.valueProperty().addListener(e -> {
			Double newValue = (double) Math.round(slider.getValue() * 10.0) / 10.0;
			lbl.textProperty().setValue(String.valueOf(newValue));
			ConfigurationLoader.getConfig().setCustomParam(text, String.valueOf(newValue));
		});
	}

	public HBox getHbox(){
		return hbox;
	}
	
	public Slider getSlider() {
		return slider;
	}

}
