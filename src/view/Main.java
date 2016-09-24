package view;
import config.Configuration;
import model.ConfigurationLoader;

public class Main {

	public static void main(String[] args) {
		// testing purpose, will change
		Configuration config;
		try {
			ConfigurationLoader.loader().setSource("testxml.xml").load();
			config = ConfigurationLoader.getConfig();
			System.out.println(config.getAllStates());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
