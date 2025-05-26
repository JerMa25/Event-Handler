package com.example.eventhandler;

public class EventType {
    private static EventType instance;
    private int type;

    private EventType() {}

    public static EventType getInstance() {
        if (instance == null) {
            instance = new EventType();
        }
        return instance;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
