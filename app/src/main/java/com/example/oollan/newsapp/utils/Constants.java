package com.example.oollan.newsapp.utils;

import java.util.Calendar;

public class Constants {

    public static final int NEWS_LOADER_ID = 0;
    public static final String DASH_SEPARATOR = " / ";
    public static final String DATE_KEY = "date";
    public static final String IMAGE_SWITCH_KEY = "image_switch";
    public static final String DATE_SWITCH_KEY = "date_switch";

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return day + DASH_SEPARATOR +
                month + DASH_SEPARATOR + year;
    }
}