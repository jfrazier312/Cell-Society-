package model;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import config.Configuration;
import config.ControllerConfiguration;
import config.ModelConfiguration;
import config.ViewConfiguration;

public class ConfigurationLoader {
	
	public static final String PATH_PREFIX = "data/";
	
	private String sourcePath;
	private static ConfigurationLoader loader;

	/**
	 * Singleton Pattern.
	 * Only one instance of XML configuration thru out the simulation.
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
	
	public ConfigurationLoader setSource(String src) {
		sourcePath = PATH_PREFIX + src;
		return this;
	}
	
	public Configuration load()
			throws ParserConfigurationException, SAXException, IOException {
		Document doc = XMLParser.parse(sourcePath);
		Configuration config = new Configuration();
		config.setModelConfig(new ModelConfiguration(doc))
			  .setCtrlrConfig(new ControllerConfiguration(doc))
			  .setViewConfig(new ViewConfiguration(doc));
		return config;
	}
}
