package h4131.controller;

import h4131.model.Intersection;
import h4131.model.CurrentDeliveryPoint;
import h4131.model.DeliveryPoint;
import h4131.model.TimeWindow;
import h4131.view.WindowBuilder;

public class AddDeliveryPointState implements State {

    private Intersection currentIntersection;
    
    public void setCurrentIntersection(Intersection anIntersection){
        this.currentIntersection = anIntersection;
    }

    public Intersection getCurrentIntersection(){
        return this.currentIntersection;
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
    public void cancelDeliveryPoint(Controller c, WindowBuilder w){
        w.unSetIntersection(currentIntersection.getId());
        w.disableBackground(false);
        c.setCurrentState(c.initialState);
    }
}
