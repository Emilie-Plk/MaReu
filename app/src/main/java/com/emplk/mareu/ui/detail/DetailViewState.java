package com.emplk.mareu.ui.detail;

import androidx.annotation.NonNull;

import java.util.Objects;

public class DetailViewState {
    @NonNull
    private final String meetingTitle;
    @NonNull
    private final String roomName;
    @NonNull
    private final String date;
    @NonNull
    private final String timeStart;
    @NonNull
    private final String timeEnd;
    @NonNull
    private final String participants;
    @NonNull
    private final String meetingObject;

    private final int roomColor;

    private final long meetingId;

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
    public String getTimeEnd() { return timeEnd; }

    @NonNull
    public String getParticipants() {
        return participants;
    }

    @NonNull
    public String getMeetingObject() {
        return meetingObject;
    }

    public int getRoomColor() {
        return roomColor;
    }

    public long getMeetingId() {
        return meetingId;
    }

    public DetailViewState(
            @NonNull String meetingTitle,
            @NonNull String roomName,
            @NonNull String date, @NonNull String timeStart,
            @NonNull String timeEnd, @NonNull String participants,
            @NonNull String meetingObject,
            int roomColor,
            long meetingId) {
        this.meetingTitle = meetingTitle;
        this.roomName = roomName;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.participants = participants;
        this.meetingObject = meetingObject;
        this.roomColor = roomColor;
        this.meetingId = meetingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailViewState that = (DetailViewState) o;
        return roomColor == that.roomColor && meetingId == that.meetingId && meetingTitle.equals(that.meetingTitle) && roomName.equals(that.roomName) && timeStart.equals(that.timeStart) && participants.equals(that.participants) && meetingObject.equals(that.meetingObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingTitle, roomName, timeStart, participants, meetingObject, roomColor, meetingId);
    }

    @Override
    public String toString() {
        return "DetailViewState{" +
                "meetingTitle='" + meetingTitle + '\'' +
                ", roomName='" + roomName + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", participants='" + participants + '\'' +
                ", meetingObject='" + meetingObject + '\'' +
                ", roomColor=" + roomColor +
                ", meetingId=" + meetingId +
                '}';
    }
}
