package com.tamerbarsbay.depothouston.presentation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamerbarsbay.depothouston.presentation.model.RecentStopModel;

import java.util.ArrayList;

public class RecentStopUtils {

    private static final String LOG_TAG = "RecentStopUtils";

    private static final String PREFS_RECENT_STOPS = "prefs_recent_stops";

    public static ArrayList<RecentStopModel> getRecentStops(@NonNull Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String recentStopsString = sp.getString(PREFS_RECENT_STOPS, null);
        if (recentStopsString != null) {
            ArrayList<RecentStopModel> recentStops =
                    new Gson().fromJson(
                            recentStopsString,
                            new TypeToken<ArrayList<RecentStopModel>>(){}.getType());
            if (recentStops != null) {
                return recentStops;
            }
        }
        return new ArrayList<RecentStopModel>();
    }

    public static void addRecentStop(@NonNull Context context, @NonNull RecentStopModel stop) {
        ArrayList<RecentStopModel> recentStops = getRecentStops(context);
        if (!isRecentStop(context, stop)) {
            recentStops.add(stop);
            saveRecentStopsToSharedPreferences(context, recentStops);
        }
    }

    public static void clearRecentStops(@NonNull Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().remove(PREFS_RECENT_STOPS).apply();
    }

    private static void saveRecentStopsToSharedPreferences(@NonNull Context context,
                                                           ArrayList<RecentStopModel> stops) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String recentStopsString = new Gson().toJson(stops);
        if (recentStopsString != null) {
            sp.edit().putString(PREFS_RECENT_STOPS, recentStopsString).apply();
        }
    }

    private static boolean isRecentStop(@NonNull Context context, RecentStopModel stop) {
        for (RecentStopModel recentStop : getRecentStops(context)) {
            if (stop.getStopId().equals(recentStop.getStopId())) {
                return true;
            }
        }
        return false;
    }

}
