package h4131.view;

import h4131.model.DeliveryPoint;
import javafx.scene.control.Label;

public class DeliveryPointLabel extends Label{

    private DeliveryPoint deliveryPoint;
    private int courier;

    public DeliveryPointLabel(String text, DeliveryPoint aDeliveryPoint, int aCourier){
        super(text);
        this.deliveryPoint = aDeliveryPoint;
        this.courier = aCourier;
    }

    public void setDeliveryPoint(DeliveryPoint aDeliveryPoint){
        this.deliveryPoint = aDeliveryPoint;
    }

    public DeliveryPoint getDeliveryPoint(){
        return this.deliveryPoint;
    }

    public void setCourier(int aCourier){
        this.courier = aCourier;
    }

    public int getCourier(){
        return this.courier;
    }
}
