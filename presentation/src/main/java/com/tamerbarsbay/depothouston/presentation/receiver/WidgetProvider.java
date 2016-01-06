package com.tamerbarsbay.depothouston.presentation.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.tamerbarsbay.depothouston.R;

public abstract class WidgetProvider extends AppWidgetProvider {

    // Intent action to update widget
    private static final String ACTION_UPDATE_ARRIVALS = "com.tamerbarsbay.depothouston.presentation.WIDGET_UPDATE_ARRIVALS_1X1";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(ACTION_UPDATE_ARRIVALS)) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateWidget(Context context, AppWidgetManager widgetManager, int widgetId) {
        //TODO get all the values from shared prefs
        //TODO get arrival times
        RemoteViews updatedViews = new RemoteViews(context.getPackageName(), R.layout.); //TODO get the right layout by size
        //TODO update remoteviews bg primary color
        //TODO update remoteviews bg secondary color
        //TODO if white bg, set text colors and divider colors
        //TODO set arrival text
        //TODO set title text
        //TODO set on click pending intent for both arrival layout and title layout
        widgetManager.updateAppWidget(widgetId, updatedViews);
    }

    private static PendingIntent createRefreshIntent(Context context, int widgetId) {
        Intent intent = new Intent(ACTION_UPDATE_ARRIVALS);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
