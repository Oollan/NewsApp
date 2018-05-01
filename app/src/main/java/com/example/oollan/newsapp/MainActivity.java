package com.example.oollan.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oollan.newsapp.news.News;
import com.example.oollan.newsapp.news.NewsAdapter;
import com.example.oollan.newsapp.news.NewsLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.oollan.newsapp.SettingsActivity.NewsPreferenceFragment.*;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks
        <List<News>> {

    public static final int NEWS_LOADER_ID = 0;
    public static final String DASH_SEPARATOR = " / ";
    public static final Calendar c = Calendar.getInstance();
    public static int year = c.get(Calendar.YEAR);
    public static int month = c.get(Calendar.MONTH) + 1;
    public static int day = c.get(Calendar.DAY_OF_MONTH);
    public static String date = day + DASH_SEPARATOR +
            month + DASH_SEPARATOR + year;
    public static String thumbnail = "thumbnail";
    private static final String URL = "http://content.guardianapis.com/search?section=games&" +
            "to-date=" + year + "-" + month + "-" + day + "&order-by=newest&" +
            "show-fields=" + thumbnail + "&api-key=86dc7c4d-c3db-49ef-8284-97be1527587a";
    private NewsAdapter adapter = new NewsAdapter();
    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    TextView emptyStateTextView;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        adapter.setNewsAdapter(new ArrayList<News>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        networkInitializer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void networkInitializer() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =
                connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingIndicator.setVisibility(View.VISIBLE);
        emptyStateTextView.setVisibility(View.VISIBLE);
        networkInitializer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.destroyLoader(NEWS_LOADER_ID);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        NewsLoader newsLoader = null;
        if (PreferenceManager.getDefaultSharedPreferences(this).contains(DATE_KEY)) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            thumbnail = preferences.getString(IMAGE_SWITCH_KEY, thumbnail);
            String orderBy = preferences
                    .getString(DATE_KEY, date);
            if (orderBy != null) {
                Uri baseUri = Uri.parse(URL);
                String[] splits = orderBy.split(DASH_SEPARATOR);
                Uri.Builder uriBuilder = baseUri.buildUpon();
                uriBuilder.appendQueryParameter
                        ("to-date", splits[2] + "-" + splits[1] + "-" + splits[0]);
                newsLoader = new NewsLoader(this, uriBuilder.toString());
            }
        } else {
            newsLoader = new NewsLoader(this, URL);
        }
        return newsLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        adapter.clearData();
        loadingIndicator.setVisibility(View.GONE);
        if (newsList != null && !newsList.isEmpty()) {
            adapter.setNewsAdapter(newsList);
            emptyStateTextView.setVisibility(View.GONE);
        } else {
            emptyStateTextView.setVisibility(View.VISIBLE);
            emptyStateTextView.setText(R.string.no_news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clearData();
    }
}