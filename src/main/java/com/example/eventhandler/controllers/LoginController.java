package com.example.eventhandler.controllers;

import com.example.eventhandler.AlertBox;
import com.example.eventhandler.UserSession;
import com.example.eventhandler.application.EventHandlerApplication;
import com.example.eventhandler.models.personne.Participant;
import com.example.eventhandler.persistence.Deserialization;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class LoginController {
    public PasswordField passwordField;
    public TextField usernameField;

    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException {
        String password = passwordField.getText().trim();
        String id = usernameField.getText().trim();

        Participant p = Deserialization.getParticipant(id);
        if(p == null || !p.getPassword().equals(password)){
            AlertBox.showAlert("Error","mot de passe ou nom d'utilisateur incorrect.", Alert.AlertType.ERROR);
            return;
        }else {
            UserSession.getInstance().setUser(p);
            EventHandlerApplication.setRoot("menuView");
        }
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("hello-view");
    }
}
