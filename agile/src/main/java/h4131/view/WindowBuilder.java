package h4131.view;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Optional;

import h4131.controller.Controller;
import h4131.model.CurrentDeliveryPoint;
import h4131.model.DeliveryPoint;
import h4131.model.DeliveryPoint;
import h4131.model.GlobalTour;
import h4131.model.Intersection;
import h4131.model.Map;
import h4131.model.Segment;
import h4131.model.TimeWindow;
import h4131.model.Tour;
import h4131.observer.Observable;
import h4131.observer.Observer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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

    private final Color[] colors = { Color.CHARTREUSE, Color.DARKSALMON, Color.DEEPPINK, Color.GOLD,Color.BLUEVIOLET, Color.TOMATO,
            Color.GREEN, Color.AQUA, Color.RED, Color.DARKBLUE };

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

        Image icon = new Image(getClass().getResourceAsStream("/h4131/icon_logo.png"));
        stage.getIcons().add(icon);

        stage.setTitle("INSA Path Master");
        stage.setMaximized(true);
        stage.setFullScreenExitHint("Press ESC to escape full screen mode");
        stage.setFullScreen(true);
        stage.show();
        stage.setFullScreenExitHint("");
    }

    @Override
    public void update(Observable observed, Object arg) {
        for (Node node : shapesPane.getChildren()) {
            if (node instanceof IntersectionCircle) {
                IntersectionCircle circle = (IntersectionCircle) node;
                circle.setFill(Color.TRANSPARENT);
                circle.setRadius(2);
            } else if (node instanceof Text) {
                Text number = (Text) node;
                number.setVisible(false);
                number.setManaged(false);
            }
        }
        displayPointsOnMap();
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
     * Pop up if the number of loaded couriers is greater than the current number of couriers
     * 
     * @param currentNumberOfCourier the number of current couriers
     * @param numberOfCourierLoaded the number of couriers loaded
     * @return boolean true if he wants to load all the tours
     */
    public boolean NumberCourierChoice(int currentNumberOfCourier, int numberOfCourierLoaded) {
        ButtonType loaded = new ButtonType("All " + numberOfCourierLoaded, ButtonBar.ButtonData.OK_DONE);
        ButtonType current = new ButtonType("Only " + currentNumberOfCourier, ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(AlertType.WARNING,
                "You currently have " + currentNumberOfCourier + " couriers and you're trying to load a Global Tour with " + numberOfCourierLoaded + " couriers. How many tours do you want to load ?",
                current,
                loaded);

        alert.setTitle(null);
        alert.setHeaderText("Number of couriers mismatched");

        stage.setFullScreen(false);
        Optional<ButtonType> result = alert.showAndWait();
        stage.setFullScreen(true);
        return (result.get() == loaded);
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
     * Refresh the number of courier field after a load
     * 
     * 
     */

    public void refreshNumberOfCourier(){
        displayMapSceneController.setNumberOfCourierFieldValue(String.valueOf(controller.getNumberOfCourier()));
    }

    public void setLoadingAnimation(boolean bool){
        System.out.println("passe 1" + bool);
        // disableBackground(bool);
        displayMapSceneController.setBikeWheelVisible(bool);        
        System.out.println("passe 2" + bool);
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
            for (Entry<Long, Collection<Segment>> entry : map.getAdjacency().entrySet()) {
                Long key = entry.getKey();
                Intersection origine = map.getIntersectionById(key);
                Collection<Segment> segments = entry.getValue();
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
                addCircle(intersectionX, intersectionY, 2, intersection.getId(), Color.TRANSPARENT);
            }

            // drawing warehouse
            double x = map.getWarehouse().getPlace().getLongitude();
            double xWarehouse = ((x - longMin) / (longMax - longMin)) * screenHeight + (screenWidth - screenHeight) / 2;
            double y = map.getWarehouse().getPlace().getLatitude();
            double yWarehouse = screenHeight - ((y - latMin) / (latMax - latMin)) * screenHeight;
            Rectangle warehouse = new Rectangle(xWarehouse - 5, yWarehouse - 5, 10, 10);
            warehouse.setFill(Color.CORNFLOWERBLUE);
            shapesPane.getChildren().add(warehouse);
            Tooltip tooltip = new Tooltip("Warehouse");
            tooltip.setShowDelay(Duration.millis(200));
            tooltip.setHideDelay(Duration.millis(100));
            tooltip.setFont(javafx.scene.text.Font.font("Arial", 14));
            Tooltip.install(warehouse, tooltip);

            Group group = new Group(shapesPane);
            displayMapSceneController.setShapesPane(shapesPane);
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
        hideOldTour();
        // Get screen dimensions
        Screen screen = Screen.getPrimary();
        double screenHeight = screen.getVisualBounds().getHeight();
        double screenWidth = screen.getVisualBounds().getWidth();

        int color = 0;

        LinkedList<Segment> tours = new LinkedList<Segment>();
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
                tours.add(segment);
            }
            color++;
        }
        for (Segment segment : tours) {
            if (segment.getLength() > 75) {
                drawArrow(segment, 5, 5);
            }
        }

        // drawing intersections order
        for (Tour tour : globalTour.getTours()) {
            int deliveryNumber = 1;
            for (DeliveryPoint delivery : tour.getDeliveryPoints()) {
                if (delivery.getTime() != TimeWindow.WAREHOUSE) {
                    double lat = delivery.getPlace().getLatitude();
                    double longi = delivery.getPlace().getLongitude();
                    double deliveryX = ((longi - longMin) / (longMax - longMin)) * screenHeight
                            + (screenWidth - screenHeight) / 2;
                    double deliveryY = screenHeight - ((lat - latMin) / (latMax - latMin)) * screenHeight;
                    Text deliveryOrder = new Text(String.valueOf(deliveryNumber));
                    deliveryOrder.setFont(Font.font("Calibri", FontWeight.BOLD, 4));
                    deliveryOrder.setFill(Color.WHITE);
                    deliveryOrder.setMouseTransparent(true);
                    deliveryOrder.setX(deliveryX - deliveryOrder.getBoundsInLocal().getWidth() / 2);
                    deliveryOrder.setY(deliveryY + deliveryOrder.getBoundsInLocal().getHeight() / 4);
                    shapesPane.getChildren().add(deliveryOrder);
                    deliveryOrder.setViewOrder(-1);
                    deliveryNumber++;
                }
            }
        }
    }

    /**
     * Method called before printing new tour to hide the old one
     */
    private void hideOldTour() {
        for (Node node : shapesPane.getChildren()) {
            if (node instanceof SegmentLine) {
                SegmentLine segment = (SegmentLine) node;
                if (segment.getStroke() != Color.WHITE && segment.getStroke() != Color.BLUE) {
                    segment.setVisible(false);
                    segment.setManaged(false);
                }
            } else if (node instanceof Polygon) {
                Polygon arrow = (Polygon) node;
                arrow.setVisible(false);
                arrow.setManaged(false);
            } else if (node instanceof Text) {
                Text number = (Text) node;
                number.setVisible(false);
                number.setManaged(false);
            }
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
        courierChoiceBox.setValue(displayMapSceneController.getPreviousCourierChoice());
        Label whichIntersection = displayMapSceneController.getWhichIntersection();
        whichIntersection.setText(
                "Intersection coordinates:\n" + intersection.getLatitude() + "°, " + intersection.getLongitude() + "°");
        whichIntersection.setWrapText(true);
        Pane validationPane = displayMapSceneController.getValidationPane();
        fadeIn(validationPane);
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
    private void addCircle(double x, double y, double radius, Long intersectionId, Color fillColor) {
        IntersectionCircle circle = new IntersectionCircle(x, y, radius, fillColor, intersectionId);
        circle.setOnMouseClicked(displayMapSceneController::handleIntersectionClicked);
        circle.setOnMouseEntered(displayMapSceneController::handleIntersectionEntered);
        circle.setOnMouseExited(displayMapSceneController::handleIntersectionExited);
        shapesPane.getChildren().add(circle);
        circle.setViewOrder(0);
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
        line.setPreviousColor(Color.WHITE);
        line.setOnMouseEntered(displayMapSceneController::handleSegmentEntered);
        line.setOnMouseExited(displayMapSceneController::handleSegmentExited);
        Tooltip tooltip = new Tooltip("Name : " + segment.getName() + "\nLength : " + segment.getLength() + "m");
        tooltip.setShowDelay(Duration.millis(200));
        tooltip.setHideDelay(Duration.millis(100));
        tooltip.setFont(javafx.scene.text.Font.font("Arial", 14));
        Tooltip.install(line, tooltip);
        shapesPane.getChildren().add(line);
        line.setViewOrder(3);
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
        line.setPreviousColor(colors[(color % colors.length)]);
        line.setStrokeWidth(2.0);
        line.setOnMouseEntered(displayMapSceneController::handleSegmentEntered);
        line.setOnMouseExited(displayMapSceneController::handleSegmentExited);
        line.setMouseTransparent(true);
        shapesPane.getChildren().add(line);
        line.setViewOrder(2);
    }

    /**
     * used to draw an arrow indicating the direction of a segment
     * 
     * @param segment the segment on witch the arrow will be drawn
     * @param width   the width of the arrow
     * @param length  the length of the arrow
     */
    private void drawArrow(Segment segment, double width, double length) {
        Screen screen = Screen.getPrimary();
        double screenHeight = screen.getVisualBounds().getHeight();
        double screenWidth = screen.getVisualBounds().getWidth();
        double destY = screenHeight
                - ((segment.getDestination().getLatitude() - latMin) / (latMax - latMin)) * screenHeight;
        double destX = ((segment.getDestination().getLongitude() - longMin) / (longMax - longMin)) * screenHeight
                + (screenWidth - screenHeight) / 2;
        double originY = screenHeight
                - ((segment.getOrigin().getLatitude() - latMin) / (latMax - latMin)) * screenHeight;
        double originX = ((segment.getOrigin().getLongitude() - longMin) / (longMax - longMin)) * screenHeight
                + (screenWidth - screenHeight) / 2;

        double slope = (destY - originY) / (destX - originX);
        double norm = Math.sqrt(slope * slope + 1);
        double norm2 = Math.sqrt(1 / (slope * slope) + 1);

        double middleX = 0.55 * (destX - originX) + originX;
        double middleY = 0.55 * (destY - originY) + originY;

        double arrowBaseX = middleX - ((destY - originY < 0 ? 1 : -1) / (slope * norm2)) * 1.5;
        double arrowBaseY = middleY - (destY - originY < 0 ? 1 : -1) * 1.5 / norm2;

        double arrowHeadX = arrowBaseX - ((destY - originY < 0 ? 1 : -1) / (slope * norm2)) * length;
        double arrowHeadY = arrowBaseY - (destY - originY < 0 ? 1 : -1) * length / norm2;

        double leftX = middleX + width / 2 * slope / norm;
        double leftY = middleY + width / 2 * (-1) / norm;
        double rightX = middleX - (width / 2 * slope / norm);
        double rightY = middleY - (width / 2 * (-1) / norm);

        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(
                arrowBaseX, arrowBaseY,
                leftX, leftY,
                arrowHeadX, arrowHeadY,
                rightX, rightY);
        arrow.setFill(Color.WHITE);
        arrow.setMouseTransparent(true);
        shapesPane.getChildren().add(arrow);
        arrow.setViewOrder(1);
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
                TitledPane titledPane = new TitledPane();
                Circle circle = new Circle(8.75, colors[(courier - 1) % colors.length]);
                circle.setStroke(Color.BLACK);
                Label title = new Label("Courier : " + courier);
                HBox titleContent = new HBox(12);
                titleContent.getChildren().addAll(circle, title);
                titledPane.setGraphic(titleContent);
                titledPane.setContent(listDeliveryPoint);
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

            titledPane.setStyle("-fx-background-color: #FF0000");
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
        fadeIn(modifyPane);
        modifyPane.setDisable(false);
        disableBackground(true);
    }

    /**
     * Called by update to display the current delivery points on the map
     */
    public void displayPointsOnMap() {
        Screen screen = Screen.getPrimary();
        double screenHeight = screen.getVisualBounds().getHeight();
        double screenWidth = screen.getVisualBounds().getWidth();
        CurrentDeliveryPoint currentDeliveryPoint = controller.getCurrentDeliveryPoint();
        int courier = 1;
        for (LinkedList<DeliveryPoint> list : currentDeliveryPoint.getAffectedDeliveryPoints()) {
            if (!list.isEmpty()) {
                for (DeliveryPoint deliveryPoint : list) {
                    Intersection intersection = deliveryPoint.getPlace();
                    double intersectionX = ((intersection.getLongitude() - longMin) / (longMax - longMin))
                            * screenHeight + (screenWidth - screenHeight) / 2;
                    double intersectionY = screenHeight
                            - ((intersection.getLatitude() - latMin) / (latMax - latMin)) * screenHeight;
                    addCircle(intersectionX, intersectionY, 3, intersection.getId(),
                            colors[(courier - 1) % colors.length]);
                }
            }
            courier++;
        }
        if (!currentDeliveryPoint.getNonAffectedDeliveryPoints().isEmpty()) {
            for (DeliveryPoint deliveryPoint : currentDeliveryPoint.getNonAffectedDeliveryPoints()) {
                Intersection intersection = deliveryPoint.getPlace();
                double intersectionX = ((intersection.getLongitude() - longMin) / (longMax - longMin)) * screenHeight
                        + (screenWidth - screenHeight) / 2;
                double intersectionY = screenHeight
                        - ((intersection.getLatitude() - latMin) / (latMax - latMin)) * screenHeight;
                addCircle(intersectionX, intersectionY, 3, intersection.getId(), Color.LIGHTSLATEGRAY);
            }
        }
    }

    /**
     * Used to notify the user with a pane
     * 
     * @param pane the pane to show
     */
    public void alertMapChange(String mapSize) {
        displayMapSceneController.getMapChangeText().setText(mapSize);
        fadeIn(displayMapSceneController.getAlertMapChange());
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> fadeOut(displayMapSceneController.getAlertMapChange()));
        pause.play();
    }

    /**
     * Used to display an element with fading
     * 
     * @param node the element to show
     */
    public void fadeIn(Node node) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        node.setVisible(true);
    }

    /**
     * Used to hide an element with fading
     * 
     * @param node the element to hide
     */
    public void fadeOut(Node node) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), node);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.play();
        node.setVisible(false);
    }
}
