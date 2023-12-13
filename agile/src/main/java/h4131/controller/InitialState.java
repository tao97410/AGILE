package h4131.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.w3c.dom.Element;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import h4131.calculus.Graph;

import h4131.calculus.TemplateGraph;
import h4131.calculus.TSP;
import h4131.calculus.TSP1;
import h4131.calculus.Arc;
import h4131.model.GlobalTour;
import h4131.model.Map;
import h4131.model.Tour;
import h4131.model.CurrentDeliveryPoint;
import h4131.model.DeliveryPoint;
import h4131.model.Segment;
import h4131.view.WindowBuilder;
import h4131.xml.ExceptionXML;
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
            // if(loadedGlobalTour.getMap().equals(c.getNameOfMap())){
            //     c.setGlobalTour(loadedGlobalTour);
            //     c.setNumberOfCourier(c.getCurrentDeliveryPoint().getAffectedDeliveryPoints().size());
            //     w.setFullScreen(true);
            //     w.drawGlobalTour(loadedGlobalTour);
            // }
            // else{
            //     loadMap(c, w, loadedGlobalTour.getMap());
            //     c.setGlobalTour(loadedGlobalTour);
            //     c.setNumberOfCourier(c.getCurrentDeliveryPoint().getAffectedDeliveryPoints().size());
            //     w.setFullScreen(true);
            //     w.drawGlobalTour(loadedGlobalTour);
            // }
            if(!loadedGlobalTour.getMap().equals(c.getNameOfMap())){
                loadMap(c, w, loadedGlobalTour.getMap());
            }
            XMLdeserializer.buildRestFromDOMXMLGT(file, loadedGlobalTour, c.getMap(), loadedCurrentDeliveryPoint);
            c.setGlobalTour(loadedGlobalTour);
            loadedCurrentDeliveryPoint.addObserver(w);
            c.setCurrentDeliveryPoint(loadedCurrentDeliveryPoint);
            c.getCurrentDeliveryPoint().update();
            w.setFullScreen(true);
            w.drawGlobalTour(loadedGlobalTour);

            
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            if(!e.getMessage().equals("Problem when opening file")){
                w.alert(e.getMessage());
                e.printStackTrace();
            }
            w.setFullScreen(true);
            
        } 
    }

    @Override
    public void loadMap(Controller c, WindowBuilder w, String fileName){
        Map newMap = new Map();
        try {
            XMLdeserializer.loadMap(fileName, newMap);
            c.setNameOfMap(fileName);
            c.getCurrentDeliveryPoint().empty(c.getNumberOfCourier());
            w.drawMap(newMap);
            c.setMap(newMap);
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            w.alert(e.getMessage());
            e.printStackTrace();
        } 
    }

    @Override
    public void setNumberOfCourier(Controller c, WindowBuilder w, int numberOfCourier){
        c.setNumberOfCourier(numberOfCourier);
        int size = c.getCurrentDeliveryPoint().getAffectedDeliveryPoints().size();
        if(size>numberOfCourier){
            for(int i = size-1; i>=numberOfCourier; i--){
                c.getCurrentDeliveryPoint().addAllNonAffectedDeliveryPoints(c.getCurrentDeliveryPoint().getAffectedDeliveryPoints().get(i));
                c.getCurrentDeliveryPoint().removeLastCourier();
            }
        }else{
            for(int i = size; i<numberOfCourier; i++){
                c.getCurrentDeliveryPoint().addNewCourier();
            }
        }
    }
    @Override
    public void leftClick(Controller c, WindowBuilder windowBuilder, Long intersectionId){
        Map map = c.getMap();
        c.addDeliveryPointState.setCurrentIntersection(map.getIntersectionById(intersectionId));
        c.setCurrentState(c.addDeliveryPointState);
        windowBuilder.openMenuIntersection(c.getNumberOfCourier(), c.addDeliveryPointState.getCurrentIntersection());
    }

    @Override
    public void modifyDeliveryPoint(Controller c, WindowBuilder windowBuilder, DeliveryPoint deliveryPoint, int courier){
        c.modifyDeliveryPointState.setCurrentDeliveryPoint(deliveryPoint);
        c.modifyDeliveryPointState.setCourier(courier);
        windowBuilder.openMenuModifyDeliveryPoint(c.getNumberOfCourier(), deliveryPoint, courier);
        c.setCurrentState(c.modifyDeliveryPointState);
    }

    @Override
    public void computeGlobalTour(Controller c, WindowBuilder windowBuilder){
        c.setGlobalTour(new GlobalTour());
        c.getGlobalTour().setMap(c.getNameOfMap());
        int courier = 0;
        for(LinkedList<DeliveryPoint> listDeliveryPoints : c.getCurrentDeliveryPoint().getAffectedDeliveryPoints()){
            courier ++;
            if(!listDeliveryPoints.isEmpty()){
                Graph graph = c.getMap().getGraphFromPoints(listDeliveryPoints);
                graph.computeBestTour(c.getGlobalTour(),courier);
                   
                if(graph.getDeliveryErreur()!=null){
                windowBuilder.alert("error on this time window : " + graph.getDeliveryErreur().getTime() + "on tour n°" + courier);
            }
            }
            
        }
        windowBuilder.drawGlobalTour(c.getGlobalTour());
    }

    @Override
    public void saveGlobalTour(Controller c, WindowBuilder w){
        System.out.println("save tour");
        try {
            XMLserializer serializer = XMLserializer.getInstance();
            serializer.save(c.getGlobalTour());
        } catch (ParserConfigurationException | ExceptionXML | TransformerFactoryConfigurationError | TransformerException e) {
            w.alert(e.getMessage());
            e.printStackTrace();
        } 
    }

}
