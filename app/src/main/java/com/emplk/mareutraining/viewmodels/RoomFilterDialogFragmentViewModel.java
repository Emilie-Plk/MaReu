package com.emplk.mareutraining.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.MeetingsRepository;

public class RoomFilterDialogFragmentViewModel extends ViewModel {

    @NonNull
    private final MeetingsRepository repository;

    public RoomFilterDialogFragmentViewModel(@NonNull MeetingsRepository repository) {
        this.repository = repository;
    }

    private void onRoomFilterClicked(
            @NonNull String room,
            @NonNull Meeting meeting
            ) {
getSelectedRoom(room);
    }

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

}
