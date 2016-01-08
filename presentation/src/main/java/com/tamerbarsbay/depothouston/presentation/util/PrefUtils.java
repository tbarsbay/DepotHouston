package com.tamerbarsbay.depothouston.presentation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tamerbarsbay.depothouston.presentation.model.WidgetModel;

public class PrefUtils {

    private static final String PREFS_KEY_NEARBY_STOPS = "prefs_nearby_stops";
    private static final String PREFS_KEY_WIDGET = "widget_";

    public static void setFilterNearbyRoutesEnabled(Context context, boolean enabled) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        s.edit().putBoolean(PREFS_KEY_NEARBY_STOPS, enabled).apply();
    }

    public static boolean isFilterNearbyRoutesEnabled(Context context) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
        return s.getBoolean(PREFS_KEY_NEARBY_STOPS, false);
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

    private static String getWidgetPrefsKey(int widgetId) {
        return PREFS_KEY_WIDGET + widgetId;
    }

}
