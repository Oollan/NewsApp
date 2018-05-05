package com.example.oollan.newsapp.news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.example.oollan.newsapp.R;
import com.example.oollan.newsapp.utils.DatePickerFragment;
import com.example.oollan.newsapp.utils.PreferenceInterface;

import butterknife.ButterKnife;

import static com.example.oollan.newsapp.utils.Constants.*;

public class NewsPreferenceFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private SwitchPreference imagesSwitchPreference;
    private SwitchPreference dateSwitchPreference;
    private Preference datePreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());
        addPreferencesFromResource(R.xml.settings_main);
        imagesSwitchPreference = (SwitchPreference)
                findPreference(getString(R.string.settings_images_switch_key));
        dateSwitchPreference = (SwitchPreference)
                findPreference(getString(R.string.settings_date_switch_key));
        datePreference = findPreference(getString(R.string.settings_date_key));
        preferenceInitializer();
        setListeners();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();
        preference.setSummary(stringValue);
        return true;
    }

    public void preferenceInitializer() {
        SharedPreferences preferences = PreferenceManager.
                getDefaultSharedPreferences(getActivity());
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()) != null) {
            dateSwitchPreference.setChecked
                    (preferences.getBoolean(DATE_SWITCH_KEY, false));
            if (preferences.getBoolean(DATE_SWITCH_KEY, false)) {
                datePreference.setEnabled(false);
            } else {
                datePreference.setEnabled(true);
            }
            datePreference.setSummary(preferences.getString(DATE_KEY, getCurrentDate()));
        } else {
            datePreference.setEnabled(false);
            datePreference.setSummary(getCurrentDate());
        }
    }

    public void setListeners() {
        imagesSwitchPreference.setOnPreferenceChangeListener
                (new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        if (imagesSwitchPreference.isChecked()) {
                            PreferenceManager.getDefaultSharedPreferences(getActivity())
                                    .edit().putBoolean(IMAGE_SWITCH_KEY, false)
                                    .apply();
                        } else {
                            PreferenceManager.getDefaultSharedPreferences(getActivity())
                                    .edit().putBoolean(IMAGE_SWITCH_KEY, true)
                                    .apply();
                        }
                        return true;
                    }
                });

        dateSwitchPreference.setOnPreferenceChangeListener
                (new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        if (dateSwitchPreference.isChecked()) {
                            datePreference.setEnabled(true);
                            PreferenceManager.getDefaultSharedPreferences(getActivity())
                                    .edit().putBoolean(DATE_SWITCH_KEY, false)
                                    .apply();
                            dateSwitchPreference.setChecked
                                    (PreferenceManager.getDefaultSharedPreferences(getActivity())
                                            .getBoolean(DATE_SWITCH_KEY, false));
                            datePreference.setSummary
                                    (PreferenceManager.getDefaultSharedPreferences(getActivity())
                                            .getString(DATE_KEY, getCurrentDate()));
                        } else {
                            datePreference.setEnabled(false);
                            PreferenceManager.getDefaultSharedPreferences(getActivity())
                                    .edit().putBoolean(DATE_SWITCH_KEY, true)
                                    .apply();
                            dateSwitchPreference.setChecked
                                    (PreferenceManager.getDefaultSharedPreferences(getActivity())
                                            .getBoolean(DATE_SWITCH_KEY, false));
                            PreferenceManager.getDefaultSharedPreferences(getActivity())
                                    .edit().putString(DATE_KEY, getCurrentDate())
                                    .apply();
                            datePreference.setSummary(getCurrentDate());
                        }
                        return true;
                    }
                });

        datePreference.setOnPreferenceClickListener
                (new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(final Preference preference) {
                        DatePickerFragment newFragment = new DatePickerFragment();
                        PreferenceInterface preferenceInterface = new PreferenceInterface() {
                            @Override
                            public void populatePreference(int year, int month, int day) {
                                datePreference.setSummary
                                        (day + DASH_SEPARATOR + month + DASH_SEPARATOR + year);
                            }
                        };
                        newFragment.setPreferenceInterface(preferenceInterface);
                        newFragment.show(getFragmentManager(), null);
                        return true;
                    }
                });
    }
}
