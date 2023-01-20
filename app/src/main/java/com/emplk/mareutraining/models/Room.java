package com.emplk.mareutraining.models;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.emplk.mareutraining.R;

public enum Room {

    ROOM_ONE("Salle 1", R.drawable.ic_baseline_circle_room_one),
    ROOM_TWO("Salle 2", R.drawable.ic_baseline_circle_room_two),
    ROOM_THREE("Salle 3", R.drawable.ic_baseline_circle_room_three),
    ROOM_FOUR("Salle 4", R.drawable.ic_baseline_circle_room_four),
    ROOM_FIVE("Salle 5", R.drawable.ic_baseline_circle_room_five),
    ROOM_SIX("Salle 6", R.drawable.ic_baseline_circle_room_six),
    ROOM_SEVEN("Salle 7", R.drawable.ic_baseline_circle_room_seven),
    ROOM_EIGHT("Salle 8", R.drawable.ic_baseline_circle_room_eight),
    ROOM_NINE("Salle 9", R.drawable.ic_baseline_circle_room_nine),
    ROOM_TEN("Salle 10", R.drawable.ic_baseline_circle_room_ten);


    private final String name;

    @DrawableRes
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

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}