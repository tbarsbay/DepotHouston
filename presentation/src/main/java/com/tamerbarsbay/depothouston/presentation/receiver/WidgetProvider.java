package com.tamerbarsbay.depothouston.presentation.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.WidgetModel;
import com.tamerbarsbay.depothouston.presentation.util.PrefUtils;
import com.tamerbarsbay.depothouston.presentation.util.WidgetUtils;

public abstract class WidgetProvider extends AppWidgetProvider {

    // Intent action to update widget
    private static final String ACTION_UPDATE_ARRIVALS = "com.tamerbarsbay.depothouston.presentation.WIDGET_UPDATE_ARRIVALS_1X1";

    private static final String SET_BACKGROUND_COLOR = "setBackgroundColor";

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
        WidgetModel widgetModel = PrefUtils.getWidget(context, widgetId);
        if (widgetModel == null) {
            return;
        }
        //TODO get arrival times

        int layoutResId = widgetModel.getSize() == WidgetUtils.SIZE_1X1
                ? R.layout.widget_layout_1x1
                : R.layout.widget_layout_2x1;
        RemoteViews updatedViews = new RemoteViews(context.getPackageName(), layoutResId);
        updatedViews.setInt(
                R.id.tv_widget_title,
                SET_BACKGROUND_COLOR,
                WidgetUtils.getPrimaryColorInt(context, widgetModel.getBgColor()));
        updatedViews.setInt(
                R.id.layout_widget_arrivals,
                SET_BACKGROUND_COLOR,
                WidgetUtils.getSecondaryColorInt(context, widgetModel.getBgColor()));

        if (widgetModel.getBgColor() == WidgetUtils.BG_WHITE) {
            updatedViews.setTextColor(R.id.tv_widget_title, Color.DKGRAY);
            updatedViews.setTextColor(R.id.tv_widget_arrival_1, Color.DKGRAY);
            updatedViews.setTextColor(R.id.tv_widget_arrival_2, Color.DKGRAY);
            updatedViews.setInt(R.id.iv_widget_divider_1, SET_BACKGROUND_COLOR, Color.DKGRAY);
            if (widgetModel.getSize() != WidgetUtils.SIZE_1X1) {
                // There is a third arrival text to update
                updatedViews.setTextColor(R.id.tv_widget_arrival_3, Color.DKGRAY);
                updatedViews.setInt(R.id.iv_widget_divider_2, SET_BACKGROUND_COLOR, Color.DKGRAY);
            }
        }

        //TODO how do we handle updating text after receiving data, without an asynctask??
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
