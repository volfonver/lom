package com.example.lom.sites;

import com.example.lom.model.vo.Track;
import com.example.lom.util.UtilClass;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mp3xa implements MusicSite {
    public static List<Track> tracks = new ArrayList<>();
    private static final String DOMAIN = "https://mp3xa.cc";
    private static final String SEARCH_PATTERN = DOMAIN + "/search/%s";
    private static final String GENRES_PATTERN = DOMAIN + "/genres.html/";
    private static final String ALBUM_PATTERN = DOMAIN + "/album/%s";

    @Override
    public List<Track> getDefaultTracks() throws IOException {
        tracks.clear();
        Elements elements = getDocument(DOMAIN, null, null).select("div.plyr-item");

        for (Element e : elements) {
            String title = e.selectFirst("div.song_name").text();
            String signer = e.selectFirst("div.name").text();
            String urlImage = e.select("div.ya-share2 social").attr("data-image");
            String urlToLoading = e.select("a.play").attr("data-url");
                int duration = UtilClass.getNanosecondsOnDuration(e.select("span.duration").text());
            tracks.add(new Track(signer, title, urlImage, urlToLoading, duration));
        }
        return tracks;
    }

    @Override
    public List<Track> getTracksBySigner(String signer) throws IOException {
        Document document = getDocument(DOMAIN, "3", signer);
        return null;
    }

    @Override
    public List<Track> getTracksByTitle(String title) throws IOException {
        Document document = getDocument(DOMAIN, "2", title);
        return null;
    }

    @Override
    public List<Track> getAllTracks(String title) throws IOException {
        Document document = getDocument(DOMAIN, "0", title);
        return null;
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

    /**
     * @param type 0 = all, 1 = album, 2 = name, 3 = signer
     */
    public Document getDocument(String url, String type, String searchStr) throws IOException {
//        Connection.Response response = HttpConnection.connect(s).execute();
//        System.out.println(response.headers() + "\n");
//        System.out.println(response.body() + "\n");
//        System.out.println(response.cookies() + "\n");
        Connection conn = Jsoup.connect(url);
        return searchStr != null ? conn
                .data("type", type)
                .data("story", searchStr)
                .data("do", "search")
                .data("subaction", "search")
                .get() : conn.get();
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
}
