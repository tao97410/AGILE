package h4131.controller;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import h4131.model.GlobalTour;
import h4131.model.Map;
import h4131.model.CurrentDeliveryPoint;
import h4131.model.TimeWindow;
import h4131.view.WindowBuilder;
import h4131.xml.ExceptionXML;
import h4131.xml.XMLdeserializer;
import javafx.stage.Stage;

public class Controller {
    
    private WindowBuilder windowBuilder;
	private Map map;
	private CurrentDeliveryPoint currentDeliveryPoint;
	private GlobalTour globalTour;
	private int numberOfCourier;

    private State currentState;

    // Instances associated with each possible state of the controller
	protected final InitialState initialState = new InitialState();
	protected final AddDeliveryPointState addDeliveryPointState = new AddDeliveryPointState();

	public int getNumberOfCourier(){
		return this.numberOfCourier;
	}

	public void setNumberOfCourier(int numberOfCourier){
		this.numberOfCourier = numberOfCourier;
	}

	public void changeNumberOfCourier(int aNumber){
		currentState.setNumberOfCourier(this, windowBuilder, aNumber);
	}

	public Map getMap(){
		return this.map;
	}

	public void setMap(Map aMap){
		this.map = aMap;
	}

	public GlobalTour getGlobalTour(){
		return this.globalTour;
	}

	public void setGlobalTour(GlobalTour aGlobalTour){
		this.globalTour = aGlobalTour;
	}

	public CurrentDeliveryPoint getCurrentDeliveryPoint(){
		return this.currentDeliveryPoint;
	}

    /**
	 * Create the controller of the application
	 * @param primaryStage the stage created by the App Class when launching the application
	 */
    public Controller(Stage primaryStage){
		try {
			this.map = new Map(null);
			XMLdeserializer.loadMap("largeMap.xml", this.map);
		} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
			e.printStackTrace();
		}
        this.windowBuilder = new WindowBuilder(this, primaryStage, this.map);
        currentState = initialState;
		numberOfCourier = 3;
		currentDeliveryPoint = new CurrentDeliveryPoint();
		currentDeliveryPoint.addObserver(windowBuilder);
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
	 * 
	 */
	public void loadGlobalTour() {
		currentState.loadGlobalTour(this, windowBuilder);
	}

    /**
	 * Method called after click on button "Manage your couriers"
	 */
	public void manageCouriers() {
		currentState.manageCouriers(this, windowBuilder);
	} 

	/**
	 * Method called after click on button "Load chosen map"
	 */
	public void loadMap(String fileName) {
		currentState.loadMap(this, windowBuilder, fileName);
	} 

	public void leftClick(Long intersectionId){
		setCurrentState(addDeliveryPointState);
		currentState.openMenuIntersection(this, windowBuilder, intersectionId);
	}

	public void addDeliveryPoint(TimeWindow tw, int courier){
		currentState.addDeliveryPoint(this, windowBuilder, tw, courier);
	}

	public void cancelDeliveryPoint(Long idIntersection){
		currentState.cancelDeliveryPoint(this, windowBuilder, idIntersection);
	}

}
