package h4131.controller;

import h4131.model.DeliveryPoint;
import h4131.model.TimeWindow;
import h4131.view.WindowBuilder;

public class ModifyDeliveryPointState implements State{

    private DeliveryPoint currentDeliveryPoint;
    private int courier;
    
    public void setCurrentDeliveryPoint(DeliveryPoint anDeliveryPoint){
        this.currentDeliveryPoint = anDeliveryPoint;
    }

    public DeliveryPoint getCurrentDeliveryPoint(){
        return this.currentDeliveryPoint;
    }

    public void setCourier(int aCourier){
        this.courier = aCourier;
    }

    public int getCourier(){
        return this.courier;
    }

    public void deleteDeliveryPoint(Controller controller, WindowBuilder windowBuilder){
        if(this.courier == 0){
            controller.getCurrentDeliveryPoint().removeNonAssignedDeliveryPoint(this.currentDeliveryPoint);
        }
        else{
            controller.getCurrentDeliveryPoint().removeAssignedDeliveryPoint(this.currentDeliveryPoint, courier);
        }
        windowBuilder.disableBackground(false);
        controller.setCurrentState(controller.initialState);
    }

    public void changeInfosDeliveryPoint(Controller controller, WindowBuilder windowBuilder, TimeWindow newTime, int newCourier){
        if(this.courier == 0){
            controller.getCurrentDeliveryPoint().removeNonAssignedDeliveryPoint(this.currentDeliveryPoint);
        }
        else{
            controller.getCurrentDeliveryPoint().removeAssignedDeliveryPoint(this.currentDeliveryPoint, this.courier);
        }
        currentDeliveryPoint.setTime(newTime);
        controller.getCurrentDeliveryPoint().addAffectedDeliveryPoint(newCourier-1, currentDeliveryPoint);
        windowBuilder.disableBackground(false);
        controller.setCurrentState(controller.initialState);
    }
    
}
