package com.example.oollan.newsapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;

import butterknife.ButterKnife;

import static com.example.oollan.newsapp.MainActivity.*;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements
            Preference.OnPreferenceChangeListener {

        public static SharedPreferences preferences;
        public static final String DATE_KEY = "date";
        public static final String IMAGE_SWITCH_KEY = "image_switch";
        private static final String DATE_SWITCH_KEY = "date_switch";
        public static SwitchPreference imagesSwitchPreference;
        private static SwitchPreference dateSwitchPreference;
        private static Preference datePreference;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ButterKnife.bind(getActivity());
            addPreferencesFromResource(R.xml.settings_main);
            imagesSwitchPreference = (SwitchPreference)
                    findPreference(getString(R.string.settings_images_switch_key));
            dateSwitchPreference = (SwitchPreference) findPreference
                    (getString(R.string.settings_date_switch_key));
            datePreference = findPreference(getString(R.string.settings_date_key));
            preferenceInitializer();
            imagesSwitchPreference.setOnPreferenceChangeListener
                    (new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object o) {
                            if (imagesSwitchPreference.isChecked()) {
                                thumbnail = "";
                                preferences.edit().putString(IMAGE_SWITCH_KEY, thumbnail)
                                        .apply();
                            } else {
                                thumbnail = "thumbnail";
                                preferences.edit().putString(IMAGE_SWITCH_KEY, thumbnail)
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
                                preferences.edit().putBoolean(DATE_SWITCH_KEY, false)
                                        .apply();
                                dateSwitchPreference.setChecked
                                        (preferences.getBoolean(DATE_SWITCH_KEY, false));
                                datePreference.setSummary(preferences.getString(DATE_KEY, date));
                            } else {
                                datePreference.setEnabled(false);
                                preferences.edit().putBoolean(DATE_SWITCH_KEY, true)
                                        .apply();
                                dateSwitchPreference.setChecked
                                        (preferences.getBoolean(DATE_SWITCH_KEY, false));
                                preferences.edit().putString(DATE_KEY, date)
                                        .apply();
                                datePreference.setSummary(date);
                            }
                            return true;
                        }
                    });
            datePreference.setOnPreferenceClickListener
                    (new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(final Preference preference) {
                            DialogFragment newFragment = new DatePickerFragment();
                            newFragment.show(getFragmentManager(), null);
                            return true;
                        }
                    });
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }

        public void preferenceInitializer() {
            if (preferences != null) {
                dateSwitchPreference.setChecked
                        (preferences.getBoolean(DATE_SWITCH_KEY, false));
                if (preferences.getBoolean(DATE_SWITCH_KEY, false)) {
                    datePreference.setEnabled(false);
                } else {
                    datePreference.setEnabled(true);
                }
                datePreference.setSummary(preferences.getString(DATE_KEY, date));
            } else {
                preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                datePreference.setEnabled(false);
                datePreference.setSummary(date);
            }
        }

        public static class DatePickerFragment extends DialogFragment {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                return new DatePickerDialog(getActivity(), dateSetListener,
                        year, month - 1, day);
            }

            private DatePickerDialog.OnDateSetListener dateSetListener =
                    new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            datePreference.setKey(view.getDayOfMonth()
                                    + DASH_SEPARATOR + (view.getMonth() + 1) +
                                    DASH_SEPARATOR + view.getYear());
                            preferences.edit().putString(DATE_KEY, datePreference.getKey())
                                    .apply();
                            datePreference.setSummary(preferences.getString
                                    (DATE_KEY, date));
                        }
                    };
        }
    }
}