package com.emplk.mareutraining.ui.main;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.ui.list.MeetingViewModel;
import com.emplk.mareutraining.ui.list.MeetingsViewStateItem;
import com.emplk.mareutraining.utils.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RunWith(MockitoJUnitRunner.class)
public class MeetingViewModelTest {

    private MutableLiveData<List<Meeting>> meetingsList;

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();


    @Mock
    private MeetingsRepository repository;

    private MeetingViewModel viewModel;


    @Before
    public void setUp() {
        meetingsList = new MutableLiveData<>();

        // Set dummy values to MutableLiveData
        List<Meeting> dummyMeetings = getDefaultMeetings();
        meetingsList.setValue(dummyMeetings);

        // Mocked LiveData from repo
        given(repository.getMeetings()).willReturn(meetingsList);

        viewModel = new MeetingViewModel(repository);
    }

    @Test
    public void nominalCase() {
        // GIVEN meetingList (in setup)
        // WHEN calling Meeting View State items
        TestUtil.observeForTesting(viewModel.getMeetingViewStateItems(), value -> {
            // THEN all 5 meetings are found
            assertEquals(5, value.size());
            verify(repository).getMeetings();
            verifyNoMoreInteractions(repository);
        });
    }

    @Test
    public void edge_case_no_meeting() {
        // GIVEN LiveData populated with empty list of Meeting (edge case)
        List<Meeting> emptyMeetingsList = new ArrayList<>();
        meetingsList.setValue(emptyMeetingsList);

        // WHEN observe for testing
        TestUtil.observeForTesting(viewModel.getMeetingViewStateItems(), value -> {
            // THEN no View State item found (empty)
            assertEquals(0, value.size());

            verify(repository).getMeetings();
            verifyNoMoreInteractions(repository);
        });
    }

    @Test
    public void check_on_delete_meeting() {
        // GIVEN meetingId
        long meetingId = 4;

        // WHEN delete given meeting
        viewModel.onDeleteMeetingClicked(meetingId);

        // THEN verify repo's behavior and check nothing else has been invoked in the mocks
        verify(repository).deleteMeeting(meetingId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    //TODO: is it enough? we already check it in repo, here we just verify viewModel <-> repo ?
    public void check_meeting_filtered_by_room_with_success() {
        // GIVEN room name
        String roomName = "Salle 4";

        // WHEN filter meetings with given room name
        viewModel.onFetchingMeetingsFilteredByRoom(roomName);

        // THEN verify repo's behavior and check nothing else has been invoked in the mocks
        verify(repository).getMeetingsFilteredByRoom(roomName);
        verifyNoMoreInteractions(repository);
    }


    @Test
    public void check_meeting_filtered_by_date_with_success() {
        // GIVEN date
        LocalDate date = LocalDate.of(2023, 1, 16);

        // WHEN filter meetings with given date
        viewModel.onFetchingMeetingsFilteredByDate(date);

        // THEN verify repo's behavior and check nothing else has been invoked in the mocks
        verify(repository).getMeetingsFilteredByDate(date);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void reset_meetings_filtered_with_success() {
        viewModel.onResetFilter();
        verify(repository).getAllMeetings();
    }

    // region dummy meetings list
    @NonNull
    private List<Meeting> getDefaultMeetings() {
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
