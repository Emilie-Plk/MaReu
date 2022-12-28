package com.emplk.mareutraining.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.RoomRepository;

public class RoomFilterDialogFragmentViewModel extends ViewModel {

    @NonNull
    private final RoomRepository repository;

    public RoomFilterDialogFragmentViewModel(@NonNull RoomRepository repository) {
        this.repository = repository;
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
        // TODO: int for color or String ? + est-ce que je récup vraiment une enum constant ? à priori oui
        return selectedRoom;
    }
}
