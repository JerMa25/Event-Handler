package com.example.eventhandler.models.personne;

import com.example.eventhandler.models.evenement.Concert;
import com.example.eventhandler.models.evenement.Conference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Participant.class, name = "participant"),
        @JsonSubTypes.Type(value = Organisateur.class, name = "organisateur"),
        @JsonSubTypes.Type(value = Intervenant.class, name = "intervenant")
})

public class Participant{

    private String id;
    private String nom;
    private String email;
    private String password;

    //constructors
    public Participant(){}

    @JsonCreator
    public Participant(
            @JsonProperty("username") String id,
            @JsonProperty("nom") String nom,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password){
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.password = password;
    }

    //getters and setters
    public String getId() {return id;}
    public String getNom(){return nom;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}

    public void setId(String id) {this.id = id;}
    public void setNom(String nom) {this.nom = nom;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}

    @Override
    public String toString(){
        return "Participant [id="+id+", nom="+nom+" email="+email+"]";
    }
}
