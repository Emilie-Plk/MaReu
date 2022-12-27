package com.emplk.mareutraining.models;


import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class Meeting {
    private final long id;

    @NonNull
    private final String meetingTitle;

    @NonNull
    private final LocalDate date;

    @NonNull
    private final Room room;

    @NonNull
    private final LocalTime timeStart;

    @NonNull
    private final LocalTime timeEnd;

    @NonNull
    private final List<String> participants;

    private final String meetingObject;

    public Meeting(
            long id,
            @NonNull String meetingTitle,
            @NonNull Room room,
            @NonNull LocalDate date,
            @NonNull LocalTime timeStart,
            @NonNull LocalTime timeEnd,
            @NonNull List<String> participants,
            @NonNull String meetingObject) {
        this.id = id;
        this.meetingTitle = meetingTitle;
        this.room = room;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.participants = participants;
        this.meetingObject = meetingObject;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getMeetingTitle() {
        return meetingTitle;
    }

    @NonNull
    public Room getRoom() {
        return room;
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    @NonNull
    public LocalTime getTimeStart() {
        return timeStart;
    }

    @NonNull
    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    @NonNull
    public List<String> getParticipants() {
        return participants;
    }

    @NonNull
    public String getMeetingObject() {
        return meetingObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return meetingTitle.equals(meeting.meetingTitle) && date.equals(meeting.date) && room.equals(meeting.room) && timeStart.equals(meeting.timeStart) && timeEnd.equals(meeting.timeEnd) && participants.equals(meeting.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingTitle, date, room, timeStart, timeEnd, participants);
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "name='" + meetingTitle + '\'' +
                ", date=" + date +
                ", room=" + room +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", participants=" + participants +
                '}';
    }
}