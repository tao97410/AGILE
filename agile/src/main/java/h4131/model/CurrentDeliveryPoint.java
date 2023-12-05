package h4131.model;

import java.util.LinkedList;

import h4131.observer.Observable;

public class CurrentDeliveryPoint extends Observable{
    
	private LinkedList<LinkedList<DeliveryPoint>> affectedDeliveryPoints;
	private LinkedList<DeliveryPoint> nonAffectedDeliveryPoints;

    public CurrentDeliveryPoint(){
        
		this.affectedDeliveryPoints = new LinkedList<>();
		for(int i = 0; i<3; i++){
			this.affectedDeliveryPoints.add(new LinkedList<DeliveryPoint>());
		}
		this.nonAffectedDeliveryPoints = new LinkedList<>();
    }


    /**
     * @return LinkedList<LinkedList<DeliveryPoint>> return the affectedDeliveryPoints
     */
    public LinkedList<LinkedList<DeliveryPoint>> getAffectedDeliveryPoints() {
        return affectedDeliveryPoints;
    }

    /**
     * @param affectedDeliveryPoints the affectedDeliveryPoints to set
     */
    public void setAffectedDeliveryPoints(LinkedList<LinkedList<DeliveryPoint>> affectedDeliveryPoints) {
        this.affectedDeliveryPoints = affectedDeliveryPoints;
    }

    /**
     * @return LinkedList<DeliveryPoint> return the nonAffectedDeliveryPoints
     */
    public LinkedList<DeliveryPoint> getNonAffectedDeliveryPoints() {
        return nonAffectedDeliveryPoints;
    }

    /**
     * @param nonAffectedDeliveryPoints the nonAffectedDeliveryPoints to set
     */
    public void setNonAffectedDeliveryPoints(LinkedList<DeliveryPoint> nonAffectedDeliveryPoints) {
        this.nonAffectedDeliveryPoints = nonAffectedDeliveryPoints;
    }

    public void addAffectedDeliveryPoint(int index, DeliveryPoint deliveryPoint){
        this.affectedDeliveryPoints.get(index).add(deliveryPoint);
        notifyObservers();
    }

    public void addNonAffectedDeliveryPoint(DeliveryPoint deliveryPoint){
        this.nonAffectedDeliveryPoints.add(deliveryPoint);
        notifyObservers();
    }

    public void addAllNonAffectedDeliveryPoints(LinkedList<DeliveryPoint> deliveryPoints){
        this.nonAffectedDeliveryPoints.addAll(deliveryPoints);
        notifyObservers();
    }

    public void removeLastCourier(){
        this.affectedDeliveryPoints.removeLast();
        notifyObservers();
    }

}
