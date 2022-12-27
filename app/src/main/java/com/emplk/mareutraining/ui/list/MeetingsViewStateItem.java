package com.emplk.mareutraining.ui.list;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MeetingsViewStateItem {

    /*ViewStateItem
     Un objet 'ViewState' contient toutes les données nécessaires pour décrire l'état
     de chaque sous-composant de la vue à la fois*/

    @NonNull
    private final String meetingTitle;
    @NonNull
    private final String roomName;

    @NonNull
    private final String date;
    @NonNull
    private final String timeStart;
    @NonNull
    private final String participants;

    private final int roomColor;

    private final long meetingId;

    public MeetingsViewStateItem(
            @NonNull String meetingTitle,
            @NonNull String roomName,
            @NonNull String date, @NonNull String timeStart,
            @NonNull String participants,
            int roomColor,
            long meetingId) {
        this.meetingTitle = meetingTitle;
        this.roomName = roomName;
        this.date = date;
        this.timeStart = timeStart;
        this.participants = participants;
        this.roomColor = roomColor;
        this.meetingId = meetingId;
    }

    @NonNull
    public String getMeetingTitle() {
        return meetingTitle;
    }

    @NonNull
    public String getRoomName() {
        return roomName;
    }

    @NonNull
    public String getDate() { return date; }

    @NonNull
    public String getTimeStart() {
        return timeStart;
    }

    @NonNull
    public String getParticipants() {
        return participants;
    }

    public int getRoomColor() {
        return roomColor;
    }

    public long getMeetingId() {
        return meetingId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingsViewStateItem that = (MeetingsViewStateItem) o;
        return roomColor == that.roomColor && meetingId == that.meetingId && meetingTitle.equals(that.meetingTitle) && roomName.equals(that.roomName) && timeStart.equals(that.timeStart) && participants.equals(that.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingTitle, roomName, timeStart, participants, roomColor, meetingId);
    }

    @NonNull
    @Override
    public String toString() {
        return "MeetingsViewStateItem{" +
                "meetingTitle='" + meetingTitle + '\'' +
                ", roomName='" + roomName + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", participants='" + participants + '\'' +
                ", roomColor=" + roomColor +
                ", meetingId=" + meetingId +
                '}';
    }
}
