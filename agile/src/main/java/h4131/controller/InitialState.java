package h4131.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import h4131.calculus.Graph;
import h4131.model.GlobalTour;
import h4131.model.Map;
import h4131.model.Tour;
import h4131.model.CurrentDeliveryPoint;
import h4131.model.DeliveryPoint;
import h4131.view.WindowBuilder;
import h4131.xml.ExceptionXML;
import h4131.xml.PDFgenerator;
import h4131.xml.XMLdeserializer;
import h4131.xml.XMLserializer;




public class InitialState implements State{

    public InitialState(){}
    
    @Override
    public void loadGlobalTour(Controller c, WindowBuilder w){
        
        
        Collection<Tour> course = new ArrayList<>();
        GlobalTour loadedGlobalTour = new GlobalTour(course);
        System.out.println(c.getNumberOfCourier());
        CurrentDeliveryPoint loadedCurrentDeliveryPoint = new CurrentDeliveryPoint(c.getNumberOfCourier());
        try {
            w.setFullScreen(false);
            Element file = XMLdeserializer.loadGlobalTourFirst(loadedGlobalTour);
            boolean maploaded = true;
            if(!loadedGlobalTour.getMap().equals(c.getNameOfMap())){
                maploaded = loadMap(c, w, loadedGlobalTour.getMap());
            }
            if(maploaded){
                XMLdeserializer.buildRestFromDOMXMLGT(file, loadedGlobalTour, c.getMap(), loadedCurrentDeliveryPoint);
                c.setGlobalTour(loadedGlobalTour);
                loadedCurrentDeliveryPoint.addObserver(w);
                c.setCurrentDeliveryPoint(loadedCurrentDeliveryPoint);
                c.getCurrentDeliveryPoint().update();
                int currentNumberOfCourier = c.getNumberOfCourier();
                int loadedNumberOfCourier = c.getCurrentDeliveryPoint().getAffectedDeliveryPoints().size();
                if(loadedNumberOfCourier>currentNumberOfCourier){
                    if(w.NumberCourierChoice(currentNumberOfCourier, loadedNumberOfCourier))
                        setNumberOfCourier(c, w, loadedNumberOfCourier);
                    else{
                        setNumberOfCourier(c, w, currentNumberOfCourier);
                        computeGlobalTour(c, w);
                    }
                }
                
                
                
                w.drawGlobalTour(c.getGlobalTour());
                w.refreshNumberOfCourier();
            }
            

            w.setFullScreen(true);
            

            
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            if (!e.getMessage().equals("Problem when opening file")) {
                w.alert(e.getMessage());
            }
            w.setFullScreen(true);
            
        } 
    }

    @Override
    public boolean loadMap(Controller c, WindowBuilder w, String fileName){
        Map newMap = new Map();
        try {
            XMLdeserializer.loadMap(fileName, newMap);
            c.setNameOfMap(fileName);
            c.getCurrentDeliveryPoint().empty(c.getNumberOfCourier());
            w.drawMap(newMap);
            w.alertMapChange(fileName.substring(0, fileName.length() - "Map.xml".length()));
            c.setMap(newMap);
            return true;
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            w.alert(e.getMessage());
            return false;
        } 
    }

    @Override
    public void setNumberOfCourier(Controller c, WindowBuilder w, int numberOfCourier) {
        c.setNumberOfCourier(numberOfCourier);
        int size = c.getCurrentDeliveryPoint().getAffectedDeliveryPoints().size();
        if (size > numberOfCourier) {
            for (int i = size - 1; i >= numberOfCourier; i--) {
                c.getCurrentDeliveryPoint().addAllNonAffectedDeliveryPoints(
                        c.getCurrentDeliveryPoint().getAffectedDeliveryPoints().get(i));
                c.getCurrentDeliveryPoint().removeLastCourier();
            }
        } else {
            for (int i = size; i < numberOfCourier; i++) {
                c.getCurrentDeliveryPoint().addNewCourier();
            }
        }
    }
    @Override
    public void leftClick(Controller c, WindowBuilder windowBuilder, Long intersectionId) {
        Map map = c.getMap();
        c.addDeliveryPointState.setCurrentIntersection(map.getIntersectionById(intersectionId));
        c.setCurrentState(c.addDeliveryPointState);
        windowBuilder.openMenuIntersection(c.getNumberOfCourier(), c.addDeliveryPointState.getCurrentIntersection());
    }

    @Override
    public void modifyDeliveryPoint(Controller c, WindowBuilder windowBuilder, DeliveryPoint deliveryPoint,
            int courier) {
        c.modifyDeliveryPointState.setCurrentDeliveryPoint(deliveryPoint);
        c.modifyDeliveryPointState.setCourier(courier);
        windowBuilder.openMenuModifyDeliveryPoint(c.getNumberOfCourier(), deliveryPoint, courier);
        c.setCurrentState(c.modifyDeliveryPointState);
    }

    @Override
    public void computeGlobalTour(Controller c, WindowBuilder windowBuilder) {
        try {
            c.setGlobalTour(new GlobalTour());
            c.setNameOfMap(c.getNameOfMap());
            c.getGlobalTour().setMap(c.getNameOfMap());
            int courier = 0;
            c.clearAllGraphs();
            for(LinkedList<DeliveryPoint> listDeliveryPoints : c.getCurrentDeliveryPoint().getAffectedDeliveryPoints()){
                courier ++;
                if(!listDeliveryPoints.isEmpty()){
                
                    Graph graph = c.getMap().getGraphFromPoints(listDeliveryPoints);
                    graph.computeBestTour(c.getGlobalTour(),courier);
                    c.addGraph(graph);
                    if(graph.getTimeExceeded()==true){
                        windowBuilder.alert("Time exceeded for the tour n° "+ courier+". Calculation was not possible and tour is not displayed.");
                        c.getGlobalTour().addTour(new Tour());
       
                    }
                    else if (graph.getDeliveryErreur() != null) {
                        windowBuilder.alert("Calculation impossible on tour n°" + courier + " and on the time window : "
                                + graph.getDeliveryErreur().getTime().getRepresentation());
                    }
                    
                } else {
                    c.getGlobalTour().addTour(new Tour());
                }
            }
        } catch (NullPointerException e) {
            windowBuilder.alert("No path found. Check that every intersections are accessibles in both ways.");
        }finally{
            windowBuilder.drawGlobalTour(c.getGlobalTour());
        }
        
    }

    @Override
    public void saveGlobalTour(Controller c, WindowBuilder w){
        try {
            w.setFullScreen(false);
            XMLserializer serializer = XMLserializer.getInstance();
            String resulting_path = serializer.save(c.getGlobalTour());
            Path path = Paths.get(resulting_path);
            String file_name = path.getFileName().toString().substring(0, path.getFileName().toString().indexOf('.'));

            List<Graph> graphs = c.getGraphs();
            long tourId = 1;
            for (Graph g : graphs) {
                PDFgenerator.getPdfInstructions(g, tourId, resulting_path + "\\..\\" + file_name + "_instructions_" + tourId + ".pdf");
                tourId ++;
            }
            
        } catch (ParserConfigurationException | ExceptionXML | TransformerFactoryConfigurationError | TransformerException e) {
            w.alert(e.getMessage());
        }catch (NullPointerException e){
        }finally {
            w.setFullScreen(true);
        }
    }

}
