package com.example.lom.model;

import com.example.lom.model.vo.Track;
import com.example.lom.sites.MusicSite;

import java.io.IOException;
import java.util.List;

public enum SearchType {
    DEFAULT{
        @Override
        List<Track> get(MusicSite site, String s) throws IOException {
            return site.getDefaultTracks();
        }
    },
    TITLE{
        @Override
        List<Track> get(MusicSite site, String s) throws IOException {
            return site.getTracksByTitle(s);
        }
    },
    SIGNER{
        @Override
        List<Track> get(MusicSite site, String s) throws IOException {
            return site.getTracksBySigner(s);
        }
    },
    GENRE{
        @Override
        List<Track> get(MusicSite site, String s) throws IOException {
            return site.getTracksByGenre(s);
        }
    },
    ALBUM{
        @Override
        List<Track> get(MusicSite site, String s) throws IOException {
            return site.getTracksByAlbum(s);
        }
    },
    ALL{
        @Override
        List<Track> get(MusicSite site, String s) throws IOException {
            return site.getAllTracks(s);
        }
    };

    abstract List<Track> get(MusicSite site, String s) throws IOException;
}
