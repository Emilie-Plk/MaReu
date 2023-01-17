package com.emplk.mareutraining.ui.detail;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.config.BuildConfigResolver;
import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.ui.create.CreateMeetingViewModel;
import com.emplk.mareutraining.utils.TestUtil;

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
import java.util.Objects;


@RunWith(MockitoJUnitRunner.class)
public class DetailMeetingViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    List<Meeting> dummyMeetings = new ArrayList<>();
    private final Meeting meetingOne = new Meeting(
            0,
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

    @Mock
    private MeetingsRepository repository;

    private DetailMeetingViewModel viewModel;

    private MutableLiveData<Meeting> meeting;

    @Before
    public void setUp() {
        meeting = new MutableLiveData<>();

        dummyMeetings.add(meetingOne);

        viewModel = new DetailMeetingViewModel(repository);
    }

    @Test
    public void getDetailMeeting() {
        // GIVEN
        long meetingId = 0;
        meeting.setValue(meetingOne);
        given(repository.getSingleMeeting(meetingId)).willReturn(meeting);
        // WHEN
        viewModel.getDetailViewStateLiveData(meetingId);
        verify(repository).getSingleMeeting(meetingId);
        // THEN
        TestUtil.observeForTesting(viewModel.getDetailViewStateLiveData(0), value ->
                assertEquals(meetingOne.getMeetingTitle(), value.getMeetingTitle()));
    }
}
