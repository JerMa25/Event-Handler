package com.example.eventhandler.controllers;

import com.example.eventhandler.application.EventHandlerApplication;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MenuController {
    public void onSubscriptionClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("subscribeEventView");
    }

    public void onCreateEventClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("eventTypeView");
    }

    public void onNotificationsClick(ActionEvent actionEvent) {
    }

    public void onMyEventsClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("MyEventsView");
    }
}
