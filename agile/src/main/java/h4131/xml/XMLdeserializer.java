package h4131.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
import h4131.model.CurrentDeliveryPoint;
import h4131.model.DeliveryPoint;


public class XMLdeserializer {
	/**
	 * Open an XML file and create different objects from this file
	 * @param fileType the name of the file
	 * @return Element the root Element of the xml document
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


	/**
	 * loads a map thanks to a xml file
	 * @param mapFileName the name of the file
	 * @param map the map which is going to be loaded by the xml document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ExceptionXML
	 */

	public static void loadMap(String mapFileName, Map map) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		InputStream xml;
		xml = XMLdeserializer.class.getResourceAsStream("/h4131/"+mapFileName);
		if(xml==null)
			throw new ExceptionXML("You're trying to load a global tour on a non-existing map");
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
		Document document = docBuilder.parse(xml);
		Element root = document.getDocumentElement();
		if (root.getNodeName().equals("map")) {
			buildFromDOMXMLMap(root, map);
		}
		else
			throw new ExceptionXML("Wrong format");
		
	}

	/**
	 * Builds a map thanks to the root Element
	 * @param noeudDOMRacine the root Element of the xml document
	 * @param map the map which is going to be loaded by the xml document
	 * @throws NumberFormatException
	 * @throws ExceptionXML
	 */
	
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

	/**
	 * Loads a global tour thanks to a xml file
	 * @param gt the Global tour which is going to be loaded by the xml document
	 * @return Element the root element of the xml document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ExceptionXML
	 */
	public static Element loadGlobalTourFirst(GlobalTour gt) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		Element root = openFile("Choose a tour");
        if (root.getNodeName().equals("globalTour")) {
           buildMapFromDOMXMLGT(root,gt);
        }
        else
        	throw new ExceptionXML("Wrong format");
		return root;
	}

	/**
	 * Builds the first part of a global tour thanks to the root element
	 * @param noeudDOMRacine the root Element of the xml document
	 * @param gt the global tour which is going to be loaded by the xml document
	 * @throws NumberFormatException
	 * @throws ExceptionXML
	 */
	
	
    private static void buildMapFromDOMXMLGT(Element noeudDOMRacine,GlobalTour gt) throws ExceptionXML, NumberFormatException{
		String nameOfMap = ((Element)noeudDOMRacine.getElementsByTagName("map").item(0)).getAttribute("name");
		gt.setMap(nameOfMap);
       		
    }

	/**
	 * Builds the rest of the global tour thanks to the root element
	 * @param noeudDOMRacine the root Element of the xml document
	 * @param gt the global tour to load
	 * @param map the map on which the global tour will be loaded
	 * @param currentDp the CurrentDeliveryPoints of the Global Tour
	 * @param gt the global tour which is going to be loaded by the xml document
	 * @throws NumberFormatException
	 * @throws ExceptionXML
	 */ 


	 public static void buildRestFromDOMXMLGT(Element noeudDOMRacine, GlobalTour gt,Map map, CurrentDeliveryPoint currentDp) throws ExceptionXML, NumberFormatException{
		NodeList TourList = noeudDOMRacine.getElementsByTagName("tour");
		for(int i =0;i<TourList.getLength();i++){
			Tour tour = createTour((Element)TourList.item(i),map,currentDp);
			if(tour != null)
				gt.addTour(tour);
		}
       		
    }

	/**
	 * Creates an intersection for an intersection element
	 * @param elt the intersection element
	 * @return Intersection the intersection created
	 * @throws ExceptionXML
	 */ 

    
    private static Intersection createIntersection(Element elt) throws ExceptionXML{
   		double latitude = Double.parseDouble(elt.getAttribute("latitude"));
   		double longitude = Double.parseDouble(elt.getAttribute("longitude"));
		long id = Long.parseLong(elt.getAttribute("id"));
		if(id<0)
			throw new ExceptionXML("Error when reading file: The id of an intersection must be positive");
   		return new Intersection(id,latitude,longitude);
    }

	/**
	 * Creates a segment for an segment element
	 * @param elt the segment element
	 * @param map the map to which the segment belongs
	 * @return Segment the segment created
	 * @throws ExceptionXML
	 */ 
    
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

	/**
	 * Creates a tour for an tour element
	 * @param elt the tour element
	 * @param map the map to which the tour belongs
	 * @param currentDp the CurrentDeliveryPoints associated to the global tour
	 * @return Tour the tour created
	 * @throws ExceptionXML
	 */ 

	private static Tour createTour(Element elt, Map map, CurrentDeliveryPoint currentDp) throws ExceptionXML{
		int CourierId = Integer.parseInt(elt.getAttribute("courierId"));
		Tour tour = null;
		if(CourierId<0){
			throw new ExceptionXML("Error when reading file: The id of the courier must be positive");
		}
		
			tour = new Tour(CourierId);
			NodeList routes = elt.getElementsByTagName("route");
			NodeList deliverypointList = elt.getElementsByTagName("deliveryPoint");
			for (int i=1; i<deliverypointList.getLength(); i++ ){
				DeliveryPoint deliveryPoint = createDeliveryPoint((Element) deliverypointList.item(i), map);
				tour.addDeliveryPoint(deliveryPoint);
				if(CourierId>currentDp.getAffectedDeliveryPoints().size())
					currentDp.addNewCourier();
				currentDp.addAffectedDeliveryPoint(CourierId, deliveryPoint);
			}
			for (int i=0; i<routes.getLength(); i++ ){
				Segment route = createSegment((Element) routes.item(i), map);
				tour.addSegment(route);
			}
		
		
		
		return tour;
		
	}

	/**
	 * Creates a DeliveryPoint for an DeliveryPoint element
	 * @param elt the DeliveryPoint element
	 * @param map the map to which the DeliveryPoint belongs
	 * @return DeliveryPoint the DeliveryPoint created
	 * @throws ExceptionXML
	 */ 

	private static DeliveryPoint createDeliveryPoint(Element elt, Map map) throws ExceptionXML{
		TimeWindow[] timeWindows = TimeWindow.values();
		long idDP = Long.parseLong(elt.getAttribute("idIntersection"));
			if(!map.hasIntersection(idDP))
				throw new ExceptionXML("Error when reading global tour file: The delivery points must exist in the loaded map");
			Intersection intersectionDP = map.getIntersectionById(idDP);
			int TWindex = Integer.parseInt(elt.getAttribute("timeWindow"));
			if(TWindex<0 || TWindex>4)
				throw new ExceptionXML("Error when reading file: The time window doesn't correspond to anything");//a changer
			return new DeliveryPoint(intersectionDP,timeWindows[TWindex]);
	} 

	
 
}