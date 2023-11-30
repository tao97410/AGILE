package h4131.controller;

import h4131.view.WindowBuilder;

public interface State {
    /**
	 * Method called by the controller after a click on the button "load a tour"
	 * @param c the controller
	 * @param w the window
	 * @param screenHeight
	 */
	public default void loadGlobalTour(Controller c, WindowBuilder w, double screenHeight){};
	
	/**
	 * Method called by the controller after a click on the button "Create tour"
	 * @param c the controller
	 * @param w the window
	 */
	public default void createTour(Controller c, WindowBuilder w, double screenHeight){};
	
	/**
	 * Method called by the controller after a click on the button "Manage your couriers"
	 * @param c the controller
	 * @param w the window
	 */
	public default void manageCouriers(Controller c, WindowBuilder w){};
}
