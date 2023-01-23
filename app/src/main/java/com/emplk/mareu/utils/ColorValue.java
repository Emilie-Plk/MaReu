package com.emplk.mareu.utils;

import androidx.annotation.NonNull;

public enum ColorValue {

    ERROR_COLOR("#b00020"),
    VALID_GREY_COLOR("#546E7A");

    private final String colorString;

    ColorValue(String colorString) {
        this.colorString = colorString;
    }

    @NonNull
    @Override
    public String toString() {
        return "ColorValue{" +
                "colorString='" + colorString + '\'' +
                '}';
    }

    public String getColorString() {
        return colorString;
    }
}


