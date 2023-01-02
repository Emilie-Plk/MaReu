package com.emplk.mareutraining.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.emplk.mareutraining.config.BuildConfigResolver;
import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.models.Room;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MeetingsRepository {


    private final MutableLiveData<List<Meeting>> meetingsLiveData = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<List<Meeting>> meetingsFilteredByRoomLiveData = new MutableLiveData<>(new ArrayList<>());

    private int maxId = 0;

    public MeetingsRepository(BuildConfigResolver buildConfigResolver) {
        // At startup, when creating repo, if we're in debug mode, add random Neighbours
        if (buildConfigResolver.isDebug()) {
            generateRandomMeetings();
        }
    }

    public void addMeeting(
            @NonNull String meetingTitle,
            @NonNull Room room,
            @NonNull LocalDate date,
            @NonNull LocalTime timeStart,
            @NonNull LocalTime timeEnd,
            @NonNull List<String> participants,
            @NonNull String meetingObject
    ) {
        List<Meeting> meetings = meetingsLiveData.getValue();

        if (meetings == null) return;

        meetings.add(
                new Meeting(
                        maxId++,
                        meetingTitle,
                        room,
                        date,
                        timeStart,
                        timeEnd,
                        participants,
                        meetingObject
                )
        );

        meetingsLiveData.setValue(meetings);
    }

    public LiveData<List<Meeting>> getMeetingsLiveData() {
        return meetingsLiveData;
    }

    public LiveData<Meeting> getSingleMeetingLiveData(long meetingId) {
        return Transformations.map(meetingsLiveData, meetings -> {
            for (Meeting meeting : meetings) {
                if (meeting.getId() == meetingId) {
                    return meeting;
                }
            }
            return null;
        });
    }

    public void deleteMeeting(long meetingId) {
        List<Meeting> meetings = meetingsLiveData.getValue();

        if (meetings == null) return;

        for (Iterator<Meeting> iterator = meetings.iterator();
             iterator.hasNext(); ) {
            Meeting meeting = iterator.next();

            if (meeting.getId() == meetingId) {
                iterator.remove();
                break;
            }
        }
        meetingsLiveData.setValue(meetings);
    }

    public LiveData<List<Meeting>> getMeetingsFilteredByRoom(String roomName) {
        List<Meeting> meetings = meetingsLiveData.getValue();
        List<Meeting> meetingsFilteredByRoom = new ArrayList<>();
        assert meetings != null;
        for (Meeting meeting : meetings) {
            if (meeting.getRoom().getRoomName().equals(roomName)) {
                meetingsFilteredByRoom.add(meeting);
            }
        }
        if (!meetingsFilteredByRoom.isEmpty()) {
            return meetingsFilteredByRoomLiveData;
        }
        meetingsFilteredByRoomLiveData.setValue(meetingsFilteredByRoom);
        return null;
    }


    private void generateRandomMeetings() {
        addMeeting(
                "Réunion d'info",
                Room.ROOM_FOUR,
                LocalDate.of(2022, 12, 8),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                Arrays.asList(
                        "pierre@lamzone.fr",
                        "charlotte@lamzone.fr",
                        "patrice@lamzone.fr"),
                "blablablabla blablablabla blablablabla");
        addMeeting(
                "Réunion d'info",
                Room.ROOM_ONE,
                LocalDate.of(2022, 12, 8),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                Arrays.asList(
                        "marie@lamzone.fr",
                        "ahmed@lamzone.fr",
                        "jocelyn@lamzone.fr"),
                "blablablabla blablablabla");
        addMeeting(
                "Présentation nouveau design",
                Room.ROOM_TEN,
                LocalDate.of(2022, 12, 8),
                LocalTime.of(11, 0),
                LocalTime.of(11, 20),
                Arrays.asList(
                        "nicolas@lamzone.fr",
                        "jpaul@lamzone.fr",
                        "soizic@lamzone.fr"),
                "blabla blabllablabla blablabla");
        addMeeting(
                "Projet secret",
                Room.ROOM_EIGHT,
                LocalDate.of(2022, 12, 9),
                LocalTime.of(14, 30),
                LocalTime.of(14, 50),
                Arrays.asList(
                        "djamilla@lamzone.fr",
                        "hubert@lamzone.fr",
                        "joan@lamzone.fr"),
                "Point avec Joan et Hubert sur l'avancée des maquettes + phases de tests");
        addMeeting(
                "Brainstorm dev",
                Room.ROOM_SEVEN,
                LocalDate.of(2022, 12, 9),
                LocalTime.of(15, 0),
                LocalTime.of(15, 45),
                Arrays.asList(
                        "nicolas@lamzone.fr",
                        "gregory@lamzone.fr",
                        "pauline@lamzone.fr"),
                "Debrief hebdo dev");
    }

}
