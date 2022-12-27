package com.emplk.mareutraining.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.emplk.mareutraining.models.Room;

import java.util.Arrays;
import java.util.List;

public class RoomRepository {

    private final List<Room> roomList = Arrays.asList(Room.values());
    MutableLiveData<List<Room>> roomData = new MutableLiveData<>();

    public LiveData<List<Room>> getRooms() {
       roomData.setValue(roomList);
       return roomData;
    }
}
