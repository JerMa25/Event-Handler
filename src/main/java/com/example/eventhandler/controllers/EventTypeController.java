package com.example.eventhandler.controllers;

import com.example.eventhandler.EventType;
import com.example.eventhandler.application.EventHandlerApplication;
import javafx.event.ActionEvent;

import java.io.IOException;

public class EventTypeController {
    public void onConcertClick(ActionEvent actionEvent) throws IOException {
        EventType.getInstance().setType(1);
        EventHandlerApplication.setRoot("createEvenementView");
    }

    public void onConferenceClick(ActionEvent actionEvent) throws IOException {
        EventType.getInstance().setType(2);
        EventHandlerApplication.setRoot("createEvenementView");

    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("menuView");
    }
}
