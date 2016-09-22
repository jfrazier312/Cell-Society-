import javafx.scene.control.Button;

public class SimulationButton extends Button {
	
	private String displayName;
	
	public SimulationButton(String displayName) throws Exception {
		
		this.displayName = displayName;
		setDisplayName(this.displayName);	
		
	}
	
	private void setDisplayName(String displayName) {
		this.setText(displayName);
	}
	
	public String getDisplayName() {
		return displayName;
	}

}
