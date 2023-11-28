package h4131.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class HomeSceneController {

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

    @FXML
    void doCreateDeliveryTour(ActionEvent event) {
        RadioButton selectedMapButton = (RadioButton) mapChoice.getSelectedToggle();
        System.out.println("create delivery tour with the " + selectedMapButton.getText());
    }
    
    @FXML
    void doLoadTour(ActionEvent event) {
        System.out.println("load tour");
    }

    @FXML
    void doManageCouriers(ActionEvent event) {
        System.out.println("manage couriers");
    }


}

