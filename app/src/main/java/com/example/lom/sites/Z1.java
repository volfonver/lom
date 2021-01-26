package com.example.lom.sites;

import com.example.lom.model.vo.Track;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Z1 implements MusicSite {

    @Override
    public List<Track> getDefaultTracks() {
        return Collections.emptyList();
    }

    @Override
    public List<Track> getTracksBySigner(String signer) {
        return Collections.emptyList();
    }

    @Override
    public List<Track> getTracksByTitle(String title) {
        return Collections.emptyList();
    }

    @Override
    public List<Track> getAllTracks(String title) throws IOException {
        return Collections.emptyList();
    }

    @Override
    public List<String> getListOfGenres() {
        return null;
    }

    @Override
    public List<String> getListOfAlbum() {
        return null;
    }

    @Override
    public List<Track> getTracksByGenre(String genre) throws IOException {
        return null;
    }

    @Override
    public List<Track> getTracksByAlbum(String album) throws IOException {
        return null;
    }

    @Override
    public String getDomain() {
        return null;
    }
}
