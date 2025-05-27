package com.example.eventhandler.controllers;

import com.example.eventhandler.AlertBox;
import com.example.eventhandler.EventType;
import com.example.eventhandler.UserSession;
import com.example.eventhandler.application.EventHandlerApplication;
import com.example.eventhandler.exceptions.EvenementDejaExistantException;
import com.example.eventhandler.models.evenement.Concert;
import com.example.eventhandler.models.evenement.Conference;
import com.example.eventhandler.models.evenement.Evenement;
import com.example.eventhandler.models.personne.Intervenant;
import com.example.eventhandler.models.personne.Organisateur;
import com.example.eventhandler.models.personne.Participant;
import com.example.eventhandler.persistence.Deserialization;
import com.example.eventhandler.persistence.Serialization;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateEvenementController implements Initializable {
    public TextField nomField;
    public TextField dateField;
    public TextField locationField;
    public TextField maxCapacityField;
    public TextField durationField;
    public TextField artisteField;
    public TextField themeField;
    public TextField gernreMusicalField;
    public TextField intervenantsField;

    public Label ArtisteLabel;
    public Label ThemeLabel;
    public Label GenremusicalLabel;
    public Label IntervenantsLabel;

    int type = EventType.getInstance().getType();

    // French day names mapping
    private static final Map<String, String> FRENCH_DAYS = new HashMap<>();
    static {
        FRENCH_DAYS.put("Lundi", "Monday");
        FRENCH_DAYS.put("Mardi", "Tuesday");
        FRENCH_DAYS.put("Mercredi", "Wednesday");
        FRENCH_DAYS.put("Jeudi", "Thursday");
        FRENCH_DAYS.put("Vendredi", "Friday");
        FRENCH_DAYS.put("Samedi", "Saturday");
        FRENCH_DAYS.put("Dimanche", "Sunday");
    }

    // French month names mapping
    private static final Map<String, String> FRENCH_MONTHS = new HashMap<>();
    static {
        FRENCH_MONTHS.put("Janvier", "January");
        FRENCH_MONTHS.put("Février", "February");
        FRENCH_MONTHS.put("Mars", "March");
        FRENCH_MONTHS.put("Avril", "April");
        FRENCH_MONTHS.put("Mai", "May");
        FRENCH_MONTHS.put("Juin", "June");
        FRENCH_MONTHS.put("Juillet", "July");
        FRENCH_MONTHS.put("Août", "August");
        FRENCH_MONTHS.put("Septembre", "September");
        FRENCH_MONTHS.put("Octobre", "October");
        FRENCH_MONTHS.put("Novembre", "November");
        FRENCH_MONTHS.put("Décembre", "December");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (type == 1) { // Concert
            ArtisteLabel.setVisible(true);
            GenremusicalLabel.setVisible(true);
            artisteField.setVisible(true);
            gernreMusicalField.setVisible(true);

        } else { // Conférence
            ThemeLabel.setVisible(true);
            IntervenantsLabel.setVisible(true);
            themeField.setVisible(true);
            intervenantsField.setVisible(true);

        }
    }


    private List<Intervenant> validateAndCreateIntervenants(List<String> intervenantNames) throws IllegalArgumentException {
        List<Intervenant> intervenants = new ArrayList<>();

        try {
            // Get all existing participants from JSON file
            List<Participant> allParticipants = Deserialization.getAllParticipants(); // You'll need this method

            // Create a map for faster lookup (name -> participant)
            Map<String, Participant> participantMap = new HashMap<>();
            for (Participant participant : allParticipants) {
                // Assuming Participant has a getName() method
                participantMap.put(participant.getId().toLowerCase(), participant);
            }

            // Check each intervenant name
            for (String name : intervenantNames) {
                String normalizedName = name.toLowerCase().trim();

                if (!participantMap.containsKey(normalizedName)) {
                    throw new IllegalArgumentException("Participant '" + name + "' not found in the system. Please ensure all intervenants are registered as participants first.");
                }

                // Get the participant and create an Intervenant
                Participant participant = participantMap.get(normalizedName);

                // Create Intervenant from Participant data
                // Assuming Intervenant constructor takes participant data + additional intervenant-specific data
                Intervenant intervenant = new Intervenant(
                        participant.getId(),
                        participant.getNom(),
                        participant.getEmail(),
                        participant.getPassword()
                        // Add any additional Intervenant-specific parameters if needed
                );

                intervenants.add(intervenant);
            }

            return intervenants;

        } catch (Exception e) {
            throw new IllegalArgumentException("Error validating intervenants: " + e.getMessage());
        }
    }



    public void onCreateButtonClick(ActionEvent actionEvent) {

        int id = 1;
        String nom = nomField.getText().trim();
        LocalDateTime date;
        String location = locationField.getText().trim();
        int capaciteMax;
        Duration duration;
        String artiste = artisteField.getText().trim();
        String genreMusical = gernreMusicalField.getText().trim();
        String theme = themeField.getText().trim();
        List<Intervenant> intervenants = new ArrayList<>();


        // Basic validation
        if(type == 1) {
            if (nom.isEmpty() || location.isEmpty() || artiste.isEmpty() || genreMusical.isEmpty()) {
                AlertBox.showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
                return;
            }
        } else {
            if (nom.isEmpty() || location.isEmpty() || theme.isEmpty() || intervenantsField.getText().trim().isEmpty()) {
                AlertBox.showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
                return;
            }

            // Parse intervenant names and validate them
            String[] names = intervenantsField.getText().trim().split(",\\s*");
            List<String> intervenantNames = Arrays.asList(names);

            try {
                // Validate and create Intervenant objects
                intervenants = validateAndCreateIntervenants(intervenantNames);
                System.out.println("Found " + intervenants.size() + " valid intervenants");
            } catch (IllegalArgumentException e) {
                AlertBox.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        }

        try {
            capaciteMax = Integer.parseInt(maxCapacityField.getText().trim());
        } catch (NumberFormatException e) {
            AlertBox.showAlert("Error", "maxCapacity is not an integer.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Parse French date string to LocalDateTime
            date = parseFrenchDate(dateField.getText().trim());
            System.out.println("Parsed date: " + date);
        } catch (DateTimeParseException e) {
            AlertBox.showAlert(
                    "Erreur de format",
                    "Format de date incorrect. Exemple: Lundi, 21 Août 2025 à 19:00",
                    Alert.AlertType.ERROR
            );
            return;
        }

        try {
            // Parse duration string to Duration
            duration = parseDuration(durationField.getText().trim());
            System.out.println("Parsed duration: " + duration);
        } catch (IllegalArgumentException e) {
            AlertBox.showAlert(
                    "Erreur de format",
                    "Format de durée incorrect. Exemples: 2hrs30mins, 3hrs, 1hr15mins",
                    Alert.AlertType.ERROR
            );
            return;
        }

        // Continue with your event creation logic here...
        System.out.println("Event created successfully!");
        System.out.println("Name: " + nom);
        System.out.println("Date: " + date);
        System.out.println("Location: " + location);
        System.out.println("Max Capacity: " + capaciteMax);
        System.out.println("Duration: " + duration);

        Evenement event;

        //Attribut id to event
        for(Evenement evenement : Deserialization.getAllEvenements()){
            if(evenement.getId() == id){
                id += 1;
            }
        }

        // create new event
        if(type == 1) {
            event = new Concert(id, nom, date, location, capaciteMax, duration, artiste, genreMusical);
        } else {
            event = new Conference(id, nom, date, location, capaciteMax, duration, theme, intervenants);
        }

        boolean success;

        try {
            // Add evenement using Serialization class
            success = Serialization.addEvenement(event);

        }catch (EvenementDejaExistantException e) {
            AlertBox.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            return;
        }if (success) {
            boolean isInstance = true;
            Participant p = UserSession.getInstance().getUser();
            List<Organisateur> organisateurs = Deserialization.getAllOrganisateurs();
            for(Organisateur organisateur : organisateurs){
                if(p.getId().equals(organisateur.getId())){
                    organisateur.getEvenementsOrganises().add(event);
                    Serialization.saveAllOrganisateur(organisateurs);
                    isInstance = false;
                    break;
                }
            }
            if (isInstance){
                List<Evenement> evenements = new ArrayList<>();
                evenements.add(event);
                Organisateur org = new Organisateur(p.getId(), p.getNom(), p.getEmail(), p.getPassword(), evenements);
                Serialization.addOrganisateur(org);
                System.out.println("New organisateur '"+org.getId()+"' added successfully");
            }

            System.out.println("Adding new event to existing records.");
            AlertBox.showAlert("Success", "Event created successfully!\nEventID: " + id, Alert.AlertType.INFORMATION);
            System.out.println("New event count: " + Deserialization.getEvenementCount());
        } else {
            AlertBox.showAlert("Error", "Failed to create Event. Please try again.", Alert.AlertType.ERROR);
        }

    }
    
    /**
     * Parses French date format like "Lundi, 21 Août 2025 à 19:00" to LocalDateTime
     */
    private LocalDateTime parseFrenchDate(String frenchDateStr) throws DateTimeParseException {
        try {
            // Pattern to match: "Day, DD Month YYYY à HH:MM"
            Pattern pattern = Pattern.compile("(\\w+),\\s*(\\d{1,2})\\s+(\\w+)\\s+(\\d{4})\\s+à\\s+(\\d{1,2}):(\\d{2})");
            Matcher matcher = pattern.matcher(frenchDateStr);

            if (!matcher.matches()) {
                throw new DateTimeParseException("Invalid French date format", frenchDateStr, 0);
            }

            String dayName = matcher.group(1);
            int day = Integer.parseInt(matcher.group(2));
            String monthName = matcher.group(3);
            int year = Integer.parseInt(matcher.group(4));
            int hour = Integer.parseInt(matcher.group(5));
            int minute = Integer.parseInt(matcher.group(6));

            // Convert French month to English
            String englishMonth = FRENCH_MONTHS.get(monthName);
            if (englishMonth == null) {
                throw new DateTimeParseException("Unknown French month: " + monthName, frenchDateStr, 0);
            }

            // Create the English date string and parse it
            String englishDateStr = String.format("%d %s %d %02d:%02d", day, englishMonth, year, hour, minute);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm", Locale.ENGLISH);

            return LocalDateTime.parse(englishDateStr, formatter);

        } catch (Exception e) {
            throw new DateTimeParseException("Error parsing French date: " + e.getMessage(), frenchDateStr, 0);
        }
    }


    /**
     * Parses duration format like "2hrs30mins", "3hrs", "1hr15mins" to Duration
     */
    private Duration parseDuration(String durationStr) throws IllegalArgumentException {
        try {
            // Pattern to match combinations of hours and minutes
            Pattern pattern = Pattern.compile("(?:(\\d+)hrs?)?(?:(\\d+)mins?)?");
            Matcher matcher = pattern.matcher(durationStr.toLowerCase());

            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid duration format: " + durationStr);
            }

            String hoursStr = matcher.group(1);
            String minutesStr = matcher.group(2);

            // Check if we have at least hours or minutes
            if (hoursStr == null && minutesStr == null) {
                throw new IllegalArgumentException("No valid time components found in: " + durationStr);
            }

            long totalMinutes = 0;

            if (hoursStr != null) {
                int hours = Integer.parseInt(hoursStr);
                totalMinutes += hours * 60L;
            }

            if (minutesStr != null) {
                int minutes = Integer.parseInt(minutesStr);
                totalMinutes += minutes;
            }

            if (totalMinutes <= 0) {
                throw new IllegalArgumentException("Duration must be positive: " + durationStr);
            }

            return Duration.ofMinutes(totalMinutes);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number in duration: " + durationStr);
        }
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("menuView");
    }
}