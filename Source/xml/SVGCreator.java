package xml;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import model.Intersection;
import model.Map;
import model.Segment;

public class SVGCreator {
    
    public static void createSvg(){

        Map map = new Map(null);
        
        try {
            XMLdeserializer.load(map);
            double longMax = 0;
            double longMin = 1000;
            double latMax = 0;
            double latMin = 1000;

            Element svgRootElement = createSvgRootElement();

            for (Entry<Long, Intersection> entry : map.getIntersections().entrySet()) {
                Intersection intersection = entry.getValue();
                double latitude = intersection.getLatitude();
                double longitude = intersection.getLongitude();
                if(longitude > longMax ){
                    longMax = longitude;
                }
                if(longitude < longMin ){
                    longMin = longitude;
                }
                if(latitude > latMax ){
                    latMax = latitude;
                }
                if(latitude < latMin ){
                    latMin = latitude;
                }
            }
            for (Entry<Long, List<Segment>> entry : map.getAdjacency().entrySet()) {
                Long key = entry.getKey();
                Intersection origine = map.getIntersectionById(key);
                List<Segment> segments = entry.getValue();
                for(int i = 0; i<segments.size(); i++){
                    drawSegment(svgRootElement, segments.get(i), map, latMin, latMax, longMin, longMax, origine);
                }
            }
            writeToSvgFile(svgRootElement, "output.svg"); // Remplacez "output.svg" par le nom de fichier de sortie souhaité

            svgRootElement.appendChild(svgRootElement.getOwnerDocument().createElement("desc"));

            saveSvgToFile(svgRootElement, "output.svg");

        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static Element createSvgRootElement() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            return doc.createElement("svg");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void drawSegment(Element svgRootElement, Segment segment, Map map, double latMin, double latMax, double longMin, double longMax, Intersection origine) {
        
        double originX = ((origine.getLongitude() - longMin) / (longMax - longMin)) * 800.0;
        double destX = ((segment.getDestination().getLongitude() - longMin) / (longMax - longMin)) * 800.0;
        double originY = 800.0 - ((origine.getLatitude() - latMin) / (latMax - latMin)) * 800.0;
        double destY = 800.0 - ((segment.getDestination().getLatitude() - latMin) / (latMax - latMin)) * 800.0;

        // Ajouter un trait au fichier SVG (vous devez remplacer ces valeurs par les coordonnées calculées)
        Element line = svgRootElement.getOwnerDocument().createElement("line");
        line.setAttribute("x1", Double.toString(originX)); // Remplacez par la coordonnée x de l'intersection d'origine
        line.setAttribute("y1", Double.toString(originY)); // Remplacez par la coordonnée y de l'intersection d'origine
        line.setAttribute("x2", Double.toString(destX)); // Remplacez par la coordonnée x de l'intersection de destination
        line.setAttribute("y2", Double.toString(destY)); // Remplacez par la coordonnée y de l'intersection de destination
        line.setAttribute("stroke", "black"); // Couleur noire
        svgRootElement.appendChild(line);

        Element dot  = svgRootElement.getOwnerDocument().createElement("circle");
        dot.setAttribute("cx", Double.toString(originX));
        dot.setAttribute("cy", Double.toString(originY));
        dot.setAttribute("r", "2");
        dot.setAttribute("style", "fill:transparent");
        svgRootElement.appendChild(dot);


    }

    private static void writeToSvgFile(Element svgRootElement, String fileName) {
        try {
            // Transformer le document en fichier SVG
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(svgRootElement.getOwnerDocument());
            StreamResult result = new StreamResult(new FileWriter(fileName));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ajoutez cette fonction à votre classe
    private static void saveSvgToFile(Element svgRootElement, String fileName) {
        try {
            // Créer une source DOM pour la transformation
            DOMSource source = new DOMSource(svgRootElement);
    
            // Créer un résultat pour l'écriture du fichier SVG
            StreamResult result = new StreamResult(new FileWriter(fileName));
    
            // Transformer le document en fichier SVG
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(source, result);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
