package com.example.eventhandler.controllers;

import com.example.eventhandler.AlertBox;
import com.example.eventhandler.application.EventHandlerApplication;
import com.example.eventhandler.exceptions.ParticipantDejaExistantException;
import com.example.eventhandler.models.personne.Participant;
import com.example.eventhandler.persistence.Deserialization;
import com.example.eventhandler.persistence.Serialization;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class createAccountController {
    public TextField fullNameField;
    public TextField emailField;
    public TextField usernameField;
    public Button signUpButton;
    public TextField passwordField;

    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        String id = usernameField.getText().trim();
        String nom = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Basic validation
        if (id.isEmpty() || nom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            AlertBox.showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }

        // Create new participant
        Participant p = new Participant(id, nom, email, password);

        boolean success;

        try {

            // Add participant using Serialization class
            success = Serialization.addParticipant(p);

        }catch (ParticipantDejaExistantException e) {
            AlertBox.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            return;

        }if (success) {
            System.out.println("Adding new participant to existing records.");
            AlertBox.showAlert("Success", "Account created successfully!\nUsername: " + id, Alert.AlertType.INFORMATION);
            System.out.println("New participant count: " + Deserialization.getParticipantCount());
        } else {
            AlertBox.showAlert("Error", "Failed to create account. Please try again.", Alert.AlertType.ERROR);
        }

        EventHandlerApplication.setRoot("loginView");
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("hello-view");
    }
}