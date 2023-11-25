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
       	for (int i = 0; i < intersectionsList.getLength(); i++) {
        	map.addIntersection((createIntersection((Element) intersectionsList.item(i))));
       	}
       	NodeList segmentList = noeudDOMRacine.getElementsByTagName("segment");
       	for (int i = 0; i < segmentList.getLength(); i++) {
          	map.addSegment(createSegement((Element) segmentList.item(i),map));
       	}
		Element warehouseElem = (Element) noeudDOMRacine.getElementsByTagName("warehouse").item(0);
		long warehouseId = warehouseElem.getAttribute("adress"); 
		if (!map.hasIntersection(warehouseId))
   			throw new ExceptionXML("Error when reading file: The adress of the warehouse must be an existing intersection");
		Intersection warehouse = map.getIntersectionById(warehouseId);
		map.setWarehouse(warehouse);		
    }
    
    private static Intersection createIntersection(Element elt) throws ExceptionXML{
   		double latitude = Double.parseDouble(elt.getAttribute("latitude"));
   		double longitude = Double.parseDouble(elt.getAttribute("longitude"));
		long id = Long.parseLong(elt.getAttribute("id"));
		if(id<0)
			throw new ExceptionXML("Error when reading file: The id of an intersection must be positive");
   		return new Intersection(id,latitude,longitude);
    }
    
    private static Segment createSegment(Element elt,Map map) throws ExceptionXML{
   		long idDestination = Long.parseLong(elt.getAttribute("destination"));
   		long idOrigin = Long.parseLong(elt.getAttribute("origin"));
   		if (!map.hasIntersection(idOrigin) || !map.hasIntersection(idDestination))
   			throw new ExceptionXML("Error when reading file: The origin and destination of a segment must be an existing intersection");
		Intersection origin = map.getIntersectionById(idOrigin);
		Intersection destination = map.getIntersectionById(idDestination);
      	double length = Double.parseDouble(elt.getAttribute("length"));
		if(length<=0)
			throw new ExceptionXML("Error when reading file: The length of a segment must be positive");
		String name = elt.getAttribute("name");
   		return new Segment(origin, destination,length,name);
    }
 
}
