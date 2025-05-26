package com.example.eventhandler.controllers;

import com.example.eventhandler.application.EventHandlerApplication;
import javafx.event.ActionEvent;

import java.io.IOException;

public class myEventsController {
    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("menuView");
    }

    public void onSubscribedClick(ActionEvent actionEvent) {
    }

    public void onCreatedClick(ActionEvent actionEvent) {

    }
}
