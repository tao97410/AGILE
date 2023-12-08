package h4131.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import h4131.model.GlobalTour;
import h4131.model.Map;
import h4131.model.Intersection;
import h4131.model.Segment;
import h4131.model.Tour;
import h4131.model.TimeWindow;
import h4131.model.DeliveryPoint;


public class XMLdeserializer {
	/**
	 * Open an XML file and create plan from this file
	 * @param plan the plan to create from the file
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ExceptionXML
	 */

	private static Element openFile(String fileType) throws ExceptionXML, ParserConfigurationException, SAXException, IOException{
		File xml = XMLfileOpener.getInstance().open(true, fileType);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element root = document.getDocumentElement();
		return root;
	} 

	/////////////////MAP////////////////////////////////
	public static void loadMap(String mapFileName, Map map) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		InputStream xml;
		xml = XMLdeserializer.class.getResourceAsStream("/h4131/"+mapFileName);
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
		Document document = docBuilder.parse(xml);
		Element root = document.getDocumentElement();
		if (root.getNodeName().equals("map")) {
			buildFromDOMXMLMap(root, map);
		}
		else
			throw new ExceptionXML("Wrong format");
		
	}
	
    private static void buildFromDOMXMLMap(Element noeudDOMRacine, Map map) throws ExceptionXML, NumberFormatException{
       	NodeList intersectionsList = noeudDOMRacine.getElementsByTagName("intersection");
       	for (int i = 0; i < intersectionsList.getLength(); i++) {
        	map.addIntersection((createIntersection((Element) intersectionsList.item(i))));
       	}
       	NodeList segmentList = noeudDOMRacine.getElementsByTagName("segment");
       	for (int i = 0; i < segmentList.getLength(); i++) {
          	map.addSegment(createSegment((Element) segmentList.item(i),map));
       	}
		Element warehouseElem = (Element) noeudDOMRacine.getElementsByTagName("warehouse").item(0);
		long warehouseId = Long.parseLong(warehouseElem.getAttribute("address")); 
		if (!map.hasIntersection(warehouseId))
   			throw new ExceptionXML("Error when reading file: The adress of the warehouse must be an existing intersection");
		Intersection warehouse = map.getIntersectionById(warehouseId);
		DeliveryPoint warehousePoint = new DeliveryPoint(warehouse, TimeWindow.WAREHOUSE);
		map.setWarehouse(warehousePoint);		
    }
	//////////////////GLOBAL TOUR////////////////////////////
	public static void loadGlobalTour(GlobalTour gt,Map map) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		Element root = openFile("Choose a tour");
        if (root.getNodeName().equals("globalTour")) {
           buildFromDOMXMLGT(root, gt,map);
        }
        else
        	throw new ExceptionXML("Wrong format");
	}
	
    private static void buildFromDOMXMLGT(Element noeudDOMRacine, GlobalTour gt,Map map) throws ExceptionXML, NumberFormatException{
		NodeList TourList = noeudDOMRacine.getElementsByTagName("tour");
		for(int i =0;i<TourList.getLength();i++){
			gt.addTour(createTour((Element)TourList.item(i),map));
		}
       		
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

	private static Tour createTour(Element elt, Map map) throws ExceptionXML{
		long CourierId = Long.parseLong(elt.getAttribute("courierId"));
		if(CourierId<0){
			throw new ExceptionXML("Error when reading file: The id of the courier must be positive");
		}
		Tour tour = new Tour(CourierId);
		NodeList routes = elt.getElementsByTagName("route");
		NodeList deliverypointList = elt.getElementsByTagName("deliveryPoint");
		for (int i=0; i<deliverypointList.getLength(); i++ ){
			DeliveryPoint deliveryPoint = createDeliveryPoint((Element) deliverypointList.item(i), map);
			tour.addDeliveryPoint(deliveryPoint);
		}
		for (int i=0; i<routes.getLength(); i++ ){
			Segment route = createSegment((Element) routes.item(i), map);
			tour.addSegment(route);
		}
		return tour;
		
	}

	private static DeliveryPoint createDeliveryPoint(Element elt, Map map) throws ExceptionXML{
		TimeWindow[] timeWindows = TimeWindow.values();
		long idDP = Long.parseLong(elt.getAttribute("idIntersection"));
			if(!map.hasIntersection(idDP))
				throw new ExceptionXML("Error when reading global tour file: The delivery points must exist in the loaded map");
			Intersection intersectionDP = map.getIntersectionById(idDP);
			int TWindex = Integer.parseInt(elt.getAttribute("timeWindow"));
			if(TWindex<0 || TWindex>3)
				throw new ExceptionXML("Error when reading file: The time window doesn't correspond to anything");//a changer
			return new DeliveryPoint(intersectionDP,timeWindows[TWindex]);
	} 
 
}