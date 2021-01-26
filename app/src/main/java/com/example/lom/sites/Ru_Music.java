package com.example.lom.sites;

import com.example.lom.model.vo.Track;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ru_Music implements MusicSite {
    public static List<Track> tracks = new ArrayList<>();
    private static final String DOMAIN = "https://ru-music.com";
    private static final String SEARCH_PATTERN = DOMAIN + "/search/%s";
    private static final String GENRES_PATTERN = DOMAIN + "/genre/%s";
    private static final String ALBUM_PATTERN = DOMAIN + "/album/%s";

    private static final Map<String, String> GENRES = new HashMap<>();
    private static final Map<String, String> ALBUM = new HashMap<>();

    static {
        GENRES.put("Попса", "pop");
        GENRES.put("Рок", "rock");
        GENRES.put("Рэп / хип-хоп", "rap");
        GENRES.put("Альтернатива", "alternative");
        GENRES.put("Панк", "punk");
        GENRES.put("Регги", "reggae");
        GENRES.put("Металл", "metal");
        GENRES.put("Шансон", "chanson");
        GENRES.put("Дабстеп", "dubstep");
        GENRES.put("Инструментальная", "instrumental");
        GENRES.put("Фолк", "ethnic");
        GENRES.put("Дэнс и Хаус", "dance-and-house");
        GENRES.put("Медляки", "relax");
        GENRES.put("Акустика и вокал", "vocal");
        GENRES.put("Транс", "trance");
        GENRES.put("Танцевальная Дискотечная", "dance");
        GENRES.put("Драм-н-бэйс", "drum-and-bass");
        GENRES.put("Саундтреки", "ost-soundtrack");
        GENRES.put("Классика", "classic");

        ALBUM.put("Золотые хиты 2000, топ 100 песен", "16-топ-100-популярных-песен-2000");
        ALBUM.put("Песни на Рождество и Новый год", "15-рождественские-и-новогодние-песни");
        ALBUM.put("Популярные хиты 80х", "14-greatest-hits-of-the-80");
        ALBUM.put("Популярная музыка 70-х", "13-популярные-хиты-70");
        ALBUM.put("Популярные песни 60х", "12-песни-60");
        ALBUM.put("Rock'n'Roll: Топ самых популярных хитов рок-н-ролла", "11-rock-n-roll");
        ALBUM.put("Музыка для детей - детские песни", "10-детские-песни");
        ALBUM.put("Свадебные песни", "9-свадебные-песни");
        ALBUM.put("Песни ко дню Рождения", "8-песни-ко-дню-рождения");
        ALBUM.put("Старые песни, музыка 50х годов", "7-старые-песни-музыка-50-х-годов");
        ALBUM.put("Топ Самых популярных и известных песен мира", "6-самые-известные-песни-в-мире");
        ALBUM.put("Песни ко дню победы - 9 мая", "5-песни-на-9-мая");
        ALBUM.put("Русские Народные Песни", "4-русские-народные-песни");
        ALBUM.put("Зарубежные золотые хиты 90х", "3-зарубежные-хиты-90х");
        ALBUM.put("Русские хиты 90х", "2-русские-хиты-90х");
        ALBUM.put("Популярные хиты 90х", "1-хиты-90х");
    }

    @Override
    public List<Track> getDefaultTracks() throws IOException {
        tracks.clear();
        Document doc = Jsoup.connect(DOMAIN).get();
        Elements info = doc.select("li[class=track]");
        for (Element e : info) {
            Track track = getTrackFromElement(e);
            track.setUrlToLoading(DOMAIN + track.getUrlToLoading());
            tracks.add(track);
        }
        return tracks;
    }

    @Override
    public List<Track> getTracksBySigner(String signer) throws IOException {
        List<Track> list = new ArrayList<>();
        for (Track track : getAllTracks(signer)) {
            if (track.getSinger().equalsIgnoreCase(signer)) list.add(track);
        }
        return list;
    }

    @Override
    public List<Track> getTracksByTitle(String title) throws IOException {
        List<Track> list = new ArrayList<>();
        for (Track track : getAllTracks(title)) {
            if (track.getTitle().equalsIgnoreCase(title)) list.add(track);
        }
        return list;
    }

    @Override
    public List<Track> getAllTracks(String title) throws IOException {
        tracks.clear();
        Document doc = Jsoup.connect(String.format(SEARCH_PATTERN, title)).get();
        Elements info = doc.select("li[class=track]");
        for (Element e : info) {
            Track track = getTrackFromElement(e);
            track.setUrlToLoading(DOMAIN + track.getUrlToLoading());
            tracks.add(track);
        }
        return tracks;
    }

    @Override
    public List<String> getListOfGenres() {
        return new ArrayList<>(GENRES.keySet());
    }

    @Override
    public List<String> getListOfAlbum() {
        return new ArrayList<>(ALBUM.keySet());
    }

    @Override
    public List<Track> getTracksByGenre(String genre) throws IOException {
        tracks.clear();
        Document doc = Jsoup.connect(String.format(GENRES_PATTERN, GENRES.get(genre))).get();
        Elements info = doc.select("li[class=track]");
        for (Element e : info) {
            tracks.add(getTrackFromElement(e));
        }
        return tracks;
    }

    @Override
    public List<Track> getTracksByAlbum(String album) throws IOException {
        tracks.clear();
        Document doc = Jsoup.connect(String.format(ALBUM_PATTERN, ALBUM.get(album))).get();
        Elements info = doc.select("li[class=track]");
        for (Element e : info) {
            Track track = getTrackFromElement(e);
            track.setUrlToLoading(DOMAIN + track.getUrlToLoading());
            tracks.add(track);
        }
        return tracks;
    }

    private Track getTrackFromElement(Element element) {
        Element el = element.selectFirst("h2[class=playlist-name]");

        String signer = el.getElementsByTag("b").first().text();
        String name = el.getElementsByTag("em").first().text();
        String img = element.selectFirst("div.cover_image").selectFirst("img").attr("src");
        String url = element.attr("data-mp3");
        int duration = Integer.parseInt(element.attr("data-duration"));

        return new Track(signer, name, img, url, duration);
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
}
