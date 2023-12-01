package h4131.view;

import h4131.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
public class HomeSceneController {

    private Controller controller;

    @FXML
    private Button createTourButton;

    @FXML
    private Button loadTourButton;

    // @FXML
    // private Button manageCourriersButton;

    /**
	 * Method called by the windowBuilder to set the controller when creating homeScene
	 * @param c the global controller of the application
	 */
    public void setController(Controller c){
        this.controller = c;
    }

    @FXML
    void doCreateTour(ActionEvent event) {
        controller.createTour();
    }
    
    @FXML
    void doLoadTour(ActionEvent event) {
        controller.loadGlobalTour();
    }

    @FXML
    void doManageCouriers(ActionEvent event) {
        System.out.println("manage couriers");
    }


}

