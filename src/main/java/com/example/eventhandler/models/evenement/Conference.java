package com.example.eventhandler.models.evenement;

import com.example.eventhandler.models.personne.Intervenant;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;

public class Conference extends Evenement {
    private String theme;
    private List<Intervenant> intervenants;

    // Default constructor (required for Jackson)
    public Conference() {
        super();
    }

    public Conference(int id, String nom, LocalDateTime date, String lieu, int capaciteMax,
                      Duration duration, String theme, List<Intervenant> intervenants) {
        super(id, nom, date, lieu, capaciteMax, duration);
        this.theme = theme;
        this.intervenants = intervenants;
    }

    // Getters and setters
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public List<Intervenant> getIntervenants() { return intervenants; }
    public void setIntervenants(List<Intervenant> intervenants) { this.intervenants = intervenants; }
}