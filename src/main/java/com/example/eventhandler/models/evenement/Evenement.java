package com.example.eventhandler.models.evenement;

import com.example.eventhandler.models.personne.Participant;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Evenement {

    private int id;
    private String nom;
    private LocalDateTime date;
    private String lieu;
    private int capaciteMax;
    private Duration duration;

    //constructors
    public Evenement(){}

    @JsonCreator
    public Evenement(
            @JsonProperty("id")int id,
            @JsonProperty("nom")String nom,
            @JsonProperty("date/heure")LocalDateTime date,
            @JsonProperty("lieu")String lieu,
            @JsonProperty("capaciteMax")int capaciteMax,
            @JsonProperty("Duree")Duration duration
    ){
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
        this.duration = duration;
    }

    //getters et setters
    public int getId() {return id;}
    public String getNom() {return nom;}
    public LocalDateTime getDate() {return date;}
    public String getLieu() {return lieu;}
    public int getCapaciteMax() {return capaciteMax;}
    public Duration getDuration() {return duration;}

    public void setId(int id) {this.id = id;}
    public void setNom(String nom) {this.nom = nom;}
    public void setDate(LocalDateTime date) {this.date = date;}
    public void setLieu(String lieu) {this.lieu = lieu;}
    public void setCapaciteMax(int capaciteMax) {this.capaciteMax = capaciteMax;}
    public void setDuration(Duration duration) {this.duration = duration;}

    //methodes
    public void ajouterParticipant(Participant participant){}
    public void annuler(){}
    public void afficherDetails(){}

}
