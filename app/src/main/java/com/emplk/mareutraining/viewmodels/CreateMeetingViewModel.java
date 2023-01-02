package com.emplk.mareutraining.viewmodels;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.utils.SingleLiveEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreateMeetingViewModel extends ViewModel {

    @NonNull
    private final MeetingsRepository repository;

    private final SingleLiveEvent<Void> closeActivitySingleLiveEvent = new SingleLiveEvent<>();

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
        repository.addMeeting(meetingTitle,
                getSelectedRoom(room),
                formatDate(date),
                formatTimeStart(timeStart),
                formatTimeEnd(timeEnd),
                participants,
                meetingObject);

        // close the activity afterwards
        closeActivitySingleLiveEvent.call();
    }

  /*  public boolean checkEveryFieldFilled(String date, String startTime, String endTime, String room, List<String> participants) {
if (date != null || startTime != null || endTime != null || room != null || !participants.isEmpty()) {
    return true;
}
return false;
    }*/

    public SingleLiveEvent<Void> getCloseActivitySingleLiveEvent() {
        return closeActivitySingleLiveEvent;
    }

    private LocalDate formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, formatter);
    }

    private LocalTime formatTimeStart(String startingTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        return LocalTime.parse(startingTime, formatter);
    }

    private LocalTime formatTimeEnd(String startingTime) {
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
        // TODO: int for color or String ? + est-ce que je récup vraiment une enum constant ? à priori oui
        return selectedRoom;
    }

}
