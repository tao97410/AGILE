package h4131.view;

import h4131.model.DeliveryPoint;
import javafx.scene.control.Label;

public class DeliveryPointLabel extends Label{

    private DeliveryPoint deliveryPoint;

    public DeliveryPointLabel(String text, DeliveryPoint aDeliveryPoint){
        super(text);
        this.deliveryPoint = aDeliveryPoint;
    }

    public void setDeliveryPoint(DeliveryPoint aDeliveryPoint){
        this.deliveryPoint = aDeliveryPoint;
    }

    public DeliveryPoint getDeliveryPoint(){
        return this.deliveryPoint;
    }
}
