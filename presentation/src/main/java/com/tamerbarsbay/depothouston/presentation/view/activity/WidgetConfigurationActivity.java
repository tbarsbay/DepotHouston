package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.appwidget.AppWidgetManager;
import android.os.Bundle;

/**
 * An Activity where the user can select the route and stop for which to track arrivals
 * within a widget.
 */
public abstract class WidgetConfigurationActivity extends BaseActivity {

    // Used to uniquely track each widget that the user places
    protected int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If we get an Intent without the widget id, cancel the Activity
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        //TODO load routes
        //TODO populate spinners (bg options)
        //TODO butterknife bind
        //select a route
        //select a direction
        //select a stop
        //customize (set title, set background style)
    }

    //TODO reset selections and start over

    //TODO add option to choose from saved stops

    protected abstract void getWidgetSizeAsString();
}
