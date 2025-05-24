package com.example.eventhandler.persistence;

import com.example.eventhandler.exceptions.EvenementDejaExistantException;
import com.example.eventhandler.exceptions.ParticipantDejaExistantException;
import com.example.eventhandler.models.evenement.Evenement;
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

public class Serialization {
    private static final String PARTICIPANT_DATA_FILE_PATH = "/home/jerma/Desktop/Cours 3GI/Semestre 2/POO 2/TP/Event-Handler/ParticipantData.json";
    private static final String EVENT_DATA_FILE_PATH = "/home/jerma/Desktop/Cours 3GI/Semestre 2/POO 2/TP/Event-Handler/EvenementData.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Adds a new participant to the JSON file
     * @param participant The participant to add
     * @return true if successful, false otherwise
     */
    public static boolean addParticipant(Participant participant) throws ParticipantDejaExistantException {
        try {
            File outputFile = new File(PARTICIPANT_DATA_FILE_PATH);
            List<Participant> participants = new ArrayList<>();

            // Read existing data if file exists
            if (outputFile.exists() && outputFile.length() > 0) {
                try {
                    String existingContent = Files.readString(outputFile.toPath(), StandardCharsets.UTF_8);
                    participants = mapper.readValue(existingContent, new TypeReference<List<Participant>>() {});
                } catch (Exception e) {
                    System.out.println("Could not read existing file or file is not a valid JSON array. Starting fresh.");
                    participants = new ArrayList<>();
                }
            }

            // Check for duplicates BEFORE adding
            boolean usernameExists = participants.stream()
                    .anyMatch(existing -> existing.getId().equals(participant.getId()));
            boolean emailExists = participants.stream()
                    .anyMatch(existing -> existing.getEmail().equalsIgnoreCase(participant.getEmail()));

            if (usernameExists || emailExists) {
                throw new ParticipantDejaExistantException(participant, participants);
            }

            // Add new participant to the list
            participants.add(participant);

            // Write updated list to file
            String jsonString = mapper.writeValueAsString(participants);
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(jsonString.getBytes(StandardCharsets.UTF_8));
            }

            System.out.println("Participant added successfully!");
            return true;

        }catch (ParticipantDejaExistantException e) {
            throw e;
        }catch (Exception e) {
            System.err.println("Error during participant addition:");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addEvenement(Evenement evenement) throws EvenementDejaExistantException {
        try {
            File outputFile = new File(EVENT_DATA_FILE_PATH);
            List<Evenement> evenements = new ArrayList<>();

            // Read existing data if file exists
            if (outputFile.exists() && outputFile.length() > 0) {
                try {
                    String existingContent = Files.readString(outputFile.toPath(), StandardCharsets.UTF_8);
                    evenements = mapper.readValue(existingContent, new TypeReference<List<Evenement>>() {});
                } catch (Exception e) {
                    System.out.println("Could not read existing file or file is not a valid JSON array. Starting fresh.");
                    evenements = new ArrayList<>();
                }
            }

            // Check for duplicates BEFORE adding
            boolean locationExists = evenements.stream()
                    .anyMatch(existing -> existing.getLieu().equals(evenement.getLieu()));
            boolean dateTimeExists = evenements.stream()
                    .anyMatch(existing -> existing.getDate().equals(evenement.getDate()));

            if (locationExists || dateTimeExists) {
                throw new EvenementDejaExistantException(evenement, evenements);
            }

            // Add new event to the list
            evenements.add(evenement);

            // Write updated list to file
            String jsonString = mapper.writeValueAsString(evenements);
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(jsonString.getBytes(StandardCharsets.UTF_8));
            }

            System.out.println("Event added successfully!");
            return true;

        } catch (Exception e) {
            System.err.println("Error during event addition:");
            e.printStackTrace();
            return false;
        }
    }
}