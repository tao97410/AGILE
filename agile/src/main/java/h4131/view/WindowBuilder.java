package h4131.view;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import h4131.controller.Controller;
import h4131.model.Intersection;
import h4131.model.Map;
import h4131.model.Segment;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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

    public void drawMap(Map map){
        // Get screen dimensions
        Screen screen = Screen.getPrimary();
        double screenHeight = screen.getVisualBounds().getHeight();
        double screenWidth = screen.getVisualBounds().getWidth();

        try {
            //load fxml
            FXMLLoader displayMapSceneLoader = new FXMLLoader(getClass().getResource("/h4131/displayMapScene.fxml"));
            this.root = displayMapSceneLoader.load();
            DisplayMapSceneController displayMapSceneController = displayMapSceneLoader.getController();
            displayMapSceneController.setController(controller);
            Pane shapesPane = displayMapSceneController.getShapesPane();

            shapesPane.setPrefHeight(screenHeight);
            shapesPane.setPrefWidth(screenWidth); 
            
            //Determine max and min lat and long of intersections to convert to screen coordinates
            double longMax = 0;
            double longMin = 1000;
            double latMax = 0;
            double latMin = 1000;

            for (Entry<Long, Intersection> entry : map.getIntersections().entrySet()) {
                Intersection intersection = entry.getValue();
                double latitude = intersection.getLatitude();
                double longitude = intersection.getLongitude();
                if(longitude > longMax ){
                    longMax = longitude;
                }
                if(longitude < longMin ){
                    longMin = longitude;
                }
                if(latitude > latMax ){
                    latMax = latitude;
                }
                if(latitude < latMin ){
                    latMin = latitude;
                }
            }

            //drawing the elements :

            //drawing lines
            for (Entry<Long, List<Segment>> entry : map.getAdjacency().entrySet()) {
                Long key = entry.getKey();
                Intersection origine = map.getIntersectionById(key);
                List<Segment> segments = entry.getValue();
                double originX = ((origine.getLongitude() - longMin) / (longMax - longMin)) * screenHeight + (screenWidth-screenHeight)/2;
                double originY = screenHeight - ((origine.getLatitude() - latMin) / (latMax - latMin)) * screenHeight;
                
                for(Segment segment : segments){
                    double destX = ((segment.getDestination().getLongitude() - longMin) / (longMax - longMin)) * screenHeight + (screenWidth-screenHeight)/2;
                    double destY = screenHeight - ((segment.getDestination().getLatitude() - latMin) / (latMax - latMin)) * screenHeight;

                    addLine(shapesPane, originX, originY, destX, destY);
                }  
            }

            //drawing intersections
            for (Entry<Long, Intersection> entry : map.getIntersections().entrySet()) {
                Intersection intersection = entry.getValue();
                double intersectionX = ((intersection.getLongitude() - longMin) / (longMax - longMin)) * screenHeight + (screenWidth-screenHeight)/2;
                double intersectionY = screenHeight - ((intersection.getLatitude() - latMin) / (latMax - latMin)) * screenHeight;
                addCircle(shapesPane, intersectionX, intersectionY, 4, intersection.getId(), displayMapSceneController);
            }

            Scene scene = new Scene(root, screenWidth, screenHeight);
            stage.setScene(scene);
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCircle(Pane pane, double x, double y, double radius, Long intersectionId, DisplayMapSceneController sceneController) {
        IntersectionCircle circle = new IntersectionCircle(x, y, radius, Color.TRANSPARENT, intersectionId);
        circle.setOnMouseClicked(sceneController::handleIntersectionClicked);
        pane.getChildren().add(circle);
    }

    private void addLine(Pane pane, double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.WHITE);
        pane.getChildren().add(line);
    }
}
