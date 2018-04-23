package com.example.android.quakereport;


/**
 * Created by amalakhov on 16.04.2018.
 */

public class Quake {
    private double magnitude;
    private String location;
    private long timeInMilliseconds;
    private String url;

    public Quake(double magnitude, String location, long timeInMilliseconds, String url) {
        this.magnitude = magnitude;
        this.location = location;
        this.timeInMilliseconds = timeInMilliseconds;
        this.url = url;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public String getUrl() {
        return url;
    }
}
