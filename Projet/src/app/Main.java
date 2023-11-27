package app;

import javafx.application.Application;
import javafx.stage.Stage;
import view.WindowBuilder;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        WindowBuilder windowBuilder = new WindowBuilder(stage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
