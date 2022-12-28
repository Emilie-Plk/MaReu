package com.emplk.mareutraining.ui.list.room_filter;

import androidx.annotation.NonNull;

import com.emplk.mareutraining.models.Room;

import java.util.Objects;

public class RoomListStateItem {

    @NonNull
    private final Room room;
    private final boolean isSelected;

    public RoomListStateItem(@NonNull Room room, boolean isSelected) {
        this.room = room;
        this.isSelected = isSelected;
    }

    @NonNull
    public Room getRoom() {
        return room;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomListStateItem that = (RoomListStateItem) o;
        return isSelected == that.isSelected && room == that.room;
    }

    @Override
    public int hashCode() {
        return Objects.hash(room, isSelected);
    }

    @Override
    public String toString() {
        return "RoomListStateItem{" +
                "room=" + room +
                ", isSelected=" + isSelected +
                '}';
    }
}
