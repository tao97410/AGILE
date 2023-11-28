package h4131.view;

import java.io.IOException;

import h4131.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class WindowBuilder {

    Stage stage;
    Parent root;
    Controller controller;

    public WindowBuilder(Controller controller, Stage primaryStage){
        this.controller = controller;
        this.stage = primaryStage;
        try {
            FXMLLoader homeSceneLoader = new FXMLLoader(getClass().getResource("/h4131/homeScene.fxml"));
            this.root = homeSceneLoader.load();
            HomeSceneController homeSceneController = homeSceneLoader.getController();
            homeSceneController.setController(controller);

            Image icon = new Image(getClass().getResourceAsStream("/h4131/insa_logo.png"));
            stage.getIcons().add(icon);

            stage.setTitle("INSA Path Master");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }       
    }
}
