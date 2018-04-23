package com.example.oollan.newsapp.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.oollan.newsapp.QueryUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

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
        if (url == null) {
            return null;
        }
        List<News> newsList = null;
        try {
            newsList = QueryUtils.fetchDataFromServer(url);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }
}