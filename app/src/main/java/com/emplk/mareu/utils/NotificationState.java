package com.emplk.mareu.utils;

import androidx.annotation.NonNull;

public enum NotificationState {
    INFO_NO_MEETING("Aucune réunion à afficher"),
    INFO_DELETED_MEETING("Réunion supprimée"),
    ERROR_INVALID_TIME("Merci de choisir une heure de début antérieure à celle de fin"),
    FILTERED_SUBTITLE("Réunions filtrées : ");


    private final String notificationMessage;
    NotificationState(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    @NonNull
    @Override
    public String toString() {
        return "NotificationState{" +
                "notificationMessage='" + notificationMessage + '\'' +
                '}';
    }
}
