package com.example.oollan.newsapp.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import static com.example.oollan.newsapp.QueryUtils.fetchDataFromServer;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        List<News> newsList = null;
        try {
            newsList = fetchDataFromServer(url);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }
}