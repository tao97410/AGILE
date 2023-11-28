package h4131.app;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import h4131.view.WindowBuilder;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        WindowBuilder windowBuilder = new WindowBuilder(stage);
    }
}