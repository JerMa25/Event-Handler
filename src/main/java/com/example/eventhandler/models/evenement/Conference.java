package com.example.eventhandler.models.evenement;

import com.example.eventhandler.models.personne.Intervenant;

import java.util.List;

public class Conference extends Evenement{
    private String theme;
    private List<Intervenant> intervenants;

    //getters and setters
    public String getTheme() {return theme;}
    public List<Intervenant> getIntervenants() {return intervenants;}

    public void setTheme(String theme) {this.theme = theme;}
    public void setIntervenants(List<Intervenant> intervenants) {}
}
