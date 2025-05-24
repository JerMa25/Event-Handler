package com.example.eventhandler.persistence;

import com.example.eventhandler.models.personne.Participant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Deserialization {
    private static final String PARTICIPANT_DATA_FILE_PATH = "/home/jerma/Desktop/Cours 3GI/Semestre 2/POO 2/TP/Event-Handler/ParticipantData.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Reads all participants from the JSON file
     * @return List of all participants, empty list if no data or error
     */
    public static List<Participant> getAllParticipants() {
        try {
            File file = new File(PARTICIPANT_DATA_FILE_PATH);

            // Check if file exists and has content
            if (!file.exists() || file.length() == 0) {
                System.out.println("No data file found or file is empty.");
                return new ArrayList<>();
            }

            // Read and parse JSON file
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            List<Participant> participants = mapper.readValue(content, new TypeReference<List<Participant>>() {});

            System.out.println("Successfully loaded " + participants.size() + " participants.");
            return participants;

        } catch (Exception e) {
            System.err.println("Error reading participants from file:");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Finds a participant by username (ID)
     * @param username The username to search for
     * @return Optional containing the participant if found, empty otherwise
     */
    /**
     * Recherche un participant par son username
     * @param username Le nom d'utilisateur à rechercher
     * @return Le Participant trouvé ou null si non trouvé
     */
    public static Participant getParticipant(String username) {
        List<Participant> participants = getAllParticipants();

        for (Participant participant : participants) {
            if (participant.getId().equals(username)) {
                return participant;
            }
        }

        return null; // Aucun participant trouvé avec ce username
    }

    /**
     * Gets the total number of participants
     * @return The number of participants in the file
     */
    public static int getParticipantCount() {
        return getAllParticipants().size();
    }
}