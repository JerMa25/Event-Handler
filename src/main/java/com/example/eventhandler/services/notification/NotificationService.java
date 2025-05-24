package com.example.eventhandler.services.notification;

public interface NotificationService {
    public default void envoyerNotification(String message){}
}
