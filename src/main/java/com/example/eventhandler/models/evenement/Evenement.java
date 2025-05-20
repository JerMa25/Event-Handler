package com.example.eventhandler.models.evenement;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Evenement {

    private int id;
    private String nom;
    private LocalDateTime date;
    private String lieu;
    private int capaciteMax;

    //getters et setters
    public int getId() {return id;}
    public String getNom() {return nom;}
    public LocalDateTime getDate() {return date;}
    public String getLieu() {return lieu;}
    public int getCapaciteMax() {return capaciteMax;}

    public void setId(int id) {this.id = id;}
    public void setNom(String nom) {this.nom = nom;}
    public void setDate(LocalDateTime date) {this.date = date;}
    public void setLieu(String lieu) {this.lieu = lieu;}
    public void setCapaciteMax(int capaciteMax) {this.capaciteMax = capaciteMax;}

    //methodes
    public void ajouterParticipant(){}
    public void annuler(){}
    public void afficherDetails(){}

}
