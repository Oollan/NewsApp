package com.example.oollan.newsapp.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
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

import com.example.oollan.newsapp.R;
import com.example.oollan.newsapp.news.News;
import com.example.oollan.newsapp.news.NewsAdapter;
import com.example.oollan.newsapp.news.NewsLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.oollan.newsapp.utils.Constants.*;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks
        <List<News>> {

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
        networkBinder(networkInitializer());
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

    private NetworkInfo networkInitializer() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService
                (Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
    }

    private void networkBinder(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    public String getUrl() {
        String[] splits = getCurrentDate().split(DASH_SEPARATOR);
        return "http://content.guardianapis.com/search?section=games&" +
                "to-date=" + splits[2] + "-" + splits[1] + "-" + splits[0] + "&order-by=newest&" +
                "show-fields=thumbnail&api-key=86dc7c4d-c3db-49ef-8284-97be1527587a";
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingIndicator.setVisibility(View.VISIBLE);
        emptyStateTextView.setVisibility(View.VISIBLE);
        networkBinder(networkInitializer());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.destroyLoader(NEWS_LOADER_ID);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        NewsLoader newsLoader;
        if (PreferenceManager.getDefaultSharedPreferences(this).contains(DATE_KEY)) {
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(this);
            String orderBy = preferences
                    .getString(DATE_KEY, getCurrentDate());
            Uri baseUri = Uri.parse(getUrl());
            String[] splits = orderBy.split(DASH_SEPARATOR);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter
                    ("to-date", splits[2] + "-" + splits[1] + "-" + splits[0]);
            newsLoader = new NewsLoader(this, uriBuilder.toString());
        } else {
            newsLoader = new NewsLoader(this, getUrl());
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