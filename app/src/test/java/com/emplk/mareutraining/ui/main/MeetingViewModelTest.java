package com.emplk.mareutraining.ui.main;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.ui.list.MeetingViewModel;
import com.emplk.mareutraining.ui.list.MeetingViewStateItem;
import com.emplk.mareutraining.utils.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        given(repository.getMeetingsLiveData()).willReturn(meetingsList);

        viewModel = new MeetingViewModel(repository);

        verify(repository).getMeetingsLiveData();
    }

    @Test
    public void nominalCase() {
        // WHEN
        List<MeetingViewStateItem> result = TestUtil.getValueForTesting(viewModel.getMeetingViewStateItemsLiveData());

        // THEN
        assertEquals(5, result.size());

        verifyNoMoreInteractions(repository);
    }

    @Test
    public void meeting_title_date_participants_are_generated() {
        // WHEN
        List<MeetingViewStateItem> result = TestUtil.getValueForTesting(viewModel.getMeetingViewStateItemsLiveData());

        // THEN
        for (int i = 0; i < result.size(); i++) {
            assertEquals(getDefaultMeetings().get(i).getMeetingTitle(), result.get(i).getMeetingTitle());
        }

        for (int i = 0; i < result.size(); i++) {
            assertEquals(getDefaultMeetings().get(i).getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), result.get(i).getDate());
        }

        for (int i = 0; i < result.size(); i++) {
            assertEquals(getDefaultMeetings().get(i).getParticipants()
                    .stream()
                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                    .map(word -> word.split("@")[0])
                    .collect(Collectors.joining(" ")), result.get(i).getParticipants());
        }
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void edge_case_no_meeting() {
        // GIVEN
        List<Meeting> emptyMeetingsList = new ArrayList<>();

        // WHEN
        meetingsList.setValue(emptyMeetingsList);

        // THEN
        List<MeetingViewStateItem> result = TestUtil.getValueForTesting(viewModel.getMeetingViewStateItemsLiveData());

        assertEquals(0, result.size());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void room_filter_meetings() {
        // WHEN
        viewModel.onRoomFilter("Salle 4");

        // THEN
        List<MeetingViewStateItem> result = TestUtil.getValueForTesting(viewModel.getMeetingViewStateItemsLiveData());
        assertEquals(2, result.size());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void date_filter_meetings() {
        // GIVEN
        LocalDate date = LocalDate.of(2022, 12, 15);
        viewModel.onDateFilter(date);

        // WHEN
        List<MeetingViewStateItem> result = TestUtil.getValueForTesting(viewModel.getMeetingViewStateItemsLiveData());

        // THEN
        assertEquals(1, result.size());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void reset_filter_meetings() {
        // GIVEN
        LocalDate date = LocalDate.of(2022, 12, 15);
        viewModel.onDateFilter(date);
        List<MeetingViewStateItem> filteredResult = TestUtil.getValueForTesting(viewModel.getMeetingViewStateItemsLiveData());
        assertEquals(1, filteredResult.size());

        // WHEN
        viewModel.onResetFilters();

        // THEN
        List<MeetingViewStateItem> result = TestUtil.getValueForTesting(viewModel.getMeetingViewStateItemsLiveData());
        assertEquals(5, result.size());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void check_on_delete_meeting() {
        // GIVEN
        long meetingId = 4;

        // WHEN
        viewModel.onDeleteMeetingClicked(meetingId);

        // THEN
        verify(repository).deleteMeeting(meetingId);
        verifyNoMoreInteractions(repository);
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
