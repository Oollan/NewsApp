package com.example.oollan.newsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.oollan.newsapp.news.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    public static List<News> fetchDataFromServer(String requestUrl)
            throws IOException, JSONException {
        String jsonResponse = makeHttpRequest(requestUrl);
        return extractFeatureFromJson(jsonResponse);
    }

    private static String makeHttpRequest(String src) throws IOException {
        String jsonResponse = "";
        URL url = new URL(src);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        if (urlConnection.getResponseCode() == 200) {
            InputStream inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static Bitmap getBitmapFromURL(String src) throws IOException {
        URL url = new URL(src);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        return BitmapFactory.decodeStream(inputStream);
    }

    private static List<News> extractFeatureFromJson(String newsJSON)
            throws JSONException, IOException {
        List<News> newsList = new ArrayList<>();
        JSONObject baseJsonResponse = new JSONObject(newsJSON);
        JSONObject response = baseJsonResponse.getJSONObject("response");
        JSONArray newsArray = response.getJSONArray("results");
        for (int i = 0; i < newsArray.length(); i++) {
            JSONObject results = newsArray.getJSONObject(i);
            JSONObject fields = results.getJSONObject("fields");
            String thumbnailUrl = fields.optString("thumbnail");
            String title = results.optString("webTitle");
            String date = results.optString("webPublicationDate");
            String url = results.optString("webUrl");
            Bitmap thumbnail = getBitmapFromURL(thumbnailUrl);
            News news = new News(thumbnail, title, date, url);
            newsList.add(news);
        }
        return newsList;
    }
}