package com.example.oollan.newsapp.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.DatePicker;

import java.util.Calendar;

import static com.example.oollan.newsapp.utils.Constants.*;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private Calendar c = Calendar.getInstance();
    private int year = c.get(Calendar.YEAR);
    private int month = c.get(Calendar.MONTH) + 1;
    private int day = c.get(Calendar.DAY_OF_MONTH);
    private PreferenceInterface preferenceInterface;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), this,
                year, month - 1, day);
    }

    public void setPreferenceInterface(PreferenceInterface preferenceInterface) {
        this.preferenceInterface = preferenceInterface;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String key = (view.getDayOfMonth()
                + DASH_SEPARATOR + (view.getMonth() + 1) +
                DASH_SEPARATOR + view.getYear());
        PreferenceManager.getDefaultSharedPreferences
                (getActivity()).edit().putString(DATE_KEY, key)
                .apply();
        preferenceInterface.populatePreference(year, month + 1, day);
    }
}