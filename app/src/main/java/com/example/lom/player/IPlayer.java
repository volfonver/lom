package com.example.lom.player;

import com.example.lom.TrackStateListener;
import com.example.lom.model.vo.Track;

import java.util.List;

public interface IPlayer {
    void playTrackByPosition(int position);
    void stopTrack();

//    Track next();
//    Track preview();

    Track getCurrentTrack();
    void pause();
    void continuePlay();
    boolean isPlaying();
    void rewind(int newProgress);
    void setTrackStateListener(TrackStateListener listener);

    void setList(List<Track> tracks);
}
