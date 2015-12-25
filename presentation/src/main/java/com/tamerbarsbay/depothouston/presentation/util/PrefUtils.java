package com.tamerbarsbay.depothouston.presentation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {

    private static final String PREFS_NEARBY_STOPS = "prefs_nearby_stops";

    public static void setFilterNearbyRoutesEnabled(Context context, boolean enabled) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        s.edit().putBoolean(PREFS_NEARBY_STOPS, enabled).apply();
    }

    public static boolean isFilterNearbyRoutesEnabled(Context context) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        return s.getBoolean(PREFS_NEARBY_STOPS, false);
    }

}
