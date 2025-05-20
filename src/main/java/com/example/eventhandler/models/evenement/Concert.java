package com.example.eventhandler.models.evenement;

public class Concert extends Evenement{

    private String artiste;
    private String genreMusical;

    //getters and setters
    public String getArtiste() {return artiste;}
    public String getGenreMusical() {return genreMusical;}

    public void setArtiste(String artiste) {this.artiste = artiste;}
    public void setGenreMusical(String genreMusical) {this.genreMusical = genreMusical;}
}
