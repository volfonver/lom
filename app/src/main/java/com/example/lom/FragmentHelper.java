package com.example.lom;

import com.example.lom.model.SearchType;
import com.example.lom.player.IPlayer;
import com.example.lom.view.TrackAdapter;

public interface FragmentHelper {
    IPlayer getPlayer();
    boolean checkPermWriteExternalStorage();
//    boolean checkPermInternet();
    boolean isOnline();
    void closeDrawer();
    void startChooseDir();
    void search();
    void search(SearchType type, String searchStr);
    TrackAdapter getTrackAdapter();
}
