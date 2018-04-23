package com.example.oollan.newsapp.news;

import android.graphics.Bitmap;

public class News {

    private Bitmap thumbnail;
    private String title;
    private String date;
    private String url;

    public News(Bitmap thumbnail, String title, String date, String url) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.date = date;
        this.url = url;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}