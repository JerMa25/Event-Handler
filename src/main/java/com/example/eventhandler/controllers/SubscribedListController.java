package com.example.eventhandler.controllers;

import com.example.eventhandler.UserSession;
import com.example.eventhandler.application.EventHandlerApplication;
import com.example.eventhandler.models.evenement.Evenement;
import com.example.eventhandler.models.personne.Organisateur;
import com.example.eventhandler.models.personne.Participant;
import com.example.eventhandler.persistence.Deserialization;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SubscribedListController implements Initializable {

    public ListView<String> listViewSubscribedEvenements;
    private List<Evenement> subscribedEvents = new ArrayList<>(); // Pour stocker les événements réels

    @Override
    public void initialize(URL location, ResourceBundle resources){
        HashMap<String, Integer> participantEvent = Deserialization.getParticipantsAuxEvenements();
        List<Evenement> evenements = new ArrayList<>();
        ObservableList<String> evenementsDisplay = FXCollections.observableArrayList();

        // Get user's events in evenements
        for(Map.Entry<String, Integer> entry : participantEvent.entrySet()){
            if(entry.getKey().equals(UserSession.getInstance().getUser().getId())){
                Evenement event = Deserialization.getEvenement(entry.getValue());
                if(event != null) {
                    evenements.add(event);
                    subscribedEvents.add(event); // Stocker pour la sélection
                    evenementsDisplay.add(formatEventDisplay(event));
                }
            }
        }

        // Personnaliser l'affichage des cellules de la ListView
        listViewSubscribedEvenements.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    int index = getIndex();
                    if (index >= 0 && index < subscribedEvents.size()) {
                        Evenement evenement = subscribedEvents.get(index);

                        // Créer l'affichage personnalisé
                        VBox eventCard = createSubscribedEventCard(evenement);

                        // Permettre la sélection
                        eventCard.setOnMouseClicked(e -> {
                            listViewSubscribedEvenements.getSelectionModel().select(index);
                            listViewSubscribedEvenements.getFocusModel().focus(index);
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
        listViewSubscribedEvenements.setItems(evenementsDisplay);

        // Style global de la ListView
        listViewSubscribedEvenements.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");

        // Message si aucun événement
        if(evenements.isEmpty()) {
            Label noEventsLabel = new Label("Vous n'êtes inscrit à aucun événement.");
            noEventsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-padding: 20;");
            listViewSubscribedEvenements.setPlaceholder(noEventsLabel);
        }
    }

    private String formatEventDisplay(Evenement evenement) {
        StringBuilder eventInfo = new StringBuilder();

        // Trouver l'organisateur
        String organisateurNom = "";
        
        List<Organisateur> organisateurs = new ArrayList<>();
        List<Participant> participants = Deserialization.getAllParticipants();
        for (Participant participant : participants){
            if(participant instanceof Organisateur){
                organisateurs.add((Organisateur) participant);
            }
        }
        
        for (Organisateur organisateur : organisateurs){
            for (Evenement e : organisateur.getEvenementsOrganises()){
                if(e.getId() == evenement.getId()){
                    organisateurNom = organisateur.getId();
                    break;
                }
            }
        }

        eventInfo.append("Événement: ").append(evenement.getNom()).append(" | ");
        eventInfo.append("Lieu: ").append(evenement.getLieu()).append(" | ");
        eventInfo.append("Date: ").append(evenement.getDate().getDayOfMonth()).append(" ")
                 .append(evenement.getDate().getMonth()).append(" ").append(evenement.getDate().getYear())
                 .append(" | ");
        eventInfo.append("Heure: ").append(evenement.getDate().getHour()).append(":").append(evenement.getDate().getMinute())
                .append(" | ");
        eventInfo.append("Durée: ").append(evenement.getDuration()).append("h | ");
        eventInfo.append("Organisateur: ").append(organisateurNom);

        return eventInfo.toString();
    }

    private VBox createSubscribedEventCard(Evenement evenement) {
        String organisateurNom = "";

        List<Organisateur> organisateurs = new ArrayList<>();
        List<Participant> participants = Deserialization.getAllParticipants();
        for (Participant participant : participants){
            if(participant instanceof Organisateur){
                organisateurs.add((Organisateur) participant);
            }
        }

        for (Organisateur organisateur : organisateurs){
            for (Evenement e : organisateur.getEvenementsOrganises()){
                if(e.getId() == evenement.getId()){
                    organisateurNom = organisateur.getId();
                    break;
                }
            }
        }

        VBox eventCard = new VBox(8);
        eventCard.setStyle("-fx-background-color: #e8f5e8; -fx-border-color: #28a745; " +
                "-fx-border-radius: 8; -fx-background-radius: 8; " +
                "-fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);");

        // Titre de l'événement avec badge "Inscrit"
        HBox titleRow = new HBox(10);
        Label eventTitle = new Label(evenement.getNom());
        eventTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #155724;");

        Label subscribedBadge = new Label("✓ Inscrit");
        subscribedBadge.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; " +
                "-fx-padding: 2 8 2 8; -fx-background-radius: 12; -fx-font-size: 10px;");

        titleRow.getChildren().addAll(eventTitle, subscribedBadge);

        // Première ligne d'informations : Lieu et Date
        HBox infoRow1 = new HBox(20);
        Label locationLabel = new Label("Lieu: " + evenement.getLieu());
        locationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #495057;");

        Label dateLabel = new Label("Date: " + evenement.getDate().getDayOfMonth() + " " + evenement.getDate().getMonth() + " " + evenement.getDate().getYear());
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #495057;");

        infoRow1.getChildren().addAll(locationLabel, dateLabel);

        // Deuxième ligne d'informations : Heure et Durée
        HBox infoRow2 = new HBox(20);
        Label timeLabel = new Label("Heure: " + evenement.getDate().getHour() +":"+evenement.getDate().getMinute());
        timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #495057;");

        Label durationLabel = new Label("Durée: " + evenement.getDuration() + " heure(s)");
        durationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #495057;");

        infoRow2.getChildren().addAll(timeLabel, durationLabel);

        // Troisième ligne : Organisateur
        Label organizerLabel = new Label("Organisateur: " + organisateurNom);
        organizerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d; -fx-font-weight: bold;");

        // Bouton de désinscription
        Button unsubscribeButton = new Button("Se désinscrire");
        unsubscribeButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; " +
                "-fx-padding: 8 15 8 15; -fx-background-radius: 5; -fx-font-size: 11px;");

        eventCard.getChildren().addAll(titleRow, infoRow1, infoRow2, organizerLabel, unsubscribeButton);

        return eventCard;
    }


    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("MyEventsView");
    }

    public void unsubscribeEvenementClick(ActionEvent actionEvent) {
    }
}
