package com.example.lom.sites;

import android.media.TimedMetaData;

import com.example.lom.model.vo.Track;
import com.example.lom.util.UtilClass;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class Sefon implements MusicSite {
    private static final String DOMAIN = "https://sefon.pro";
    private static final String HOME = DOMAIN + "/best/";

    private static final String SEARCH_PATTERN = DOMAIN + "/search/?q=%s";
    private static final String GENRES_PATTERN = DOMAIN + "/genres/%s";
    private static final String ALBUM_PATTERN = DOMAIN + "%s";

    private static final Map<String, String> GENRES = new HashMap<>();
    private static final Map<String, String> ALBUM = new HashMap<>();

    static {
        ALBUM.put("Русские хиты 90", "/collections/retro/177-russkie-hity-90/");
        ALBUM.put("За рулем", "/collections/summer/290-car_music/");
        ALBUM.put("Дискотека 80", "/collections/retro/15-diskoteka-80/");
        ALBUM.put("Басы в машину", "/collections/cool/306-basy-v-mashinu/");
        ALBUM.put("Рэп о любви", "/collections/cool/437-rap-o-lyubvi/");
        ALBUM.put("Rapsody 90-х", "/collections/retro/539-rapsody-90/");
        ALBUM.put("Воспоминания", "/collections/nastroenie/199-vospominaniya/");
        ALBUM.put("Поп-классика", "/collections/retro/747-tantsevalnaya-pop-klassika/");
        ALBUM.put("Украинские", "/collections/469-ukrainskie-pesni/");
        ALBUM.put("День рождения", "/collections/prazdniki/179-den-rozhdeniya/");
        ALBUM.put("Кавказские", "/collections/473-kavkazskie-pesni/");
        ALBUM.put("Зарубежные песни 90-х", "/collections/retro/373-zarubezhnye-pesni-90-h/");
        ALBUM.put("Советский Новый год", "/collections/retro/705-sovetskij-novyj-god/");
        ALBUM.put("Deep House", "/collections/cool/307-deep-house/");
        ALBUM.put("ВИА 70-80 годов", "/collections/retro/537-via-70-80/");
        ALBUM.put("Крутой клубняк", "/collections/cool/308-krutoj-klubnyak/");
        ALBUM.put("Шансон в дорогу", "/collections/417-shanson-v-dorogu/");
        ALBUM.put("Захватывает дух", "/collections/relationship/818-zahvatyvaet-duh/");
        ALBUM.put("Русский рок", "/collections/381-russkij-rok/");
        ALBUM.put("Это любовь", "/collections/relationship/136-lyubov/");
        ALBUM.put("Узбекские", "/collections/471-uzbekskie-pesni/");
        ALBUM.put("Рок легенды", "/collections/retro/324-rok-legendy/");
        ALBUM.put("Жаркая вечеринка", "/collections/cool/310-vecherinka/");
        ALBUM.put("Ретро вечеринка", "/collections/retro/249-retro-vecherinka/");
        ALBUM.put("День рождения детям", "/collections/children/501-detskie-na-den-rozhdeniya/");
        ALBUM.put("K-Pop", "/collections/451-k-pop/");
        ALBUM.put("Eurodance", "/collections/364-eurodance/");
        ALBUM.put("Рок баллады", "/collections/332-rok-ballady/");
        ALBUM.put("Красивый женский вокал", "/collections/beautiful/237-zhenskij-vokal/");
        ALBUM.put("Звезды хип-хопа", "/collections/cool/715-foreign-hip-hop/");
    }

    private static List<Track> tracks = new ArrayList<>();

    @Override
    public List<Track> getDefaultTracks() throws IOException {
        tracks.clear();
        Document doc = Jsoup.connect(HOME).get();
        Elements info = doc.select("div[class=content]");
        for (Element e : info) {
            Element el = e.selectFirst("div[class=mp3]");

            String signer = el.selectFirst("div.artist_name").text();
            String name = el.selectFirst("div.song_name").text();
//        String img = element.selectFirst();
            String url = e.attr("data-mp3");
            int duration = UtilClass.getNanosecondsOnDuration(e.selectFirst("span.value").text());
            Track track = new Track(signer, name, null, url, duration);

            track.setUrlToLoading(DOMAIN + track.getUrlToLoading());
            tracks.add(track);
        }
        return tracks;
    }

    @Override
    public List<Track> getTracksBySigner(String signer) throws IOException {tracks.clear();
        /*Document doc = Jsoup.connect(SEARCH_PATTERN).get();
        Elements info = doc.select("div[class=content]");
        for (Element e : info) {
            Track track = getTrackFromElement(e);
            track.setUrlToLoading(DOMAIN + track.getUrlToLoading());
            tracks.add(track);
        }*/
        return tracks;
    }

    @Override
    public List<Track> getTracksByTitle(String title) throws IOException {
        /*Document doc = Jsoup.connect(SEARCH_PATTERN).get();
        Elements signers = doc.select("div.b_list_artists");
        Elements info = doc.select("div[class=content]");
        for (Element e : info) {
            Track track = getTrackFromElement(e);
            track.setUrlToLoading(DOMAIN + track.getUrlToLoading());
            tracks.add(track);
        }*/
        return tracks;
    }

    @Override
    public List<Track> getAllTracks(String title) throws IOException {
        /*Document doc = Jsoup.connect(SEARCH_PATTERN).get();
        Elements info = doc.select("div[class=content]");
        for (Element e : info) {
            Track track = getTrackFromElement(e);
            track.setUrlToLoading(DOMAIN + track.getUrlToLoading());
            tracks.add(track);
        }*/
        return tracks;
    }

    @Override
    public List<String> getListOfGenres() {
        return null;
    }

    @Override
    public List<String> getListOfAlbum() {
        return new ArrayList<>(ALBUM.keySet());
    }

    @Override
    public List<Track> getTracksByGenre(String genre) throws IOException {
//        Document doc = Jsoup.connect(String.format(GENRES_PATTERN, GENRES.get(genre))).get();
//        Elements info = doc.select("div[class=content]");
//        for (Element e : info) {
//            Track track = getTrackFromElement(e);
//            track.setUrlToLoading(DOMAIN + track.getUrlToLoading());
//            tracks.add(track);
//        }
        return tracks;
    }

    @Override
    public List<Track> getTracksByAlbum(String album) throws IOException {
        tracks.clear();
        Document doc = Jsoup.connect(String.format(ALBUM_PATTERN, ALBUM.get(album))).get();
        Elements info = doc.select("div.content").select("div.mp3");
        for (Element e : info) {
            Track track = getTrackFromElement(e);
            track.setUrlToLoading(DOMAIN + track.getUrlToLoading());
            tracks.add(track);
        }
        return tracks;
    }

    private Track getTrackFromElement(Element el) {
        System.out.println(el == null);
        String signer = el.selectFirst("div.artist_name").text();
        String name = el.selectFirst("div.song_name").text();
//        String img = element.selectFirst();
        System.out.println(el.selectFirst("div.btns").html());
        String url = el.selectFirst("div.btns").getElementsByTag("a").attr("data-url");
        int duration = UtilClass.getNanosecondsOnDuration(el.selectFirst("span.value").text());
        return new Track(signer, name, null, url, duration);
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
}
