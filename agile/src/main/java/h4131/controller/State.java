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
	 * Method called by the controller after a click on the button "Manage your couriers"
	 * @param c the controller
	 * @param w the window
	 */
	public default void manageCouriers(Controller c, WindowBuilder w){};

	/**
	 * Method called by the controller after a click on the button "Load chosen map"
	 * @param c the controller
	 * @param w the window
	 * @param fileName which is the name of the xml map file
	 */
	public default void loadMap(Controller c, WindowBuilder w, String fileName){};

	public default void addDeliveryPoint(Controller c, WindowBuilder w, TimeWindow tw, int courier){};

    public default void cancelDeliveryPoint(Controller c, WindowBuilder w){};

	public default void setNumberOfCourier(Controller c, WindowBuilder w,int numberOfCourier){};

	public default void modifyDeliveryPoint(Controller c, WindowBuilder windowBuilder, DeliveryPoint deliveryPoint, int courier){};

	/**
	 * Method called by the controller after a click on an intersection
	 * @param c the controller
	 * @param windowBuilder the window
	 * @param intersectionId which is the id of the clicked intersection
	 */
	public default void leftClick(Controller c, WindowBuilder windowBuilder, Long intersectionId){}

	public default void deleteDeliveryPoint(Controller controller, WindowBuilder windowBuilder){};

    public default void changeInfosDeliveryPoint(Controller controller, WindowBuilder windowBuilder, TimeWindow time,
            int courier){};

}

