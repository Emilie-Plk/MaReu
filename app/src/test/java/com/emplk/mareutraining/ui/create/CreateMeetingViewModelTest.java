package com.emplk.mareutraining.ui.create;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.emplk.mareutraining.config.BuildConfigResolver;
import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.MeetingsRepository;
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
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CreateMeetingViewModelTest {

    private final String timeStart = "14:00";
    private String timeEnd = "08:00";

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    private BuildConfigResolver buildConfigResolver = Mockito.mock(BuildConfigResolver.class);

    private MeetingsRepository repository;

    private CreateMeetingViewModel viewModel;

    @Before
    public void setUp() {
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        repository = new MeetingsRepository(buildConfigResolver);
        viewModel = new CreateMeetingViewModel(repository);
    }

    @Test
    public void on_create_meeting_clicked_should_create_new_meeting() {
        // GIVEN
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

        // THEN
        List<Meeting> meetingList = TestUtil.getValueForTesting(repository.getMeetingsLiveData());
        assertEquals(6, meetingList.size());
    }

    @Test
    public void is_date_formatted_with_success() {
        // GIVEN
        String stringDate = "14/01/2023";

        // WHEN
        boolean isEqual = viewModel.formatDate(stringDate).isEqual(LocalDate.of(2023, 1, 14));

        // THEN
        assertTrue(isEqual);
    }

    @Test
    public void is_time_formatted_with_success() {
        // GIVEN
        String time = "14:30";

        // WHEN
        boolean isEqual = viewModel.formatTime(time).equals(LocalTime.of(14, 30));

        // THEN
        assertTrue(isEqual);
    }

    @Test
    public void is_meeting_start_at_14_ending_at_8_valid() {
        assertFalse(viewModel.isTimeValidValidation(timeStart, timeEnd));
    }

    @Test
    public void is_time_start_and_time_end_similar_valid() {
        // WHEN
        timeEnd = timeStart;

        // THEN
        assertFalse(viewModel.isTimeValidValidation(timeStart, timeEnd));
    }

    @Test
    public void is_time_start_at_14_ending_at_15h30_valid() {
        // WHEN
        timeEnd = "15:30";

        // THEN
        assertTrue(viewModel.isTimeValidValidation(timeStart, timeEnd));
    }

    @Test
    public void is_string_room_selected_parsed_to_room_instance_with_success() {
        // WHEN
        String selectedRoom = "Salle 1";
        Room roomOne = Room.ROOM_ONE;

        // THEN
        assertEquals(roomOne, viewModel.getSelectedRoom(selectedRoom));
    }

    @Test
    public void is_all_info_completed() {
        // GIVEN
        String meetingTitle = "MEETING TITLE";
        String room = "Salle 1";
        String date = "14/01/2023";
        String timeStart = "14:30";
        String timeEnd = "15:30";
        List<String> participants = Arrays.asList("john@doe.com", "jane@doe.com");
        String meetingObject = "MEETING OBJECT";

        // THEN
        assertTrue(viewModel.isMeetingInfoComplete(meetingTitle, room, date, timeStart, timeEnd, participants, meetingObject));
        assertFalse(viewModel.isMeetingInfoComplete("", room, date, timeStart, timeEnd, participants, meetingObject));
    }
}
