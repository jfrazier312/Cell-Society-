import config.Configuration;
import model.ConfigurationLoader;

public class Main {

	public static void main(String[] args) {
		// testing purpose, will change
		Configuration config;
		try {
			config = ConfigurationLoader.loader().setSource("testxml.xml").load().getConfig();
			System.out.println(config.getControllerConfig().getRulesOnState("dead").get(2).getNextState());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
