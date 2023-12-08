package h4131.view;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import h4131.controller.Controller;
import h4131.model.CurrentDeliveryPoint;
import h4131.model.DeliveryPoint;
import h4131.model.GlobalTour;
import h4131.model.Intersection;
import h4131.model.Map;
import h4131.model.Segment;
import h4131.model.Tour;
import h4131.observer.Observable;
import h4131.observer.Observer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WindowBuilder implements Observer {

    private Stage stage;
    private Parent root;
    private Controller controller;
    private DisplayMapSceneController displayMapSceneController;
    private Pane shapesPane;
    private VBox layout;
    private double longMin;
    private double longMax;
    private double latMin;
    private double latMax;

    private final Color[] colors = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLUEVIOLET, Color.ORANGE };

    /**
     * creates a window builder and displays the first scene of the application
     * 
     * @param controller
     * @param primaryStage
     * @param firstMap
     */
    public WindowBuilder(Controller controller, Stage primaryStage, Map firstMap) {
        this.controller = controller;
        this.stage = primaryStage;

        this.controller.setNumberOfCourier(3);

        drawMap(firstMap);

        Image icon = new Image(getClass().getResourceAsStream("/h4131/insa_logo.png"));
        stage.getIcons().add(icon);

        stage.setTitle("INSA Path Master");
        stage.setMaximized(true);
        stage.setFullScreenExitHint("Press ESC to escape full screen mode");
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.show();
    }

    @Override
    public void update(Observable observed, Object arg) {
        displayListDeliveryPoint();
    }

    /**
     * creates a pop-up error message
     * 
     * @param message the message to display
     */
    public void alert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);
    }

    /**
     * set the window fullscreen or not
     * 
     * @param bool true or false
     */
    public void setFullScreen(boolean bool) {
        stage.setFullScreen(bool);
    }

    /**
     * Method called to draw the map using shapes
     * 
     * @param map the map to draw
     */
    public void drawMap(Map map) {
        // Get screen dimensions
        Screen screen = Screen.getPrimary();
        double screenHeight = screen.getVisualBounds().getHeight();
        double screenWidth = screen.getVisualBounds().getWidth();

        try {
            // load fxml
            FXMLLoader displayMapSceneLoader = new FXMLLoader(getClass().getResource("/h4131/displayMapScene.fxml"));
            this.root = displayMapSceneLoader.load();
            displayMapSceneController = displayMapSceneLoader.getController();
            displayMapSceneController.setController(controller);
            displayMapSceneController.setNumberOfCourierFieldValue(String.valueOf(controller.getNumberOfCourier()));

            shapesPane = new Pane();
            shapesPane.setPrefHeight(screenHeight);
            shapesPane.setPrefWidth(screenWidth);

            // Determine max and min lat and long of intersections to convert to screen
            // coordinates
            longMax = 0;
            longMin = 1000;
            latMax = 0;
            latMin = 1000;

            for (Entry<Long, Intersection> entry : map.getIntersections().entrySet()) {
                Intersection intersection = entry.getValue();
                double latitude = intersection.getLatitude();
                double longitude = intersection.getLongitude();
                if (longitude > longMax) {
                    longMax = longitude;
                }
                if (longitude < longMin) {
                    longMin = longitude;
                }
                if (latitude > latMax) {
                    latMax = latitude;
                }
                if (latitude < latMin) {
                    latMin = latitude;
                }
            }

            // drawing the elements :

            // drawing lines
            for (Entry<Long, List<Segment>> entry : map.getAdjacency().entrySet()) {
                Long key = entry.getKey();
                Intersection origine = map.getIntersectionById(key);
                List<Segment> segments = entry.getValue();
                double originX = ((origine.getLongitude() - longMin) / (longMax - longMin)) * screenHeight
                        + (screenWidth - screenHeight) / 2;
                double originY = screenHeight - ((origine.getLatitude() - latMin) / (latMax - latMin)) * screenHeight;

                for (Segment segment : segments) {
                    double destX = ((segment.getDestination().getLongitude() - longMin) / (longMax - longMin))
                            * screenHeight + (screenWidth - screenHeight) / 2;
                    double destY = screenHeight
                            - ((segment.getDestination().getLatitude() - latMin) / (latMax - latMin)) * screenHeight;

                    addLine(originX, originY, destX, destY, segment);
                }
            }

            // drawing intersections
            for (Entry<Long, Intersection> entry : map.getIntersections().entrySet()) {
                Intersection intersection = entry.getValue();
                double intersectionX = ((intersection.getLongitude() - longMin) / (longMax - longMin)) * screenHeight
                        + (screenWidth - screenHeight) / 2;
                double intersectionY = screenHeight
                        - ((intersection.getLatitude() - latMin) / (latMax - latMin)) * screenHeight;
                addCircle(intersectionX, intersectionY, 2, intersection.getId());
            }

            Group group = new Group(shapesPane);
            Parent zoomPane = displayMapSceneController.createZoomPane(group);
            layout = displayMapSceneController.getLayout();
            layout.getChildren().setAll(zoomPane);
            VBox.setVgrow(zoomPane, Priority.ALWAYS);
            layout.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
            layout.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method called to draw a glabal tour
     * 
     * @param the Global Tour to display
     */
    public void drawGlobalTour(GlobalTour globalTour) {

        // Get screen dimensions
        Screen screen = Screen.getPrimary();
        double screenHeight = screen.getVisualBounds().getHeight();
        double screenWidth = screen.getVisualBounds().getWidth();

        int color = 0;

        for (Tour tour : globalTour.getTours()) {
            for (Segment segment : tour.getCourse()) {
                double destY = screenHeight
                        - ((segment.getDestination().getLatitude() - latMin) / (latMax - latMin)) * screenHeight;
                double destX = ((segment.getDestination().getLongitude() - longMin) / (longMax - longMin))
                        * screenHeight + (screenWidth - screenHeight) / 2;
                double originY = screenHeight
                        - ((segment.getOrigin().getLatitude() - latMin) / (latMax - latMin)) * screenHeight;
                double originX = ((segment.getOrigin().getLongitude() - longMin) / (longMax - longMin)) * screenHeight
                        + (screenWidth - screenHeight) / 2;

                addLineTour(originX, originY, destX, destY, color, segment);
            }
            color++;
        }
    }

    /**
     * method called by controller to display the menu when clicking on an
     * intersection
     * 
     * @param numberOfCourier to offer the user the choice of courier number
     * @param intersection    the selected intersection
     */
    public void openMenuIntersection(int numberOfCourier, Intersection intersection) {
        ChoiceBox<Integer> courierChoiceBox = displayMapSceneController.getCourierChoice();
        courierChoiceBox.getItems().clear();
        for (int i = 1; i <= numberOfCourier; i++) {
            courierChoiceBox.getItems().add(i);
        }
        courierChoiceBox.setValue(1);
        Label whichIntersection = displayMapSceneController.getWhichIntersection();
        whichIntersection.setText(
                "Intersection coordinates:\n" + intersection.getLatitude() + "°, " + intersection.getLongitude() + "°");
        whichIntersection.setWrapText(true);
        Pane validationPane = displayMapSceneController.getvalidationPane();
        validationPane.setVisible(true);
        validationPane.setDisable(false);
        disableBackground(true);
    }

    /**
     * methode called to disable background and prevent any click
     * 
     * @param bool
     */
    public void disableBackground(boolean bool) {
        layout.setDisable(bool);
        shapesPane.setDisable(bool);
    }

    /**
     * method called to delete the circle around an intersection when
     * not selected anymore
     * 
     * @param id
     */
    public void unSetIntersection(Long id) {
        for (Node node : shapesPane.getChildren()) {
            if (node instanceof IntersectionCircle) {
                IntersectionCircle circle = (IntersectionCircle) node;
                if (circle.getIntersectionId().equals(id)) {
                    circle.setStroke(Color.TRANSPARENT);
                }
            }
        }
    }

    /**
     * used to draw a circle on the map representing an intersection
     * 
     * @param x              coordinate of the intersection
     * @param y              coordinate of the intersection
     * @param radius         of the circle
     * @param intersectionId of the represented intersection
     */
    private void addCircle(double x, double y, double radius, Long intersectionId) {
        IntersectionCircle circle = new IntersectionCircle(x, y, radius, Color.TRANSPARENT, intersectionId);
        circle.setOnMouseClicked(displayMapSceneController::handleIntersectionClicked);
        circle.setOnMouseEntered(displayMapSceneController::handleIntersectionEntered);
        circle.setOnMouseExited(displayMapSceneController::handleIntersectionExited);
        shapesPane.getChildren().add(circle);
    }

    /**
     * used to draw a line on the map representing a segment
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param segment
     */
    private void addLine(double startX, double startY, double endX, double endY, Segment segment) {
        SegmentLine line = new SegmentLine(startX, startY, endX, endY, segment);
        line.setStroke(Color.WHITE);
        line.setOnMouseEntered(displayMapSceneController::handleSegmentEntered);
        line.setOnMouseExited(displayMapSceneController::handleSegmentExited);
        Tooltip tooltip = new Tooltip("Name : " + segment.getName() + "\nLength : " + segment.getLength() + "m");
        tooltip.setShowDelay(Duration.millis(200));
        tooltip.setHideDelay(Duration.millis(100));
        tooltip.setFont(javafx.scene.text.Font.font("Arial", 14));
        Tooltip.install(line, tooltip);
        shapesPane.getChildren().add(line);
    }

    /**
     * used to draw a line on the map representing a segment of a tour
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param color
     * @param segment
     */
    private void addLineTour(double startX, double startY, double endX, double endY, int color, Segment segment) {
        SegmentLine line = new SegmentLine(startX, startY, endX, endY, segment);
        line.setStroke(colors[(color % colors.length)]);
        line.setStrokeWidth(2.0);
        line.setOnMouseEntered(displayMapSceneController::handleSegmentEntered);
        line.setOnMouseExited(displayMapSceneController::handleSegmentExited);
        shapesPane.getChildren().add(line);
    }

    /**
     * called by update() to display the lists of delivery point by courier on the
     * right of the screen
     */
    public void displayListDeliveryPoint() {
        CurrentDeliveryPoint currentDeliveryPoint = controller.getCurrentDeliveryPoint();
        VBox tourListGroup = displayMapSceneController.getTourListGroup();
        tourListGroup.getChildren().clear();
        int courier = 1;
        for (LinkedList<DeliveryPoint> list : currentDeliveryPoint.getAffectedDeliveryPoints()) {
            if (!list.isEmpty()) {
                VBox listDeliveryPoint = new VBox();
                TitledPane titledPane = new TitledPane("Courier : " + courier, listDeliveryPoint);
                tourListGroup.getChildren().add(titledPane);
                for (DeliveryPoint deliveryPoint : list) {
                    DeliveryPointLabel label = new DeliveryPointLabel(
                            "Intersection : " + deliveryPoint.getPlace().getLatitude()
                                    + "°," + deliveryPoint.getPlace().getLongitude() + "° | "
                                    + deliveryPoint.getTime().getRepresentation(),
                            deliveryPoint, courier);
                    label.setOnMouseClicked(displayMapSceneController::handleDeliveryPointLabelClicked);
                    listDeliveryPoint.getChildren().add(label);
                }
            }
            courier++;
        }
        if (!currentDeliveryPoint.getNonAffectedDeliveryPoints().isEmpty()) {
            VBox listDeliveryPoint = new VBox();
            TitledPane titledPane = new TitledPane("Non Affected delivery points ", listDeliveryPoint);
            tourListGroup.getChildren().add(titledPane);
            for (DeliveryPoint deliveryPoint : currentDeliveryPoint.getNonAffectedDeliveryPoints()) {
                DeliveryPointLabel label = new DeliveryPointLabel(
                        "Intersection : " + deliveryPoint.getPlace().getLatitude()
                                + "°," + deliveryPoint.getPlace().getLongitude() + "° | "
                                + deliveryPoint.getTime().getRepresentation(),
                        deliveryPoint, 0);
                label.setOnMouseClicked(displayMapSceneController::handleDeliveryPointLabelClicked);
                listDeliveryPoint.getChildren().add(label);
            }
        }

    }

    /**
     * called by controller to open the menu allowing the user to modify a delivery
     * point
     * 
     * @param numberOfCourier to propose the choice of courier number
     * @param deliveryPoint   the delivery point to modify
     * @param currentCourier  the current courier affected to the delivery point
     */
    public void openMenuModifyDeliveryPoint(int numberOfCourier, DeliveryPoint deliveryPoint, int currentCourier) {
        ChoiceBox<Integer> modifyCourierChoiceBox = displayMapSceneController.getModifyCourierChoice();
        modifyCourierChoiceBox.getItems().clear();
        for (int i = 1; i <= numberOfCourier; i++) {
            modifyCourierChoiceBox.getItems().add(i);
        }
        modifyCourierChoiceBox.setValue(currentCourier == 0 ? 1 : currentCourier);
        ChoiceBox<String> modifyTimeWindowChoice = displayMapSceneController.getModifyTimeWindowChoice();
        modifyTimeWindowChoice.setValue(deliveryPoint.getTime().getRepresentation());
        Label whichDeliveryPoint = displayMapSceneController.getWhichDeliveryPoint();
        whichDeliveryPoint.setText("Delivery point coordinates:\n" + deliveryPoint.getPlace().getLatitude() + "°, "
                + deliveryPoint.getPlace().getLongitude() + "°");
        whichDeliveryPoint.setWrapText(true);
        Pane modifyPane = displayMapSceneController.getModifyPane();
        modifyPane.setVisible(true);
        modifyPane.setDisable(false);
        disableBackground(true);
    }
}
