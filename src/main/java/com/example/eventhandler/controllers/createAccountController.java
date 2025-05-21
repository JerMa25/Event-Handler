package com.example.eventhandler.controllers;

import com.example.eventhandler.models.personne.Participant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class createAccountController {
    public TextField fullNameField;
    public TextField emailField;
    public TextField usernameField;
    public Button signUpButton;
    public PasswordField passwordField;


    public void onSaveButtonClick(ActionEvent actionEvent) {

        String id = usernameField.getText().trim();
        String nom = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        Participant p = new Participant(id, nom, email, password);

        /* JSON Serialization */
        try {
            // Create ObjectMapper with pretty printing
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Convert Participant to JSON string
            String jsonString = mapper.writeValueAsString(p);

            // Write JSON to file
            File outputFile = new File("/home/jerma/Desktop/Cours 3GI/Semestre 2/POO 2/TP/Event Handler/Data.json");
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(jsonString.getBytes(StandardCharsets.UTF_8));
            }

            System.out.println("Participant object serialized to JSON successfully!");
            System.out.println("JSON content:\n" + jsonString);

        } catch (Exception e) {
            System.err.println("Error during JSON serialization:");
            e.printStackTrace();
        }
    }
}
