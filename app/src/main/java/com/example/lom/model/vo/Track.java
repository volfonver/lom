package com.example.lom.model.vo;

public class Track {
    //    private String site;
    private String singer;
    private String title;
    private String urlImage;
    private String urlToLoading;
    private int duration;

    public Track(String singer, String title, String urlImage, String urlToLoading, int duration) {
        this.singer = singer;
        this.title = title;
        this.urlImage = urlImage;
        this.urlToLoading = urlToLoading;
        this.duration = duration;
    }

    public String getSinger() {
        return singer;
    }

    public String getTitle() {
        return title;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getUrlToLoading() {
        return urlToLoading;
    }

    public void setUrlToLoading(String urlToLoading) {
        this.urlToLoading = urlToLoading;
    }

    public int getDuration() {
        return duration;
    }

}