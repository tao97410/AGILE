package h4131.controller;

import h4131.model.DeliveryPoint;
import h4131.model.TimeWindow;
import h4131.view.WindowBuilder;

public interface State {
    /**
	 * Method called by the controller after a click on the button "load a tour"
	 * @param c the controller
	 * @param w the window
	 */
	public default void loadGlobalTour(Controller c, WindowBuilder w){};
	
	/**
	 * Method called by the controller after a click on the button "Load chosen map" or by the loadGlobalTour
	 * @param c the controller
	 * @param w the window
	 * @param fileName which is the name of the xml map file
	 * @return boolean whether an exception has been thrown or not
	 */
	public default boolean loadMap(Controller c, WindowBuilder w, String fileName){return true;};

	/**
	 * Method called by controller after click on "OK" button when adding delivery
	 * @param c the controller
	 * @param w the windowBuider
	 * @param tw the delivery time window
	 * @param courier the affected courier
	 */
	public default void addDeliveryPoint(Controller c, WindowBuilder w, TimeWindow tw, int courier){};

    /**
	 * Method called by controller after click on "Cancel" button when adding delivery
	 * @param c the controller
	 * @param w the windowBuilder
	 */
	public default void cancelDeliveryPoint(Controller c, WindowBuilder w){};

	/**
	 * Method called by controller to set the number of delivery point
	 * @param c the controller
	 * @param w the windowBuilder
	 * @param numberOfCourier the new number of couriers
	 */
	public default void setNumberOfCourier(Controller c, WindowBuilder w,int numberOfCourier){};

	/**
	 * Method called by controller after click on a delivery point in the list to modify it
	 * @param c the controller
	 * @param windowBuilder
	 * @param deliveryPoint
	 * @param courier
	 */
	public default void modifyDeliveryPoint(Controller c, WindowBuilder windowBuilder, DeliveryPoint deliveryPoint, int courier){};

	/**
	 * Method called by the controller after a click on an intersection
	 * @param c the controller
	 * @param windowBuilder the window
	 * @param intersectionId which is the id of the clicked intersection
	 */
	public default void leftClick(Controller c, WindowBuilder windowBuilder, Long intersectionId){}

	/**
	 * Method called by controller to delete a delivery point
	 * @param controller
	 * @param windowBuilder
	 */
	public default void deleteDeliveryPoint(Controller controller, WindowBuilder windowBuilder){};

    /**
	 * Method called by controller to change the timeWindow or courier of a delivery
	 * @param controller
	 * @param windowBuilder
	 * @param time the new timeWindow
	 * @param courier the new courier
	 */
	public default void changeInfosDeliveryPoint(Controller controller, WindowBuilder windowBuilder, TimeWindow time, int courier){}

	/**
	 * Method called by controller to compute the tour
	 * @param controller
	 * @param windowBuilder
	 */
	public default void computeGlobalTour(Controller controller, WindowBuilder windowBuilder){}

	/**
	 * Method called by controller to save the tour
	 * @param controller
	 * @param windowBuilder
	 */
    public default void saveGlobalTour(Controller controller, WindowBuilder windowBuilder){}

}

