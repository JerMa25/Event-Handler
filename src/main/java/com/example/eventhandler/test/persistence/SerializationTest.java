package com.example.eventhandler.test.persistence;

import com.example.eventhandler.models.personne.Participant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SerializationTest {

    public static void main(String[] args) {
        Participant p = new Participant("JerMa", "Tchami Jerry", "tjer@gmail.com", "1234");

        File outputFile = new File("/home/jerma/Desktop/Cours 3GI/Semestre 2/POO 2/TP/Event-Handler/Data.json");

        /* JSON Serialization with Append */
        try {
            // Create ObjectMapper with pretty printing
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            List<Participant> participants = new ArrayList<>();

            // Check if file exists and read existing data
            if (outputFile.exists() && outputFile.length() > 0) {
                try {
                    String existingContent = Files.readString(outputFile.toPath(), StandardCharsets.UTF_8);
                    // Parse existing JSON array
                    participants = mapper.readValue(existingContent, new TypeReference<List<Participant>>() {});
                } catch (Exception e) {
                    System.out.println("Could not read existing file or file is not a valid JSON array. Starting fresh.");
                    participants = new ArrayList<>();
                }
            }

            // Add new participant to the list
            participants.add(p);

            // Convert list to JSON string
            String jsonString = mapper.writeValueAsString(participants);

            // Write JSON array to file
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(jsonString.getBytes(StandardCharsets.UTF_8));
            }

            System.out.println("Participant object added to JSON successfully!");
            System.out.println("Total participants: " + participants.size());
            System.out.println("JSON content:\n" + jsonString);

        } catch (Exception e) {
            System.err.println("Error during JSON serialization:");
            e.printStackTrace();
        }
    }
}