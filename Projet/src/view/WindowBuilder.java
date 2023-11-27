package view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class WindowBuilder {

    Stage stage;
    Parent root;

    public WindowBuilder(Stage primaryStage){
        this.stage = primaryStage;
        try {
            this.root = FXMLLoader.load(getClass().getResource("homeScene.fxml"));

            Image icon = new Image(getClass().getResourceAsStream("../assets/insa_logo.png"));
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
