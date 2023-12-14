package h4131.xml;

import java.io.File;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import h4131.model.DeliveryPoint;
import h4131.model.GlobalTour;
import h4131.model.Segment;
import h4131.model.Tour;



public class XMLserializer {// Singleton
	
	private Element shapeRoot;
	private Document document;
	private static XMLserializer instance = null;
	private XMLserializer(){}
	public static XMLserializer getInstance(){
		if (instance == null)
			instance = new XMLserializer();
		return instance;
	}
 
	/**
	 * Open an XML file and write an XML description of the global tour in it 
	 * @param gt the plan to serialise
     * @return String path
	 * @throws ParserConfigurationException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 * @throws ExceptionXML
	 */
	public String save(GlobalTour gt) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, ExceptionXML{
		File xml = XMLfileOpener.getInstance().open(false,null);
  		StreamResult result = new StreamResult(xml);
       	document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
       	document.appendChild(createGTElt(gt));
        DOMSource source = new DOMSource(document);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xformer.transform(source, result);
        return xml.getAbsolutePath();
	}

	private Element createGTElt(GlobalTour gt) {
		Element racine = document.createElement("globalTour");
        Element map = document.createElement("map");
        racine.appendChild(map);
        createAttribute(map, "name", gt.getMap());
		for (Tour tour : gt.getTours()){
            racine.appendChild(createTourElt(tour));
        }
		return racine;
	}

    private Element createTourElt(Tour tour){
        Element tourElement = document.createElement("tour");
        createAttribute(tourElement, "courierId", Integer.toString(tour.getCourierId()));
        for (Segment route : tour.getCourse()){
            tourElement.appendChild(createRouteElt(route));
        }
        for(DeliveryPoint dp : tour.getDeliveryPoints()){
            tourElement.appendChild(createDpElt(dp));
        }

        return tourElement;
    }

    private Element createRouteElt(Segment seg){
        Element segElement = document.createElement("route");
        createAttribute(segElement, "destination", Long.toString(seg.getDestination().getId()));
        createAttribute(segElement, "length", Double.toString(seg.getLength()));
        createAttribute(segElement, "name", seg.getName());
        createAttribute(segElement, "origin", Long.toString(seg.getOrigin().getId()));
        return segElement;
    }

    private Element createDpElt(DeliveryPoint dp){
        Element dpElement = document.createElement("deliveryPoint");
        createAttribute(dpElement, "idIntersection", Long.toString(dp.getPlace().getId()));
        createAttribute(dpElement, "timeWindow", Integer.toString(dp.getTime().ordinal()));

        return dpElement;
    }

    private void createAttribute(Element root, String name, String value){
    	Attr attribut = document.createAttribute(name);
    	root.setAttributeNode(attribut);
    	attribut.setValue(value);
    }
   
	
}
