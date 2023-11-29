module h4131 {
    requires transitive javafx.graphics;
    requires transitive javafx.web;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens h4131.app to javafx.fxml;
    opens h4131.view to javafx.fxml, javafx.web;
    opens h4131.xml to java.xml, javafx.fxml, javafx.controls;
    
    exports h4131.app;
    exports h4131.view;
    exports h4131.controller;
    exports h4131.xml;
    exports h4131.model;
}
