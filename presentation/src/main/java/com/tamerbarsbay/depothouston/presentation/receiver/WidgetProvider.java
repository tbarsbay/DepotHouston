package com.tamerbarsbay.depothouston.presentation.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.domain.Arrival;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.domain.interactor.GetArrivalsByStopAndRoute;
import com.tamerbarsbay.depothouston.presentation.AndroidApplication;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.DaggerWidgetProviderComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.WidgetProviderComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.WidgetProviderModule;
import com.tamerbarsbay.depothouston.presentation.mapper.ArrivalModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.model.ArrivalModel;
import com.tamerbarsbay.depothouston.presentation.model.WidgetModel;
import com.tamerbarsbay.depothouston.presentation.util.PrefUtils;
import com.tamerbarsbay.depothouston.presentation.util.WidgetUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class WidgetProvider extends AppWidgetProvider {

    @Inject
    static GetArrivalsByStopAndRoute getArrivalsByStopAndRoute;

    @Inject
    static ArrivalModelDataMapper arrivalModelDataMapper;

    // Intent action to update widget
    public static final String ACTION_UPDATE_ARRIVALS = "com.tamerbarsbay.depothouston.presentation.WIDGET_UPDATE_ARRIVALS";

    private static final String SET_BACKGROUND_COLOR = "setBackgroundColor";

    private static final int[] ARRIVAL_TEXTVIEW_RES_ID = new int[] {
            R.id.tv_widget_arrival_1,
            R.id.tv_widget_arrival_2,
            R.id.tv_widget_arrival_3
    };

    private static final String LOG_TAG = "WidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetProviderComponent component = DaggerWidgetProviderComponent.builder()
                .applicationComponent(((AndroidApplication)context.getApplicationContext()).getApplicationComponent())
                .widgetProviderModule(new WidgetProviderModule())
                .build();
        component.inject(this);

        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            executeArrivalsRequest(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(ACTION_UPDATE_ARRIVALS)) {
            WidgetProviderComponent component = DaggerWidgetProviderComponent.builder()
                    .applicationComponent(((AndroidApplication)context.getApplicationContext()).getApplicationComponent())
                    .widgetProviderModule(new WidgetProviderModule())
                    .build();
            component.inject(this);
            executeArrivalsRequest(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            PrefUtils.deleteWidget(context, appWidgetId);
        }
    }

    public static void executeArrivalsRequest(Context context, AppWidgetManager widgetManager, int widgetId) {
        WidgetModel widgetModel = PrefUtils.getWidget(context, widgetId);
        if (widgetModel == null) {
            return;
        }

        getArrivalsByStopAndRoute.setParameters(widgetModel.getRouteId(), widgetModel.getStopId());
        getArrivalsByStopAndRoute.execute(new WidgetArrivalsSubscriber(context, widgetManager, widgetModel));
    }

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                                     WidgetModel widgetModel, ArrayList<ArrivalModel> arrivals) {
        // Set the base layout to use
        int layoutResId = widgetModel.getSize() == WidgetUtils.SIZE_1X1
                ? R.layout.widget_layout_1x1
                : R.layout.widget_layout_2x1;
        RemoteViews updatedViews = new RemoteViews(context.getPackageName(), layoutResId);

        // Set widget background colors
        updatedViews.setInt(
                R.id.tv_widget_title,
                SET_BACKGROUND_COLOR,
                WidgetUtils.getPrimaryColorInt(context, widgetModel.getBgColor()));
        updatedViews.setInt(
                R.id.layout_widget_arrivals,
                SET_BACKGROUND_COLOR,
                WidgetUtils.getSecondaryColorInt(context, widgetModel.getBgColor()));

        // If the user set the widget color to white, we have to change the colors of
        // some UI elements so that they show properly.
        if (widgetModel.getBgColor() == WidgetUtils.BG_WHITE) {
            updatedViews.setTextColor(R.id.tv_widget_title, Color.DKGRAY);
            updatedViews.setTextColor(R.id.tv_widget_arrival_1, Color.DKGRAY);
            updatedViews.setTextColor(R.id.tv_widget_arrival_2, Color.DKGRAY);
            updatedViews.setInt(R.id.iv_widget_divider_1, SET_BACKGROUND_COLOR, Color.DKGRAY);
            if (getNumArrivalsToShow(widgetModel) == 3) {
                // There is a third arrival text to update
                updatedViews.setTextColor(R.id.tv_widget_arrival_3, Color.DKGRAY);
                updatedViews.setInt(R.id.iv_widget_divider_2, SET_BACKGROUND_COLOR, Color.DKGRAY);
            }
        }

        // Set the widget title
        updatedViews.setTextViewText(R.id.tv_widget_title, widgetModel.getTitle());

        // Update the shown arrival times
        for (int i=0; i < Math.min(arrivals.size(), getNumArrivalsToShow(widgetModel)); i++) {
            updatedViews.setTextViewText(
                    ARRIVAL_TEXTVIEW_RES_ID[i],
                    String.valueOf(arrivals.get(i).getMinsUntilArrival()));
        }

        // Set the widget to refresh on click
        updatedViews.setOnClickPendingIntent(
                R.id.tv_widget_title,
                createRefreshIntent(context, widgetModel.getWidgetId()));
        updatedViews.setOnClickPendingIntent(
                R.id.layout_widget_arrivals,
                createRefreshIntent(context, widgetModel.getWidgetId()));

        // Update the widget view
        appWidgetManager.updateAppWidget(widgetModel.getWidgetId(), updatedViews);
    }

    private static int getNumArrivalsToShow(WidgetModel widgetModel) {
        return widgetModel.getSize() == WidgetUtils.SIZE_1X1 ? 2 : 3;
    }

    private static PendingIntent createRefreshIntent(Context context, int widgetId) {
        Intent intent = new Intent(ACTION_UPDATE_ARRIVALS);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @RxLogSubscriber
    private static final class WidgetArrivalsSubscriber extends DefaultSubscriber<List<Arrival>> {

        final Context context;
        final AppWidgetManager appWidgetManager;
        final WidgetModel widgetModel;

        public WidgetArrivalsSubscriber(Context context, AppWidgetManager appWidgetManager, WidgetModel widgetModel) {
            this.context = context;
            this.appWidgetManager = appWidgetManager;
            this.widgetModel = widgetModel;
        }

        @Override
        public void onNext(List<Arrival> arrivals) {
            ArrayList<ArrivalModel> arrivalModels =
                    (ArrayList<ArrivalModel>) arrivalModelDataMapper.transform(arrivals);
            updateWidget(context, appWidgetManager, widgetModel, arrivalModels);
        }

        @Override
        public void onCompleted() {}

        @Override
        public void onError(Throwable e) {}
    }
}
