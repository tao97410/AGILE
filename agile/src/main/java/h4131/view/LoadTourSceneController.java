package h4131.view;

import h4131.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.web.WebView;

public class LoadTourSceneController {

    private Controller controller;
    private double scaleFactor = 1.0;
    private double lastX;
    private double lastY;

    @FXML
    private WebView webView;
    
    @FXML
    private ScrollPane scrollPane;

    public void initialize(){
        addZoomingFunctionality();
    }

    @FXML
    void handleMouseDragEntered(MouseDragEvent event) {
        lastX = event.getX();
        lastY = event.getY();
        System.out.println("mouse pressed");
    }

    @FXML
    void handleMouseDragReleased(MouseDragEvent event) {
        System.out.println("mouse dragged");
        double deltaX = event.getX() - lastX;
        double deltaY = event.getY() - lastY;

        double hValue = scrollPane.getHvalue();
        double vValue = scrollPane.getVvalue();

        // Ajuster les valeurs horizontales et verticales en fonction du déplacement de la souris
        scrollPane.setHvalue(hValue - deltaX / webView.getWidth());
        scrollPane.setVvalue(vValue - deltaY / webView.getHeight());

        lastX = event.getX();
        lastY = event.getY();
    }

    /**
	 * Method called by the windowBuilder to set the controller when creating loadTourScene
	 * @param c the global controller of the application
	 */
    public void setController(Controller c){
        this.controller = c;
    }

    public WebView getWebView(){
        return webView;
    }

    private void addZoomingFunctionality() {
        webView.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() == 0) {
                return;
            }
            double scaleFactorDelta = 1.05;
            if (event.getDeltaY() < 0) {
                scaleFactor /= scaleFactorDelta;
            } else {
                scaleFactor *= scaleFactorDelta;
            }
            webView.setZoom(scaleFactor);
            event.consume();
        });
        
    }

    
}
