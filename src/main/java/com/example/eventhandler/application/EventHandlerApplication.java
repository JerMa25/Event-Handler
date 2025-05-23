package com.example.eventhandler.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.beans.EventHandler;
import java.io.IOException;

public class EventHandlerApplication extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EventHandlerApplication.class.getResource("/com/example/eventhandler/hello-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("Event Handler");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException{
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(EventHandlerApplication.class.getResource("/com/example/eventhandler/"+fxml+".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}