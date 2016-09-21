package model;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLParser {
	
	public static Document parse(String sourcePath) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(sourcePath));
			doc.getDocumentElement().normalize();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		return doc;
	}

}
