package xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.Courier;
import model.Map;
import model.Intersection;
import model.Segment;
import model.Tour;


public class XMLdeserializer {
	/**
	 * Open an XML file and create plan from this file
	 * @param plan the plan to create from the file
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ExceptionXML
	 */
	public static void load(Map map) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		File xml = XMLfileOpener.getInstance().open(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element root = document.getDocumentElement();
        if (root.getNodeName().equals("map")) {
           buildFromDOMXML(root, map);
        }
        else
        	throw new ExceptionXML("Wrong format");
	}

    private static void buildFromDOMXML(Element noeudDOMRacine, Map map) throws ExceptionXML, NumberFormatException{
       	NodeList intersectionsList = noeudDOMRacine.getElementsByTagName("intersection");
       	for (int i = 0; i < circleList.getLength(); i++) {
        	map.add(createCircle((Element) intersectionsList.item(i)));
       	}
       	NodeList segmentList = noeudDOMRacine.getElementsByTagName("segment");
       	for (int i = 0; i < rectangleList.getLength(); i++) {
          	map.add(createRectangle((Element) segmentList.item(i)));
       	}
    }
    
    private static Intersection createIntersection(Element elt) throws ExceptionXML{
   		double latitude = Double.parseDouble(elt.getAttribute("latitude"));
   		double longitude = Double.parseDouble(elt.getAttribute("longitude"));
		long id = Long.parseLong(elt.getAttribute("id"));
   		return new Intersection(id,latitude,longitude);
    }
    
    private static Segment createSegment(Element elt) throws ExceptionXML{
   		int x = Integer.parseInt(elt.getAttribute("x"));
   		int y = Integer.parseInt(elt.getAttribute("y"));
   		Point p = PointFactory.createPoint(x, y);
   		if (p == null)
   			throw new ExceptionXML("Error when reading file: Point coordinates must belong to the plan");
      	int rectangleWidth = Integer.parseInt(elt.getAttribute("width"));
   		if (rectangleWidth <= 0)
   			throw new ExceptionXML("Error when reading file: Rectangle width must be positive");
      	int rectangleHeight = Integer.parseInt(elt.getAttribute("height"));
   		if (rectangleHeight <= 0)
   			throw new ExceptionXML("Error when reading file: Rectangle height must be positive");
   		return new Rectangle(p, rectangleWidth, rectangleHeight);
    }
 
}
