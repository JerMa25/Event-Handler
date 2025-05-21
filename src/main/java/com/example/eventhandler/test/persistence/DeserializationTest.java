package com.example.eventhandler.test.persistence;

import com.example.eventhandler.models.personne.Participant;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeserializationTest {

    public static void main(String[] args) {
        // Path to the JSON file created by SerializationTest
        String jsonFilePath = "Data.json";

        try {
            // Read JSON content from file
            String jsonContent = Files.readString(
                    Path.of(jsonFilePath),
                    StandardCharsets.UTF_8
            );

            // Create ObjectMapper instance
            ObjectMapper mapper = new ObjectMapper();

            // Deserialize JSON to Participant object
            Participant participant = mapper.readValue(jsonContent, Participant.class);

            // Print the deserialized object
            System.out.println("Successfully deserialized Participant:");
            System.out.println("Username: " + participant.getId());
            System.out.println("Full Name: " + participant.getNom());
            System.out.println("Email: " + participant.getEmail());
            System.out.println("Password: "+participant.getPassword());

        } catch (Exception e) {
            System.err.println("Error during deserialization:");
            e.printStackTrace();
        }
    }
}