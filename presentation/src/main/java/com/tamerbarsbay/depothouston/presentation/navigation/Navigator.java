package com.tamerbarsbay.depothouston.presentation.navigation;

import android.content.Context;
import android.content.Intent;

import com.tamerbarsbay.depothouston.presentation.view.activity.ArrivalListActivity;
import com.tamerbarsbay.depothouston.presentation.view.activity.MapSearchActivity;
import com.tamerbarsbay.depothouston.presentation.view.activity.RouteListActivity;
import com.tamerbarsbay.depothouston.presentation.view.activity.StopListActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Navigator {

    @Inject
    public Navigator() {};

    public void navigateToRouteList(Context context) {
        if (context != null) {
            Intent intent = RouteListActivity.getCallingIntent(context);
            context.startActivity(intent);
        }
    }

    public void navigateToRouteListAsNewTask(Context context) {
        if (context != null) {
            Intent intent = RouteListActivity.getCallingIntent(context);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }

    public void navigateToMapSearch(Context context) {
        if (context != null) {
            Intent intent = MapSearchActivity.getCallingIntent(context);
            context.startActivity(intent);
        }
    }

    public void navigateToSavedStops(Context context) {
        //TODO
    }

    public void navigateToRecentStops(Context context) {
        //TODO
    }

    public void navigateToSettings(Context context) {
        //TODO
    }

    /**
     * Navigate to the stop list screen.
     * @param context Context.
     * @param routeId The id of the route for which we're loading stops.
     */
    public void navigateToStopList(Context context, String routeId) {
        if (context != null) {
            Intent intent = StopListActivity.getCallingIntent(context, routeId);
            context.startActivity(intent);
        }
    }

    /**
     * Navigate to the arrival list screen.
     * @param context Context.
     * @param stopId The id of the stop for which we're loading stops.
     */
    public void navigateToArrivalList(Context context, String stopId) {
        if (context != null) {
            Intent intent = ArrivalListActivity.getCallingIntent(context, stopId);
            context.startActivity(intent);
        }
    }

}
