package com.emplk.mareu.ui.list;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * A ViewState item contains all required dynamic info of the View (adapter for MainActivity).
 * It's like a 'model' for the view.
 */
public class MeetingViewStateItem {

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

    public MeetingViewStateItem(
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
        MeetingViewStateItem that = (MeetingViewStateItem) o;
        return roomColor == that.roomColor && meetingId == that.meetingId && meetingTitle.equals(that.meetingTitle) && roomName.equals(that.roomName) && timeStart.equals(that.timeStart) && participants.equals(that.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingTitle, roomName, timeStart, participants, roomColor, meetingId);
    }

    @NonNull
    @Override
    public String toString() {
        return "MeetingViewStateItem{" +
                "meetingTitle='" + meetingTitle + '\'' +
                ", roomName='" + roomName + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", participants='" + participants + '\'' +
                ", roomColor=" + roomColor +
                ", meetingId=" + meetingId +
                '}';
    }
}
