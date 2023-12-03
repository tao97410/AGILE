package h4131.controller;

import h4131.model.TimeWindow;
import h4131.view.IntersectionCircle;
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

	/**
	 * Method called by the controller after a click on an intersection
	 * @param c the controller
	 * @param w the window
	 * @param intersectionId which is the id of the clicked intersection
	 */
	public default void openMenuIntersection(Controller c, WindowBuilder w, IntersectionCircle intersectionId){};

	public default void addDeliveryPoint(Controller c, WindowBuilder w, Long idIntersection, TimeWindow tw, int courier){};

    public default void cancelDeliveryPoint(Controller c, WindowBuilder w, Long idIntersection){};

}

