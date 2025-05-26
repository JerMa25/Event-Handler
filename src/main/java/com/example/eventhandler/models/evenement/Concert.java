package com.example.eventhandler.models.evenement;

import java.time.LocalDateTime;
import java.time.Duration;

public class Concert extends Evenement {
    private String artiste;
    private String genreMusical;

    // Default constructor (required for Jackson)
    public Concert() {
        super();
    }

    public Concert(int id, String nom, LocalDateTime date, String lieu, int capaciteMax,
                   Duration duration, String artiste, String genreMusical) {
        super(id, nom, date, lieu, capaciteMax, duration);
        this.artiste = artiste;
        this.genreMusical = genreMusical;
    }

    // Getters and setters
    public String getArtiste() { return artiste; }
    public void setArtiste(String artiste) { this.artiste = artiste; }

    public String getGenreMusical() { return genreMusical; }
    public void setGenreMusical(String genreMusical) { this.genreMusical = genreMusical; }
}