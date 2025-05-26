package com.example.eventhandler.exceptions;

import com.example.eventhandler.models.evenement.Evenement;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Exception thrown when attempting to create an event that conflicts with an existing one
 * (same location or overlapping date/time)
 */
public class EvenementDejaExistantException extends RuntimeException {

    public EvenementDejaExistantException() {
        super("Conflit détecté avec un événement existant");
    }
}