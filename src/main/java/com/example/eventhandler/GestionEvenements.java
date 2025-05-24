package com.example.eventhandler;

import com.example.eventhandler.models.evenement.Evenement;

import java.util.Map;

public class GestionEvenements {

    //volatile prevents a thread from returning the instance if it has not been fully constructed by the previous thread constructing it.
    private static volatile GestionEvenements instance;
    private Map<String, Evenement> evenements;

    private GestionEvenements(Map<String, Evenement> evenements){
        this.evenements = evenements;
    }

    public void ajouterEvenement(Evenement evenement){}
    public void supprimerEvenement(Evenement evenement){}
    public void rechercherEvenement(Evenement evenement){}

    public static GestionEvenements getInstance(Map<String, Evenement> evenements){

        //if instance is not null here, no need for synchronisation since a thread is using the instanciated class
        GestionEvenements result = instance; //to avoid multiple direct read of instance from the memory
        if(result == null) {
            //synchronisation prevents 2 threads from instanciating the class at thesame time and thus returning 2 instances
            synchronized (GestionEvenements.class) {
                result = instance;
                //the verification here permits to instanciate the class only if it hasn't been done before
                if (result == null) {
                    instance = result = new GestionEvenements(evenements);
                }
            }
        }

        return result;
    }
}
