package h4131.controller;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import h4131.model.Map;
import h4131.view.WindowBuilder;
import h4131.xml.ExceptionXML;
import h4131.xml.XMLdeserializer;

public class InitialState implements State{
    
    @Override
    public void loadGlobalTour(Controller c, WindowBuilder w, double screenHeight){
        
        Map loadedMap = new Map(null);
        try {
            XMLdeserializer.load(loadedMap);
            w.drawMap(loadedMap);
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
