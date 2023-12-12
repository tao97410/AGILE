package h4131.model;

import java.util.LinkedList;

import h4131.observer.Observable;

public class CurrentDeliveryPoint extends Observable{
    
	private LinkedList<LinkedList<DeliveryPoint>> affectedDeliveryPoints;
	private LinkedList<DeliveryPoint> nonAffectedDeliveryPoints;

    public CurrentDeliveryPoint(int nbofCourier){
        
		this.affectedDeliveryPoints = new LinkedList<>();
		for(int i = 0; i<nbofCourier; i++){
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

    /**
     * Add an affected delivery point to one of the lists
     * @param courier the courier to who you want to add a point
     * @param deliveryPoint to add
     */
    public void addAffectedDeliveryPoint(int courier, DeliveryPoint deliveryPoint){
        this.affectedDeliveryPoints.get(courier - 1).add(deliveryPoint);
        notifyObservers();
    }

      /**
     * Add an delivery point to one of the lists - affected or non affected
     * @param courier the courier to who you want to add a point
     * @param deliveryPoint to add
     */
    public void addDeliveryPointToAssociatedList(int courier,DeliveryPoint deliveryPoint){
        if(courier<=this.getAffectedDeliveryPoints().size())
            this.affectedDeliveryPoints.get(courier - 1).add(deliveryPoint);
        else
            this.nonAffectedDeliveryPoints.add(deliveryPoint);
        notifyObservers();
    }

    /**
     * Add a delivery point to the list of non affected points
     * @param deliveryPoint
     */
    public void addNonAffectedDeliveryPoint(DeliveryPoint deliveryPoint){
        this.nonAffectedDeliveryPoints.add(deliveryPoint);
        notifyObservers();
    }

    /**
     * Add all the points of the list in parameter to the list of non
     * affected points
     * @param deliveryPoints the list of points to add
     */
    public void addAllNonAffectedDeliveryPoints(LinkedList<DeliveryPoint> deliveryPoints){
        this.nonAffectedDeliveryPoints.addAll(deliveryPoints);
        notifyObservers();
    }

    /**
     * Removes the list of delivery points of the last courier
     */
    public void removeLastCourier(){
        this.affectedDeliveryPoints.removeLast();
        notifyObservers();
    }

    /**
     * Append a new list of points to the affectedDeliveryPoints list
     */
    public void addNewCourier(){
        this.affectedDeliveryPoints.add(new LinkedList<DeliveryPoint>());
        notifyObservers();
    }

    /**
     * Removes a delivery point from the non affected delivery points list
     * @param deliveryPointToRemove
     */
    public void removeNonAssignedDeliveryPoint(DeliveryPoint deliveryPointToRemove) {
        this.nonAffectedDeliveryPoints.remove(deliveryPointToRemove);
        notifyObservers();
    }

    /**
     * Removes the delivery point of a courier list of delivery point
     * @param deliveryPointToRemove
     * @param courier the courier number of the delivery point you want to remove
     */
    public void removeAssignedDeliveryPoint(DeliveryPoint deliveryPointToRemove, int courier) {
        this.affectedDeliveryPoints.get(courier-1).remove(deliveryPointToRemove);
        notifyObservers();
    }

    public void update(){
        notifyObservers();
    }

    public void empty(int newNumberOfCourier){
		this.affectedDeliveryPoints = new LinkedList<>();
		for(int i = 0; i<newNumberOfCourier; i++){
			this.affectedDeliveryPoints.add(new LinkedList<DeliveryPoint>());
		}
		this.nonAffectedDeliveryPoints = new LinkedList<>();
    }

}
