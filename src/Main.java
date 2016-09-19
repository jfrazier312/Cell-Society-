import config.Configuration;
import model.ConfigurationLoader;

public class Main {

	public static void main(String[] args) {
		// testing purpose, will change
		Configuration config;
		try {
			config = ConfigurationLoader.loader().setSource("testxml.xml").load().getConfig();
			for (String s : config.getModelConfig().getAllStates().getValues()) {
				System.out.println(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
