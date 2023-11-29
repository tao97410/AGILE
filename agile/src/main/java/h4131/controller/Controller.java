package h4131.controller;

import h4131.view.WindowBuilder;
import javafx.stage.Stage;

public class Controller {
    
    private WindowBuilder windowBuilder;

    private State currentState;

    // Instances associated with each possible state of the controller
	protected final InitialState initialState = new InitialState();
    
    /**
	 * Create the controller of the application
	 * @param primaryStage the stage created by the App Class when launching the application
	 */
    public Controller(Stage primaryStage){
        this.windowBuilder = new WindowBuilder(this, primaryStage);
        currentState = initialState;
    }

    /**
	 * Change the current state of the controller
	 * @param state the new current state
	 */
	protected void setCurrentState(State state){
		currentState = state;
	}

	// Methods corresponding to user events 
	/**
	 * Method called after click on button "Load a global Tour"
	 * @param screenHeight
	 */
	public void loadGlobalTour(double screenHeight) {
		currentState.loadGlobalTour(this, windowBuilder, screenHeight);
	}

    /**
	 * Method called after click on button "Create a tour"
	 */
	public void createTour() {
		currentState.createTour(this, windowBuilder);
	}

    /**
	 * Method called after click on button "Manage your couriers"
	 */
	public void manageCouriers() {
		currentState.manageCouriers(this, windowBuilder);
	}  







}
