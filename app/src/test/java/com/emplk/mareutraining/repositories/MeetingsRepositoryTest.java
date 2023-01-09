package com.emplk.mareutraining.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.emplk.mareutraining.config.BuildConfigResolver;
import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.utils.TestUtil;


import net.bytebuddy.asm.Advice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MeetingsRepositoryTest {

    private static final String TITLE = "TEST MEETING TITLE";
    private static final Room ROOM = Room.ROOM_ONE;
    private static final LocalDate DATE = LocalDate.now();
    private static final LocalTime TIME_START = LocalTime.of(14,0);
    private static final LocalTime TIME_END = LocalTime.of(14, 30);
    private static final List<String> PARTICIPANTS = Arrays.asList("john@doe.com", "jane@doe.com");
    private static final String OBJECT = "TEST MEETING OBJECT";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private BuildConfigResolver buildConfigResolver = Mockito.mock(BuildConfigResolver.class);

    @Before
    public void setUp() {
        Mockito.doReturn(false).when(buildConfigResolver).isDebug();
    }

    @Test
    public void checkEmptyList() {
        MeetingsRepository repository = new MeetingsRepository(buildConfigResolver);

        List<Meeting> result = TestUtil.getValueForTesting(repository.getMeetings());

        assertEquals(0, result.size());
    }

    @Test
    public void getMeetingListWithSuccess() {
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        MeetingsRepository repository = new MeetingsRepository(buildConfigResolver);

        List<Meeting> result = TestUtil.getValueForTesting(repository.getMeetings());

        assertEquals(getDummyMeetings(), result);
    }

    @Test
    public void deleteMeetingWithSuccess() {
        MeetingsRepository repository = new MeetingsRepository(buildConfigResolver);

        repository.addMeeting(
                TITLE,
                ROOM,
                DATE,
                TIME_START,
                TIME_END,
                PARTICIPANTS,
                OBJECT
        );

        List<Meeting> result = TestUtil.getValueForTesting(repository.getMeetings());

        assertEquals(1, result.size());
    }


    @NonNull
    private List<Meeting> getDummyMeetings() {
        List<Meeting> dummyMeetings = new ArrayList<>();

        dummyMeetings.add(new Meeting(0,
                        "Réunion d'info",
                        Room.ROOM_FOUR,
                        LocalDate.of(2022, 12, 8),
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 30),
                        Arrays.asList(
                                "pierre@lamzone.fr",
                                "charlotte@lamzone.fr",
                                "patrice@lamzone.fr"),
                        "Nouveaux arrivants dans l'équipe + point sur les congés"));
        dummyMeetings.add(
                new Meeting(1,
                        "Retour sur les tests",
                        Room.ROOM_ONE,
                        LocalDate.of(2022, 12, 8),
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 30),
                        Arrays.asList(
                                "marie@lamzone.fr",
                                "ahmed@lamzone.fr",
                                "jocelyn@lamzone.fr"),
                        "Résultats des premiers tests par l'équipe Android"));

        dummyMeetings.add(
                new Meeting(
                        2,
                        "Présentation nouveau design",
                        Room.ROOM_TEN,
                        LocalDate.of(2022, 12, 15),
                        LocalTime.of(11, 0),
                        LocalTime.of(11, 20),
                        Arrays.asList(
                                "nicolas@lamzone.fr",
                                "jpaul@lamzone.fr",
                                "soizic@lamzone.fr"),
                        "Retour des utilisateurs du projet MaRéu, présentation du nouveau design"));

        dummyMeetings.add(
                new Meeting(3,
                        "Projet secret",
                        Room.ROOM_FOUR,
                        LocalDate.of(2022, 12, 9),
                        LocalTime.of(14, 30),
                        LocalTime.of(14, 50),
                        Arrays.asList(
                                "djamilla@lamzone.fr",
                                "hubert@lamzone.fr",
                                "joan@lamzone.fr"),
                        "Point avec Joan et Hubert sur l'avancée des maquettes + phases de tests"));

        dummyMeetings.add(
                new Meeting(4,
                        "Brainstorm dev",
                        Room.ROOM_SEVEN,
                        LocalDate.of(2022, 12, 11),
                        LocalTime.of(15, 0),
                        LocalTime.of(15, 45),
                        Arrays.asList(
                                "nicolas@lamzone.fr",
                                "gregory@lamzone.fr",
                                "pauline@lamzone.fr"),
                        "Debrief hebdo dev"));

        return dummyMeetings;
    }
    }
