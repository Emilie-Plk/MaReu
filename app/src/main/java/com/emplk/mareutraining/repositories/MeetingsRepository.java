package com.emplk.mareutraining.repositories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.List;

/**
 * Data source for the meetings
 */
public class MeetingsRepository {


    private final MutableLiveData<List<Meeting>> meetings = new MutableLiveData<>(new ArrayList<>());

    private List<Meeting> allMeetings = new ArrayList<>();

    private int idIncrement = 0;

    /**
     * At startup, when creating repo, if we're in debug mode, add dummy meetings
     *
     * @param buildConfigResolver buildConfigResolver
     */
    public MeetingsRepository(BuildConfigResolver buildConfigResolver) {
        if (buildConfigResolver.isDebug()) {
            generateRandomMeetings();
            allMeetings = this.meetings.getValue();
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
        allMeetings.add(
                new Meeting(
                        idIncrement++,
                        meetingTitle,
                        room,
                        date,
                        timeStart,
                        timeEnd,
                        participants,
                        meetingObject
                )
        );
        this.meetings.setValue(allMeetings);
    }


    /**
     * Fetch all existing meetings
     *
     * @return List of Meeting LiveData
     */
    public LiveData<List<Meeting>> getMeetings() {
        return meetings;
    }


    /**
     * Fetch a single meeting
     *
     * @param meetingId long
     * @return Meeting LiveData
     */
    public LiveData<Meeting> getSingleMeeting(long meetingId) {
        return Transformations.map(meetings, meetings -> {
            for (Meeting meeting : meetings) {
                if (meeting.getId() == meetingId) {
                    return meeting;
                }
            }
            return null;
        });
    }

    /**
     * Delete a given meeting
     *
     * @param meetingId long
     */
    public void deleteMeeting(long meetingId) {
        List<Meeting> meetings = this.meetings.getValue();
        if (meetings == null) return;

        for (Meeting meeting : meetings) {
            if (meeting.getId() == meetingId) {
                meetings.remove(meeting);
                break;
            }
        }
        this.meetings.setValue(meetings);
    }

    public void getAllMeetings() {
        this.meetings.setValue(allMeetings);
    }

    /**
     * Generate dummy meetings for the demo
     */
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
                "Nouveaux arrivants dans l'équipe + point sur les congés");
        addMeeting(
                "Retour sur les tests",
                Room.ROOM_ONE,
                LocalDate.of(2022, 12, 8),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                Arrays.asList(
                        "marie@lamzone.fr",
                        "ahmed@lamzone.fr",
                        "jocelyn@lamzone.fr"),
                "Résultats des premiers tests par l'équipe Android");
        addMeeting(
                "Présentation nouveau design",
                Room.ROOM_TEN,
                LocalDate.of(2022, 12, 15),
                LocalTime.of(11, 0),
                LocalTime.of(11, 20),
                Arrays.asList(
                        "nicolas@lamzone.fr",
                        "jpaul@lamzone.fr",
                        "soizic@lamzone.fr"),
                "Retour des utilisateurs du projet MaRéu, présentation du nouveau design");
        addMeeting(
                "Projet secret",
                Room.ROOM_FOUR,
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
                LocalDate.of(2022, 12, 11),
                LocalTime.of(15, 0),
                LocalTime.of(15, 45),
                Arrays.asList(
                        "nicolas@lamzone.fr",
                        "gregory@lamzone.fr",
                        "pauline@lamzone.fr"),
                "Debrief hebdo dev");
    }

}
