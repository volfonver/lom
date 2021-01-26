package com.example.lom;

public interface TrackStateListener {

    void setProgress(int progress);
    void setDuration(int duration);
    void setTrackName(String track);
    void trackIsOver();
}
