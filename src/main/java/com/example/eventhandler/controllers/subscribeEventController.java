package com.example.eventhandler.controllers;

import com.example.eventhandler.UserSession;
import com.example.eventhandler.application.EventHandlerApplication;
import com.example.eventhandler.models.personne.Organisateur;
import com.example.eventhandler.models.personne.Participant;
import com.example.eventhandler.models.evenement.Evenement;
import com.example.eventhandler.models.evenement.Conference;
import com.example.eventhandler.models.evenement.Concert;
import com.example.eventhandler.persistence.Deserialization;
import com.example.eventhandler.persistence.Serialization;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.eventhandler.AlertBox.showAlert;

public class subscribeEventController implements Initializable{
    public ListView<String> listViewEvenements;
    private final List<Evenement> evenementsList = new ArrayList<>(); // Pour stocker les événements réels

    @Override
    public void initialize(URL location, ResourceBundle resources){
        List<Participant> participants = Deserialization.getAllParticipants();
        List<Organisateur> organisateurs = new ArrayList<>();
        ObservableList<String> evenementsDisplay = FXCollections.observableArrayList();

        // Récupérer tous les organisateurs
        for (Participant participant : participants) {
            if (participant instanceof Organisateur) {
                organisateurs.add((Organisateur) participant);
            }
        }

        // Parcourir tous les organisateurs et leurs événements
        for (Organisateur organisateur : organisateurs) {
            List<Evenement> evenementsOrganises = organisateur.getEvenementsOrganises();

            for (Evenement evenement : evenementsOrganises) {
                evenementsList.add(evenement); // Stocker l'événement réel
                evenementsDisplay.add(formatEventDisplay(evenement, organisateur));
            }
        }

        // Personnaliser l'affichage des cellules de la ListView
        listViewEvenements.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // Récupérer l'événement correspondant
                    int index = getIndex();
                    if (index >= 0 && index < evenementsList.size()) {
                        Evenement evenement = evenementsList.get(index);

                        // Trouver l'organisateur de cet événement
                        Organisateur organisateur = null;
                        for (Organisateur org : organisateurs) {
                            if (org.getEvenementsOrganises().contains(evenement)) {
                                organisateur = org;
                                break;
                            }
                        }

                        // Créer l'affichage personnalisé
                        VBox eventCard = createEventCard(evenement, organisateur);
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
        listViewEvenements.setItems(evenementsDisplay);

        // Style global de la ListView
        listViewEvenements.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
    }

    private String formatEventDisplay(Evenement evenement, Organisateur organisateur) {
        StringBuilder eventInfo = new StringBuilder();
        eventInfo.append("Organisateur: ").append(organisateur.getNom()).append(" | ");
        eventInfo.append("Événement: ").append(evenement.getNom()).append(" | ");
        eventInfo.append("Date: ").append(evenement.getDate().getDayOfMonth())
                .append(" ").append(evenement.getDate().getMonth())
                .append(" ").append(evenement.getDate().getYear()).append(" | ");
        eventInfo.append("Time: ").append(evenement.getDate().getHour())
                .append(":").append(evenement.getDate().getMinute()).append(" | ");
        eventInfo.append("Lieu: ").append(evenement.getLieu());

        if (evenement instanceof Conference) {
            Conference conference = (Conference) evenement;
            eventInfo.append(" | Thème: ").append(conference.getTheme());
        } else if (evenement instanceof Concert) {
            Concert concert = (Concert) evenement;
            eventInfo.append(" | Artiste: ").append(concert.getArtiste());
        }

        return eventInfo.toString();
    }

    private VBox createEventCard(Evenement evenement, Organisateur organisateur) {
        VBox eventCard = new VBox(5);
        eventCard.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; " +
                "-fx-border-radius: 8; -fx-background-radius: 8; " +
                "-fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 1);");

        // Titre de l'événement
        Label eventTitle = new Label(evenement.getNom());
        eventTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Informations de base
        HBox infoRow1 = new HBox(20);
        Label organizerLabel = new Label("Organisateur: " + (organisateur != null ? organisateur.getNom() : "N/A"));
        organizerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");

        Label dateLabel = new Label("Date: " + evenement.getDate().getDayOfMonth() + " " +
                evenement.getDate().getMonth() + " " + evenement.getDate().getYear());
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");

        infoRow1.getChildren().addAll(organizerLabel, dateLabel);

        // Deuxième ligne d'informations
        HBox infoRow2 = new HBox(20);
        Label timeLabel = new Label("Heure: " + String.format("%02d:%02d",
                evenement.getDate().getHour(), evenement.getDate().getMinute()));
        timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");

        Label locationLabel = new Label("Lieu: " + evenement.getLieu());
        locationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");

        infoRow2.getChildren().addAll(timeLabel, locationLabel);

        // Informations spécifiques selon le type
        Label specificInfo = null;
        if (evenement instanceof Conference) {
            Conference conference = (Conference) evenement;
            specificInfo = new Label("Thème: " + conference.getTheme());
            specificInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: #17a2b8; -fx-font-weight: bold;");
        } else if (evenement instanceof Concert) {
            Concert concert = (Concert) evenement;
            specificInfo = new Label("Artiste: " + concert.getArtiste());
            specificInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: #e83e8c; -fx-font-weight: bold;");
        }

        eventCard.getChildren().addAll(eventTitle, infoRow1, infoRow2);
        if (specificInfo != null) {
            eventCard.getChildren().add(specificInfo);
        }

        return eventCard;
    }

    public void onSubscribeEvenementClick(ActionEvent actionEvent) {
        try {
            // Récupérer l'index de l'événement sélectionné
            int selectedIndex = listViewEvenements.getSelectionModel().getSelectedIndex();

            if (selectedIndex < 0 || selectedIndex >= evenementsList.size()) {
                // Afficher une alerte à l'utilisateur au lieu d'un simple println
                showAlert("Erreur", "Veuillez sélectionner un événement dans la liste.", Alert.AlertType.ERROR);
                return;
            }

            Evenement selectedEvent = evenementsList.get(selectedIndex);
            System.out.println(selectedEvent.getNom());
            Participant currentUser = UserSession.getInstance().getUser();
            System.out.println(currentUser.getId());


            // Procéder à l'inscription
            boolean success = Serialization.addParticipantsAuxEvenements(currentUser, selectedEvent);

            if (success) {
                showAlert("Succès", "Inscription réussie à l'événement : " + selectedEvent.getNom(), Alert.AlertType.ERROR);
            } else {
                showAlert("Erreur", "Échec de l'inscription. Veuillez réessayer.", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur inattendue s'est produite : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        EventHandlerApplication.setRoot("menuView");
    }
}