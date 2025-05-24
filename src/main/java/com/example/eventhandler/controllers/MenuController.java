package com.example.eventhandler.controllers;

import com.example.eventhandler.application.EventHandlerApplication;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MenuController {
    public void onSubscriptionClick(ActionEvent actionEvent) {
    }

    public void onCreateEventClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("createEvenementView");
    }

    public void onNotificationsClick(ActionEvent actionEvent) {
    }
}
