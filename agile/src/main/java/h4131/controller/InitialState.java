package h4131.controller;

import h4131.view.WindowBuilder;
import h4131.xml.SVGCreator;

public class InitialState implements State{
    
    @Override
    public void loadGlobalTour(Controller c, WindowBuilder w, double screenHeight){
        SVGCreator.createSvg(screenHeight);
        w.printSVGMap();
    }
}
