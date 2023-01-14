package com.emplk.mareutraining.ui.main;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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

        // Mocked LiveData from repo
        given(repository.getMeetings()).willReturn(meetingsList);

        // Set dummy values to MutableLiveData
        List<Meeting> dummyMeetings = getDefaultMeetings();
        meetingsList.setValue(dummyMeetings);

        viewModel = new MeetingViewModel(repository);
    }

    @Test
    public void nominalCase() {
        TestUtil.observeForTesting(viewModel.getMeetingViewStateItems(), value -> {
            assertEquals(4, value.size());
            verify(repository).getMeetings();
            verifyNoMoreInteractions(repository);
        });
    }

    @Test
    public void edge_case_no_meetings() {
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

        // WHEN
        viewModel.onDeleteMeetingClicked(4);

        // THEN verify behavior in repo and check nothing else has been invoked in the mocks
        verify(repository).deleteMeeting(meetingId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void check_meeting_filtered_by_room_with_success() {
      // WHEN
        viewModel.onFetchingMeetingsFilteredByRoom("Salle 4");
        verify(repository).getMeetingsFilteredByRoom("Salle 4");
    }

    @Test
    public void check_meeting_filtered_by_room_with_success2() {
        viewModel.onFetchingMeetingsFilteredByRoom("Salle 3");
        repository.getMeetingsFilteredByRoom("Salle 3");

        // WHEN
        TestUtil.observeForTesting(viewModel.getMeetingViewStateItems(), value -> {
            // TODO: expected:<2> but was:<4>, why no filter applied?
            assertEquals(4, value.size());
            viewModel.onFetchingMeetingsFilteredByRoom("Salle 3");
            repository.getMeetingsFilteredByRoom("Salle 3");
            assertEquals(2, value.size());
        });
    }


    @Test
    public void check_meeting_filtered_by_date_with_success() {
        // GIVEN
        LocalDate date = LocalDate.of(2023, 1, 16);

        // WHEN

        // THEN
        TestUtil.observeForTesting(viewModel.getMeetingViewStateItems(), value -> {
            viewModel.onFetchingMeetingsFilteredByDate(date);
            verify(repository).getMeetingsFilteredByDate(date);
            assertEquals(2, value.size());
            verify(repository).getMeetings();
            verifyNoMoreInteractions(repository);
        });
    }

    // region private method (getDefaultMeetings)
    private List<Meeting> getDefaultMeetings() {
        List<Meeting> meetingList = new ArrayList<>();
        meetingList.add(new Meeting(
                0,
                "REUNION 1",
                Room.ROOM_ONE,
                LocalDate.of(2023, 1, 15),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                Arrays.asList("john@doe.fr", "jane@doe.fr"),
                "MEETING OBJECT 1"
        ));
        meetingList.add(new Meeting(
                1,
                "REUNION 2",
                Room.ROOM_TWO,
                LocalDate.of(2023, 1, 16),
                LocalTime.of(11, 0),
                LocalTime.of(11, 30),
                Arrays.asList("johnny@doe.fr", "jolyne@doe.fr"),
                "MEETING OBJECT 2"
        ));
        meetingList.add(new Meeting(
                2,
                "REUNION 3",
                Room.ROOM_THREE,
                LocalDate.of(2023, 1, 16),
                LocalTime.of(12, 0),
                LocalTime.of(12, 30),
                Arrays.asList("joseph@doe.fr", "jotaro@doe.fr"),
                "MEETING OBJECT 2"
        ));
        meetingList.add(new Meeting(
                3,
                "REUNION 4",
                Room.ROOM_THREE,
                LocalDate.of(2023, 1, 17),
                LocalTime.of(15, 0),
                LocalTime.of(15, 30),
                Arrays.asList("johnathan@doe.fr", "josuke@doe.fr"),
                "MEETING OBJECT 2"
        ));
        return meetingList;
    }
    // endregion
}
