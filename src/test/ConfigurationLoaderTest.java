package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import config.Configuration;
import config.ConfigurationLoader;
import exceptions.MalformedXMLSourceException;
import exceptions.QueryExpressionException;
import exceptions.UnrecognizedQueryMethodException;
import exceptions.XMLParserException;

/**
 * @author CharlesXu
 */
public class ConfigurationLoaderTest {
	
	private static String SOURCE = "testxml.xml";
	private static String SOURCE2 = "testxml2.xml";
	
	@Before
	public void onceExecutedBeforeEachTestCase() {
		try {
			try {
				ConfigurationLoader.loader().load(SOURCE).load(SOURCE2);
			} catch (XMLParserException | UnrecognizedQueryMethodException | QueryExpressionException e) {
				e.printStackTrace();
			}
		} catch (MalformedXMLSourceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void sourceXMLLoadedProperly() {
		Configuration c1 = ConfigurationLoader.getConfig(SOURCE);
		Configuration c2 = ConfigurationLoader.getConfig(SOURCE2);
		assertNotNull(c1);
		assertNotNull(c2);
		assertNotEquals(c1, c2);
	}
	
	@Test
	public void flushResetsConfigLoaderStorage() {
		ConfigurationLoader.loader().flush();
		Configuration c1 = ConfigurationLoader.getConfig(SOURCE);
		Configuration c2 = ConfigurationLoader.getConfig(SOURCE2);
		assertNull(c1);
		assertNull(c2);
	}
	
	@Test
	public void storeOverwritePreviousKV() {
		ConfigurationLoader.loader().store(
				SOURCE2, ConfigurationLoader.getConfig(SOURCE));
		assertEquals(ConfigurationLoader.getConfig(SOURCE),
					 ConfigurationLoader.getConfig(SOURCE2));
	}

	@Test
	public void testStore() {
		ConfigurationLoader.loader().store(
				SOURCE2, ConfigurationLoader.getConfig(SOURCE));
		assertEquals(ConfigurationLoader.getConfig(SOURCE),
					 ConfigurationLoader.getConfig(SOURCE2));
	}
}
