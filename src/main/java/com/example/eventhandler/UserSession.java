package com.example.eventhandler;

import com.example.eventhandler.models.personne.Participant;

public class UserSession {
    private static UserSession instance;
    private Participant user;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(Participant user) {
        this.user = user;
    }

    public Participant getUser() {
        return user;
    }

    public void clear() {
        user = null;
    }
}
