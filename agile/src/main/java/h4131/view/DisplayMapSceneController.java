package h4131.view;

import java.util.LinkedList;

import h4131.controller.Controller;
import h4131.model.DeliveryPoint;
import h4131.model.TimeWindow;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DisplayMapSceneController {

    private Controller controller;
    private String previousNumberOfCourier = "3";
    private int previousCourierChoice = 1;

    // Main containers
    @FXML
    private StackPane container;
    @FXML
    private VBox layout;

    // Main menu controls
    @FXML
    private ChoiceBox<String> mapChoiceBox;
    @FXML
    private TextField numberOfCourierField;

    // Delivery point validation menu
    @FXML
    private Pane validationPane;
    @FXML
    private ChoiceBox<String> timeWindowChoice;
    @FXML
    private ChoiceBox<Integer> courierChoice;
    @FXML
    private Button validationButton;
    @FXML
    private Label idIntersection;
    @FXML
    private Button cancelButton;

    // Delivery point modification menu
    @FXML
    private Button deleteButton;
    @FXML
    private Pane modifyPane;
    @FXML
    private Label idDeliveryPoint;
    @FXML
    private ChoiceBox<String> modifyTimeWindowChoice;
    @FXML
    private ChoiceBox<Integer> modifyCourierChoice;
    @FXML
    private Button modifyButton;

    // List of delivery points by courier
    @FXML
    private VBox tourListGroup;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private void initialize() {
        mapChoiceBox.setValue("Select a map  ");
        mapChoiceBox.getItems().addAll("smallMap", "mediumMap", "largeMap");

        // Add a ChangeListener to the ChoiceBox to detect selection changes
        mapChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // load the chosen map
                String fileName = mapChoiceBox.getValue() + ".xml";
                controller.loadMap(fileName);
            }
        });

        numberOfCourierField.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("\\d")) {
                event.consume(); // Consume non-numeric input
            }
            if (numberOfCourierField.getText().equals("0")) {
                numberOfCourierField.setText(previousNumberOfCourier);
            }

        });

        timeWindowChoice.getItems().addAll(TimeWindow.EIGHT_NINE.getRepresentation(),
                TimeWindow.NINE_TEN.getRepresentation(),
                TimeWindow.TEN_ELEVEN.getRepresentation(), TimeWindow.ELEVEN_TWELVE.getRepresentation());
        timeWindowChoice.setValue(TimeWindow.EIGHT_NINE.getRepresentation());

        modifyTimeWindowChoice.getItems().addAll(TimeWindow.EIGHT_NINE.getRepresentation(),
                TimeWindow.NINE_TEN.getRepresentation(),
                TimeWindow.TEN_ELEVEN.getRepresentation(), TimeWindow.ELEVEN_TWELVE.getRepresentation());
        modifyTimeWindowChoice.setValue(TimeWindow.EIGHT_NINE.getRepresentation());

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    @FXML
    void onNumberOfCouriersChanged(KeyEvent event) {
        if (!numberOfCourierField.getText().equals("") && !numberOfCourierField.getText().equals("0")
                && event.getCode() == KeyCode.ENTER) {
            previousNumberOfCourier = numberOfCourierField.getText();
            this.controller.changeNumberOfCourier(Integer.parseInt(numberOfCourierField.getText()));
        }
    }

    @FXML
    void doLoadTour(ActionEvent event) {
        controller.loadGlobalTour();
    }

    @FXML
    void validateInformation(ActionEvent event) {
        this.validationPane.setVisible(false);
        String timeValue = this.timeWindowChoice.getValue();
        TimeWindow time = TimeWindow.getTimeWindowByRepresentation(timeValue);
        previousCourierChoice = this.courierChoice.getValue();
        controller.addDeliveryPoint(time, previousCourierChoice);
    }

    @FXML
    void cancelIntersection(ActionEvent event) {
        this.validationPane.setVisible(false);
        controller.cancelDeliveryPoint();
    }

    @FXML
    void modifyDeliveryPoint(ActionEvent event) {
        this.modifyPane.setVisible(false);
        String timeValue = this.modifyTimeWindowChoice.getValue();
        TimeWindow time = TimeWindow.getTimeWindowByRepresentation(timeValue);
        controller.changeInfosDeliveryPoint(time, this.modifyCourierChoice.getValue());
    }

    @FXML
    void deleteDeliveryPoint(ActionEvent event) {
        this.modifyPane.setVisible(false);
        controller.deleteDeliveryPoint();
    }

    @FXML
    void doComputeGlobalTour(ActionEvent event) {
        controller.computeGlobalTour();
    }

    @FXML
    void doSaveGlobalTour(ActionEvent event) {
        controller.saveGlobalTour();
    }

    public int getPreviousCourierChoice() {
        return previousCourierChoice;
    }

    public VBox getTourListGroup() {
        return this.tourListGroup;
    }

    public Label getWhichIntersection() {
        return this.idIntersection;
    }

    public ChoiceBox<String> getTimeWindowChoice() {
        return this.timeWindowChoice;
    }

    public ChoiceBox<Integer> getCourierChoice() {
        return this.courierChoice;
    }

    public Button getValidateButton() {
        return this.validationButton;
    }

    public Pane getvalidationPane() {
        return this.validationPane;
    }

    public void setChoiceBoxValue(String value) {
        mapChoiceBox.setValue(value);
    }

    public void setNumberOfCourierFieldValue(String value) {
        numberOfCourierField.setText(value);
    }

    public void setController(Controller c) {
        this.controller = c;
    }

    public VBox getLayout() {
        return layout;
    }

    public ChoiceBox<Integer> getModifyCourierChoice() {
        return modifyCourierChoice;
    }

    public Label getWhichDeliveryPoint() {
        return idDeliveryPoint;
    }

    public Pane getModifyPane() {
        return modifyPane;
    }

    public ChoiceBox<String> getModifyTimeWindowChoice() {
        return modifyTimeWindowChoice;
    }

    /**
     * Method called after click on an intersection on the map
     */
    public void handleIntersectionClicked(MouseEvent event) {
        if (event.getSource() instanceof IntersectionCircle) {
            IntersectionCircle intersectionClicked = (IntersectionCircle) event.getSource();
            boolean isPresent = false;
            int courier = 1;
            for (LinkedList<DeliveryPoint> list : controller.getCurrentDeliveryPoint().getAffectedDeliveryPoints()) {
                for (DeliveryPoint deliveryPoint : list) {
                    if (deliveryPoint.getPlace().getId() == intersectionClicked.getIntersectionId()) {
                        controller.modifyDeliveryPoint(deliveryPoint, courier);
                        isPresent = true;
                        break;
                    }
                }
                courier++;
            }
            if (!isPresent) {
                for (DeliveryPoint deliveryPoint : controller.getCurrentDeliveryPoint()
                        .getNonAffectedDeliveryPoints()) {
                    if (deliveryPoint.getPlace().getId() == intersectionClicked.getIntersectionId()) {
                        controller.modifyDeliveryPoint(deliveryPoint, 0);
                        isPresent = true;
                        break;
                    }
                }
            }
            if (!isPresent) {
                this.controller.leftClick(intersectionClicked.getIntersectionId());
            }
        }
    }

    /**
     * Method called after click on an label of a delivery point on the right scroll
     * pane
     */
    public void handleDeliveryPointLabelClicked(MouseEvent event) {

        if (event.getSource() instanceof DeliveryPointLabel) {
            DeliveryPointLabel clickedPoint = (DeliveryPointLabel) event.getSource();
            System.out.println(clickedPoint.getDeliveryPoint().getPlace().getId());
            this.controller.modifyDeliveryPoint(clickedPoint.getDeliveryPoint(), clickedPoint.getCourier());
        }
    }

    /**
     * Method called after the cursor entered an intersection on the map
     */
    public void handleIntersectionEntered(MouseEvent event) {

        if (event.getSource() instanceof IntersectionCircle) {
            IntersectionCircle intersection = (IntersectionCircle) event.getSource();
            intersection.setPreviousColor(intersection.getFill());
            intersection.setFill(Color.LIGHTSKYBLUE);
            intersection.setCursor(Cursor.HAND);
        }
    }

    /**
     * Method called after the cursor exited an intersection on the map
     */
    public void handleIntersectionExited(MouseEvent event) {

        if (event.getSource() instanceof IntersectionCircle) {
            IntersectionCircle intersection = (IntersectionCircle) event.getSource();
            intersection.setFill(intersection.getPreviousColor());
            intersection.setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Method called after the cursor entered a segment on the map
     */
    public void handleSegmentEntered(MouseEvent event) {

        if (event.getSource() instanceof SegmentLine) {
            SegmentLine segment = (SegmentLine) event.getSource();
            segment.setPreviousColor(segment.getStroke());
            segment.setStroke(Color.LIGHTSKYBLUE);
            segment.setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Method called after the cursor exited an segment on the map
     */
    public void handleSegmentExited(MouseEvent event) {

        if (event.getSource() instanceof SegmentLine) {
            SegmentLine segment = (SegmentLine) event.getSource();
            segment.setStroke(segment.getPreviousColor());
        }
    }

    /**
     * Method called by the WindowBuilder to create the scrollPane
     * (with zooming and panning features) that will contains the shapes
     * used to draw the map
     * 
     * @param group containing the shapes
     * @return the scrollPane
     */
    public Parent createZoomPane(Group group) {
        final double SCALE_DELTA = 1.1;
        final StackPane zoomPane = new StackPane();

        zoomPane.getChildren().add(group);
        Group zoomContent = new Group(zoomPane);
        StackPane canvasPane = new StackPane();
        canvasPane.getChildren().add(zoomContent);

        final ScrollPane scroller = new ScrollPane();
        final Group scrollContent = new Group(canvasPane);
        scroller.setContent(scrollContent);

        scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                canvasPane.setMinSize(newValue.getWidth(), newValue.getHeight());
            }
        });

        zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;

                // amount of scrolling in each direction in scrollContent coordinate
                // units
                Point2D scrollOffset = figureScrollOffset(scrollContent, scroller);

                double newScaleX = group.getScaleX() * scaleFactor;
                double newScaleY = group.getScaleY() * scaleFactor;

                // Prevent zooming out beyond the original size
                if (newScaleX < 1.0 || newScaleY < 1.0) {
                    return;
                }

                group.setScaleX(newScaleX);
                group.setScaleY(newScaleY);

                // move viewport so that old center remains in the center after the
                // scaling
                repositionScroller(scrollContent, scroller, scaleFactor, scrollOffset);
            }
        });

        // Panning with mouse drag
        final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<Point2D>();
        scrollContent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
            }
        });

        scrollContent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double deltaX = event.getX() - lastMouseCoordinates.get().getX();
                double extraWidth = scrollContent.getLayoutBounds().getWidth()
                        - scroller.getViewportBounds().getWidth();
                double deltaH = deltaX * (scroller.getHmax() - scroller.getHmin()) / extraWidth;
                double desiredH = (Double.isNaN(scroller.getHvalue()) ? 0 : scroller.getHvalue()) - deltaH;
                scroller.setHvalue(Math.max(0, Math.min(scroller.getHmax(), desiredH)));

                double deltaY = event.getY() - lastMouseCoordinates.get().getY();
                double extraHeight = scrollContent.getLayoutBounds().getHeight()
                        - scroller.getViewportBounds().getHeight();
                double deltaV = deltaY * (scroller.getVmax() - scroller.getVmin()) / extraHeight;
                double desiredV = (Double.isNaN(scroller.getVvalue()) ? 0 : scroller.getVvalue()) - deltaV;
                scroller.setVvalue(Math.max(0, Math.min(scroller.getVmax(), desiredV)));
            }
        });

        return scroller;
    }

    /**
     * Computes the amount of scrolling in each direction in scrollContent
     * coordinate units
     * 
     * @param scrollContent
     * @param scroller
     * @return a point containing the scrolling in X and Y directions
     */
    private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = ((Double.isNaN(scroller.getHvalue()) ? 0 : scroller.getHvalue())
                - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = ((Double.isNaN(scroller.getVvalue()) ? 0 : scroller.getVvalue())
                - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    /**
     * method called after a zoom to reposition the previous center in the new
     * center.
     * 
     * @param scrollContent
     * @param scroller
     * @param scaleFactor   the zooming factor
     * @param scrollOffset
     */
    private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
            double halfWidth = scroller.getViewportBounds().getWidth() / 2;
            double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
            scroller.setHvalue(
                    scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
            double halfHeight = scroller.getViewportBounds().getHeight() / 2;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            scroller.setVvalue(
                    scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
            scroller.setVvalue(scroller.getVmin());
        }
    }
}
