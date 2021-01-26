package com.example.lom.model;

import android.os.AsyncTask;

import com.example.lom.IUpdate;
import com.example.lom.model.vo.Track;
import com.example.lom.sites.MusicSite;
import com.example.lom.sites.Ru_Music;
import com.example.lom.sites.Sefon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SiteFactory {
    private static SiteFactory factory;
    private static IUpdate update;
    private static final Map<Type, MusicSite> MUSIC_SITE_MAP = new HashMap<Type, MusicSite>(){
        {
            put(Type.RU_MUSIC, new Ru_Music());
            put(Type.SEFON, new Sefon());
        }
    };

    private static MusicSite currentSite = MUSIC_SITE_MAP.get(Type.RU_MUSIC);

    private SiteFactory(IUpdate i) {
        update = i;
    }

    public void search() {
        /*List<Track> list = new ArrayList<>();
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        list.add(new Track("kakagfhfg", "------------", "fghghf", "fsf", 10));
        update.updateList(list);*/
        new AnalyzerTask(SearchType.DEFAULT).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    public void search(SearchType type, String searchStr) {
        System.out.println("search");
        new AnalyzerTask(type).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, searchStr);
    }

    public static SiteFactory init(IUpdate i) {
        if (factory != null) return factory;
        factory = new SiteFactory(i);
        return factory;
    }

    private static class AnalyzerTask extends AsyncTask<String, Void, List<Track>> {
        private SearchType type;

        public AnalyzerTask(SearchType type) {
            this.type = type;
        }

        @Override
        protected List<Track> doInBackground(String... strings) {
            try {
                System.out.println(currentSite.getClass().getSimpleName());
                return type.get(currentSite, strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new LinkedList<>();
        }

        @Override
        protected void onPostExecute(List<Track> list) {
            super.onPostExecute(list);
            System.out.println("SiteTypeFactory.AnalyzerTask.onPostExecute, size = " + list.size());
            update.updateList(list);
        }
    }

    public static MusicSite getCurrentSite() {
        return currentSite;
    }

    public static void setCurrentSite(Type type) {
        currentSite = MUSIC_SITE_MAP.get(type);
    }

    public static enum Type {
        RU_MUSIC,
        SEFON;
    }
}
