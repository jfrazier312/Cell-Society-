package view;

import config.ConfigurationLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** 
 * 
 * @author Jordan Frazier (jrf30)
 *
 */

public class SimulationSlider {

	private HBox genericSliderContainer;
	private Slider slider;
	private VBox customSliderContainer;

	// Used in MainView to determine whether a slider needs to reset the grid on release
	public static boolean reset;
	
	public SimulationSlider(String text) {
		reset = false;

		double defaultValue = Double.parseDouble(ConfigurationLoader.getConfig().getCustomParam(text));
		Label lbl = new Label(String.valueOf(defaultValue * 100));
		Label displayName = new Label(text);

		slider = new Slider(0, 1, defaultValue);
		customSliderContainer = new VBox(5);
		genericSliderContainer = new HBox(10);
		genericSliderContainer.getChildren().addAll(slider, lbl);
		customSliderContainer.getChildren().addAll(displayName, genericSliderContainer);

		updateSliderOnDrag(lbl, 100.0);

		slider.setOnMouseReleased(e -> {
			Double newValue = (double) Math.round(slider.getValue() * 100.0) / 100.0;
			lbl.textProperty().setValue(String.valueOf(newValue * 100.00));
			ConfigurationLoader.getConfig().setCustomParam(text, String.valueOf(newValue));
			if (text.contains("percent")) {
				reset = true;
			}
		});
	}

	public SimulationSlider(double min, double max, double defaultVal, String displayName, boolean set) {
		Label lbl = new Label(String.valueOf(defaultVal));
		Label display = new Label(displayName);

		reset = false;
		genericSliderContainer = new HBox(10);
		slider = new Slider(min, max, defaultVal);
		genericSliderContainer.getChildren().addAll(display, slider, lbl);

		updateSliderOnDrag(lbl, 10.0);
		
		if (displayName.contains("percent") || displayName.toLowerCase().contains("row")
				|| displayName.toLowerCase().contains("col")) {
			slider.setOnMouseReleased(e -> {
				Double newValue = (double) Math.round(slider.getValue() * 10.0) / 10.0;
				if (displayName.toLowerCase().contains("row") || displayName.toLowerCase().contains("col")) {
					int i = newValue.intValue();
					lbl.textProperty().setValue(String.valueOf(i));
				} else {
					lbl.textProperty().setValue(String.valueOf(newValue));
				}
				reset = true;
			});
		}

	}

	private void updateSliderOnDrag(Label lbl, double roundValue) {
		slider.valueProperty().addListener(e -> {
			Double newValue = (double) Math.round(slider.getValue() * roundValue) / roundValue;
			lbl.textProperty().setValue(String.valueOf(newValue));
		});
	}

	public HBox getGenericSlider() {
		return genericSliderContainer;
	}

	public VBox getCustomSlider() {
		return customSliderContainer;
	}

	public Slider getSlider() {
		return slider;
	}

	public void setReset(boolean reset) {
		SimulationSlider.reset = reset;
	}

}
