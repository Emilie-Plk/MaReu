package com.emplk.mareutraining.ui.detail;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.emplk.mareutraining.config.BuildConfigResolver;
import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.MeetingsRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DetailMeetingViewModelTest {

    private MutableLiveData<List<Meeting>> meetingsList;

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();


    private DetailMeetingViewModel viewModel;

    @Mock
    private MeetingsRepository repository;


    @Before
    public void setUp() {
        meetingsList = new MutableLiveData<>();

        given(repository.getMeetings()).willReturn(meetingsList);

        List<Meeting> dummyMeetings = getDefaultMeetings();
        meetingsList.setValue(dummyMeetings);

        viewModel = new DetailMeetingViewModel(repository);

    }

    @Test
    public void getSingleMeetingViewState() {
        viewModel.getDetailViewStateLiveData(0);
        verify(repository).getSingleMeeting(0);
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
