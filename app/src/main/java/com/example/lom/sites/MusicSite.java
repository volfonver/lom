package com.example.lom.sites;

import com.example.lom.model.vo.Track;

import java.io.IOException;
import java.util.List;

public interface MusicSite {
    List<Track> getDefaultTracks() throws IOException;
    List<Track> getTracksBySigner(String signer) throws IOException;
    List<Track> getTracksByTitle(String title) throws IOException;
    List<Track> getAllTracks(String title) throws IOException;
    List<String> getListOfGenres();
    List<String> getListOfAlbum();
    List<Track> getTracksByGenre(String genre) throws IOException;
    List<Track> getTracksByAlbum(String album) throws IOException;

    String getDomain();

    enum Type{
        SIGNER
    }
}
