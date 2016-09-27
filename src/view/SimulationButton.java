package view;
import javafx.scene.control.Button;

public class SimulationButton extends Button {
	
	private String displayName;
	
	public SimulationButton(GenericButton displayName) throws Exception {
		
		this.displayName = displayName.toString();
		setDisplayName(this.displayName);	
		
	}
	
	private void setDisplayName(String displayName) {
		this.setText(displayName);
	}
	
	public String getDisplayName() {
		return displayName;
	}

}
