module h4131 {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens h4131.app to javafx.fxml;
    opens h4131.view to javafx.fxml;
    exports h4131.app;
    exports h4131.view;
    exports h4131.controller;
}
