package h4131.view;

import h4131.controller.Controller;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.util.Duration;

public class DisplayMapSceneController {
    
    private Controller controller;
    private static final double TRANSLATION_FACTOR = 1.4;
    private static final double ZOOM_FACTOR = 1.4;
    private static final Duration ZOOM_DURATION = Duration.millis(150);
    private double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    private double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    private double lastMouseX;
    private double lastMouseY;
    
    @FXML
    private Pane shapesPane;

    /**
	 * Method called by the windowBuilder to set the controller when creating homeScene
	 * @param c the global controller of the application
	 */
    public void setController(Controller c){
        this.controller = c;
    }

    /**
	 * Method called by the windowBuilder to get the shapes pane when drawing map
	 * @return the attribute Pane shapesPane
	 */
    public Pane getShapesPane(){
        return shapesPane;
    }

    /**
	 * Method called after click on an intersection on the map
	 */
	public void handleIntersectionClicked(MouseEvent event) {
		
        if(event.getSource() instanceof IntersectionCircle){
            IntersectionCircle intersectionClicked = (IntersectionCircle) event.getSource();
            System.out.println(intersectionClicked.getIntersectionId());
            //controller.leftclick(intersectionClicked.getIntersectionId());
        }
	} 

    @FXML
    void handleZoom(ScrollEvent event) {
        double scrollY = event.getDeltaY();
        double scale = (scrollY > 0) ? ZOOM_FACTOR : 1.0 / ZOOM_FACTOR;
        if(isValidScale(shapesPane, scale)){  
            scaleTransition(shapesPane, scale);
            
            double leftBorder = shapesPane.localToScene(shapesPane.getBoundsInLocal().getMinX(), 0).getX();
            double rightBorder = shapesPane.localToScene(shapesPane.getBoundsInLocal().getMaxX(), 0).getX();
            double topBorder = shapesPane.localToScene(shapesPane.getBoundsInLocal().getMinY(), 0).getY();
            double bottomBorder = shapesPane.localToScene(0, shapesPane.getBoundsInParent().getMaxY()).getY();
            
            if(leftBorder>0)
                shapesPane.setTranslateX(shapesPane.getTranslateX() - leftBorder );
            else if(rightBorder<screenWidth)
                shapesPane.setTranslateX(shapesPane.getTranslateX() + (screenWidth-rightBorder));
            if(topBorder>0)
                shapesPane.setTranslateY(shapesPane.getTranslateY() - topBorder );
            else if(bottomBorder<screenHeight)
                shapesPane.setTranslateY(shapesPane.getTranslateY() + (screenHeight-bottomBorder));
            
            event.consume();
        }     
    }

    private boolean isValidScale(Pane pane, double scale) {
        double newScaleX = pane.getScaleX() * scale;
        double newScaleY = pane.getScaleY() * scale;
    
        return newScaleX >= 1.0 && newScaleY >= 1.0;
    }

    private void scaleTransition(Pane pane, double scale) {
        ScaleTransition scaleTransition = new ScaleTransition(ZOOM_DURATION, pane);
        scaleTransition.setToX(pane.getScaleX() * scale);
        scaleTransition.setToY(pane.getScaleY() * scale);
        scaleTransition.play();
    }

    @FXML
    void handleMousePressed(MouseEvent event) {
        lastMouseX = event.getX();
        lastMouseY = event.getY();
    }

    @FXML
    void handleMouseDragged(MouseEvent event) {
        double deltaX = event.getX() - lastMouseX;
        double deltaY = event.getY() - lastMouseY;

        double leftBorder = shapesPane.localToScene(shapesPane.getBoundsInLocal().getMinX(), 0).getX();
        double rightBorder = shapesPane.localToScene(shapesPane.getBoundsInLocal().getMaxX(), 0).getX();
        double topBorder = shapesPane.localToScene(0, shapesPane.getBoundsInLocal().getMinY()).getY();
        double bottomBorder = shapesPane.localToScene(0, shapesPane.getBoundsInLocal().getMaxY()).getY();
        
        double newLeftBorder = leftBorder + deltaX*TRANSLATION_FACTOR;
        double newRightBorder = rightBorder + deltaX*TRANSLATION_FACTOR;
        double newTopBorder = topBorder + deltaY*TRANSLATION_FACTOR;
        double newBottomBorder = bottomBorder + deltaY*TRANSLATION_FACTOR;

        if(newLeftBorder <=0 && newRightBorder >= screenWidth){
            shapesPane.setTranslateX(shapesPane.getTranslateX() + deltaX*TRANSLATION_FACTOR);
        }

        if(newTopBorder <= 0 && newBottomBorder >= screenHeight-20){
            shapesPane.setTranslateY(shapesPane.getTranslateY() + deltaY*TRANSLATION_FACTOR);
        }

        lastMouseX = event.getX();
        lastMouseY = event.getY();
    }

}
