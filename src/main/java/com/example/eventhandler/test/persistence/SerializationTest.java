package com.example.eventhandler.test.persistence;

import com.example.eventhandler.models.personne.Participant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class SerializationTest {

    public static void main(String[] args) {
        Participant p = new Participant("JerMa", "Tchami Jerry", "tjer@gmail.com", "1234");

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