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

        // WHEN
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
        TestUtil.observeForTesting(viewModel.getCloseActivity(), value -> {
            verify(repository).addMeeting(
                    eq(title),
                    eq(room),
                    eq(date),
                    eq(timeStart),
                    eq(timeEnd),
                    eq(participants),
                    eq(object)
            );

// check that nothing else has been invoked in the mocks
            verifyNoMoreInteractions(repository);
        });
    }

    @Test
    public void isDateFormattedWithSuccess() {
        String date = "14-01-2023";
        boolean isEqual = viewModel.formatDate(date).isEqual(LocalDate.of(2023, 1, 14));
        assertTrue(isEqual);
    }

    @Test
    public void isTimeFormattedWithSuccess() {
        String time = "14:30";
        boolean isEqual = viewModel.formatTime(time).equals(LocalTime.of(14, 30));
        assertTrue(isEqual);
    }


    @Test
    public void isValidTime() {
        String timeStart = "14:00";
        String timeEnd = "08:00";
        assertTrue(viewModel.isInvalidTime(timeStart, timeEnd));

        // check if time start and time end are the same
        timeEnd = timeStart;
        assertTrue(viewModel.isInvalidTime(timeStart, timeEnd));

        // check when time start is prior to time end
        timeEnd = "15:30";
        assertFalse(viewModel.isInvalidTime(timeStart, timeEnd));
    }

    @Test
    public void isStringRoomSelectedParsedToRoomInstanceWithSuccess() {
        // GIVEN "Salle 1" string and corresponding Room constant
        String selectedRoom = "Salle 1";
        Room roomOne = Room.ROOM_ONE;
        // WHEN calling getSelectedRoom
        // THEN should return the corresponding Room constant
        assertEquals(viewModel.getSelectedRoom(selectedRoom), roomOne);
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
