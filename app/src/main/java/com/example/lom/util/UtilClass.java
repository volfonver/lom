package com.example.lom.util;

public class UtilClass {

    public static int getNanosecondsOnDuration(String duration) {
        String[] dur = duration.split(":");
        return (Integer.parseInt(dur[0]) + Integer.parseInt(dur[1]) * 60) * 1000;
    }
}
