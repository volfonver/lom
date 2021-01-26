package com.example.lom.util;

import android.os.AsyncTask;

import com.example.lom.sites.MusicSite;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Aggregator extends AsyncTask<String, Void, Boolean> {
    private MusicSite[] platforms;
    public Aggregator(MusicSite... platforms) {
        this.platforms = platforms;
    }
    //    https://z1.fm/mp3/search?keywords=газманов

    @Override
    protected Boolean doInBackground(String... strings) {
        for (MusicSite mp : platforms) {
//            mp.aggregate(strings[0]);
//            if (!finished) System.out.println(mp.getClass().getSimpleName() + " - do not finished...");
        }
        return null;
    }

    private Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.94 Safari/537.36")
                // кто клиент - браузер Mozilla
                .referrer("https://www.google.com/") // Это как бы с какого сайта (url) ты перешел на этот.
//                .header("Cache-Control", "max-age=0") //
//                .header("Origin", "https://z1.com/") //
//                .header("Upgrade-Insecure-Requests", "1") //
                .header("Content-Type", "application/x-www-form-urlencoded") //
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8") //
                .header("Accept-Encoding", "gzip, deflate, br") //
                .header("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7") //
                .get();
    }
}
