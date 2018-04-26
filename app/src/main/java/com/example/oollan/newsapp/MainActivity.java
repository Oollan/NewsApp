package com.example.oollan.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oollan.newsapp.news.News;
import com.example.oollan.newsapp.news.NewsAdapter;
import com.example.oollan.newsapp.news.NewsLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks
        <List<News>> {

    private static final String URL = "http://content.guardianapis.com/search?section=games&" +
            "from-date=2018-01-01&order-by=newest&show-fields=thumbnail&" +
            "api-key=86dc7c4d-c3db-49ef-8284-97be1527587a";
    public static final int NEWS_LOADER_ID = 0;
    private NewsAdapter adapter = new NewsAdapter();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
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
        return new NewsLoader(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> earthquakes) {
        adapter.clearData();
        loadingIndicator.setVisibility(View.GONE);
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.setNewsAdapter(earthquakes);
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