package h4131.view;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import h4131.controller.Controller;
import h4131.model.Intersection;
import h4131.model.Map;
import h4131.model.Segment;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WindowBuilder {

    Stage stage;
    Parent root;
    Controller controller;
    private static final double ZOOM_FACTOR = 1.4;
    private static final Duration ZOOM_DURATION = Duration.millis(150);
    Pane shapesPane;
    private double lastMouseX, lastMouseY;

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
            Screen screen = Screen.getPrimary();
            webView.setPrefHeight(screen.getVisualBounds().getHeight());
            webView.setPrefWidth(screen.getVisualBounds().getWidth());
            webView.setCache(true);

            stage.setScene(new Scene(root));           
            stage.setWidth(screen.getVisualBounds().getWidth());
            stage.setHeight(screen.getVisualBounds().getHeight());

        } catch (IOException e) {
            e.printStackTrace();
        } 

    }

    public void drawMap(Map map){

        //creates the shape container
        shapesPane = new Pane();

        //Determine max and min lat and long to convert to screen coordinates
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

        //draw lines and circles for segments and intersections
        Screen screen = Screen.getPrimary();
        double screenHeight = screen.getVisualBounds().getHeight();
        double screenWidth = screen.getVisualBounds().getWidth();

        for (Entry<Long, List<Segment>> entry : map.getAdjacency().entrySet()) {
            Long key = entry.getKey();
            Intersection origine = map.getIntersectionById(key);
            List<Segment> segments = entry.getValue();
            double originX = ((origine.getLongitude() - longMin) / (longMax - longMin)) * screenHeight;
            double originY = screenHeight - ((origine.getLatitude() - latMin) / (latMax - latMin)) * screenHeight;
            
            for(Segment segment : segments){
                double destX = ((segment.getDestination().getLongitude() - longMin) / (longMax - longMin)) * screenHeight;
                double destY = screenHeight - ((segment.getDestination().getLatitude() - latMin) / (latMax - latMin)) * screenHeight;

                addLine(shapesPane, originX, originY, destX, destY);
            }

            addCircle(shapesPane, originX, originY, 5);
        }

        shapesPane.setOnScroll(this::zoomer);

        // Gestion du panoramique
        shapesPane.setOnMousePressed(this::sourisPressee);
        shapesPane.setOnMouseDragged(this::sourisGlissee);

        Scene scene = new Scene(shapesPane, screenWidth, screenHeight);
        scene.getStylesheets().add(getClass().getResource("/h4131/style.css").toExternalForm());
        stage.setScene(scene);
    }

    private void addCircle(Pane pane, double x, double y, double radius) {
        Circle circle = new Circle(x, y, radius, Color.RED);
        circle.setOnMouseClicked(this::shapeClicked);
        // circle.setOnMouseEntered(event -> shapeEntered(event));
        // circle.setOnMouseEntered(event -> shapeExited(event));
        pane.getChildren().add(circle);
    }

    private void addLine(Pane pane, double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.BLACK);
        line.setOnMouseClicked(this::shapeClicked);
        pane.getChildren().add(line);
    }

    private void shapeClicked(MouseEvent event) {
        System.out.println("Forme cliquée à la position : " + event.getX() + ", " + event.getY());
    }

    private void shapeEntered(MouseEvent event) {
        System.out.println("circle entered");
        if(event.getSource() instanceof Circle){
            System.out.println("circle entered");
            Circle sourceCircle = (Circle) event.getSource();
            sourceCircle.setRadius(10);
        }
    }

    private void shapeExited(MouseEvent event) {
        if(event.getSource() instanceof Circle){
            Circle sourceCircle = (Circle) event.getSource();
            sourceCircle.setRadius(5);
        }
    }

    private void zoomer(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        double scale = (deltaY > 0) ? ZOOM_FACTOR : 1.0 / ZOOM_FACTOR;
        scaleTransition(shapesPane, scale);
        event.consume();
             
    }

    private void scaleTransition(Pane pane, double scale) {
        ScaleTransition scaleTransition = new ScaleTransition(ZOOM_DURATION, pane);
        scaleTransition.setToX(pane.getScaleX() * scale);
        scaleTransition.setToY(pane.getScaleY() * scale);
        scaleTransition.play();
    }

    private void sourisPressee(MouseEvent event) {
        lastMouseX = event.getX();
        lastMouseY = event.getY();
    }

    private void sourisGlissee(MouseEvent event) {
        double deltaX = event.getX() - lastMouseX;
        double deltaY = event.getY() - lastMouseY;

        shapesPane.setTranslateX(shapesPane.getTranslateX() + deltaX*1.3);
        shapesPane.setTranslateY(shapesPane.getTranslateY() + deltaY*1.3);

        lastMouseX = event.getX();
        lastMouseY = event.getY();
    }
}
