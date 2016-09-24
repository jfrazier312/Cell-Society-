package config;

import org.w3c.dom.Document;

public class ConfigurationLoader {
	
	public static final String DATA_PATH_PREFIX = "data/";
	
	private String sourcePath;
	private Configuration config;

	private static ConfigurationLoader loader;

	/**
	 * Singleton Pattern.
	 * Only one instance of XML configuration thru out the simulation.
	 * Set once, update everywhere.
	 */
	private ConfigurationLoader() {

	}
	
	public synchronized static ConfigurationLoader loader() {
        if(loader == null)
        	loader = new ConfigurationLoader();
		return loader;
	}
	
	public synchronized ConfigurationLoader setSource(String src) {
		sourcePath = DATA_PATH_PREFIX + src;
		return this;
	}
	
	public synchronized ConfigurationLoader load() throws Exception {
		Document doc = XMLParser.parse(sourcePath);
//		if (!XMLParser.validate(doc)){
//			throw new Exception("sourcePath not initialized");
//		}
		config = new Configuration(doc, "Xpath");
		return this;
	}
	
	public synchronized static Configuration getConfig() {
		if(loader().config == null) {
        	try {
        		loader().load();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		return loader().config;
	}
}
