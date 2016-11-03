package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import config.Configuration;
import config.ConfigurationLoader;
import config.State;
import config.States;
import exceptions.MalformedXMLSourceException;
import exceptions.QueryExpressionException;
import exceptions.UnrecognizedQueryMethodException;
import exceptions.XMLParserException;

/**
 * @author CharlesXu
 */
public class ConfigurationTest {
	
	private static Configuration config;
	private static final String SOURCE = "testxml.xml";
	private static final String OUTPUT_SOURCE = "newCreatedXML.xml";

	@Before
	public void executedOnceBeforeEach() {
		try {
			config = new Configuration(SOURCE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void basicProptertiesLoadedProperly() {
		assertEquals("The Game of Life", config.getSimulationName());
		assertEquals("team19", config.getAuthor());
		assertEquals(30, config.getNumRows());
		assertEquals(20, config.getNumCols());
		assertEquals(1, config.getFramesPerSec());
	}
	
	@Test
	public void statesLoadedProperly() {
		Set<String> colors = new HashSet<>();
		colors.add("color1");
		colors.add("color2");
		States states = config.getAllStates();
		for (State s : states) {
			assertTrue(colors.contains(s.getAttributes().get("color")));
			if (s.getAttributes().containsKey("lifeTime")) {
				assertEquals("20", s.getAttributes().get("lifeTime"));
				assertEquals("state2", s.getValue());
			}
		}
		assertNotNull(config.getAllStates().getStateByName("state2"));
	}
	
	@Test
	public void neighborhoodLoadedProperly() {
		assertEquals(5, config.getNeighborhood().getSize());
		assertEquals("constant", config.getNeighborhood().getEdgeType());
		assertEquals("state1", config.getNeighborhood().getEdgeValue());
	}
	
	@Test
	public void customParamsLoadedProperly() {
		assertEquals("0.8", config.getCustomParam("probability"));
	}
	
	private static final String AUTHOR = "different author",
							  SIMULATION = "different simulation",
							  STATE_CHOSEN = "state1",
							  STATE_ATTR = "color",
							  STATE_ATTR_VALUE = "new color",
							  EDGE_TYPE = "random",
							  SHAPE = "Triangle",
							  PARAM = "probability",
							  PARAM_VALUE = "0.2";
	private static final int GRID_WIDTH = 100,
						   GRID_HEIGHT = 200,
						   FPS = 10,
						   NEIGHBORHOOD_SIZE = 9;
	
	@Test
	@Deprecated
	public void loadAgainWillReset() {
		try {
			config.setAuthor(AUTHOR);
			ConfigurationLoader.loader().load(SOURCE);
			assertNotEquals(ConfigurationLoader.getConfig(SOURCE), AUTHOR);
		} catch (MalformedXMLSourceException | XMLParserException | UnrecognizedQueryMethodException | QueryExpressionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void resetClearsOldStates() {
		try {
			config.setAuthor(AUTHOR);
			config.reset();
			assertNotEquals(config.getAuthor(), AUTHOR);
		} catch (XMLParserException | UnrecognizedQueryMethodException | QueryExpressionException
				| MalformedXMLSourceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void serializationWritesUpdatedValues() {
		File file = new File(Configuration.DATA_PATH_PREFIX + OUTPUT_SOURCE);
		if (file.exists()) {
			assertTrue(file.delete());
		}
		try {
			config.setSimulationName(SIMULATION)
				  .setAuthor(AUTHOR)
				  .setNumRows(GRID_HEIGHT)
				  .setNumCols(GRID_WIDTH)
				  .setFramesPerSec(FPS)
				  .setCustomParam(PARAM, PARAM_VALUE)
				  .setShape(SHAPE);
			config.getAllStates().getStateByName(STATE_CHOSEN)
				  .getAttributes().put(STATE_ATTR,  STATE_ATTR_VALUE);
			config.getNeighborhood().setSize(NEIGHBORHOOD_SIZE).setEdgeType(EDGE_TYPE);
			config.serializeTo(OUTPUT_SOURCE);
			assertTrue(file.exists());
		} catch (QueryExpressionException e) {
			assertNull(e); // should not have exceptions
		}
		try {
			Configuration newConfig = new Configuration(OUTPUT_SOURCE);
			assertEquals(newConfig.getSimulationName(), SIMULATION);
			assertEquals(newConfig.getAuthor(), AUTHOR);
			assertEquals(newConfig.getNumCols(), GRID_WIDTH);
			assertEquals(newConfig.getNumRows(), GRID_HEIGHT);
			assertEquals(newConfig.getFramesPerSec(), FPS);
			assertEquals(newConfig.getAllStates().getStateByName(STATE_CHOSEN).getAttributes()
					.get(STATE_ATTR), STATE_ATTR_VALUE);
			assertEquals(newConfig.getNeighborhood().getSize(), NEIGHBORHOOD_SIZE);
			assertEquals(newConfig.getNeighborhood().getEdgeType(), EDGE_TYPE);
			assertEquals(newConfig.getCustomParam(PARAM), PARAM_VALUE);
			assertEquals(newConfig.getShape(), SHAPE);
		} catch (NumberFormatException | MalformedXMLSourceException | XMLParserException
				| UnrecognizedQueryMethodException | QueryExpressionException e) {
			assertNull(e); // should not have exceptions
		}
	}
}
