module com.example.eventhandler {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    exports com.example.eventhandler.models.personne;

    opens com.example.eventhandler to javafx.fxml;
    exports com.example.eventhandler.application;
    opens com.example.eventhandler.application to javafx.fxml;
    exports com.example.eventhandler.controllers;
    opens com.example.eventhandler.controllers to javafx.fxml;
}