package h4131.view;

import h4131.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Screen;

public class HomeSceneController {

    private Controller controller;

    @FXML
    private RadioButton bigMapButton;

    @FXML
    private Button createTourButton;

    @FXML
    private Button loadTourButton;

    @FXML
    private Button manageCourriersButton;

    @FXML
    private ToggleGroup mapChoice;

    @FXML
    private RadioButton mediumMapButton;

    @FXML
    private RadioButton smallMapButton;

    /**
	 * Method called by the windowBuilder to set the controller when creating homeScene
	 * @param c the global controller of the application
	 */
    public void setController(Controller c){
        this.controller = c;
    }

    @FXML
    void doCreateTour(ActionEvent event) {
        RadioButton selectedMapButton = (RadioButton) mapChoice.getSelectedToggle();
        System.out.println("create delivery tour with the " + selectedMapButton.getText());
    }
    
    @FXML
    void doLoadTour(ActionEvent event) {
        Screen screen = Screen.getPrimary();
        controller.loadGlobalTour(screen.getVisualBounds().getHeight());
    }

    @FXML
    void doManageCouriers(ActionEvent event) {
        System.out.println("manage couriers");
    }


}

