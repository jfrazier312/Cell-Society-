package config;

import java.util.HashMap;
import java.util.Map;

public class State {
	
	private String value;
	private Map<String, String> attributes;
	
	public State() {
		attributes = new HashMap<String, String>();
	}
	
	public String getValue() {
		return value;
	}
	
	public State setValue(String value) {
		this.value = value;
		return this;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
}
