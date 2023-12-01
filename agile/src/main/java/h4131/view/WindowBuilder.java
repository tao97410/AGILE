package h4131.view;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import h4131.controller.Controller;
import h4131.model.GlobalTour;
import h4131.model.Intersection;
import h4131.model.Map;
import h4131.model.Segment;
import h4131.model.Tour;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class WindowBuilder {

    private Stage stage;
    private Parent root;
    private Controller controller;
    private Pane shapesPane;
    private double longMin;
    private double longMax;
    private double latMin;
    private double latMax;

    

    public WindowBuilder(Controller controller, Stage primaryStage, Map firstMap){
        this.controller = controller;
        this.stage = primaryStage;

        drawMap(firstMap);

        Image icon = new Image(getClass().getResourceAsStream("/h4131/insa_logo.png"));
        stage.getIcons().add(icon);

        stage.setTitle("INSA Path Master");
        stage.setMaximized(true);
        stage.setFullScreenExitHint("Press ESC to escape full screen mode");
        stage.setFullScreen(true);
        
        stage.show();    
    }

    public void alert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);
    }

    public void setFullScreen(boolean bool){
        stage.setFullScreen(bool);
    }


    public void drawMap(Map map){
        // Get screen dimensions
        Screen screen = Screen.getPrimary();
        double screenHeight = screen.getVisualBounds().getHeight();
        double screenWidth = screen.getVisualBounds().getWidth();

        try {
            // load fxml
            FXMLLoader displayMapSceneLoader = new FXMLLoader(getClass().getResource("/h4131/displayMapScene.fxml"));
            this.root = displayMapSceneLoader.load();
            DisplayMapSceneController displayMapSceneController = displayMapSceneLoader.getController();
            displayMapSceneController.setController(controller);
            
            shapesPane = new Pane();
            shapesPane.setPrefHeight(screenHeight);
            shapesPane.setPrefWidth(screenWidth); 
            
            //Determine max and min lat and long of intersections to convert to screen coordinates
            longMax = 0;
            longMin = 1000;
            latMax = 0;
            latMin = 1000;

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

            Group group = new Group(shapesPane);
            Parent zoomPane = displayMapSceneController.createZoomPane(group);
            VBox layout = displayMapSceneController.getLayout();
            layout.getChildren().setAll(zoomPane);
            VBox.setVgrow(zoomPane, Priority.ALWAYS);
            layout.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
            layout.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreenExitHint("");
            stage.setFullScreen(true);
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawGlobalTour(GlobalTour globalTour){

        // Get screen dimensions
        Screen screen = Screen.getPrimary();
        double screenHeight = screen.getVisualBounds().getHeight();
        double screenWidth = screen.getVisualBounds().getWidth();

        int color = 0;   

        for(Tour tour : globalTour.getTours()){             
            for(Segment segment : tour.getCourse()){
                double destY = screenHeight - ((segment.getDestination().getLatitude() - latMin) / (latMax - latMin)) * screenHeight;
                double destX = ((segment.getDestination().getLongitude() - longMin) / (longMax - longMin)) * screenHeight + (screenWidth-screenHeight)/2;
                double originY = screenHeight - ((segment.getOrigin().getLatitude() - latMin) / (latMax - latMin)) * screenHeight;
                double originX = ((segment.getOrigin().getLongitude() - longMin) / (longMax - longMin)) * screenHeight + (screenWidth-screenHeight)/2;

                addLineTour(shapesPane, originX, originY, destX, destY, color);
            }
            color++;
        }
    }

    

    private void addCircle(Pane pane, double x, double y, double radius, Long intersectionId, DisplayMapSceneController sceneController) {
        IntersectionCircle circle = new IntersectionCircle(x, y, radius, Color.TRANSPARENT, intersectionId);
        circle.setOnMouseClicked(sceneController::handleIntersectionClicked);
        circle.setOnMouseEntered(sceneController::handleIntersectionEntered);
        circle.setOnMouseExited(sceneController::handleIntersectionExited);
        pane.getChildren().add(circle);
    }

    private void addLine(Pane pane, double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.WHITE);
        pane.getChildren().add(line);
    }

    private void addLineTour(Pane pane, double startX, double startY, double endX, double endY, int color) {
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLUEVIOLET, Color.ORANGE};
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(colors[(color%6)]);
        line.setStrokeWidth(2.0);
        pane.getChildren().add(line);
    }
}
