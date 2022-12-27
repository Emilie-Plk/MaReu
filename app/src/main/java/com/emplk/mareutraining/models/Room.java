package com.emplk.mareutraining.models;

import com.emplk.mareutraining.R;

public enum Room {

    ROOM_ONE("Salle 1", R.color.room_one),
    ROOM_TWO("Salle 2", R.color.room_two),
    ROOM_THREE("Salle 3", R.color.room_three),
    ROOM_FOUR("Salle 4", R.color.room_four),
    ROOM_FIVE("Salle 5", R.color.room_five),
    ROOM_SIX("Salle 6", R.color.room_six),
    ROOM_SEVEN("Salle 7", R.color.room_seven),
    ROOM_EIGHT("Salle 8", R.color.room_eight),
    ROOM_NINE("Salle 9", R.color.room_nine),
    ROOM_TEN("Salle 10", R.color.room_ten);


    private final String name;
    private final int color;

    Room(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public int getRoomColor() {
        return color;
    }

    public String getRoomName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}