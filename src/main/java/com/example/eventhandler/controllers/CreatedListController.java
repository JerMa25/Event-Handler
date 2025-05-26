package com.example.eventhandler.controllers;

import com.example.eventhandler.UserSession;
import com.example.eventhandler.application.EventHandlerApplication;
import com.example.eventhandler.models.evenement.Evenement;
import com.example.eventhandler.models.personne.Organisateur;
import com.example.eventhandler.models.personne.Participant;
import com.example.eventhandler.persistence.Deserialization;
import com.example.eventhandler.persistence.Serialization;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CreatedListController implements Initializable {

    public ListView<String> listViewCreatedEvenements;
    private List<Evenement> createdEvents = new ArrayList<>(); // Pour stocker les événements réels

    @Override
    public void initialize(URL location, ResourceBundle resources){
        List<Evenement> evenements = new ArrayList<>();
        List<Participant> participants = Deserialization.getAllParticipants();
        ObservableList<String> evenementsDisplay = FXCollections.observableArrayList();

        // Get all events organised by user
        Organisateur organisateur = null;
        for(Participant participant : participants){
            if(participant instanceof Organisateur && Objects.equals(participant.getId(), UserSession.getInstance().getUser().getId())){
                organisateur = (Organisateur) participant;
                break;
            }
        }

        if(organisateur != null && organisateur.getEvenementsOrganises() != null) {
            evenements = organisateur.getEvenementsOrganises();
            createdEvents.addAll(evenements); // Stocker pour la sélection

            // Créer l'affichage pour chaque événement
            for(Evenement event : evenements) {
                evenementsDisplay.add(formatEventDisplay(event));
            }
        }

        // Personnaliser l'affichage des cellules de la ListView
        listViewCreatedEvenements.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    int index = getIndex();
                    if (index >= 0 && index < createdEvents.size()) {
                        Evenement evenement = createdEvents.get(index);

                        // Créer l'affichage personnalisé
                        VBox eventCard = createCreatedEventCard(evenement);

                        // Permettre la sélection
                        eventCard.setOnMouseClicked(e -> {
                            listViewCreatedEvenements.getSelectionModel().select(index);
                            listViewCreatedEvenements.getFocusModel().focus(index);
                        });

                        setGraphic(eventCard);
                        setText(null);
                    } else {
                        setGraphic(null);
                        setText(item);
                    }
                }
            }
        });

        // Définir les éléments de la ListView
        listViewCreatedEvenements.setItems(evenementsDisplay);

        // Style global de la ListView
        listViewCreatedEvenements.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");

        // Message si aucun événement créé
        if(evenements.isEmpty()) {
            Label noEventsLabel = new Label("Vous n'avez créé aucun événement.");
            noEventsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-padding: 20;");
            listViewCreatedEvenements.setPlaceholder(noEventsLabel);
        }
    }

    private String formatEventDisplay(Evenement evenement) {
        StringBuilder eventInfo = new StringBuilder();

        eventInfo.append("Événement: ").append(evenement.getNom()).append(" | ");
        eventInfo.append("Lieu: ").append(evenement.getLieu()).append(" | ");
        eventInfo.append("Date: ").append(formatDate(evenement.getDate())).append(" | ");
        eventInfo.append("Heure: ").append(formatTime(evenement.getDate())).append(" | ");
        eventInfo.append("Durée: ").append(evenement.getDuration()).append("h");

        return eventInfo.toString();
    }

    private VBox createCreatedEventCard(Evenement evenement) {
        VBox eventCard = new VBox(8);
        eventCard.setStyle("-fx-background-color: #fff3cd; -fx-border-color: #ffc107; " +
                "-fx-border-radius: 8; -fx-background-radius: 8; " +
                "-fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);");

        // Titre de l'événement avec badge "Organisateur"
        HBox titleRow = new HBox(10);
        Label eventTitle = new Label(evenement.getNom());
        eventTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #856404;");

        Label organizerBadge = new Label("Organisateur");
        organizerBadge.setStyle("-fx-background-color: #ffc107; -fx-text-fill: #212529; " +
                "-fx-padding: 2 8 2 8; -fx-background-radius: 12; -fx-font-size: 10px; -fx-font-weight: bold;");

        titleRow.getChildren().addAll(eventTitle, organizerBadge);

        // Première ligne d'informations : Lieu et Date
        HBox infoRow1 = new HBox(20);
        Label locationLabel = new Label("Lieu: " + evenement.getLieu());
        locationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #495057;");

        Label dateLabel = new Label("Date: " + formatDate(evenement.getDate()));
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #495057;");

        infoRow1.getChildren().addAll(locationLabel, dateLabel);

        // Deuxième ligne d'informations : Heure et Durée
        HBox infoRow2 = new HBox(20);
        Label timeLabel = new Label("Heure: " + formatTime(evenement.getDate()));
        timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #495057;");

        Label durationLabel = new Label("Durée: " + evenement.getDuration() + " heure(s)");
        durationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #495057;");

        infoRow2.getChildren().addAll(timeLabel, durationLabel);

        // Statistiques (nombre de participants)
        Label statsLabel = new Label("Nombre de Participants inscrits: " + getParticipantCount(evenement));
        statsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d; -fx-font-weight: bold;");

        // Boutons d'action
        HBox buttonsRow = new HBox(10);

        Button viewParticipantsButton = new Button("Voir participants");
        viewParticipantsButton.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; " +
                "-fx-padding: 6 12 6 12; -fx-background-radius: 5; -fx-font-size: 11px;");
        viewParticipantsButton.setOnAction(e -> viewEventParticipants(evenement));

        Button editButton = new Button("Modifier");
        editButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; " +
                "-fx-padding: 6 12 6 12; -fx-background-radius: 5; -fx-font-size: 11px;");
        editButton.setOnAction(e -> editEvent(evenement));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; " +
                "-fx-padding: 6 12 6 12; -fx-background-radius: 5; -fx-font-size: 11px;");
        deleteButton.setOnAction(e -> deleteEvent(evenement));

        buttonsRow.getChildren().addAll(viewParticipantsButton, editButton, deleteButton);

        eventCard.getChildren().addAll(titleRow, infoRow1, infoRow2, statsLabel, buttonsRow);

        return eventCard;
    }

    private int getParticipantCount(Evenement evenement) {
        try {
            HashMap<String, Integer> participantEvent = Deserialization.getParticipantsAuxEvenements();
            int count = 0;

            for(Map.Entry<String, Integer> entry : participantEvent.entrySet()) {
                if(entry.getValue().equals(evenement.getId())) {
                    count++;
                }
            }

            return count;
        } catch (Exception e) {
            System.err.println("Erreur lors du comptage des participants: " + e.getMessage());
            return 0;
        }
    }

    private void viewEventParticipants(Evenement evenement) {
        try {
            HashMap<String, Integer> participantEvent = Deserialization.getParticipantsAuxEvenements();
            List<Participant> allParticipants = Deserialization.getAllParticipants();
            List<String> eventParticipants = new ArrayList<>();

            // Trouver tous les participants inscrits à cet événement
            for(Map.Entry<String, Integer> entry : participantEvent.entrySet()) {
                if(entry.getValue().equals(evenement.getId())) {
                    // Trouver le participant correspondant
                    for(Participant p : allParticipants) {
                        if(p.getId().equals(entry.getKey())) {
                            eventParticipants.add(p.getNom() + " (" + p.getEmail() + ")");
                            break;
                        }
                    }
                }
            }

            // Afficher la liste des participants
            Alert participantsAlert = new Alert(Alert.AlertType.INFORMATION);
            participantsAlert.setTitle("Participants inscrits");
            participantsAlert.setHeaderText("Événement: " + evenement.getNom());

            if(eventParticipants.isEmpty()) {
                participantsAlert.setContentText("Aucun participant inscrit pour le moment.");
            } else {
                participantsAlert.setContentText("Participants inscrits (" + eventParticipants.size() + "):\n\n" +
                        String.join("\n", eventParticipants));
            }

            participantsAlert.showAndWait();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la récupération des participants: " + e.getMessage());
        }
    }

    private void editEvent(Evenement evenement) {
        // Placeholder pour la fonctionnalité de modification
        showAlert("Information", "Fonctionnalité de modification à implémenter.\nÉvénement: " + evenement.getNom());
    }

    private void deleteEvent(Evenement evenement) {
        try {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation de suppression");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer l'événement \"" +
                    evenement.getNom() + "\" ?\n\nCette action est irréversible et supprimera également toutes les inscriptions.");

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur inattendue s'est produite : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
        return date.format(formatter);
    }

    private String formatTime(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return date.format(formatter);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("MyEventsView");
    }

    public void deleteEvenementClick(ActionEvent actionEvent) {
        try {
            int selectedIndex = listViewCreatedEvenements.getSelectionModel().getSelectedIndex();

            if (selectedIndex < 0 || selectedIndex >= createdEvents.size()) {
                showAlert("Sélection requise", "Veuillez sélectionner un événement pour le supprimer.");
                return;
            }

            Evenement selectedEvent = createdEvents.get(selectedIndex);
            deleteEvent(selectedEvent);

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la suppression : " + e.getMessage());
            e.printStackTrace();
        }
    }
}