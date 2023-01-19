package com.emplk.mareutraining.ui.create;


import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.utils.SingleLiveEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

/**
 * Business logic for CreateNewMeetingActivity
 */
public class CreateMeetingViewModel extends ViewModel {

    @NonNull
    private final MeetingsRepository repository;

    private final SingleLiveEvent<Void> closeActivity = new SingleLiveEvent<>();

    private final SingleLiveEvent<Boolean> buttonEnabled = new SingleLiveEvent<>();

    public CreateMeetingViewModel(@NonNull MeetingsRepository repository) {
        this.repository = repository;
    }

    public void onCreateMeetingClicked(
            @NonNull String meetingTitle,
            @NonNull Room room,
            @NonNull LocalDate date,
            @NonNull LocalTime timeStart,
            @NonNull LocalTime timeEnd,
            @NonNull List<String> participants,
            @NonNull String meetingObject
    ) {
        // add my newly created meeting
        repository.addMeeting(
                meetingTitle,
                room,
                date,
                timeStart,
                timeEnd,
                participants,
                meetingObject);

        // close the activity afterwards
        closeActivity.call();
    }

    /**
     * Called to enabled submit button
     *
     * @return SingleLiveEvent of type Boolean
     */
    public SingleLiveEvent<Boolean> getButtonEnabled() {
        return buttonEnabled;
    }

    /**
     * Called to CreateNewMeetingActivity
     *
     * @return SingleLiveEvent of type Void
     */
    public SingleLiveEvent<Void> getCloseActivity() {
        return closeActivity;
    }

    /**
     * Parse date in String to LocalDate
     *
     * @param date String
     * @return LocalDate
     */
    public LocalDate formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, formatter);
    }

    /**
     * Parse time in String to LocalTime
     *
     * @param stringTime date in String
     * @return LocalTime formatted time HH:mm
     */
    public LocalTime formatTime(String stringTime) {
        return LocalTime.parse(stringTime);
    }

    /**
     * Parse given roomName (String) to corresponding Room constant
     *
     * @param roomName room name in String
     * @return Room constant
     */
    public Room getSelectedRoom(String roomName) {
        Room selectedRoom = Room.ROOM_ONE;
        Room[] rooms = Room.values();
        for (Room room : rooms) {
            if (roomName.equals(room.getRoomName())) {
                selectedRoom = room;
                break;
            }
        }
        return selectedRoom;
    }

    public boolean isMeetingInfoComplete(
            @NonNull String meetingTitle,
            @NonNull String room,
            @NonNull String date,
            @NonNull String timeStart,
            @NonNull String timeEnd,
            @NonNull List<String> participants,
            @NonNull String meetingObject
    ) {
        boolean isAllFieldsNotEmpty = Stream.of(meetingTitle, room, date, timeStart, timeEnd, meetingObject)
                .noneMatch(s -> s.isEmpty()) && !participants.isEmpty();
        buttonEnabled.setValue(isAllFieldsNotEmpty);
        return isAllFieldsNotEmpty;
    }


    public boolean isInvalidTime(String timeStart, String timeEnd) {
        return formatTime(timeStart).isAfter(formatTime(timeEnd)) ||
                formatTime(timeStart).equals(formatTime(timeEnd));
    }

    /**
     * Check if entered email address is valid (correctly stylized)
     *
     * @param target String
     * @return boolean (false: set message error)
     */
    public boolean isValidEmail(String target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
