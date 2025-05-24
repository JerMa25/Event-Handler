package com.example.eventhandler.exceptions;

import com.example.eventhandler.models.evenement.Evenement;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Exception thrown when attempting to create an event that conflicts with an existing one
 * (same location and overlapping date/time)
 */
public class EvenementDejaExistantException extends RuntimeException {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, MMMM dd yyyy 'at' HH:mm").withLocale(Locale.FRENCH);

    public EvenementDejaExistantException(Evenement newEvent, List<Evenement> existingEvents) {
        super(createMessage(newEvent, existingEvents));
    }

    private static String createMessage(Evenement newEvent, List<Evenement> existingEvents) {
        // Check for existing event at same location and overlapping time
        boolean conflictExists = existingEvents.stream()
                .anyMatch(existing ->
                        existing.getLieu().equalsIgnoreCase(newEvent.getLieu()) &&
                                existing.getDate().isEqual(newEvent.getDate()));

        if (conflictExists) {
            return String.format(
                    "Un événement existe déjà à ce lieu '%s' à la date/heure suivante: %s",
                    newEvent.getLieu(),
                    newEvent.getDate().format(DATE_FORMATTER)
            );
        }

        return "Conflit détecté avec un événement existant";
    }
}