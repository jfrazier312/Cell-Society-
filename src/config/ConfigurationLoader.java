package config;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;

import exceptions.MalformedXMLSourceException;
import exceptions.SourcePathFoundNoFileException;
import exceptions.SourcePathNotInitialized;

public class ConfigurationLoader {
	
	public static final String DATA_PATH_PREFIX = "data/";
	
	private Map<String, Configuration> storage;

	private static ConfigurationLoader loader;

	/**
	 * Singleton Pattern.
	 * Set once, update everywhere.
	 * Prevent Stale Copy at all time thruout the process.
	 */
	private ConfigurationLoader() {

	}
	
	public synchronized static ConfigurationLoader loader() {
        if(loader == null) { 
        	loader = new ConfigurationLoader();
        	loader.flush();
        }
		return loader;
	}
	
	public synchronized ConfigurationLoader load(String src)
			throws MalformedXMLSourceException {
		String sourcePath = buildSourcePath(src);
		Document doc;
		try {
			doc = XMLParser.parse(sourcePath);
			// TODO (cx15):  validation
			Configuration config = new Configuration(doc, "Xpath");
			storage.put(sourcePath, config);
			// TODO throw
		} catch (SourcePathFoundNoFileException | SourcePathNotInitialized e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public synchronized ConfigurationLoader flush() {
		storage = new HashMap<>();
		return this;
	}
	
	public synchronized static Configuration getConfig(String src) {
		String sourcePath = buildSourcePath(src);
		return loader().storage.get(sourcePath);
	}
	
	public synchronized void store(String src, Configuration config) {
		storage.put(buildSourcePath(src), config);
	}
	
	public static String buildSourcePath(String src) {
		return DATA_PATH_PREFIX + src;
	}
}
