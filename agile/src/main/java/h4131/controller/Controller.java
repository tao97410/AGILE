package h4131.controller;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import h4131.model.GlobalTour;
import h4131.model.Map;
import h4131.model.CurrentDeliveryPoint;
import h4131.model.DeliveryPoint;
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
	protected final ModifyDeliveryPointState modifyDeliveryPointState = new ModifyDeliveryPointState();

    /**
	 * Create the controller of the application
	 * @param primaryStage the stage created by the App Class when launching the application
	 */
    public Controller(Stage primaryStage){
		try {
			this.map = new Map();
			XMLdeserializer.loadMap("largeMap.xml", this.map);
		} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
			e.printStackTrace();
		}
        this.windowBuilder = new WindowBuilder(this, primaryStage, this.map);
        currentState = initialState;
		numberOfCourier = 3;
		globalTour = new GlobalTour();
		currentDeliveryPoint = new CurrentDeliveryPoint(numberOfCourier);
		currentDeliveryPoint.addObserver(windowBuilder);
    }

	public Controller(){}

    /**
	 * Change the current state of the controller
	 * @param state the new current state
	 */
	protected void setCurrentState(State state){
		currentState = state;
	}

	public int getNumberOfCourier(){
		return this.numberOfCourier;
	}

	public void setNumberOfCourier(int numberOfCourier){
		this.numberOfCourier = numberOfCourier;
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

	public void setCurrentDeliveryPoint(CurrentDeliveryPoint currentDeliveryPoint){
		this.currentDeliveryPoint = currentDeliveryPoint;
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
	 * Method called after modifying the number of courier field
	 * @param aNumber
	 */
	public void changeNumberOfCourier(int aNumber){
		currentState.setNumberOfCourier(this, windowBuilder, aNumber);
	}

	/**
	 * Method called after click on button "Load chosen map"
	 * @param fileName the name of the map file to open
	 */
	public void loadMap(String fileName) {
		currentState.loadMap(this, windowBuilder, fileName);
	} 

	/**
	 * Method called after left mouse click on an intersection
	 * @param intersectionId
	 */
	public void leftClick(Long intersectionId){
		currentState.leftClick(this, windowBuilder, intersectionId);
	}

	/**
	 * Method called after validation of a delivery point 
	 * @param tw the delivery time window
	 * @param courier the affected courier
	 */
	public void addDeliveryPoint(TimeWindow tw, int courier){
		currentState.addDeliveryPoint(this, windowBuilder, tw, courier);
	}

	/**
	 * Method called after cancellation of a delivery point
	 */
	public void cancelDeliveryPoint(){
		currentState.cancelDeliveryPoint(this, windowBuilder);
	}

	/**
	 * Method called after a click on the delivery point list to modify it
	 * @param deliveryPoint
	 * @param courier the current courier (before modification of the delivery)
	 */
    public void modifyDeliveryPoint(DeliveryPoint deliveryPoint, int courier) {
		currentState.modifyDeliveryPoint(this, windowBuilder, deliveryPoint, courier);
    }

	/**
	 * Method called after a point is deleted
	 */
	public void deleteDeliveryPoint(){
		currentState.deleteDeliveryPoint(this, windowBuilder);
	}

	/**
	 * Method called after a click on "modify delivery point" button
	 * @param time the new delivery time window
	 * @param courier the new affected courier
	 */
	public void changeInfosDeliveryPoint(TimeWindow time, int courier){
		currentState.changeInfosDeliveryPoint(this, windowBuilder, time, courier);
	}

	/**
	 * Method called after a click on "compute gloabl tour" button
	 */
    public void computeGlobalTour() {
		currentState.computeGlobalTour(this, windowBuilder);
    }

	/**
	 * Method called after a click on "save gloabl tour" button
	 */
	public void saveGlobalTour() {
		currentState.saveGlobalTour(this, windowBuilder);
	}


}
