// Evenement.java (abstract class)
package com.example.eventhandler.models.evenement;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.time.LocalDateTime;
import java.time.Duration;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(Concert.class),
        @JsonSubTypes.Type(Conference.class)
})
public abstract class Evenement {
    private int id;
    private String nom;
    private LocalDateTime date;
    private String lieu;
    private int capaciteMax;
    private Duration duration;

    // Default constructor (required for Jackson)
    public Evenement() {}

    public Evenement(int id, String nom, LocalDateTime date, String lieu, int capaciteMax, Duration duration) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
        this.duration = duration;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public int getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(int capaciteMax) { this.capaciteMax = capaciteMax; }

    public Duration getDuration() { return duration; }
    public void setDuration(Duration duration) { this.duration = duration; }
}