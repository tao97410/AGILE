package h4131.controller;


import java.util.LinkedList;

import h4131.model.Intersection;
import h4131.model.Map;
import h4131.model.CurrentDeliveryPoint;
import h4131.model.DeliveryPoint;
import h4131.model.TimeWindow;
import h4131.view.WindowBuilder;

public class AddDeliveryPointState implements State {

    private Intersection currentIntersection;
    
    @Override
    public void openMenuIntersection(Controller c, WindowBuilder w, Long intersectionId){
        Map map = c.getMap();
        currentIntersection = map.getIntersectionById(intersectionId);
        w.openMenuIntersection(map, c.getNumberOfCourier(), intersectionId);
    }

    @Override
    public void addDeliveryPoint(Controller c, WindowBuilder w, TimeWindow tw, int courier){
        System.out.println(courier);
        CurrentDeliveryPoint deliveryPoints = c.getCurrentDeliveryPoint();
        deliveryPoints.addAffectedDeliveryPoint(courier-1, new DeliveryPoint(currentIntersection, tw));
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
