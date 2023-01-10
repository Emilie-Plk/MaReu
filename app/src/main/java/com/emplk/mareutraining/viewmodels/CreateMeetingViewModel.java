package com.emplk.mareutraining.viewmodels;


import android.content.Context;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.utils.SingleLiveEvent;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Business logic for CreateNewMeetingActivity
 */
public class CreateMeetingViewModel extends ViewModel {

    @NonNull
    private final MeetingsRepository repository;

    private final SingleLiveEvent<Void> closeActivity = new SingleLiveEvent<>();

    public CreateMeetingViewModel(@NonNull MeetingsRepository repository) {
        this.repository = repository;
    }

    public void onCreateMeetingClicked(
            @NonNull String meetingTitle,
            @NonNull String room,
            @NonNull String date,
            @NonNull String timeStart,
            @NonNull String timeEnd,
            @NonNull List<String> participants,
            @NonNull String meetingObject
    ) {
        // add my newly created meeting
        repository.addMeeting(meetingTitle,
                getSelectedRoom(room),
                formatDate(date),
                formatTime(timeStart),
                formatTime(timeEnd),
                participants,
                meetingObject);

        // close the activity afterwards
        closeActivity.call();
    }


    /**
     * Close the CreateNewMeetingActivity
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
    private LocalDate formatDate(String date) {
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(stringTime, formatter);
    }

    /**
     * Parse given roomName (String) to corresponding Room constant
     *
     * @param roomName room name in String
     * @return Room constant
     */
    private Room getSelectedRoom(String roomName) {
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

    public boolean checkIfAllFieldsComplete(
            @NonNull String meetingTitle,
            @NonNull String room,
            @NonNull String date,
            @NonNull String timeStart,
            @NonNull String timeEnd,
            @NonNull List<String> participants,
            @NonNull String meetingObject
    ) {
        return meetingTitle.isEmpty() ||
                room.isEmpty() ||
                date.isEmpty() ||
                timeStart.isEmpty() ||
                timeEnd.isEmpty() ||
                participants.isEmpty() ||
                meetingObject.isEmpty();
    }

    public boolean checkIfTimeOk(String timeStart, String timeEnd) {
        return !formatTime(timeStart).isAfter(formatTime(timeEnd)) &&
                !formatTime(timeStart).equals(formatTime(timeEnd));
    }

    /**
     * Check if entered email address is valid (correctly stylized)
     *
     * @param target      String
     * @param inputLayout Layout from email EditText
     * @param context     Context (CreateNewMeetingActivity)
     * @return boolean (false: set message error)
     */
    public boolean isValidEmail(String target, TextInputLayout inputLayout, Context context) {
        inputLayout.setError(null);
        if (Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            return true;
        } else {
            inputLayout.setError(context.getString(R.string.invalid_email_input));
            return false;
        }
    }

}
