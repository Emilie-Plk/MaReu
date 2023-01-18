package com.emplk.mareutraining.ui.create;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.utils.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CreateMeetingViewModelTest {

    private final String timeStart = "14:00";
    private String timeEnd = "08:00";

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    private MeetingsRepository repository;

    private CreateMeetingViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new CreateMeetingViewModel(repository);
    }

    @Test
    public void onCreateMeetingClicked() {
        // GIVEN meeting info
        String title = "TEST MEETING TITLE";
        Room room = Room.ROOM_ONE;
        LocalDate date = LocalDate.of(2023, 1, 14);
        LocalTime timeStart = LocalTime.of(15, 0);
        LocalTime timeEnd = LocalTime.of(15, 45);
        List<String> participants = Arrays.asList("john@doe.com", "jane@doe.com");
        String object = "TEST MEETING OBJECT";

        // WHEN creating new meeting
        viewModel.onCreateMeetingClicked(
                title,
                room,
                date,
                timeStart,
                timeEnd,
                participants,
                object
        );

        // THEN should return same info added from the repo
        // and check that nothing else has been invoked in the mocks
      /*  TestUtil.observeForTesting(viewModel.getCloseActivity(), value -> {
            verify(repository).addMeeting(
                    eq(title),
                    eq(room),
                    eq(date),
                    eq(timeStart),
                    eq(timeEnd),
                    eq(participants),
                    eq(object)
            );
            verifyNoMoreInteractions(repository);
        });*/
    }

    @Test
    public void isDateFormattedWithSuccess() {
        // GIVEN date string
        String date = "14-01-2023";

        // WHEN checking formatted date string and local time for equality
        boolean isEqual = viewModel.formatDate(date).isEqual(LocalDate.of(2023, 1, 14));

        // THEN equality returns true
        assertTrue(isEqual);
    }

    @Test
    public void isTimeFormattedWithSuccess() {
        // GIVEN time string
        String time = "14:30";

        // WHEN checking formatted time string and local time for equality
        boolean isEqual = viewModel.formatTime(time).equals(LocalTime.of(14, 30));

        // THEN equality returns true
        assertTrue(isEqual);
    }

    @Test
    public void is_meeting_time_invalid() {
        // GIVEN time start (14h00) and time end (8h00)
        // WHEN checking if invalid
        // THEN return true
        assertTrue(viewModel.isInvalidTime(timeStart, timeEnd));
    }

    @Test
    public void is_time_start_and_time_end_similar() {
        // GIVEN time start equals time end
        timeEnd = timeStart;
        // WHEN checking if invalid
        // THEN return true
        assertTrue(viewModel.isInvalidTime(timeStart, timeEnd));
    }

    @Test
    public void is_time_start_prior_to_time_end() {
        // GIVEN time start (14h00) and time end (15h30)
        timeEnd = "15:30";
        // WHEN checking if invalid
        // THEN return false
        assertFalse(viewModel.isInvalidTime(timeStart, timeEnd));
    }

    @Test
    public void isStringRoomSelectedParsedToRoomInstanceWithSuccess() {
        // GIVEN "Salle 1" string and corresponding Room constant
        String selectedRoom = "Salle 1";
        Room roomOne = Room.ROOM_ONE;
        // WHEN calling getSelectedRoom...
        // ...THEN should return the corresponding Room constant
        assertEquals(roomOne, viewModel.getSelectedRoom(selectedRoom));
    }

    @Test
    public void isAllInfoCompletedForCreatedMeeting() {
        // GIVEN meeting info
        String meetingTitle = "MEETING TITLE";
        String room = "Salle 1";
        String date = "14/01/2023";
        String timeStart = "14:30";
        String timeEnd = "15:30";
        List<String> participants = Arrays.asList("john@doe.com", "jane@doe.com");
        String meetingObject = "MEETING OBJECT";

        // WHEN checking if incomplete
        // THEN return false if every field isn't empty
        assertFalse(viewModel.isMeetingInfoIncomplete(meetingTitle, room, date, timeStart, timeEnd, participants, meetingObject));

        // return true if at least one field is empty
        assertTrue(viewModel.isMeetingInfoIncomplete("", room, date, timeStart, timeEnd, participants, meetingObject));
    }
}
