package com.example.lom;

import com.example.lom.model.vo.Track;

import java.util.List;

public interface IUpdate {
    void updateList(List<Track> list);

//    void selectItem(int position);
    Track nextTrack();
    Track previewTrack();
}
