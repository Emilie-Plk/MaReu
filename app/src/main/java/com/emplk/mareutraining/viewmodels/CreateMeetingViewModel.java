package com.emplk.mareutraining.viewmodels;


import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

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

import es.dmoral.toasty.Toasty;

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
            @NonNull String meetingObject,
            @NonNull Context context
    ) {
        // check if all fields are filled, else display a toast
        if (meetingTitle.isEmpty() || room.isEmpty() || date.isEmpty()
                || timeStart.isEmpty() || timeEnd.isEmpty() || participants.isEmpty() || meetingObject.isEmpty()) {
            setToast(context, context.getString(R.string.check_submit_btn_toast));
        } else if (
                formatTime(timeStart).isAfter(formatTime(timeEnd)) ||
                formatTime(timeStart).equals(formatTime(timeEnd))) {
            setToast(context, context.getString(R.string.check_time_ok_toast));
        } else {
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
    }


    // close the activity
    public SingleLiveEvent<Void> getCloseActivity() {
        return closeActivity;
    }

    /**
     * Format date
     * @param date String
     * @return LocalDate
     */
    private LocalDate formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, formatter);
    }

    private LocalTime formatTime(String startingTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        return LocalTime.parse(startingTime, formatter);
    }


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

    public void setToast(Context context, String message) {
        Toasty.error(context, message, Toast.LENGTH_SHORT).show();
    }


    public boolean isValidEmail(String target, TextInputLayout inputLayout, Context context) {
        inputLayout.setError(null);
        if (Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            return true;
        } else {
            inputLayout.requestFocus();
            inputLayout.setError(context.getString(R.string.invalid_email_input));
            return false;
        }
    }

}
