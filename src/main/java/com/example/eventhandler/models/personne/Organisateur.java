package com.example.eventhandler.models.personne;

import com.example.eventhandler.models.evenement.Evenement;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Organisateur extends Participant{
    private List<Evenement> evenementsOrganises;

    public Organisateur(){ super();}

    @JsonCreator
    public Organisateur(
            @JsonProperty("username")String id,
            @JsonProperty("nom")String nom,
            @JsonProperty("email")String email,
            @JsonProperty("password")String password,
            @JsonProperty("evenements")List<Evenement> evenementsOrganises){
        super(id, nom, email, password);
        this.evenementsOrganises = evenementsOrganises;
    }

    public List<Evenement> getEvenementsOrganises() {
        return evenementsOrganises;
    }

    public void setEvenementsOrganises(List<Evenement> evenementsOrganises) {
        this.evenementsOrganises = evenementsOrganises;
    }
}
