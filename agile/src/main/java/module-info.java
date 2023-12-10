module h4131 {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires itextpdf;

    opens h4131.app to javafx.fxml;
    opens h4131.view to javafx.fxml;
    opens h4131.xml to java.xml, javafx.fxml, javafx.controls;
    opens h4131.controller;
    opens h4131.model;
    opens h4131.calculus;
    
    exports h4131.app;
    exports h4131.view;
    exports h4131.controller;
    exports h4131.xml;
    exports h4131.model;
    exports h4131.observer;
}
