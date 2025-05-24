package com.example.eventhandler.controllers;

import com.example.eventhandler.application.EventHandlerApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class NewAccount_LoginController {
    @FXML
    private Label welcomeText;

    public void onCreateAccountClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("createAccountView");
    }

    public void onLoginClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("loginView");
    }
}