package h4131.controller;


import h4131.model.Map;
import h4131.model.TimeWindow;
import h4131.view.IntersectionCircle;
import h4131.view.WindowBuilder;

public class AddDeliveryPointState implements State {
    
    @Override
    public void openMenuIntersection(Controller c, WindowBuilder w, IntersectionCircle intersectionId){
        Map map = c.getMap();
        w.openMenuIntersection(map, c.getNumberOfCourier(), intersectionId);
    }

    @Override
    public void addDeliveryPoint(Controller c, WindowBuilder w, Long idIntersection, TimeWindow tw, int courier){
        System.out.println(idIntersection);
        System.out.println(tw);
        System.out.println(courier);
        w.disableBackground(false);
        c.setCurrentState(c.initialState);
    }

    @Override
    public void cancelDeliveryPoint(Controller c, WindowBuilder w, Long idIntersection){
        w.unSetIntersection(idIntersection);
        w.disableBackground(false);
        c.setCurrentState(c.initialState);
    }
}
