package view;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class SliderCreator extends HBox {

	private HBox hbox;

	public SliderCreator(String text) {

		hbox = new HBox(20);
		Label lbl = new Label(text);
		lbl.setPrefWidth(50);
		Slider slider = new Slider();
		slider.setMax(100);
		slider.setMin(0);

		hbox.getChildren().addAll(lbl, slider);

		slider.valueProperty().addListener(e -> {
			Double d = (double) Math.round(slider.getValue() * 10.0) / 10.0;
			lbl.textProperty().setValue(String.valueOf(d));
		});

		slider.setValue(50.0);

	}

	public HBox getSlider() {
		return hbox;
	}

}
