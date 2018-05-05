package com.example.oollan.newsapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.oollan.newsapp.R;
import com.example.oollan.newsapp.news.NewsPreferenceFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new NewsPreferenceFragment()).commit();
    }
}