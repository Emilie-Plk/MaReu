package com.emplk.mareutraining.ui.create;


import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
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

    private final SingleLiveEvent<String> displayError = new SingleLiveEvent<>();

    MutableLiveData<String> timeEndColor = new MutableLiveData<>();

    MutableLiveData<Boolean> errorIconVisible = new MutableLiveData<>();

    private boolean isValidTime;

    private boolean isAllFieldCompleted;


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
     * Called to display error message
     * for adding participants
     *
     * @return SingleLiveEvent of type String
     */
    public SingleLiveEvent<String> getDisplayError() {
        return displayError;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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

    /**
     * Check if all given info is filled (not empty)
     * @param meetingTitle String
     * @param room String
     * @param date String
     * @param timeStart String
     * @param timeEnd String
     * @param participants List of String
     * @param meetingObject String
     * @return boolean
     */
    public boolean isMeetingInfoComplete(
            @NonNull String meetingTitle,
            @NonNull String room,
            @NonNull String date,
            @NonNull String timeStart,
            @NonNull String timeEnd,
            @NonNull List<String> participants,
            @NonNull String meetingObject
    ) {
        isAllFieldCompleted = Stream.of(meetingTitle, room, date, timeStart, timeEnd, meetingObject)
                .noneMatch(s -> s.isEmpty()) && !participants.isEmpty();
        checkButtonEnabled();
        return isAllFieldCompleted;
    }

    /**
     * Helper method to check if chosen time is valid,
     * if time start is before and not equal to time end
     * @param timeStart String
     * @param timeEnd String
     * @return boolean
     */
    private boolean isChosenTimeValid(String timeStart, String timeEnd) {
        return formatTime(timeStart).isBefore(formatTime(timeEnd));
    }


    /**
     * On time validation, check if time is valid,
     * not empty, and set logic for error/button enabling
     *
     * @param timeStart String
     * @param timeEnd String
     * @return boolean
     */
    public boolean isTimeValidValidation(String timeStart, String timeEnd) {
        if (timeStart.isEmpty() || timeEnd.isEmpty()) return false;
        isValidTime = isChosenTimeValid(timeStart, timeEnd);
        checkButtonEnabled();
        if (!isValidTime) {
            updateForInvalidTime();
            return false;
        }
        updateForValidTime();
        return true;
    }

    /**
     * Update errorIconVisible, timeEndColor and displayError
     * MutableLiveData's values when invalid time
     */
    private void updateForInvalidTime() {
        timeEndColor.setValue("#b00020");
        errorIconVisible.setValue(true);
        displayError.setValue("Merci de choisir une heure de début antérieure à celle de fin");
    }

    /**
     * Update errorIconVisible and timeEndColor
     * MutableLiveData's values when valid time
     */
    private void updateForValidTime() {
        timeEndColor.setValue("#546E7A");
        errorIconVisible.setValue(false);
    }


    /**
     * Check for fields completion and time validation
     * (SingleLiveEvent for submit button)
     */
    public void checkButtonEnabled() {
        buttonEnabled.setValue(isAllFieldCompleted && isValidTime);
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
