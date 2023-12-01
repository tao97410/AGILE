package h4131.view;

import h4131.controller.Controller;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class DisplayMapSceneController {
    
    private Controller controller;

    @FXML
    private VBox layout;

    /**
	 * Method called by the windowBuilder to set the controller when creating homeScene
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
            System.out.println(intersectionClicked.getIntersectionId());
            if(intersectionClicked.getStroke() == Color.RED){
                intersectionClicked.setStroke(null);
            }else{
                intersectionClicked.setStroke(Color.RED);
            }
            
            //controller.leftclick(intersectionClicked.getIntersectionId());
        }
	} 

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
    private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = ((Double.isNaN(scroller.getHvalue()) ? 0:scroller.getHvalue()) - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = ((Double.isNaN(scroller.getVvalue()) ? 0:scroller.getVvalue()) - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

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
