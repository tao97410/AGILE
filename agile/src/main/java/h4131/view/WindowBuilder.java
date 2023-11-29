package h4131.view;

import java.io.IOException;
import java.net.URL;

import h4131.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
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

    /**
	 * Method called by initial state to set the scene to loadTourScene and print the svg map/tour
	 */
    public void printSVGMap(){

        try {
            FXMLLoader loadTourSceneLoader = new FXMLLoader(getClass().getResource("/h4131/loadTourScene.fxml"));
            this.root = loadTourSceneLoader.load();
            LoadTourSceneController loadTourSceneController = loadTourSceneLoader.getController();
            loadTourSceneController.setController(controller);

            WebView webView = loadTourSceneController.getWebView();
            URL url = getClass().getResource("/h4131/output.svg");
            webView.getEngine().load(url.toExternalForm());

            stage.setScene(new Scene(root));           
            Screen screen = Screen.getPrimary();
            stage.setWidth(screen.getVisualBounds().getWidth());
            stage.setHeight(screen.getVisualBounds().getHeight());

        } catch (IOException e) {
            e.printStackTrace();
        } 

    }
}
