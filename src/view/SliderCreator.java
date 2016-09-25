package view;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.security.auth.login.Configuration;

import config.ConfigurationLoader;

public class SliderCreator extends Slider {

	private HBox hbox;
	private Slider slider;
	private VBox vbox;
	
	public static boolean reset;
	
	public SliderCreator(double min, double max, double defaultVal, String displayName) {
		reset = false;
		hbox = new HBox(10);
		Label lbl = new Label(String.valueOf(defaultVal));
		Label display = new Label(displayName);
		slider = new Slider(min, max, defaultVal);
	
		hbox.getChildren().addAll(display, slider, lbl);

		slider.valueProperty().addListener(e -> {
			Double newValue = (double) Math.round(slider.getValue() * 10.0) / 10.0;
			lbl.textProperty().setValue(String.valueOf(newValue));
		});
		
		slider.setOnMouseReleased(e -> {
			Double newValue = (double) Math.round(slider.getValue() * 10.0) / 10.0;
			lbl.textProperty().setValue(String.valueOf(newValue));
			ConfigurationLoader.getConfig().setCustomParam(displayName, String.valueOf(newValue));
			if(displayName.contains("percent")) {
				reset = true;
			}
		});
		
	}

	public SliderCreator(String text) {
		reset = false;
		
		double defaultValue = Double.parseDouble(ConfigurationLoader.getConfig().getCustomParam(text));
		
		// TODO: Jordan: Fix dimensions on this piece of shit
		hbox = new HBox(10);
		Label lbl = new Label(String.valueOf(defaultValue));
		Label displayName = new Label(text);

		slider = new Slider();
		slider.setMax(1);
		slider.setMin(0);
		slider.setValue(defaultValue);

		vbox = new VBox(5);
		hbox.getChildren().addAll(slider, lbl);
		vbox.getChildren().addAll(displayName, hbox);


		slider.valueProperty().addListener(e -> {
			Double newValue = (double) Math.round(slider.getValue() * 10.0) / 10.0;
			lbl.textProperty().setValue(String.valueOf(newValue));
		});
		
		slider.setOnMouseReleased(e -> {
			Double newValue = (double) Math.round(slider.getValue() * 10.0) / 10.0;
			lbl.textProperty().setValue(String.valueOf(newValue));
			ConfigurationLoader.getConfig().setCustomParam(text, String.valueOf(newValue));
			if(text.contains("percent")) {
				reset = true;
			}
		});
	}

	public HBox getHbox(){
		return hbox;
	}
	
	public VBox getVBox(){
		return vbox;
	}
	
	public Slider getSlider() {
		return slider;
	}
	
	public void setReset(boolean reset) {
		SliderCreator.reset = reset;
	}

}
