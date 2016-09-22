package model;

import org.w3c.dom.Document;

import config.Configuration;

public class ConfigurationLoader {
	
	public static final String PATH_PREFIX = "data/";
	
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
	
	public static ConfigurationLoader loader() {
		synchronized (ConfigurationLoader.class) { //mutex
            if(loader == null)
            	loader = new ConfigurationLoader();
        }
		return loader;
	}
	
	public synchronized ConfigurationLoader setSource(String src) {
		sourcePath = PATH_PREFIX + src;
		return this;
	}
	
	public synchronized ConfigurationLoader load() throws Exception {
		if (sourcePath == null)
			throw new Exception("sourcePath not initialized");
		Document doc = XMLParser.parse(sourcePath);
		if (doc == null)
			throw new Exception("sourcePath provided unfound");
		config = new Configuration(doc);
		return this;
	}
	
	public synchronized Configuration getConfig() {
		if(config == null) {
        	try {
				load();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		return config;
	}
}
