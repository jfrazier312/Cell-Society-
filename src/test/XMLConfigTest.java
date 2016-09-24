package test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import config.Configuration;
import config.State;
import config.States;
import model.ConfigurationLoader;

public class XMLConfigTest {
	
	private static Configuration config;

	@BeforeClass
	public static void onceExecutedBeforeAll() {
		try {
			ConfigurationLoader.loader().setSource("testxml.xml").load();
			config = ConfigurationLoader.getConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void basicProptertiesLoadedProperly() {
		assertEquals("The Game of Life", config.getSimulationName());
		assertEquals("team19", config.getAuthor());
		assertEquals(30, config.getGirdHeight());
		assertEquals(20, config.getGirdWidth());
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
		assertEquals("state1", config.getDefaultInitState().getValue());
	}
	
	@Test
	public void neighborhoodLoadedProperly() {
		assertEquals(5, config.getNeighborhood().getSize());
		assertEquals("constant", config.getNeighborhood().getEdgeType());
		assertEquals("state1", config.getNeighborhood().getEdgeValue());
	}
}
