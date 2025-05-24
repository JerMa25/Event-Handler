package com.example.eventhandler.controllers;

import com.example.eventhandler.AlertBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class CreateEvenementController {
    public TextField nomField;
    public TextField dateField;
    public TextField locationField;
    public TextField maxCapacityField;
    public TextField durationField;


    public void onCreateButtonClick(ActionEvent actionEvent) {

        String nom =nomField.getText().trim();
        LocalDateTime date;
        String location = locationField.getText().trim();
        int capaciteMax;
        Duration duration;

        // Basic validation
        if (nom.isEmpty() || location.isEmpty()) {
            AlertBox.showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }

        try {
            capaciteMax = Integer.parseInt(maxCapacityField.getText().trim());
        }catch (NumberFormatException e){
            AlertBox.showAlert("Error","maxCapacity is not an integer.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Parse French date string to LocalDateTime
            date = LocalDateTime.parse(dateField.getText().trim());
            System.out.println("Raw input: " + date);
        } catch (DateTimeParseException e) {
            AlertBox.showAlert(
                    "Erreur de format",
                    "Format de date incorrect. Exemple: Mercredi, 21 Janvier 2023 à 13:30",
                    Alert.AlertType.ERROR
            );
            return;
        }


    }
}
