package com.emplk.mareutraining.viewmodels;


import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.utils.SingleLiveEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CreateMeetingViewModel extends ViewModel {

    @NonNull
    private final MeetingsRepository repository;

    private final SingleLiveEvent<Void> closeActivity = new SingleLiveEvent<>();

    // private final MutableLiveData<Boolean> isCreateButtonEnabled = new MutableLiveData<>(false);

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

        // check if all fields are completed, else display a toast
        if (meetingTitle.isEmpty() || room.isEmpty() || date.isEmpty()
        || timeStart.isEmpty() || timeEnd.isEmpty()|| participants.isEmpty() || meetingObject.isEmpty()) {
            setToast(context, context.getString(R.string.check_submit_btn_toast));
        } else if (formatTime(timeStart).isAfter(formatTime(timeEnd))) {
            setToast(context,context.getString(R.string.check_time_ok_toast) );
        }
        else {
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



 /*   public LiveData<Boolean> getIsCreateButtonEnabled() {
        return isCreateButtonEnabled;
    }*/

    // close the activity
    public SingleLiveEvent<Void> getCloseActivity() {
        return closeActivity;
    }

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
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
