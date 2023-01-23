package com.emplk.mareu.ui.create;


import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emplk.mareu.models.Room;
import com.emplk.mareu.repositories.MeetingsRepository;
import com.emplk.mareu.utils.ColorValue;
import com.emplk.mareu.utils.NotificationState;
import com.emplk.mareu.utils.SingleLiveEvent;

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

    private final SingleLiveEvent<String> errorState = new SingleLiveEvent<>();

    private final MutableLiveData<String> timeEndColor = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isErrorIconVisible = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> isTimeEndFieldValid = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> isEachFieldCompleted = new MutableLiveData<>(false);

    private final MediatorLiveData<Boolean> isValidAndCompleted = new MediatorLiveData<>();

    public CreateMeetingViewModel(@NonNull MeetingsRepository repository) {
        this.repository = repository;

        isValidAndCompleted.addSource(isEachFieldCompleted, isCompleted -> combine());

        isValidAndCompleted.addSource(isTimeEndFieldValid, isValid -> combine());
    }


    /**
     * Combines the values of the isEachFieldCompleted & isTimeEndFieldValid properties
     * to determine whether all fields are valid and completed.
     * If all fields are valid and completed, sets the value of
     * isValidAndCompleted (MediatorLiveData) property to true, otherwise sets it to false.
     */
    private void combine() {
        assert isEachFieldCompleted.getValue() != null && isTimeEndFieldValid.getValue() != null;
        isValidAndCompleted.setValue(isEachFieldCompleted.getValue() && isTimeEndFieldValid.getValue());
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

    public SingleLiveEvent<String> getErrorState() {
        return errorState;
    }

    public MediatorLiveData<Boolean> getIsValidAndCompleted() {
        return isValidAndCompleted;
    }

    public MutableLiveData<String> getTimeEndColor() {
        return timeEndColor;
    }

    public MutableLiveData<Boolean> getIsErrorIconVisible() {
        return isErrorIconVisible;
    }

    public MutableLiveData<Boolean> getIsEachFieldCompleted() {
        return isEachFieldCompleted;
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
     * to set isEachFieldCompleted MutableLiveData (boolean) value
     *
     * @param meetingTitle  String
     * @param room          String
     * @param date          String
     * @param timeStart     String
     * @param timeEnd       String
     * @param participants  List of String
     * @param meetingObject String
     */
    public void isMeetingInfoComplete(
            @NonNull String meetingTitle,
            @NonNull String room,
            @NonNull String date,
            @NonNull String timeStart,
            @NonNull String timeEnd,
            @NonNull List<String> participants,
            @NonNull String meetingObject
    ) {
        isEachFieldCompleted.setValue(Stream.of(meetingTitle, room, date, timeStart, timeEnd, meetingObject)
                .noneMatch(String::isEmpty) && !participants.isEmpty());
    }

    /**
     * Helper method to check if chosen time is valid,
     * if time start is before and not equal to time end
     *
     * @param timeStart String
     * @param timeEnd   String
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
     * @param timeEnd   String
     * @return boolean
     */
    public boolean isTimeValidValidation(String timeStart, String timeEnd) {
        if (timeStart.isEmpty() || timeEnd.isEmpty()) return false;
        isTimeEndFieldValid.setValue(isChosenTimeValid(timeStart, timeEnd));
        assert isTimeEndFieldValid.getValue() != null;
        if (isTimeEndFieldValid.getValue()) {
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
        timeEndColor.setValue(ColorValue.ERROR_COLOR.getColorString());
        isErrorIconVisible.setValue(true);
        errorState.setValue(NotificationState.ERROR_INVALID_TIME.getNotificationMessage());
    }

    /**
     * Update errorIconVisible and timeEndColor
     * MutableLiveData's values when valid time
     */
    private void updateForValidTime() {
        timeEndColor.setValue(ColorValue.VALID_GREY_COLOR.getColorString());
        isErrorIconVisible.setValue(false);
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
