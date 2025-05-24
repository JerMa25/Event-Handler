package com.example.eventhandler.exceptions;

import com.example.eventhandler.models.personne.Participant;
import java.util.List;

/**
 * Exception thrown when attempting to create a participant with a username or email that already exists
 */
public class ParticipantDejaExistantException extends RuntimeException {

    public ParticipantDejaExistantException(Participant p, List<Participant> participants) {
        super(createMessage(p, participants));
    }

    private static String createMessage(Participant p, List<Participant> participants) {

        // Check if username exists
        if (participants.stream().anyMatch(existing -> existing.getId().equals(p.getId()))) {
            return String.format("Username '%s' already exists. Please choose a different username.", p.getId());
        }

        return String.format("Email '%s' already exists. Please choose a different email.", p.getEmail());
    }
}