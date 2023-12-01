package h4131.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
    public void loadGlobalTour(Controller c, WindowBuilder w){
        
        Map map = c.getMap();
        Collection<Tour> course = new ArrayList<>();
        GlobalTour loadedGlobalTour = new GlobalTour(course);
        try {
            w.setFullScreen(false);
            XMLdeserializer.loadGlobalTour(loadedGlobalTour, map);
            w.setFullScreen(true);
            w.drawGlobalTour(loadedGlobalTour);
            //controller.setState(...);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            w.alert(e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExceptionXML e) {
            w.alert(e.getMessage());
        } 
    }

    @Override
    public void loadMap(Controller c, WindowBuilder w, String fileName){
        Map newMap = new Map(null);
        try {
            XMLdeserializer.loadMap(fileName, newMap);
            w.drawMap(newMap);
            c.setMap(newMap);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            w.alert(e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExceptionXML e) {
            //Do nothing
        } 
    }
}
