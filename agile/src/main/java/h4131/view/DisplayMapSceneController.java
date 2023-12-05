package h4131.view;

import h4131.controller.Controller;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class DisplayMapSceneController {
    
    private Controller controller;

    @FXML
    private StackPane container;
    
    @FXML
    private TextArea idIntersection;
    
    @FXML
    private Button cancelButton;

    @FXML
    private VBox layout;

    @FXML
    private ChoiceBox<String> mapChoiceBox;
    
    @FXML
    private TextField numberOfCourierField;

    @FXML
    private ChoiceBox<TimeWindow> timeWindowChoice;

    @FXML
    private ChoiceBox<Integer> courierChoice;

    @FXML
    private Button validationButton;

    @FXML
    private Group validationGroup;

    @FXML
    private Button loadMapButton;

    @FXML
    private void initialize(){
        mapChoiceBox.setValue("Select a map...");
        mapChoiceBox.getItems().addAll("smallMap", "mediumMap", "largeMap");
        numberOfCourierField.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("\\d")) {
                event.consume(); // Consume non-numeric input
            }
        });
        timeWindowChoice.getItems().addAll(TimeWindow.EIGHT_NINE, TimeWindow.NINE_TEN, TimeWindow.TEN_ELEVEN, TimeWindow.ELEVEN_TWELVE);
        timeWindowChoice.setValue(TimeWindow.EIGHT_NINE);
    }

    @FXML
    void onNumberOfCouriersChanged(KeyEvent event) {        
        if(!numberOfCourierField.getText().equals("")){
            this.controller.setNumberOfCourier(Integer.parseInt(numberOfCourierField.getText()));
        }
        loadMapButton.setDisable(true);

        // Add a ChangeListener to the ChoiceBox to detect selection changes
        mapChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Enables load map button if a correct value is selected
                loadMapButton.setDisable(newValue.equals("Select a map..."));
            }
        });
    }

    @FXML
    void doLoadMap(ActionEvent event) {
        String fileName = mapChoiceBox.getValue() + ".xml";
        controller.loadMap(fileName);       
    }

    @FXML
    void doLoadTour(ActionEvent event) {
        controller.loadGlobalTour();
    }

    @FXML
    void validateInformation(ActionEvent event) {
        this.validationGroup.setVisible(false);
        controller.addDeliveryPoint(Long.parseLong(this.idIntersection.getText().substring(15)), this.timeWindowChoice.getValue(), this.courierChoice.getValue());
    }
    
    @FXML
    void cancelIntersection(ActionEvent event) {
        this.validationGroup.setVisible(false);
        controller.cancelDeliveryPoint(Long.parseLong(this.idIntersection.getText().substring(15)));
    }

    public TextArea getWhichIntersection(){
        return this.idIntersection;
    }
    
    public ChoiceBox<TimeWindow> getTimeWindowChoice(){
        return this.timeWindowChoice;
    }

    public ChoiceBox<Integer> getCourierChoice(){
        return this.courierChoice;
    }

    public Button getValidateButton(){
        return this.validationButton;
    }

    public Group getValidationGroup(){
        return this.validationGroup;
    }

    public void setChoiceBoxValue(String value){
        mapChoiceBox.setValue(value);
    }

    public void setNumberOfCourierFieldValue(String value){
        numberOfCourierField.setText(value);
    }
    /**
	 * Method called by the windowBuilder to set the controller when creating the scene
	 * @param c the global controller of the application
	 */
    public void setController(Controller c){
        this.controller = c;
    }

    /**
	 * Method called by the windowBuilder to get the VBox map layout when drawing map
	 * @return the attribute VBox layout
	 */
    public VBox getLayout(){
        return layout;
    }

    /**
	 * Method called after click on an intersection on the map
	 */
	public void handleIntersectionClicked(MouseEvent event) {
		
        if(event.getSource() instanceof IntersectionCircle){
            IntersectionCircle intersectionClicked = (IntersectionCircle) event.getSource();
            if(intersectionClicked.getStroke() == Color.RED){
                intersectionClicked.setStroke(null);
            }else{
                intersectionClicked.setStroke(Color.RED);
                this.controller.leftClick(intersectionClicked);
            }
        }
	} 

    /**
	 * Method called after the cursor entered an intersection on the map
	 */
	public void handleIntersectionEntered(MouseEvent event) {
		
        if(event.getSource() instanceof IntersectionCircle){
            IntersectionCircle intersection = (IntersectionCircle) event.getSource();
            intersection.setFill(Color.RED);
            intersection.setCursor(Cursor.HAND);
        }
	} 

    /**
	 * Method called after the cursor exited an intersection on the map
	 */
	public void handleIntersectionExited(MouseEvent event) {
		
        if(event.getSource() instanceof IntersectionCircle){
            IntersectionCircle intersection = (IntersectionCircle) event.getSource();
            intersection.setFill(Color.TRANSPARENT);
            intersection.setCursor(Cursor.DEFAULT);
        }
	} 

    /**
	 * Method called after the cursor entered a segment on the map
	 */
	public void handleSegmentEntered(MouseEvent event) {

        if(event.getSource() instanceof SegmentLine){
            SegmentLine segment = (SegmentLine) event.getSource();
            segment.setPreviousColor(segment.getStroke());
            segment.setStroke(Color.RED);
            segment.setCursor(Cursor.DEFAULT);
        }
	} 

    /**
	 * Method called after the cursor exited an segment on the map
	 */
	public void handleSegmentExited(MouseEvent event) {
		
        if(event.getSource() instanceof SegmentLine){
            SegmentLine segment = (SegmentLine) event.getSource();
            segment.setStroke(segment.getPreviousColor());
        }
	} 

    /**
     * Method called by the WindowBuilder to create the scrollPane 
     * (with zooming and panning features) that will contains the shapes 
     * used to draw the map
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

        // Panning via drag....
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
                double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
                double deltaH = deltaX * (scroller.getHmax() - scroller.getHmin()) / extraWidth;
                double desiredH = (Double.isNaN(scroller.getHvalue()) ? 0:scroller.getHvalue()) - deltaH;
                scroller.setHvalue(Math.max(0, Math.min(scroller.getHmax(), desiredH)));

                double deltaY = event.getY() - lastMouseCoordinates.get().getY();
                double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
                double deltaV = deltaY * (scroller.getVmax() - scroller.getVmin()) / extraHeight;
                double desiredV = (Double.isNaN(scroller.getVvalue()) ? 0:scroller.getVvalue()) - deltaV;
                scroller.setVvalue(Math.max(0, Math.min(scroller.getVmax(), desiredV)));
            }
        });

        return scroller;
    } 
    
    /**
     * Computes the amount of scrolling in each direction in scrollContent coordinate units
     * @param scrollContent
     * @param scroller 
     * @return a point containing the scrolling in X and Y directions
     */
    private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = ((Double.isNaN(scroller.getHvalue()) ? 0:scroller.getHvalue()) - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = ((Double.isNaN(scroller.getVvalue()) ? 0:scroller.getVvalue()) - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    /**
     * method called after a zoom to reposition the previous center in the new center.
     * @param scrollContent
     * @param scroller
     * @param scaleFactor the zooming factor
     * @param scrollOffset
     */
    private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
            double halfWidth = scroller.getViewportBounds().getWidth() / 2 ;
            double newScrollXOffset = (scaleFactor - 1) *  halfWidth + scaleFactor * scrollXOffset;
            scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
            double halfHeight = scroller.getViewportBounds().getHeight() / 2 ;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
            scroller.setVvalue(scroller.getVmin());
        }
    }
}
