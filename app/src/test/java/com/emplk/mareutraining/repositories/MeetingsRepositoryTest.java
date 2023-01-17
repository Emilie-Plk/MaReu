package com.emplk.mareutraining.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.emplk.mareutraining.config.BuildConfigResolver;
import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.utils.TestUtil;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeetingsRepositoryTest {

    private static final String TITLE = "TEST MEETING TITLE";
    private static final Room ROOM = Room.ROOM_FOUR;

    private static final String ROOM_FILTER = "Salle 4";
    private static final LocalDate DATE = LocalDate.now();
    private static final LocalTime TIME_START = LocalTime.of(14,0);
    private static final LocalTime TIME_END = LocalTime.of(14, 30);
    private static final List<String> PARTICIPANTS = Arrays.asList("john@doe.com", "jane@doe.com");
    private static final String OBJECT = "TEST MEETING OBJECT";

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    private BuildConfigResolver buildConfigResolver = Mockito.mock(BuildConfigResolver.class);

    @Before
    public void setUp() {
        Mockito.doReturn(false).when(buildConfigResolver).isDebug();
    }

    @Test
    public void empty_dummy_meeting_list_should_return_0_meeting() {
        // GIVEN mocked repo (empty)
        MeetingsRepository repository = new MeetingsRepository(buildConfigResolver);

        // WHEN fetching meetings
        List<Meeting> result = TestUtil.getValueForTesting(repository.getMeetings());

        // THEN meeting repo is empty
        assertEquals(0, result.size());
    }

    @Test
    public void dummy_meeting_list_should_return_5_meetings() {
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        MeetingsRepository repository = new MeetingsRepository(buildConfigResolver);

        List<Meeting> result = TestUtil.getValueForTesting(repository.getMeetings());
// TODO : list static final
        assertEquals(result, getDummyMeetings());
    }

    @Test
    public void add_one_meeting_should_increment_meeting_list_by_1() {
        // GIVEN mocked repository (empty) and adding new meeting
        MeetingsRepository repository = new MeetingsRepository(buildConfigResolver);

        // WHEN adding new meeting
        repository.addMeeting(
                TITLE,
                ROOM,
                DATE,
                TIME_START,
                TIME_END,
                PARTICIPANTS,
                OBJECT
        );

        // THEN newly added meeting has been added to repo
        List<Meeting> result = TestUtil.getValueForTesting(repository.getMeetings());
        assertEquals(1, result.size());
    }

    @Test
    public void delete_one_meeting_should_decrement_meeting_list_by_1() {
        // GIVEN mocked repository (empty) and adding new meeting
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

        // WHEN delete newly added meeting
        repository.deleteMeeting(0);
        result = TestUtil.getValueForTesting(repository.getMeetings());

        // THEN meeting has been deleted, repo is empty again
        assertTrue(result.isEmpty());
    }

    @Test
    public void filter_meeting_list_by_given_room_should_return_2_meetings() {
        // GIVEN mocked repository with 5 meetings
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        MeetingsRepository repository = new MeetingsRepository(buildConfigResolver);

        // WHEN calling this method
        repository.getMeetingsFilteredByRoom(ROOM_FILTER);
        List<Meeting> result = TestUtil.getValueForTesting(repository.getMeetings());

        // THEN return two meetings scheduled in given room
        assertEquals(2, result.size());
        assertEquals(ROOM_FILTER, result.get(0).getRoom().getRoomName());
        assertEquals(ROOM_FILTER, result.get(1).getRoom().getRoomName());
    }

    @Test
    public void filter_meeting_list_by_given_date_should_return_1_meeting() {
        // GIVEN mocked repository with 5 meetings and given date
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        MeetingsRepository repository = new MeetingsRepository(buildConfigResolver);
        LocalDate date = LocalDate.of(2022, 12, 11);

        // WHEN filtering by date
        repository.getMeetingsFilteredByDate(date);
        List<Meeting> result = TestUtil.getValueForTesting(repository.getMeetings());

        // THEN return one meeting scheduled at given date
        assertEquals(1, result.size());
        assertEquals(date, result.get(0).getDate());
    }

    @Test
    public void filterByDate_edge_case() {
        // GIVEN mocked repository with 5 meetings and given date
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        MeetingsRepository repository = new MeetingsRepository(buildConfigResolver);
        LocalDate date = LocalDate.of(2000, 12, 2);

        // WHEN filtering by date
        repository.getMeetingsFilteredByDate(date);
        List<Meeting> result = TestUtil.getValueForTesting(repository.getMeetings());

        // THEN no meeting found
        assertEquals(0, result.size());
    }

    @Test
    public void resetDateFilteredMeetings() {
        // GIVEN meetings filtered by date
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        MeetingsRepository repository = new MeetingsRepository(buildConfigResolver);
        LocalDate date = LocalDate.of(2022, 12, 11);
        repository.getMeetingsFilteredByDate(date);
        List<Meeting> filteredResult = TestUtil.getValueForTesting(repository.getMeetings());
        assertEquals(1, filteredResult.size());

        // WHEN filter meetings
        repository.getAllMeetings();

        // THEN retrieve all 5 meetings
        List<Meeting> result = TestUtil.getValueForTesting(repository.getMeetings());
        assertEquals(5, result.size());
    }

    @Test
    public void resetRoomFilteredMeetings() {
        // GIVEN meetings filtered by room
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        MeetingsRepository repository = new MeetingsRepository(buildConfigResolver);
        repository.getMeetingsFilteredByRoom(ROOM_FILTER);
        List<Meeting> filteredResult = TestUtil.getValueForTesting(repository.getMeetings());
        assertEquals(2, filteredResult.size());

        // WHEN filter meetings
        repository.getAllMeetings();

        // THEN retrieve all 5 meetings
        List<Meeting> result = TestUtil.getValueForTesting(repository.getMeetings());
        assertEquals(5, result.size());
    }

// region dummy meetings list
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
    // endregion
    }
