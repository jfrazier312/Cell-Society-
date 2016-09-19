package config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import utils.Utils;

public class ControllerConfiguration {
	
	private Map<String, List<Rule>> state2rules;
	
	public List<Rule> getRulesOnState(String state) {
		return state2rules.get(state);
	}
	
	public ControllerConfiguration(Document doc) {
		state2rules = new HashMap<String, List<Rule>>();
		buildDirectMappedRules(doc);
		buildCountRules(doc);
		buildMatchRules(doc);
	}
	
	private void buildDirectMappedRules(Document doc) {
		NodeList nl = doc.getElementsByTagName("kv");
		for (int i = 0; i < nl.getLength(); i++) {
			DirectMapped rule = new DirectMapped();
			setBaseRule(nl.item(i), rule);
			String neighborStates = Utils.getAttrFromNode(nl.item(i), "neighbors");
			rule.setNeighborStateList(neighborStates);
			addToMap(rule);
	    }
	}
	
	private void buildCountRules(Document doc) {
		NodeList nl = doc.getElementsByTagName("count");
		for (int i = 0; i < nl.getLength(); i++) {
			Count rule = new Count();
			setBaseRule(nl.item(i), rule);
			String neighborState = Utils.getAttrFromNode(nl.item(i), "neighbors");
			String lowerBound = Utils.getAttrFromNode(nl.item(i), "lowerBound");
			String upperBound = Utils.getAttrFromNode(nl.item(i), "upperBound");
			rule.setNeighborState(neighborState);
			rule.setLowerBound(Integer.parseInt(lowerBound));
			rule.setUpperBound(Integer.parseInt(upperBound));
			addToMap(rule);
	    }
	}
	
	private void buildMatchRules(Document doc) {
		NodeList nl = doc.getElementsByTagName("match");
		for (int i = 0; i < nl.getLength(); i++) {
			Match rule = new Match();
			setBaseRule(nl.item(i), rule);
			String neighborindex = Utils.getAttrFromNode(nl.item(i), "neighborIndex");
			String neighborState = Utils.getAttrFromNode(nl.item(i), "neighbor");
			rule.setNeighborIndex(Integer.parseInt(neighborindex));
			rule.setNeighborState(neighborState);
			addToMap(rule);
	    }
	}
	
	private void addToMap(Rule r) {
		if (state2rules.containsKey(r.getApplicableState())) {
			state2rules.get(r.getApplicableState()).add(r);
		} else {
			List<Rule> list = new ArrayList<Rule>();
			list.add(r);
			state2rules.put(r.getApplicableState(), list);
		}
	}
	
	/**
	 * Set the applicableState and nextState of each Rule
	 * @param n
	 * @param r
	 */
	private Rule setBaseRule(Node n, Rule r) {
		String curState = Utils.getAttrFromNode(n.getParentNode(), "currentState");
		String nextState = Utils.getAttrFromNode(n, "nextState");
		r.setApplicableState(curState);
		r.setNextState(nextState);
		return r;
	}
}
