package com.emplk.mareutraining.viewmodels;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

    private final MutableLiveData<Boolean> isCreateButtonEnabled = new MutableLiveData<>(false);

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

    public void checkEveryFieldFilled(String date, String startTime, String endTime, String room, List<String> participants) {
        isCreateButtonEnabled.setValue(!date.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty() && !room.isEmpty() && !participants.isEmpty());
    }

    public LiveData<Boolean> getIsCreateButtonEnabled() {
        return isCreateButtonEnabled;
    }

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

    public void checkIfTimeOk(String startTime, String endTime) {
        LocalTime startTimeLocalTime = formatTime(startTime);
        LocalTime endTimeLocalTime = formatTime(endTime);
        if (startTimeLocalTime.isAfter(endTimeLocalTime)) {
            Log.e("EMILIE", "Please select a timestart after");
        }
    }

}
