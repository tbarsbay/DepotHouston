package com.tamerbarsbay.depothouston.presentation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tamerbarsbay.depothouston.presentation.model.ChangelogItemModel;
import com.tamerbarsbay.depothouston.presentation.model.WidgetModel;

import java.util.ArrayList;

/**
 * Utility fields and methods related to SharedPreference objects.
 */
public class PrefUtils {

    public static final String PREF_START_SCREEN = "pref_start_screen";
    public static final String PREF_NEARBY_THRESHOLD = "pref_nearby_threshold";
    public static final String PREF_CHANGELOG = "pref_changelog";
    public static final String PREF_RATE_APP = "pref_rate_app";
    public static final String PREF_ABOUT = "pref_about";
    private static final String PREFS_KEY_NEARBY_STOPS = "prefs_nearby_stops";
    private static final String PREFS_KEY_WIDGET = "widget_";

    private static final String DEFAULT_START_SCREEN_INDEX = "0";
    private static final String DEFAULT_NEARBY_THRESHOLD_INDEX = "3";

    public static final int START_SCREEN_ROUTE_LIST = 0;
    public static final int START_SCREEN_MAP_SEARCH = 1;
    public static final int START_SCREEN_SAVED_STOPS = 2;
    public static final int START_SCREEN_RECENT_STOPS = 3;

    public static void setFilterNearbyRoutesEnabled(Context context, boolean enabled) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        s.edit().putBoolean(PREFS_KEY_NEARBY_STOPS, enabled).apply();
    }

    public static boolean isFilterNearbyRoutesEnabled(Context context) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        return s.getBoolean(PREFS_KEY_NEARBY_STOPS, false);
    }

    public static String getNearbyThresholdInMiles(Context context) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        return "." + s.getString(PREF_NEARBY_THRESHOLD, DEFAULT_NEARBY_THRESHOLD_INDEX);
    }

    public static int getStartScreen(Context context) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        String startScreenIndex = s.getString(PREF_START_SCREEN, DEFAULT_START_SCREEN_INDEX);
        return Integer.parseInt(startScreenIndex);
    }

    public static void saveWidget(Context context, WidgetModel widgetModel) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        String widgetAsString = new Gson().toJson(widgetModel);
        s.edit().putString(getWidgetPrefsKey(widgetModel.getWidgetId()), widgetAsString).apply();
    }

    public static WidgetModel getWidget(Context context, int widgetId) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        String widgetAsString = s.getString(getWidgetPrefsKey(widgetId), null);
        if (widgetAsString != null) {
            return new Gson().fromJson(widgetAsString, WidgetModel.class);
        }
        return null;
    }

    public static void deleteWidget(Context context, int widgetId) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        s.edit().remove(getWidgetPrefsKey(widgetId)).apply();
    }

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static ArrayList<ChangelogItemModel> getChangelogItems() {
        ArrayList<ChangelogItemModel> changes = new ArrayList<ChangelogItemModel>();
        changes.add(new ChangelogItemModel("1.0", "January 8, 2016", "- Initial launch!"));
        return changes;
    }

    private static String getWidgetPrefsKey(int widgetId) {
        return PREFS_KEY_WIDGET + widgetId;
    }

}
