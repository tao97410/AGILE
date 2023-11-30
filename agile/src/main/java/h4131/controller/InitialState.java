package h4131.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import h4131.model.GlobalTour;
import h4131.model.Map;
import h4131.model.Tour;
import h4131.view.WindowBuilder;
import h4131.xml.ExceptionXML;
import h4131.xml.XMLdeserializer;

public class InitialState implements State{
    
    @Override
    public void loadGlobalTour(Controller c, WindowBuilder w, double screenHeight){
        
        Map loadedMap = new Map(null);
        List<Tour> course = new ArrayList<>();
        GlobalTour loadedGlobalTour = new GlobalTour(course);
        try {
            XMLdeserializer.loadMap(loadedMap);
            XMLdeserializer.loadGlobalTour(loadedGlobalTour, loadedMap);
            w.drawMapAndGlobalTour(loadedMap, loadedGlobalTour);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExceptionXML e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }

    @Override
    public void createTour(Controller c, WindowBuilder w, double screenHeight){
        Map loadedMap = new Map(null);
        try {
            XMLdeserializer.loadMap(loadedMap);
            w.drawMapAndGlobalTour(loadedMap, null);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExceptionXML e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}
