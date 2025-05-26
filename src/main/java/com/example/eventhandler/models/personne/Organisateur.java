package com.example.eventhandler.models.personne;

import com.example.eventhandler.models.evenement.Evenement;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Organisateur extends Participant{
    @JsonProperty("evenements")
    private List<Evenement> evenementsOrganises;

    public Organisateur(){ super();}

    public Organisateur(String id, String nom, String email, String password, List<Evenement> evenementsOrganises){
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
