package h4131.view;

import h4131.controller.Controller;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.web.WebView;

public class LoadTourSceneController {

    private Controller controller;
    private double scaleFactor = 1.0;

    @FXML
    private WebView webView;
    
    @FXML
    private ScrollPane scrollPane;

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

    public void initialize() {
        addZoomingFunctionality();
        addPanningFunctionality();
    }

    private void addZoomingFunctionality() {
        webView.addEventFilter(ScrollEvent.ANY, event -> {
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

    private void addPanningFunctionality() {
        final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<>();

        webView.setOnMousePressed(event -> {
            lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
        });

        webView.setOnMouseDragged(event -> {
            double deltaX = event.getX() - lastMouseCoordinates.get().getX();
            double deltaY = event.getY() - lastMouseCoordinates.get().getY();

            double width = scrollPane.getContent().getBoundsInLocal().getWidth();
            double vValue = scrollPane.getVvalue();
            double hValue = scrollPane.getHvalue();

            scrollPane.setHvalue(hValue - deltaX / width);
            scrollPane.setVvalue(vValue - deltaY / width);

            lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
        });
    }
}
