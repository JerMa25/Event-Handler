package com.example.eventhandler.persistence;

import com.example.eventhandler.exceptions.EvenementDejaExistantException;
import com.example.eventhandler.exceptions.ParticipantDejaExistantException;
import com.example.eventhandler.models.evenement.Evenement;
import com.example.eventhandler.models.personne.Participant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Serialization {
    private static final String PARTICIPANT_DATA_FILE_PATH = "/home/jerma/Desktop/Cours 3GI/Semestre 2/POO 2/TP/Event-Handler/ParticipantData.json";
    private static final String EVENT_DATA_FILE_PATH = "/home/jerma/Desktop/Cours 3GI/Semestre 2/POO 2/TP/Event-Handler/EvenementData.json";
    private static final String PARTICIPANT_EVENT_FILE_PATH = "/home/jerma/Desktop/Cours 3GI/Semestre 2/POO 2/TP/Event-Handler/ParticipantsAuxEvenements.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // Enable pretty printing
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Register JavaTime module for LocalDateTime and Duration
        mapper.registerModule(new JavaTimeModule());

        // Disable writing dates as timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Configure polymorphic type handling
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        // Don't fail on unknown properties during deserialization
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Enable default typing for polymorphic serialization (if needed)
        // mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
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
            /*mapper.writerFor(new TypeReference<List<Participant>>() {}).withAttribute("type","Participant");    I'll be back*/
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

    public static void saveAllParticipant(List<Participant> participants) {
        try {
            mapper.writeValue(new File(PARTICIPANT_DATA_FILE_PATH), participants);
            System.out.println("Successfully saved " + participants.size() + " participants.");
        } catch (Exception e) {
            System.err.println("Error saving participants to file:");
            e.printStackTrace();
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

            // Check if location and datetime coincides
            boolean hasLocationDateTimeConflict = evenements.stream().anyMatch(existing ->
                    existing.getLieu().equals(evenement.getLieu()) && hasTimeConflict(existing, evenement));

            if (hasLocationDateTimeConflict) {
                // Pass the existing events list to your exception constructor
                throw new EvenementDejaExistantException();
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

        } catch (EvenementDejaExistantException e) {
            // Re-throw the specific exception so the caller can handle it
            throw e;
        } catch (Exception e) {
            // Only catch other exceptions, not our custom one
            System.err.println("Error during event addition:");
            e.printStackTrace();
            return false;
        }
    }

    private static boolean hasTimeConflict(Evenement existingEvent, Evenement newEvent) {
        var existingStart = existingEvent.getDate();
        var newStart = newEvent.getDate();

        // Check if events are on the same date
        if (!existingStart.toLocalDate().equals(newStart.toLocalDate())) {
            return false;
        }

        var existingEnd = existingStart.plus(existingEvent.getDuration());
        var newEnd = newStart.plus(newEvent.getDuration());

        // Check for overlap
        boolean newStartsWithinExisting = !newStart.isBefore(existingStart) && newStart.isBefore(existingEnd);
        boolean existingStartsWithinNew = !existingStart.isBefore(newStart) && existingStart.isBefore(newEnd);

        return newStartsWithinExisting || existingStartsWithinNew;
    }

    public static boolean addParticipantsAuxEvenements(Participant participant, Evenement evenement) throws EvenementDejaExistantException {
        try {
            File outputFile = new File(PARTICIPANT_EVENT_FILE_PATH);
            HashMap<String, Integer> participantEvent = new HashMap<>();

            // Read existing data if file exists
            if (outputFile.exists() && outputFile.length() > 0) {
                try {
                    String existingContent = Files.readString(outputFile.toPath(), StandardCharsets.UTF_8);
                    participantEvent = mapper.readValue(existingContent, new TypeReference<HashMap<String, Integer>>() {});
                    System.out.println("Existing data loaded from file.");
                } catch (Exception e) {
                    System.out.println("Could not read existing file or file is not a valid JSON. Starting fresh.");
                    participantEvent = new HashMap<>();
                }
            } else {
                // File doesn't exist or is empty, create new HashMap
                System.out.println("File doesn't exist or is empty. Creating new registration file.");
                participantEvent = new HashMap<>();

                // Create parent directories if they don't exist
                outputFile.getParentFile().mkdirs();
            }

            // Check if participant is already registered for this event
            String participantKey = participant.getId();
            if (participantEvent.containsKey(participantKey) && participantEvent.get(participantKey).equals(evenement.getId())) {
                System.out.println("Participant " + participant.getId() + " is already registered for event " + evenement.getNom());
                return false; // Already registered
            }

            // Add the registration
            participantEvent.put(participantKey, evenement.getId());

            // Write updated map to file
            String jsonString = mapper.writeValueAsString(participantEvent);
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(jsonString.getBytes(StandardCharsets.UTF_8));
            }

            System.out.println("Registration of participant '" + participant.getId() + "' to event '" + evenement.getNom() + "' successful!");
            return true;

        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}